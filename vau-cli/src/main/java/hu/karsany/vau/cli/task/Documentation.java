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
import hu.karsany.vau.project.datamodel.documentation.DataModelCsv;
import hu.karsany.vau.project.datamodel.documentation.DataModelHtml;
import hu.karsany.vau.project.datamodel.documentation.DataModelTgf;
import hu.karsany.vau.project.mapping.documentation.ColumnLineageCsv;
import hu.karsany.vau.project.mapping.documentation.TableLineageCsv;
import org.pmw.tinylog.Logger;

import java.io.IOException;

public class Documentation {

    private final Project projectModel;

    public Documentation(Project projectModel) {
        this.projectModel = projectModel;
    }


    public void run() throws IOException {

        Logger.info("Generating data model documentation");
        GeneratorHelper.generate(projectModel.getProjectPath(), new DataModelCsv(projectModel.getDataModel()));
        GeneratorHelper.generate(projectModel.getProjectPath(), new DataModelTgf(projectModel.getDataModel()));
        GeneratorHelper.generate(projectModel.getProjectPath(), new DataModelHtml(projectModel.getDataModel()));
        GeneratorHelper.generate(projectModel.getProjectPath(), new TableLineageCsv(projectModel));
        GeneratorHelper.generate(projectModel.getProjectPath(), new ColumnLineageCsv(projectModel));

    }
}
