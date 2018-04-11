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

import hu.karsany.vau.grammar.simplemap.SimplemapBaseListener;
import hu.karsany.vau.grammar.simplemap.SimplemapBaseVisitor;
import hu.karsany.vau.grammar.simplemap.SimplemapLexer;
import hu.karsany.vau.grammar.simplemap.SimplemapParser;
import hu.karsany.vau.project.mapping.model.simplemap.model.SimplemapDataGroupMapping;
import hu.karsany.vau.project.mapping.model.simplemap.model.SimplemapEntry;
import hu.karsany.vau.project.mapping.model.simplemap.model.SimplemapModel;
import hu.karsany.vau.project.mapping.model.simplemap.model.SourceDefinition;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

class SimplemapEvaluation extends SimplemapBaseListener {
    private SimplemapModel simplemapModel = new SimplemapModel();

    public SimplemapEvaluation(CharStream input) {
        SimplemapLexer lexer = new SimplemapLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        SimplemapParser parser = new SimplemapParser(tokens);
        SimplemapParser.SContext tree = parser.s();
        ParseTreeWalker.DEFAULT.walk(this, tree);
    }

    public SimplemapEvaluation(File file) throws IOException {
        this(CharStreams.fromStream(FileUtils.openInputStream(file)));
    }

    public SimplemapModel getSimplemapModel() {
        return simplemapModel;
    }

    @Override
    public void enterEntry(SimplemapParser.EntryContext ctx) {
        simplemapModel.add(new EntryVisitor().visitEntry(ctx));
    }

    private class EntryVisitor extends SimplemapBaseVisitor<SimplemapEntry> {

        SimplemapEntry se = new SimplemapEntry();

        @Override
        protected SimplemapEntry defaultResult() {
            return se;
        }

        @Override
        public SimplemapEntry visitEntry(SimplemapParser.EntryContext ctx) {
            se.setEntityName(ctx.entity_name().getText());
            return visitChildren(ctx);
        }

        @Override
        public SimplemapEntry visitSource(SimplemapParser.SourceContext ctx) {

            se.setSourceDefinition(new SourceDefinition(
                    ctx.system_name().getText(),
                    ctx.owner_name().getText(),
                    ctx.table_name().getText(),
                    ctx.containing_type().getText()
            ));

            return visitChildren(ctx);
        }

        @Override
        public SimplemapEntry visitBusiness_key_column(SimplemapParser.Business_key_columnContext ctx) {
            se.setBusinessKey(ctx.getText());
            return visitChildren(ctx);
        }

        @Override
        public SimplemapEntry visitDatagroup(SimplemapParser.DatagroupContext ctx) {
            se.addDataGroupMappingSpecification(new DataGroupVisitor().visitDatagroup(ctx));
            return se;
        }
    }

    private class DataGroupVisitor extends SimplemapBaseVisitor<SimplemapDataGroupMapping> {
        SimplemapDataGroupMapping dms = new SimplemapDataGroupMapping();

        @Override
        protected SimplemapDataGroupMapping defaultResult() {
            return dms;
        }

        @Override
        public SimplemapDataGroupMapping visitDatagroup(SimplemapParser.DatagroupContext ctx) {
            dms.setDatagroupName(ctx.datagroup_name().getText());
            return visitChildren(ctx);
        }

        @Override
        public SimplemapDataGroupMapping visitMapping_spec(SimplemapParser.Mapping_specContext ctx) {
            dms.addMapping(ctx.target_column_name().getText(), ctx.source_expression().getText());
            return visitChildren(ctx);
        }
    }
}
