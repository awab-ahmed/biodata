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

package org.opencb.biodata.models.variant;

/**
 * Created with IntelliJ IDEA.
 * User: aleman
 * Date: 8/26/13
 * Time: 6:10 PM
 * To change this template use File | Settings | File Templates.
 */
public enum AllelesCode {
    ALLELES_OK,
    ALLELES_MISSING,
//    FIRST_ALLELE_MISSING,
//    SECOND_ALLELE_MISSING,
//    ALL_ALLELES_MISSING,
    MULTIPLE_ALTERNATES,
    /** @deprecated Ask for ploidy or use {@link Genotype#isHaploid()} */
    @Deprecated HAPLOID
}
