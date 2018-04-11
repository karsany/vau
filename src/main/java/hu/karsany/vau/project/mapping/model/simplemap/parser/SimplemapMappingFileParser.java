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

package hu.karsany.vau.project.mapping.model.simplemap.parser;

import hu.karsany.vau.project.datamodel.model.DataModel;
import hu.karsany.vau.project.mapping.generator.loader.LoaderParameter;
import hu.karsany.vau.project.mapping.parser.MappingParser;
import hu.karsany.vau.project.mapping.model.simplemap.model.SimplemapEntry;
import hu.karsany.vau.common.VauException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SimplemapMappingFileParser implements MappingParser {

    private final File file;
    private final DataModel dataModel;

    public SimplemapMappingFileParser(File file, DataModel dataModel) {
        this.file = file;
        this.dataModel = dataModel;
    }

    @Override
    public List<LoaderParameter> getMapping() {
        try {
            List<LoaderParameter> lps = new ArrayList<>();

            SimplemapEvaluation simplemapEvaluation = new SimplemapEvaluation(file);
            List<SimplemapEntry> entries = simplemapEvaluation.getSimplemapModel().getEntries();
            for (SimplemapEntry entry : entries) {
                lps.addAll(new SimplemapMappingEntryParser(entry, dataModel, file).getMapping());
            }
            return lps;
        } catch (IOException e) {
            throw new VauException(e);
        }
    }
}
