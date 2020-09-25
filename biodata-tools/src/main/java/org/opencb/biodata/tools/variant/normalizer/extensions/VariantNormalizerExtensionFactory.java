package org.opencb.biodata.tools.variant.normalizer.extensions;

import org.apache.commons.lang3.StringUtils;
import org.opencb.biodata.models.variant.Variant;
import org.opencb.biodata.models.variant.VariantFileMetadata;
import org.opencb.biodata.models.variant.metadata.VariantFileHeaderComplexLine;
import org.opencb.commons.run.Task;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class VariantNormalizerExtensionFactory {

    public static final Set<String> ALL_EXTENSIONS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            "FILE_DP_TO_SAMPLE",
            "SAMPLE_DP_TO_FORMAT"
    )));
    private final Set<String> enabledExtensions;

    public VariantNormalizerExtensionFactory() {
        this(ALL_EXTENSIONS);
    }

    public VariantNormalizerExtensionFactory(Set<String> enabledExtensions) {
        this.enabledExtensions = enabledExtensions;
    }


    public Task<Variant, Variant> buildExtensions(VariantFileMetadata fileMetadata) {
        Task<Variant, Variant> extensions = null;
        for (String normalizerExtension : enabledExtensions) {
            VariantNormalizerExtension extension;
            switch (normalizerExtension) {
                case "FILE_DP_TO_SAMPLE":
                    extension = new VariantNormalizerExtensionFileToSample("DP");
                    break;
                case "FILE_AD_TO_SAMPLE_DP":
                    extension = new VariantNormalizerExtensionFileToSample("AD", "DP",
                            new VariantFileHeaderComplexLine("FORMAT", "DP", "", "1", "Integer", Collections.emptyMap()),
                            ad -> {
                                String[] split = ad.split(",");
                                int dp = 0;
                                for (String s : split) {
                                    dp += Integer.parseInt(s);
                                }
                                return String.valueOf(dp);
                            });
                    break;
                default:
                    throw new IllegalArgumentException("Unknown normalizer extension " + normalizerExtension);
            }
            if (extension.canUseExtension(fileMetadata)) {
                extension.init(fileMetadata);
                if (extensions == null) {
                    extensions = extension;
                } else {
                    extensions = extensions.then(extension);
                }
            }
        }
        return extensions;
    }

}
