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

package hu.karsany.vau.project.mapping.sqlmap;

import hu.karsany.vau.grammar.loader.LoaderBaseListener;
import hu.karsany.vau.grammar.loader.LoaderLexer;
import hu.karsany.vau.project.datamodel.model.DataModel;
import hu.karsany.vau.project.mapping.generator.LoaderParameter;
import hu.karsany.vau.project.mapping.MappingParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class SqlMappingParser extends LoaderBaseListener implements MappingParser {

    private final File loaderFile;
    private final LoaderParameter loaderParameter = new LoaderParameter();

    public SqlMappingParser(File loaderFile, DataModel m) throws IOException {
        this.loaderFile = loaderFile;
        loaderParameter.setDataModel(m);
        fillConfigAndScript();

        LoaderLexer lexer = new LoaderLexer(new ANTLRInputStream(loaderParameter.getLoaderConfig()));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        hu.karsany.vau.grammar.loader.LoaderParser parser = new hu.karsany.vau.grammar.loader.LoaderParser(tokens);
        hu.karsany.vau.grammar.loader.LoaderParser.SContext tree = parser.s();
        ParseTreeWalker.DEFAULT.walk(this, tree);
    }

    private void fillConfigAndScript() throws IOException {
        List<String> lines = Files.readAllLines(loaderFile.toPath());

        this.loaderParameter.setSqlScript("");
        this.loaderParameter.setLoaderConfig("");
        boolean isSql = false;
        for (String l : lines) {
            if (!isSql) {
                loaderParameter.setLoaderConfig(loaderParameter.getLoaderConfig() + l + "\n");
            }
            if (isSql) {
                loaderParameter.setSqlScript(loaderParameter.getSqlScript() + l + "\n");
            }
            if (l.startsWith("-- sql")) {
                isSql = true;
            }

        }
    }


    @Override
    public void enterHub(hu.karsany.vau.grammar.loader.LoaderParser.HubContext ctx) {
        loaderParameter.setLoaderType(LoaderParameter.LoaderType.HUB);
    }

    @Override
    public void enterSat(hu.karsany.vau.grammar.loader.LoaderParser.SatContext ctx) {
        loaderParameter.setLoaderType(LoaderParameter.LoaderType.SATTELITE);
    }

    @Override
    public void enterLink(hu.karsany.vau.grammar.loader.LoaderParser.LinkContext ctx) {
        loaderParameter.setLoaderType(LoaderParameter.LoaderType.LINK);
    }

    @Override
    public void enterLoad_method_name(hu.karsany.vau.grammar.loader.LoaderParser.Load_method_nameContext ctx) {
        loaderParameter.setSatteliteLoadMethod(LoaderParameter.SatteliteLoadMethod.valueOf(ctx.getText().toUpperCase()));
    }

    @Override
    public void enterDatagroup_name(hu.karsany.vau.grammar.loader.LoaderParser.Datagroup_nameContext ctx) {
        loaderParameter.setDataGroupName(ctx.getText());
    }

    @Override
    public void enterEntity_name(hu.karsany.vau.grammar.loader.LoaderParser.Entity_nameContext ctx) {
        loaderParameter.setEntityName(ctx.getText());
    }

    @Override
    public void enterRef(hu.karsany.vau.grammar.loader.LoaderParser.RefContext ctx) {
        loaderParameter.setLoaderType(LoaderParameter.LoaderType.REFERENCE);
    }

    @Override
    public void enterReference_name(hu.karsany.vau.grammar.loader.LoaderParser.Reference_nameContext ctx) {
        loaderParameter.setReferenceName(ctx.getText());
    }

    @Override
    public void enterSource_system_name(hu.karsany.vau.grammar.loader.LoaderParser.Source_system_nameContext ctx) {
        loaderParameter.setSourceSystemName(ctx.getText());
    }

    @Override
    public void enterLink_name(hu.karsany.vau.grammar.loader.LoaderParser.Link_nameContext ctx) {
        loaderParameter.setLinkName(ctx.getText());
    }

    @Override
    public List<LoaderParameter> getMapping() {
        ArrayList<LoaderParameter> loaderParameters = new ArrayList<>();
        loaderParameters.add(loaderParameter);
        return loaderParameters;
    }
}
