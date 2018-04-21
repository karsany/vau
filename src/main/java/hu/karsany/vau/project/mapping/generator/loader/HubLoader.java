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

import hu.karsany.vau.common.Generator;
import hu.karsany.vau.common.templating.TemplateEvaluation;
import hu.karsany.vau.project.datamodel.model.Hub;

public class HubLoader implements Generator {
    private final LoaderParameter lp;

    public HubLoader(LoaderParameter loaderParameter) {
        this.lp = loaderParameter;
    }

    @Override
    public String toString() {
        TemplateModel templateModel = new TemplateModel(
                lp.getDataModel().createHubIfNotExists(lp.getEntityName()),
                lp.getSqlScript(),
                lp.getSourceSystemName()
        );

        return new TemplateEvaluation("hub_loader.sql", templateModel).toString();
    }

    @Override
    public String getFileName() {
        return "HUB_" + lp.getEntityName() + "_" + lp.getSourceSystemName() + "_LOAD.sql";
    }

    @Override
    public OutputType getOutputType() {
        return OutputType.LOADER;
    }


    public class TemplateModel {
        private final Hub hub;
        private final String sqlScript;
        private final String sourceSystem;

        public TemplateModel(Hub hub, String sqlScript, String sourceSystem) {
            this.hub = hub;
            this.sqlScript = sqlScript;
            this.sourceSystem = sourceSystem;
        }

        public String getSqlScript() {
            return sqlScript;
        }

        public String getSourceSystem() {
            return sourceSystem;
        }

        public Hub getHub() {
            return hub;
        }

    }
}
