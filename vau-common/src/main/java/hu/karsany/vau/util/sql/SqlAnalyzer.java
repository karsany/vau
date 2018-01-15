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

package hu.karsany.vau.util.sql;

import com.antlr.grammarsv4.plsql.PlSqlLexer;
import com.antlr.grammarsv4.plsql.PlSqlParser;
import com.antlr.grammarsv4.plsql.PlSqlParserBaseListener;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.util.TablesNamesFinder;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.ArrayList;
import java.util.List;

public class SqlAnalyzer {
    private final String sqlScript;
    private PlSqlListenerImpl pli = null;

    public SqlAnalyzer(String sqlScript) {
        this.sqlScript = sqlScript;
    }

    public List<MappingData> getMapping() {
        if (pli == null) {
            pli = getPlSqlListener();
        }

        return pli.getMapping();
    }

    public List<String> getTables() {
        if (pli == null) {
            pli = getPlSqlListener();
        }

        return pli.getTables();

    }

    private PlSqlListenerImpl getPlSqlListener() {

        PlSqlLexer lexer = new PlSqlLexer(new CaseChangingCharStream(new ANTLRInputStream(sqlScript), true));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PlSqlParser parser = new PlSqlParser(tokens);

        PlSqlParser.Select_statementContext ctx = parser.select_statement();
        PlSqlListenerImpl pli = new PlSqlListenerImpl();
        ParseTreeWalker.DEFAULT.walk(pli, ctx);
        return pli;
    }

    public List<Table> getInputTables() throws JSQLParserException {
        Statement s = CCJSqlParserUtil.parse(sqlScript);
        TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();

        List<Table> tables = new ArrayList<>();

        for (String tbl : tablesNamesFinder.getTableList(s)) {
            if (tbl.contains(".")) {
                String[] tbb = tbl.toUpperCase().split("\\.");
                tables.add(new Table(tbb[0], tbb[1]));
            } else {
                tables.add(new Table("", tbl.toUpperCase()));
            }
        }

        return tables;
    }

    private class PlSqlListenerImpl extends PlSqlParserBaseListener {

        private List<MappingData> mdl = new ArrayList<>();
        private List<String> tbls = new ArrayList<>();

        private String currentColAlias = "";

        @Override
        public void enterSelected_element(PlSqlParser.Selected_elementContext ctx) {
            try {
                currentColAlias = ctx.column_alias().identifier().getText().toUpperCase();
            } catch (NullPointerException npe) {
                currentColAlias = "$TMP$COLUMN$";
            }
        }

        @Override
        public void enterGeneral_element_part(PlSqlParser.General_element_partContext ctx) {

            if (ctx.function_argument() == null) {
                mdl.add(new MappingData(
                        currentColAlias,
                        ctx.getText().toUpperCase()));
            }
        }


        public List<String> getTables() {
            return tbls;
        }

        @Override
        public void enterTable_ref_aux(PlSqlParser.Table_ref_auxContext ctx) {
            String tableName = "";
            String alias = "";
            try {

                PlSqlParser.Table_ref_aux_internalContext ic = ctx.table_ref_aux_internal();
                PlSqlParser.Dml_table_expression_clauseContext ecc = null;
                if (ic instanceof PlSqlParser.Table_ref_aux_internal_oneContext) {
                    ecc = ((PlSqlParser.Table_ref_aux_internal_oneContext) ic).dml_table_expression_clause();
                } else if (ic instanceof PlSqlParser.Table_ref_aux_internal_threeContext) {
                    ecc = ((PlSqlParser.Table_ref_aux_internal_threeContext) ic).dml_table_expression_clause();
                }

                tableName = ecc.tableview_name().getText().toUpperCase();
                alias = ctx.table_alias().getText().toUpperCase();
                tbls.add((tableName + " " + alias).trim());
            } catch (NullPointerException e) {
                // nothing to do
            }


        }


        public List<MappingData> getMapping() {
            return mdl;
        }
    }

    public class MappingData {
        private String trgColumnName;
        private String sourceExpression;

        public MappingData(String trgColumnName, String sourceExpression) {

            this.trgColumnName = trgColumnName;
            this.sourceExpression = sourceExpression;
        }

        public String getTrgColumnName() {
            return trgColumnName;
        }

        public String getSourceExpression() {
            return sourceExpression;
        }

    }

    public class Table {
        private final String owner;
        private final String tableName;

        public Table(String owner, String tableName) {

            this.owner = owner;
            this.tableName = tableName;
        }

        public String getOwner() {
            return owner;
        }

        public String getTableName() {
            return tableName;
        }
    }
}
