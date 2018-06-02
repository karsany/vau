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
import hu.karsany.vau.project.datamodel.model.Satellite;

public class SatLoader implements Generator {
    private final LoaderParameter lp;

    public SatLoader(LoaderParameter loaderParameter) {
        lp = loaderParameter;
    }


    @Override
    public String toString() {
        TemplateModel templateModel = new TemplateModel(
                lp.getDataModel().getSatellite(lp.getEntityName(), lp.getDataGroupName()),
                lp.getSqlScript(),
                lp.getSourceSystemName(),
                lp.getCdcColumnName(),
                lp.getCdcStartTsName()
        );

        String templateFileName = null;

        switch (lp.getSatteliteLoadMethod()) {
            case FULL:
                templateFileName = "sat_loader_full.sql";
                break;
            case DELTA:
                templateFileName = "sat_loader_delta.sql";
                break;
            case INSERT:
                templateFileName = "sat_loader_insert.sql";
                break;
            case CDC:
                templateFileName = "sat_loader_cdc.sql";
                break;
        }

        return new TemplateEvaluation(templateFileName, templateModel).toString();
    }

    @Override
    public String getFileName() {
        return "S_" + lp.getEntityName() + "_" + lp.getDataGroupName() + "_" + lp.getSourceSystemName() + "_LOAD.sql";
    }

    @Override
    public OutputType getOutputType() {
        return OutputType.LOADER;
    }

    public class TemplateModel {
        private final Satellite sat;
        private final String sqlScript;
        private final String sourceSystem;
        private final String cdcColumnName;
        private final String cdcStartTsName;

        public TemplateModel(Satellite sat, String sqlScript, String sourceSystem, String cdcColumnName, String cdcStartTsName) {
            this.sat = sat;
            this.sqlScript = sqlScript;
            this.sourceSystem = sourceSystem;
            this.cdcColumnName = cdcColumnName;
            this.cdcStartTsName = cdcStartTsName;
        }

        public String getCdcColumnName() {
            return cdcColumnName;
        }

        public String getCdcStartTsName() {
            return cdcStartTsName;
        }

        public Satellite getSat() {
            return sat;
        }

        public String getSqlScript() {
            return sqlScript;
        }

        public String getSourceSystem() {
            return sourceSystem;
        }
    }
}
