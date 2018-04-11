package hu.karsany.vau.grammar;

import hu.karsany.vau.grammar.datamodel.DataModelBaseListener;
import hu.karsany.vau.grammar.datamodel.DataModelLexer;
import hu.karsany.vau.grammar.datamodel.DataModelParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;

import java.io.IOException;

public class DataModelTest extends DataModelBaseListener {

    @Test
    public void issue2_aliasedEntityInLinks() throws IOException {
        DataModelLexer lexer = new DataModelLexer(CharStreams.fromStream(getClass().getClassLoader().getResourceAsStream("link_with_aliased_entity_name.sum")));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        DataModelParser parser = new DataModelParser(tokens);
        DataModelParser.SContext tree = parser.s();
        ParseTreeWalker.DEFAULT.walk(this, tree);
    }

    @Test
    public void issue11_handleNativeDBDatatypes() throws IOException {
        DataModelLexer lexer = new DataModelLexer(CharStreams.fromStream(getClass().getClassLoader().getResourceAsStream("issue11_handle_native_db_types.sum")));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        DataModelParser parser = new DataModelParser(tokens);
        DataModelParser.SContext tree = parser.s();
        ParseTreeWalker.DEFAULT.walk(this, tree);

    }

}
