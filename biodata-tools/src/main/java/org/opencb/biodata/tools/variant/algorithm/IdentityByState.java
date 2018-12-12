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
package org.opencb.biodata.tools.variant.algorithm;

import java.util.Arrays;

/**
 * Created by jmmut on 2015-12-02.
 *
 * @author Jose Miguel Mut Lopez &lt;jmmut@ebi.ac.uk&gt;
 */
public class IdentityByState {
    public int[] ibs = {0, 0, 0};

    public void add(IdentityByState param) {
        for (int i = 0; i < ibs.length; i++) {
            ibs[i] += param.ibs[i];
        }
    }

    @Override
    public String toString() {
        return "IBS{" +
                "ibs=" + Arrays.toString(ibs) +
                '}';
    }

    public int[] getIbs() {
        return ibs;
    }

    public IdentityByState setIbs(int[] ibs) {
        this.ibs = ibs;
        return this;
    }
}
