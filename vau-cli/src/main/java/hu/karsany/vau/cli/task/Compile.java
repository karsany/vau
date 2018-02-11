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

import hu.karsany.vau.common.GeneratorHelper;
import hu.karsany.vau.project.Project;
import hu.karsany.vau.project.datamodel.model.Entity;
import hu.karsany.vau.project.datamodel.model.Table;
import hu.karsany.vau.project.helpers.DataModelExampleMapping;
import hu.karsany.vau.project.helpers.SequenceGenerator;
import hu.karsany.vau.project.helpers.SourceTableGrants;
import hu.karsany.vau.project.mapping.generator.Loader;
import hu.karsany.vau.project.mapping.generator.LoaderParameter;
import hu.karsany.vau.project.mapping.generator.LoaderProcedure;
import org.pmw.tinylog.Logger;

import java.io.File;
import java.io.IOException;

public class Compile {

    private final Project pm;

    public Compile(Project pm) {
        this.pm = pm;
    }

    public void run() throws IOException {

        Logger.info("Generating tables");
        for (Table table : pm.getDataModel().getTables()) {
            GeneratorHelper.generate(pm.getProjectPath(), table);
        }

        Logger.info("Generating sequences");
        for (Entity entity : pm.getDataModel().getEntityTables()) {
            GeneratorHelper.generate(pm.getProjectPath(), new SequenceGenerator(entity));
        }


        Logger.info("Generating example mapping");
        for (Table table : pm.getDataModel().getTables()) {
            GeneratorHelper.generate(pm.getProjectPath(), new DataModelExampleMapping(table));
        }

        Logger.info("Generating loaders");

        for (LoaderParameter loaderParameter : pm.getMappings()) {
            Loader ldr = new Loader(loaderParameter);
            GeneratorHelper.generate(pm.getProjectPath(), ldr);
            GeneratorHelper.generate(pm.getProjectPath(), new LoaderProcedure(ldr, new File(pm.getProjectPath() + "/src/template/" + pm.getConfiguration().getTemplate().getDefaultTemplate())));
        }

        Logger.info("Generating grants");
        GeneratorHelper.generate(pm.getProjectPath(), new SourceTableGrants(pm).generateGrants());

    }

}
