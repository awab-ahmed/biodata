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

package org.opencb.biodata.formats.variant.annotation.io;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.google.common.base.Throwables;
import org.opencb.biodata.models.variant.Variant;
import org.opencb.biodata.models.variant.avro.VariantAnnotation;
import org.opencb.biodata.models.variant.avro.VariantAvro;
import org.opencb.commons.io.DataWriter;
import org.opencb.commons.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

/**
 * Created by fjlopez on 01/04/15.
 */
public class JsonAnnotationWriter implements DataWriter<Variant> {

    public static final int LOG_BATCH_SIZE = 2000;
    private String filename;
    private BufferedWriter bw;
    private int writtenVariantAnnotations = 0;
    private Logger logger;
    private OpenOption[] openMode;

    private SequenceWriter jsonObjectWriter;

    public JsonAnnotationWriter() {
        this(null);
    }

    public JsonAnnotationWriter(String filename) {
        this(filename, CREATE, TRUNCATE_EXISTING);
    }

    public JsonAnnotationWriter(String filename, OpenOption... openMode) {
        logger = LoggerFactory.getLogger(this.getClass());
        this.filename = filename;
        this.openMode = openMode;
    }

    @Override
    public boolean open() {
        try {
            OutputStream os = Files.newOutputStream(Paths.get(filename), openMode);
            if (filename.endsWith(".gz") || filename.endsWith(".gzip")) {
                os = new GZIPOutputStream(os);
            }
            bw = new BufferedWriter(new OutputStreamWriter(os));
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
        return true;
    }

    @Override
    public boolean close() {
        try {
            bw.close();
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
        return true;
    }

    @Override
    public boolean pre() {
        ObjectMapper jsonObjectMapper = new ObjectMapper();
        jsonObjectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        jsonObjectMapper.configure(MapperFeature.REQUIRE_SETTERS_FOR_GETTERS, true);
        try {
            jsonObjectWriter = jsonObjectMapper.writerFor(VariantAvro.class).writeValues(bw);
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
        return true;
    }

    @Override
    public boolean post() {
        logger.info("{} written annotations.", writtenVariantAnnotations);
        return true;
    }

    @Override
    public boolean write(Variant variant) {
        try {
            jsonObjectWriter.write(variant.getImpl());
            bw.write('\n');
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
        return true;
    }

    @Override
    public boolean write(List<Variant> list) {
        if (list != null) {
            for(Variant variant : list) {
                write(variant);
            }

            int previousBatch = writtenVariantAnnotations / LOG_BATCH_SIZE;
            writtenVariantAnnotations +=list.size();
            int newBatch = writtenVariantAnnotations / LOG_BATCH_SIZE;
            if (newBatch != previousBatch) {
                logger.info("{} written annotations.", writtenVariantAnnotations);
            }

            return true;
        } else {
            logger.warn("JsonAnnotationWriter: list is null");
        }
        return false;
    }
}
