package hu.karsany.vau.common.sql;

import net.sf.jsqlparser.JSQLParserException;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class SqlAnalyzerTest {

    @Test
    public void testGetMapping() {

        SqlAnalyzer sqlAnalyzer = new SqlAnalyzer("SELECT p AS c FROM dual");
        System.out.println(sqlAnalyzer.getMapping());

    }

    @Test
    public void testGetMapping2() {

        SqlAnalyzer sqlAnalyzer = new SqlAnalyzer("Select employee_id    As employee_bk,\n" +
                "                     hire_date      As hire_date,\n" +
                "                     last_name      As last_name,\n" +
                "                     email          As email,\n" +
                "                     phone_number   As phone_number,\n" +
                "                     first_name     As first_name,\n" +
                "                     commission_pct As commission_pct\n" +
                "                From hr.employees");

        List<SqlAnalyzer.MappingData> mapping = sqlAnalyzer.getMapping();
        Assert.assertEquals("employee_id", mapping.get(0).getSourceExpression().toLowerCase());
        Assert.assertEquals("employee_bk", mapping.get(0).getTrgColumnName().toLowerCase());
        Assert.assertEquals("hr.employees", sqlAnalyzer.getTables().get(0).toLowerCase());

    }

    @Test
    public void testGetMapping3() {

        SqlAnalyzer sqlAnalyzer = new SqlAnalyzer("Select employee_id    As employee_bk,\n" +
                "                     hire_date      As hire_date,\n" +
                "                     last_name      As last_name,\n" +
                "                     email          As email,\n" +
                "                     phone_number   As phone_number,\n" +
                "                     first_name     As first_name,\n" +
                "                     commission_pct As commission_pct\n" +
                "                From hr.employees t");

        List<SqlAnalyzer.MappingData> mapping = sqlAnalyzer.getMapping();
        Assert.assertEquals("employee_id", mapping.get(0).getSourceExpression().toLowerCase());
        Assert.assertEquals("employee_bk", mapping.get(0).getTrgColumnName().toLowerCase());
        Assert.assertEquals("hr.employees t", sqlAnalyzer.getTables().get(0).toLowerCase());

    }

}