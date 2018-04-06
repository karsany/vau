package hu.karsany.vau.project.helpers;

import hu.karsany.vau.common.Generator;
import hu.karsany.vau.project.mapping.generator.LoaderProcedure;

public class LoaderGrantGenerator implements Generator {
    private final LoaderProcedure lp;
    private final String targetExecuteGrant;

    public LoaderGrantGenerator(LoaderProcedure lp, String targetExecuteGrant) {
        this.lp = lp;
        this.targetExecuteGrant = targetExecuteGrant;
    }

    @Override
    public String toString() {
        return "grant execute on " + lp.getLoaderName() + " to " + targetExecuteGrant.toLowerCase() + ";";
    }

    @Override
    public String getFileName() {
        return "grant_" + lp.getLoaderName() + "_to_" + targetExecuteGrant.toLowerCase() + ".sql";
    }

    @Override
    public OutputType getOutputType() {
        return OutputType.GRANT;
    }
}
