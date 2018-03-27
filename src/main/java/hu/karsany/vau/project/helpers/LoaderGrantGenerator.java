package hu.karsany.vau.project.helpers;

import hu.karsany.vau.common.Generator;
import hu.karsany.vau.project.mapping.generator.Loader;

public class LoaderGrantGenerator implements Generator {
    private final Loader loader;
    private final String targetExecuteGrant;

    public LoaderGrantGenerator(Loader loader, String targetExecuteGrant) {
        this.loader = loader;
        this.targetExecuteGrant = targetExecuteGrant;
    }

    @Override
    public String getFileName() {
        return "grant_" + loader.().toLowerCase() + "_to_" + targetExecuteGrant.toLowerCase() + ".sql";
    }

    @Override
    public OutputType getOutputType() {
        return OutputType.GRANT;
    }
}
