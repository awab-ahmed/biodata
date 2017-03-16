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

package org.opencb.biodata.tools.variant.converters.ga4gh.factories;

import org.ga4gh.models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created on 09/08/16
 *
 * @author Jacobo Coll &lt;jacobo167@gmail.com&gt;
 */
public class AvroGa4GhVariantFactory implements Ga4ghVariantFactory<Variant, Call, CallSet, VariantSet, VariantSetMetadata> {

    @Override
    public CallSet newCallSet(String callSetId, String callSetName, String sampleId, ArrayList<String> variantSetIds,
                              long created, long updated, Map<String, List<String>> info) {
        return new CallSet(callSetId, callSetName, sampleId, variantSetIds, created, updated, info);
    }
    @Override
    public Variant newVariant(String id, String variantSetId, List<String> names, Long created, Long updated,
                                 String referenceName, Long start, Long end, String reference, List<String> alternates,
                                 Map<String, List<String>> info, List<Call> calls) {
        return new Variant(id, variantSetId, names, created, updated, referenceName,
                start,                // Ga4gh uses 0-based positions.
                end,                  // 0-based end does not change
                reference, alternates, info, calls);
    }

    @Override
    public Call newCall(String callSetName, String callSetId, List<Integer> allelesIdx, String phaseSet, List<Double> genotypeLikelihood,
                           Map<String, List<String>> info) {
        return new Call(callSetName, callSetId, allelesIdx, phaseSet, genotypeLikelihood, info);
    }

    @Override
    public VariantSet newVariantSet(String id, String name, String datasetId, String referenceSetId, List<VariantSetMetadata> metadata) {
        return new VariantSet(id, name, datasetId, referenceSetId, metadata);
    }

    @Override
    public VariantSetMetadata newVariantSetMetadata(String key, String value, String id, String type, String number, String description, Map<String, List<String>> info) {
        return new VariantSetMetadata(key, value, id, type, number, description, info);
    }

}
