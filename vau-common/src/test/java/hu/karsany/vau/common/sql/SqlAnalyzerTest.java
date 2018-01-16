package hu.karsany.vau.common.sql;

import org.junit.Test;

public class SqlAnalyzerTest {

    @Test
    public void getMapping() {

        SqlAnalyzer sqlAnalyzer = new SqlAnalyzer("SELECT p AS c FROM dual");
        System.out.println(sqlAnalyzer.getMapping());

    }
}