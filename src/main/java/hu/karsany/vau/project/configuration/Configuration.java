/******************************************************************************
 * Copyright (c) 2017, Ferenc Karsany                                         *
 * All rights reserved.                                                       *
 * *
 * Redistribution and use in source and binary forms, with or without         *
 * modification, are permitted provided that the following conditions are met:
 * *
 * * Redistributions of source code must retain the above copyright notice,  *
 * this list of conditions and the following disclaimer.                   *
 * * Redistributions in binary form must reproduce the above copyright       *
 * notice, this list of conditions and the following disclaimer in the     *
 * documentation and/or other materials provided with the distribution.    *
 * * Neither the name of  nor the names of its contributors may be used to   *
 * endorse or promote products derived from this software without specific *
 * prior written permission.                                               *
 * *
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

package hu.karsany.vau.project.configuration;

import com.thoughtworks.xstream.XStream;
import hu.karsany.vau.common.VauException;
import org.pmw.tinylog.Logger;

import java.io.InputStream;

public class Configuration {

    private String name;
    private Template template;
    private Documentation documentation;
    private String target = "oracle";
    private String targetSchema = "@DW@";
    private String targetExecuteGrant = "@DW@";
    private String strictMode = "true";

    public static Configuration loadConfiguration(InputStream is) {
        Class<?>[] classes = new Class[]{Configuration.class, Template.class};
        XStream xs = new XStream();
        XStream.setupDefaultSecurity(xs);
        xs.allowTypes(classes);
        xs.alias("project", Configuration.class);
        xs.aliasField("target-schema", Configuration.class, "targetSchema");
        xs.aliasField("target-execute-grant", Configuration.class, "targetExecuteGrant");
        xs.aliasField("name", Template.class, "templateName");
        xs.aliasField("type", Template.class, "templateType");
        xs.aliasField("datamodel-csv", Documentation.class, "genDatamodelCsv");
        xs.aliasField("strict-mode", Configuration.class, "strictMode");

        Configuration configuration = (Configuration) xs.fromXML(is);


        Logger.info("Configuration found in vau.xml: " + configuration.getName());

        return configuration;
    }

    public String getStrictMode() {

        return strictMode;
    }

    public void setStrictMode(String strictMode) {
        if (strictMode.equals("true") || strictMode.equals("false")) {
            this.strictMode = strictMode;
        } else {
            throw new VauException("strict-mode setting should be true or false");
        }
    }

    public String getTargetExecuteGrant() {
        if (targetExecuteGrant == null) {
            return "@DW@";
        } else {
            return targetExecuteGrant;
        }
    }

    public void setTargetExecuteGrant(String targetExecuteGrant) {
        this.targetExecuteGrant = targetExecuteGrant;
    }

    public String getTargetSchema() {
        if (targetSchema == null) {
            return "@DW@";
        } else {
            return targetSchema;
        }
    }

    public void setTargetSchema(String targetSchema) {
        this.targetSchema = targetSchema;
    }

    public String getTarget() {
        return target;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Template getTemplate() {
        return template;
    }

    public Documentation getDocumentation() {
        return documentation;
    }

}
