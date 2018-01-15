package hu.karsany.vau.util.sql;

import org.junit.Test;

import static org.junit.Assert.*;

public class SqlAnalyzerTest {

    @Test
    public void getMapping() {

        SqlAnalyzer sqlAnalyzer = new SqlAnalyzer("SELECT p AS c FROM dual");
        System.out.println(sqlAnalyzer.getMapping());

    }
}