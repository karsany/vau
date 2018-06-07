/*
 * Copyright (c) 2017, Ferenc Karsany
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *  * Neither the name of  nor the names of its contributors may be used to
 *    endorse or promote products derived from this software without specific
 *    prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package hu.karsany.vau.project.mapping.generator.loader;

import hu.karsany.vau.ApplicationContext;
import hu.karsany.vau.common.Generator;
import hu.karsany.vau.common.VauException;
import hu.karsany.vau.common.sql.SqlAnalyzer;
import hu.karsany.vau.common.templating.TemplateEvaluation;
import net.sf.jsqlparser.JSQLParserException;

import java.io.File;
import java.util.List;

public class LoaderProcedure implements Generator {
    private final Loader loader;
    private final File loaderTemplate;

    public LoaderProcedure(Loader loader, File loaderTemplate) {
        this.loader = loader;
        this.loaderTemplate = loaderTemplate;
    }

    public Loader getLoader() {
        return loader;
    }

    @Override
    public String getFileName() {

        String extension = ApplicationContext.getProject().getConfiguration().getTemplate().getTemplateType().equals("procedure") ? ".prc" : ".pck";

        if (loader.getLoaderParameter().getSourceFile().toString().endsWith(".sql")) {
            return loader.getLoaderParameter().getSourceFile().getName().replace(".sql", extension);
        } else {
            if (loader.getLoaderParameter().getLoaderType() == LoaderParameter.LoaderType.SATTELITE) {
                return (loader.getLoaderParameter().getLoaderType().toString().substring(0, 1) + "_" + loader.getLoaderParameter().getSourceSystemName() + "_" + loader.getLoaderParameter().getEntityName() + "_" + loader.getLoaderParameter().getDataGroupName() + "_m" + extension).toLowerCase();
            } else {
                return (loader.getLoaderParameter().getLoaderType().toString().substring(0, 1) + "_" + loader.getLoaderParameter().getSourceSystemName() + "_" + loader.getLoaderParameter().getEntityName() + "_m" + extension).toLowerCase();
            }
        }
    }

    @Override
    public OutputType getOutputType() {
        return ApplicationContext.getProject().getConfiguration().getTemplate().getTemplateType().equals("procedure") ? OutputType.LOADER_PROCEDURE : OutputType.LOADER_PACKAGE;
    }

    @Override
    public String toString() {
        TemplateModel tm = null;
        try {
            tm = new TemplateModel(
                    getLoaderName(),
                    loader.getLoaderParameter().getOutputTableName(),
                    loader.toString(),
                    new SqlAnalyzer(loader.getLoaderParameter().getSqlScript()).getInputTables(),
                    ApplicationContext.getProject().getConfiguration().getTargetSchema());
        } catch (JSQLParserException e) {
            throw new VauException(e);
        }
        return new TemplateEvaluation(loaderTemplate, tm).toString();
    }

    public String getLoaderName() {
        return getFileName().replace(".prc", "");
    }

    public class TemplateModel {
        private final String loaderName;
        private final String outputOwner;
        private final String outputTableName;
        private final String loaderScript;
        private final List<SqlAnalyzer.Table> inputTables;

        public TemplateModel(String loaderName, String outputTableName, String loaderScript, List<SqlAnalyzer.Table> inputTables, String outputOwner) {

            this.loaderName = loaderName;
            this.outputTableName = outputTableName;
            this.loaderScript = loaderScript;
            this.inputTables = inputTables;
            this.outputOwner = outputOwner;

        }

        public String getOutputOwner() {
            return outputOwner;
        }

        public List<SqlAnalyzer.Table> getInputTables() {
            return inputTables;
        }

        public String getLoaderName() {
            return loaderName;
        }

        public String getOutputTableName() {
            return outputTableName;
        }

        public String getLoaderScript() {
            return loaderScript;
        }
    }

}
