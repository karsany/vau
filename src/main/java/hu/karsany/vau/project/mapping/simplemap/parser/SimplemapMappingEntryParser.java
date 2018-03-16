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

package hu.karsany.vau.project.mapping.simplemap.parser;

import hu.karsany.vau.project.datamodel.model.DataModel;
import hu.karsany.vau.project.mapping.MappingParser;
import hu.karsany.vau.project.mapping.generator.LoaderParameter;
import hu.karsany.vau.project.mapping.simplemap.model.SimplemapDataGroupMapping;
import hu.karsany.vau.project.mapping.simplemap.model.SimplemapEntry;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class SimplemapMappingEntryParser implements MappingParser {

    private final SimplemapEntry simplemapEntry;
    private final DataModel dataModel;
    private final File sourceFile;

    public SimplemapMappingEntryParser(SimplemapEntry simplemapEntry, DataModel dataModel, File sourceFile) {
        this.simplemapEntry = simplemapEntry;
        this.dataModel = dataModel;
        this.sourceFile = sourceFile;
    }

    private LoaderParameter generateSatLoader(SimplemapDataGroupMapping dgms) {
        LoaderParameter lp = new LoaderParameter();

        lp.setLoaderType(LoaderParameter.LoaderType.SATTELITE);
        lp.setDataModel(dataModel);
        lp.setEntityName(simplemapEntry.getEntityName());
        lp.setSatteliteLoadMethod(LoaderParameter.SatteliteLoadMethod.valueOf(simplemapEntry.getSourceDefinition().getContains()));
        lp.setDataGroupName(dgms.getDatagroupName());
        lp.setSourceSystemName(simplemapEntry.getSourceDefinition().getSystem());

        StringBuilder columnMapping = new StringBuilder();
        for (Map.Entry<String, String> m : dgms.getMapping().entrySet()) {
            columnMapping.append(", " + m.getValue() + " as " + m.getKey() + "\n");
        }

        lp.setSqlScript(
                "select " + simplemapEntry.getBusinessKey() + " as " + simplemapEntry.getEntityName() + "_BK" +
                        columnMapping.toString()
                        + " from " + simplemapEntry.getSourceDefinition().getOwner() + "." + simplemapEntry.getSourceDefinition().getTable()
        );

        lp.setSourceFile(sourceFile);

        return lp;
    }

    private LoaderParameter generateHubLoader() {
        LoaderParameter lp = new LoaderParameter();

        lp.setLoaderType(LoaderParameter.LoaderType.HUB);
        lp.setDataModel(this.dataModel);
        lp.setEntityName(simplemapEntry.getEntityName());
        lp.setSourceSystemName(simplemapEntry.getSourceDefinition().getSystem());
        lp.setSqlScript("select " + simplemapEntry.getBusinessKey() + " as " + simplemapEntry.getEntityName() + "_BK from " + simplemapEntry.getSourceDefinition().getOwner() + "." + simplemapEntry.getSourceDefinition().getTable());

        lp.setSourceFile(sourceFile);

        return lp;
    }

    @Override
    public List<LoaderParameter> getMapping() {
        List<LoaderParameter> ret = new ArrayList<>();

        ret.add(generateHubLoader());

        for (SimplemapDataGroupMapping dgms : simplemapEntry.getSimplemapDataGroupMappingList()) {
            ret.add(generateSatLoader(dgms));
        }

        return ret;
    }
}
