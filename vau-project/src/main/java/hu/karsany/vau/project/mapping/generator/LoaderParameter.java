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

package hu.karsany.vau.project.mapping.generator;

import hu.karsany.vau.project.datamodel.model.DataModel;
import hu.karsany.vau.common.VauException;

import java.io.File;

public class LoaderParameter {
    private DataModel dataModel;
    private LoaderType loaderType;
    private String entityName;
    private String sourceSystemName;

    private String sqlScript;
    private SatteliteLoadMethod satteliteLoadMethod;
    private String dataGroupName;
    private String referenceName;
    private String linkName;
    private String loaderConfig;
    private File sourceFile;

    public LoaderParameter() {
    }

    public DataModel getDataModel() {
        return dataModel;
    }

    public void setDataModel(DataModel dataModel) {
        this.dataModel = dataModel;
    }

    public File getSourceFile() {
        return sourceFile;
    }

    public void setSourceFile(File sourceFile) {
        this.sourceFile = sourceFile;
    }

    public String getLoaderConfig() {
        return loaderConfig;
    }

    public void setLoaderConfig(String loaderConfig) {
        this.loaderConfig = loaderConfig;
    }

    public LoaderType getLoaderType() {
        return loaderType;
    }

    public void setLoaderType(LoaderType loaderType) {
        this.loaderType = loaderType;
    }

    public SatteliteLoadMethod getSatteliteLoadMethod() {
        return satteliteLoadMethod;
    }

    public void setSatteliteLoadMethod(SatteliteLoadMethod satteliteLoadMethod) {
        this.satteliteLoadMethod = satteliteLoadMethod;
    }

    public String getDataGroupName() {
        return dataGroupName;
    }

    public void setDataGroupName(String dataGroupName) {
        this.dataGroupName = dataGroupName;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getSourceSystemName() {
        return sourceSystemName;
    }

    public void setSourceSystemName(String sourceSystemName) {
        this.sourceSystemName = sourceSystemName;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getSqlScript() {
        return sqlScript;
    }

    public void setSqlScript(String sqlScript) {
        this.sqlScript = sqlScript;
    }

    public String getReferenceName() {
        return referenceName;
    }

    public void setReferenceName(String referenceName) {
        this.referenceName = referenceName;
    }

    public String getOutputTableName() {
        switch (loaderType) {
            case HUB:
                return "HUB_" + entityName;
            case SATTELITE:
                return "SAT_" + entityName + "_" + dataGroupName;
            case LINK:
                return "LNK_" + linkName;
            case REFERENCE:
                return "REF_" + referenceName;
        }
        throw new VauException("Unknown loader type: " + loaderType);
    }

    public enum LoaderType {HUB, SATTELITE, LINK, REFERENCE}

    public enum SatteliteLoadMethod {FULL, DELTA, INSERT, CDC}
}