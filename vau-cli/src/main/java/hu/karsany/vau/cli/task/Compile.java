/******************************************************************************
 * Copyright (c) 2017, Ferenc Karsany                                         *
 * All rights reserved.                                                       *
 *                                                                            *
 * Redistribution and use in source and binary forms, with or without         *
 * modification, are permitted provided that the following conditions are met:
 *                                                                            *
 *  * Redistributions of source code must retain the above copyright notice,  *
 *    this list of conditions and the following disclaimer.                   *
 *  * Redistributions in binary form must reproduce the above copyright       *
 *    notice, this list of conditions and the following disclaimer in the     *
 *    documentation and/or other materials provided with the distribution.    *
 *  * Neither the name of  nor the names of its contributors may be used to   *
 *    endorse or promote products derived from this software without specific *
 *    prior written permission.                                               *
 *                                                                            *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE  *
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE *
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE   *
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR        *
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF       *
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS   *
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN    *
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)    *
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE *
 * POSSIBILITY OF SUCH DAMAGE.                                                *
 ******************************************************************************/

package hu.karsany.vau.cli.task;

import hu.karsany.vau.project.Project;
import hu.karsany.vau.project.datamodel.documentation.DataModelCsv;
import hu.karsany.vau.project.datamodel.documentation.DataModelHtml;
import hu.karsany.vau.project.datamodel.documentation.DataModelTgf;
import hu.karsany.vau.project.datamodel.model.Table;
import hu.karsany.vau.project.helpers.DataModelExampleMapping;
import hu.karsany.vau.project.helpers.SourceTableGrants;
import hu.karsany.vau.project.mapping.documentation.ColumnLineageCsv;
import hu.karsany.vau.project.mapping.documentation.TableLineageCsv;
import hu.karsany.vau.project.mapping.generator.Loader;
import hu.karsany.vau.project.mapping.generator.LoaderParameter;
import hu.karsany.vau.util.Generator;
import hu.karsany.vau.util.VauException;
import org.apache.commons.io.FileUtils;
import org.pmw.tinylog.Logger;

import java.io.File;
import java.io.IOException;

public class Compile {

    private final Project pm;

    public Compile(Project pm) {
        this.pm = pm;
    }

    public static void generate(Generator g) throws IOException {
        generate(g, "utf-8");
    }

    public static void generate(Generator g, String encoding) throws IOException {
        File fileName = new File("./target/" + g.getOutputType().getOutputTypeName() + "/" + g.getFileName().toLowerCase());

        Logger.info("  Generating: " + fileName.toString());

        String generatedData;
        try {
            generatedData = g.toString();
        } catch (Exception e) {
            throw new VauException("Error at generating " + g.getOutputType().getOutputTypeName().toLowerCase() + " " + g.getFileName(), e);
        }
        FileUtils.write(
                fileName,
                generatedData,
                encoding);
    }

    public void run() throws IOException {

        Logger.info("Generating tables");
        for (Table table : pm.getDataModel().getTables()) {
            generate(table);
        }

        if (pm.getConfiguration().getDocumentation().getGenDatamodelCsv()) {
            Logger.info("Generating data model documentation");
            generate(new DataModelCsv(pm.getDataModel()));
            generate(new DataModelTgf(pm.getDataModel()));
            generate(new DataModelHtml(pm.getDataModel()));
        }

        Logger.info("Generating example mapping");
        for (Table table : pm.getDataModel().getTables()) {
            generate(new DataModelExampleMapping(table));
        }

        Logger.info("Generating loaders");

        for (LoaderParameter loaderParameter : pm.getMappings()) {
            generate(new Loader(loaderParameter));
//            generate(new LoaderProcedure(lp.));
        }


        generate(new TableLineageCsv(pm));
        generate(new SourceTableGrants(pm));
        generate(new ColumnLineageCsv(pm));

  /*      for (Path f : pm.getMapping()) {
            Logger.info(" Parsing " + f + "...");
            if (f.toFile().getAbsolutePath().toString().toLowerCase().endsWith(".sql")) {
                Loader loader = new Loader();
                generate(loader);
                generate(new LoaderProcedure(f.toFile(), loader, new File("src\\template\\" + pm.getConfiguration().getTemplate().getDefaultTemplate())));
            } else {
                SimplemapEvaluation smev = new SimplemapEvaluation(f.toFile());
                for (SimplemapEntry se : smev.getSimplemapModel().getEntries()) {
                    List<Loader> loaders = new SimplemapMappingEntryParser(se, pm.getDataModel()).getLoaders();
                    for (Loader loader : loaders) {
                        generate(loader);
                        generate(new LoaderProcedure(f.toFile(), loader, new File("src\\template\\" + pm.getConfiguration().getTemplate().getDefaultTemplate())));
                    }
                }

            }
        }*/
    }

}
