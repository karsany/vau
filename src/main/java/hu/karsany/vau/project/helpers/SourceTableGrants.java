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

package hu.karsany.vau.project.helpers;

import hu.karsany.vau.App;
import hu.karsany.vau.common.Generator;
import hu.karsany.vau.common.VauException;
import hu.karsany.vau.common.sql.SqlAnalyzer;
import hu.karsany.vau.project.Project;
import hu.karsany.vau.project.mapping.generator.LoaderParameter;
import net.sf.jsqlparser.JSQLParserException;

import java.util.*;

public class SourceTableGrants {

    private final Project pm;

    public SourceTableGrants(Project pm) {
        this.pm = pm;
    }

    public List<Generator> generateGrants() {
        List<Generator> r = new ArrayList<>();

        Map<String, Set<String>> grantScripts = collectGrantScriptsByOwner();

        for (Map.Entry<String, Set<String>> kv : grantScripts.entrySet()) {
            r.add(new SourceTableGrantPerOwnerGenerator(kv.getKey(), kv.getValue()));
        }

        return r;
    }

    private Map<String, Set<String>> collectGrantScriptsByOwner() {
        Map<String, Set<String>> grantScripts = new HashMap<>();

        for (LoaderParameter lp : pm.getMappings()) {
            try {
                for (SqlAnalyzer.Table table : new SqlAnalyzer(lp.getSqlScript()).getInputTables()) {
                    if (!grantScripts.containsKey(table.getOwner().toUpperCase())) {
                        grantScripts.put(table.getOwner().toUpperCase(), new HashSet<>());
                    }

                    grantScripts.get(table.getOwner().toUpperCase()).add("grant select on " + table.getOwner() + "." + table.getTableName() + " to " + App.getProjectModel().getConfiguration().getTargetSchema() + ";".toUpperCase());
                }


            } catch (JSQLParserException e) {
                throw new VauException(e);
            }
        }
        return grantScripts;
    }

    public class SourceTableGrantPerOwnerGenerator implements Generator {

        private final String owner;
        private final Set<String> grants;

        public SourceTableGrantPerOwnerGenerator(String owner, Set<String> grants) {
            this.owner = owner;
            this.grants = grants;
        }

        @Override
        public String toString() {
            StringBuffer sb = new StringBuffer();
            for (String grant : grants) {
                sb.append(grant + "\n");
            }

            return sb.toString();
        }

        @Override
        public String getFileName() {
            return "grant_from_" + owner.toLowerCase() + ".sql";
        }

        @Override
        public OutputType getOutputType() {
            return OutputType.GRANT;
        }
    }

}
