package hu.karsany.vau.grammar;

import hu.karsany.vau.grammar.simplemap.SimplemapBaseListener;
import hu.karsany.vau.grammar.simplemap.SimplemapLexer;
import hu.karsany.vau.grammar.simplemap.SimplemapParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by fkarsany on 2017. 12. 15..
 */
public class SimplemapTest extends SimplemapBaseListener {

    @Test
    public void simplemapTc1() throws IOException {
        SimplemapLexer lexer = new SimplemapLexer(CharStreams.fromStream(getClass().getClassLoader().getResourceAsStream("simplemap_testcase1.ssm")));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        SimplemapParser parser = new SimplemapParser(tokens);
        SimplemapParser.SContext tree = parser.s();
        ParseTreeWalker.DEFAULT.walk(this, tree);
    }
    
}
