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

package hu.karsany.vau.project;

import hu.karsany.vau.project.configuration.Configuration;
import hu.karsany.vau.project.datamodel.model.DataModel;
import hu.karsany.vau.project.datamodel.parser.FileDataModelParser;
import hu.karsany.vau.project.mapping.generator.loader.LoaderParameter;
import hu.karsany.vau.project.mapping.parser.GenericMappingParser;
import org.pmw.tinylog.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Project {

    private final File projectPath;
    private Configuration configuration;
    private DataModel dataModel;
    private List<LoaderParameter> mappings;

    private Project(File projectPath) {
        this.projectPath = projectPath;
    }

    public static Project initialize(File projectPath) throws IOException {
        Project pm = new Project(projectPath);

        // Configuration
        pm.configuration = Configuration.loadConfiguration(new FileInputStream(projectPath + "\\vau.xml"));


        // Data DataModel
        Logger.info("Found data model definitions");

        List<File> fileList = Files.walk(Paths.get(projectPath.getAbsolutePath() + "\\src\\model\\"))
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .collect(toList());

        pm.dataModel = new FileDataModelParser(fileList).parse();

        // Mapping

        Logger.info("Found mapping definitions");

        pm.mappings = new ArrayList<>();

        Files.walk(Paths.get(projectPath.getAbsolutePath() + "\\src\\mapping\\"))
                .filter(Files::isRegularFile)
                .forEach(path ->
                        pm.mappings.addAll(
                                new GenericMappingParser(path.toFile(), pm.dataModel).getMapping()
                        )
                );

        return pm;

    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public List<LoaderParameter> getMappings() {
        return mappings;
    }

    public File getProjectPath() {
        return projectPath;
    }

    public DataModel getDataModel() {
        return dataModel;
    }
}
