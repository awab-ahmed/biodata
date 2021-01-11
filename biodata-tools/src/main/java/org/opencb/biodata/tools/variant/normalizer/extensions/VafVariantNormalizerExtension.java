/*
 * <!--
 *   ~ Copyright 2015-2017 OpenCB
 *   ~
 *   ~ Licensed under the Apache License, Version 2.0 (the "License");
 *   ~ you may not use this file except in compliance with the License.
 *   ~ You may obtain a copy of the License at
 *   ~
 *   ~     http://www.apache.org/licenses/LICENSE-2.0
 *   ~
 *   ~ Unless required by applicable law or agreed to in writing, software
 *   ~ distributed under the License is distributed on an "AS IS" BASIS,
 *   ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   ~ See the License for the specific language governing permissions and
 *   ~ limitations under the License.
 *   -->
 *
 */

package org.opencb.biodata.tools.variant.normalizer.extensions;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.opencb.biodata.models.variant.StudyEntry;
import org.opencb.biodata.models.variant.Variant;
import org.opencb.biodata.models.variant.VariantFileMetadata;
import org.opencb.biodata.models.variant.avro.FileEntry;
import org.opencb.biodata.models.variant.avro.SampleEntry;
import org.opencb.biodata.models.variant.metadata.VariantFileHeaderComplexLine;

import java.util.*;

public class VafVariantNormalizerExtension extends VariantNormalizerExtension {

    private String caller;
    private boolean canCalculateVaf;
    private static final Map<String, List<String>> supportedCallers;

    static {
        supportedCallers = new LinkedHashMap<>();
        supportedCallers.put("caveman", Arrays.asList("ASMD", "CLPM"));
        supportedCallers.put("pindel", Arrays.asList("PC", "VT"));
    }

    public VafVariantNormalizerExtension() {
        this("");
    }

    public VafVariantNormalizerExtension(String caller) {
        this.caller= caller;
    }


    @Override
    public void init() {
        this.canCalculateVaf = false;

        // Check if a supported variant caller parameter has been provided in the constructor
        if (StringUtils.isNotEmpty(caller) && supportedCallers.containsKey(caller)) {
            canCalculateVaf = true;
            return;
        }

        // Try to find the variant caller if it is not provided
        // Check if any of the supported callers contains the INFO fields needed
        for (Map.Entry<String, List<String>> entry : supportedCallers.entrySet()) {
            if (checkCaller(entry.getKey(), entry.getValue())) {
                caller = entry.getKey();
                break;
            }
        }

        // Check if we can calculate the VAF
        if (StringUtils.isNotEmpty(caller)) {
            // Good news, a valid caller found
            canCalculateVaf = true;
        } else {
            // No caller found, but we can still calculate VAF if standard fields AD and DP are found
            // let's check if can find the fields needed to calculate the VAF

            // Parse and init internal configuration
            boolean containsFormatAD = false;
            boolean containsFormatDP = false;
            boolean containsInfoDP = false;
            boolean containsFormatExtVaf = false;
            for (VariantFileHeaderComplexLine complexLine : fileMetadata.getHeader().getComplexLines()) {
                if (complexLine.getKey().equalsIgnoreCase("INFO")) {
                    if (complexLine.getId().equals("DP")) {
                        containsInfoDP = true;
                    }
                } else if (complexLine.getKey().equalsIgnoreCase("FORMAT")) {
                    switch (complexLine.getId()) {
                        case "AD":
                            containsFormatAD = true;
                            break;
                        case "DP":
                            containsFormatDP = true;
                            break;
                        case "EXT_VAF":
                            containsFormatExtVaf = true;
                            break;
                    }
                }
            }

            if (containsFormatAD && (containsFormatDP || containsInfoDP)) {
                canCalculateVaf = true;
            }

            // Important: If EXT_VAF filter already exist in the VCF header we cannot do anything
            if (containsFormatExtVaf) {
                canCalculateVaf = false;
            }
        }
    }

    @Override
    protected boolean canUseExtension(VariantFileMetadata fileMetadata) {
        // canCalculateVaf is calculated in the init() method after checking the VCF header fields
        return canCalculateVaf;
    }

    @Override
    protected void normalizeHeader(VariantFileMetadata fileMetadata) {
        if (canCalculateVaf) {
            // Add EXT_VAF
            VariantFileHeaderComplexLine newSampleMetadataLine = new VariantFileHeaderComplexLine( "FORMAT",
                    "EXT_VAF",
                    "Variant Allele Fraction (VAF), several variant callers supported. This is a OpenCB extension field.",
                    "1",
                    "Float",
                    Collections.emptyMap());
            fileMetadata.getHeader().getComplexLines().add(newSampleMetadataLine);
        }
    }

    @Override
    protected void normalizeSample(Variant variant, StudyEntry study, FileEntry file, String sampleId, SampleEntry sample) {
        MutablePair<Float, Integer> pair = calculateVaf(variant, study, file, sample);
        if (pair != null) {
            study.addSampleDataKey("EXT_VAF");
            study.addSampleData(sampleId, "EXT_VAF", String.valueOf(pair.getLeft()));
        }
    }

    private MutablePair<Float, Integer> calculateVaf(Variant variant, StudyEntry study, FileEntry file, SampleEntry sample) {
        // If we reach this point is because canCalculateVaf is true and therefore we know we can calculate VAF
        // Init internal variables, this method calculates VAF and DEPTH and return them in a Pair tuple
        float VAF = -1f;
        int DP = 0;
        MutablePair<Float, Integer> pair = null;
        if (StringUtils.isEmpty(caller)) {
            // We assume AD and DP fields exist because canCalculateVaf is true and no caller has been found
            // Get AD
            int AD = 0;
            Integer adIndex = study.getSampleDataKeyPositions().get("AD");
            if (adIndex != null && adIndex >= 0) {
                String adString = sample.getData().get(adIndex);
                if (StringUtils.isNotEmpty(adString) && !adString.equals(".")) {
                    String[] split = adString.split(",");
                    if (split.length > 1) {
                        AD = Integer.parseInt(split[1]);
                    }
                }
            }

            // Get DEPTH
            // First, search in the FORMAT field
            if (study.getSampleDataKeyPositions().containsKey("DP")) {
                Integer depthIndex = study.getSampleDataKeyPosition("DP");
                if (adIndex != null && adIndex >= 0) {
                    String dpString = sample.getData().get(depthIndex);
                    if (StringUtils.isNotEmpty(dpString) && !dpString.equals(".")) {
                        DP = Integer.parseInt(dpString);
                    }
                }
            } else {
                // Second, search in the INFO field
                String depthString = file.getData().getOrDefault("DP", "");
                if (StringUtils.isNotEmpty(depthString)) {
                    DP = Integer.parseInt(depthString);
                }
            }

            if (AD != 0 && DP > 0) {
                VAF = (float) AD / DP;
            }
        } else {
            List<String> formatFields;
            Integer index;
            switch (caller.toUpperCase()) {
                case "CAVEMAN":
                    // DEPTH
                    formatFields = Arrays.asList("FAZ", "FCZ", "FGZ", "FTZ", "RAZ", "RCZ", "RGZ", "RTZ");
                    for (String formatField : formatFields) {
                        index = study.getSampleDataKeyPositions().get(formatField);
                        if (index != null && index >= 0) {
                            DP += Integer.parseInt(sample.getData().get(index));
                        }
                    }
                    // VAF
                    VAF = Float.parseFloat(sample.getData().get(study.getSampleDataKeyPosition("PM")));
                    break;
                case "PINDEL":
                    int PU = Integer.parseInt(sample.getData().get(study.getSampleDataKeyPosition("PU")));
                    int NU = Integer.parseInt(sample.getData().get(study.getSampleDataKeyPosition("NU")));
                    int PR = Integer.parseInt(sample.getData().get(study.getSampleDataKeyPosition("PR")));
                    int NR = Integer.parseInt(sample.getData().get(study.getSampleDataKeyPosition("NR")));

                    DP = PR + NR;
                    VAF = (float) (PU + NU) / (PR + NR);
                    break;
                default:
                    break;
            }
        }

        // Create pair object with VAF and DEPTH
        if (VAF >=0 && DP >= 0) {
            pair = new MutablePair<>(VAF, DP);
        }

        return pair;
    }

    /**
     * Checks if all header fields passed exists.
     * @param caller Variant caller name to check
     * @param keys VCF header fields to check
     * @return true if the name is found or all the field exist
     */
    private boolean checkCaller(String caller, List<String> keys) {
        // TODO we need to use caller param for some callers
        Set<String> keySet = new HashSet<>(keys);
        int counter = 0;
        for (VariantFileHeaderComplexLine complexLine : fileMetadata.getHeader().getComplexLines()) {
            if (keySet.contains(complexLine.getId())) {
                counter++;

                // Return when all needed keys have been found
                if (keys.size() == counter) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("VafVariantNormalizerExtension{");
        sb.append("caller='").append(caller).append('\'');
        sb.append(", canCalculateVaf=").append(canCalculateVaf);
        sb.append('}');
        return sb.toString();
    }
}
