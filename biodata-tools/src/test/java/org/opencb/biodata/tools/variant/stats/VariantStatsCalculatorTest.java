/*
 * Copyright 2015 OpenCB
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opencb.biodata.tools.variant.stats;

import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.opencb.biodata.models.variant.Variant;
import org.opencb.biodata.models.variant.VariantSource;
import org.opencb.biodata.models.variant.StudyEntry;
import org.opencb.biodata.models.variant.VariantVcfFactory;
import org.opencb.biodata.models.variant.avro.VariantType;
import org.opencb.biodata.models.variant.stats.VariantStats;

/**
 *
 * @author Cristina Yenyxe Gonzalez Garcia &lt;cyenyxe@ebi.ac.uk&gt;
 */
public class VariantStatsCalculatorTest {
    
    private VariantSource source = new VariantSource("filename.vcf", "fileId", "studyId", "studyName");

    @Test
    public void testCalculateBiallelicStats() {
        List<String> sampleNames = Arrays.asList("NA001", "NA002", "NA003", "NA004", "NA005", "NA006");
        source.setSamples(sampleNames);
        String line = "1\t10040\trs123\tT\tC\t10.05\tHELLO\t.\tGT:GL\t"
                + "0/0:1,2,3\t0/1:1,2,3\t0/1:1,2,3\t"
                + "1/1:1,2,3\t./.:1,2,3\t1/1:1,2,3"; // 6 samples

        // Initialize expected variants
        List<Variant> result = new VariantVcfFactory().create(source, line);
        assertEquals(1, result.size());
        
        Variant variant = result.get(0);
        StudyEntry sourceEntry = variant.getSourceEntry(source.getFileId(), source.getStudyId());
        
        VariantStats biallelicStats = new VariantStats(result.get(0));
        VariantStatsCalculator.calculate(sourceEntry, sourceEntry.getAttributes(), null, biallelicStats);
        
        assertEquals("T", biallelicStats.getRefAllele());
        assertEquals("C", biallelicStats.getAltAllele());
        assertEquals(VariantType.SNV, biallelicStats.getVariantType());
        
        assertEquals(4, biallelicStats.getRefAlleleCount().longValue());
        assertEquals(6, biallelicStats.getAltAlleleCount().longValue());
        
//    private Map<Genotype, Integer> genotypesCount;
    
        assertEquals(2, biallelicStats.getMissingAlleles().longValue());
        assertEquals(1, biallelicStats.getMissingGenotypes().longValue());
    
        assertEquals(0.4, biallelicStats.getRefAlleleFreq(), 1e-6);
        assertEquals(0.6, biallelicStats.getAltAlleleFreq(), 1e-6);
        
//    private Map<Genotype, Float> genotypesFreq;
//    private float maf;
//    private float mgf;
//    private String mafAllele;
//    private String mgfGenotype;
        
        assertFalse(biallelicStats.hasPassedFilters());
    
        assertEquals(-1, biallelicStats.getMendelianErrors().longValue());
        assertEquals(-1, biallelicStats.getCasesPercentDominant(), 1e-6);
        assertEquals(-1, biallelicStats.getControlsPercentDominant(), 1e-6);
        assertEquals(-1, biallelicStats.getCasesPercentRecessive(), 1e-6);
        assertEquals(-1, biallelicStats.getCasesPercentRecessive(), 1e-6);
    
        assertTrue(biallelicStats.isTransition());
        assertFalse(biallelicStats.isTransversion());
        
        assertEquals(10.05, biallelicStats.getQuality(), 1e-6);
        assertEquals(6, biallelicStats.getNumSamples().longValue());
    
//    private VariantHardyWeinbergStats hw;
    }
    
    @Test
    public void testCalculateMultiallelicStats() {
        List<String> sampleNames = Arrays.asList("NA001", "NA002", "NA003", "NA004", "NA005", "NA006");
        source.setSamples(sampleNames);
        String line = "1\t10040\trs123\tT\tA,GC\t.\tPASS\t.\tGT:GL\t"
                + "0/0:1,2,3,4,5,6\t0/1:1,2,3,4,5,6\t0/2:1,2,3,4,5,6\t"
                + "1/1:1,2,3,4,5,6\t1/2:1,2,3,4,5,6\t1/2:1,2,3,4,5,6"; // 6 samples

        // Initialize expected variants
        List<Variant> result = new VariantVcfFactory().create(source, line);
        assertEquals(2, result.size());
        
        // Test first variant (alt allele C)
        Variant variant_C = result.get(0);
        StudyEntry sourceEntry_C = variant_C.getStudy(source.getStudyId());
        VariantStats multiallelicStats_C = new VariantStats(result.get(0));
        VariantStatsCalculator.calculate(sourceEntry_C, sourceEntry_C.getAttributes(), null, multiallelicStats_C);
        
        assertEquals("T", multiallelicStats_C.getRefAllele());
        assertEquals("A", multiallelicStats_C.getAltAllele());
        assertEquals(VariantType.SNV, multiallelicStats_C.getVariantType());
        
        assertEquals(multiallelicStats_C.getRefAlleleCount().intValue(), 4);
        assertEquals(multiallelicStats_C.getAltAlleleCount().intValue(), 5);
//        
////    private Map<Genotype, Integer> genotypesCount;
//    
//        assertEquals(0, multiallelicStats_C.getMissingAlleles());
//        assertEquals(0, multiallelicStats_C.getMissingGenotypes());
//    
//        assertEquals(0.375, multiallelicStats_C.getRefAlleleFreq(), 1e-6);
//        assertEquals(0.5, multiallelicStats_C.getAltAlleleFreq(), 1e-6);
        
//    private Map<Genotype, Float> genotypesFreq;
//    private float maf;
//    private float mgf;
//    private String mafAllele;
//    private String mgfGenotype;
        
        assertTrue(multiallelicStats_C.hasPassedFilters());
    
        assertEquals(Integer.valueOf(-1), multiallelicStats_C.getMendelianErrors());
        assertEquals(-1, multiallelicStats_C.getCasesPercentDominant(), 1e-6);
        assertEquals(-1, multiallelicStats_C.getControlsPercentDominant(), 1e-6);
        assertEquals(-1, multiallelicStats_C.getCasesPercentRecessive(), 1e-6);
        assertEquals(-1, multiallelicStats_C.getCasesPercentRecessive(), 1e-6);
    
        assertFalse(multiallelicStats_C.isTransition());
        assertTrue(multiallelicStats_C.isTransversion());
        
        assertEquals(-1, multiallelicStats_C.getQuality(), 1e-6);
//        assertEquals(6, multiallelicStats_C.getNumSamples());
    
        
        // Test second variant (alt allele GC)
        Variant variant_GC = result.get(1);
        StudyEntry sourceEntry_GC = variant_GC.getSourceEntry(source.getFileId(), source.getStudyId());
        VariantStats multiallelicStats_GC = new VariantStats(result.get(1));
        VariantStatsCalculator.calculate(sourceEntry_GC, sourceEntry_GC.getAttributes(), null, multiallelicStats_GC);

        assertEquals("T", multiallelicStats_GC.getRefAllele());
        assertEquals("GC", multiallelicStats_GC.getAltAllele());

        assertEquals(multiallelicStats_GC.getRefAlleleCount().intValue(), 4);
        assertEquals(multiallelicStats_GC.getAltAlleleCount().intValue(), 3);
        
    }
    
}
