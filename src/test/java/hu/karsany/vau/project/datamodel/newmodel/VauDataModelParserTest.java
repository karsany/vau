package hu.karsany.vau.project.datamodel.newmodel;

import hu.karsany.vau.project.datamodel.model.DataModel;
import org.junit.Test;

public class VauDataModelParserTest {

    @Test
    public void parse() {
        VauDataModelParser vauDataModelParser = new VauDataModelParser("ref COUNTRY_CODE {\n" +
                "\tkey COUNTRY_CODE typ SMALLTEXT;\n" +
                "\tattr COUNTRY_NAME typ MIDDLETEXT;\n" +
                "\tattr REGION_NAME typ MIDDLETEXT;\n" +
                "}\n" +
                "\n" +
                "entity LOCATION {\n" +
                "\tdatagroup MAIN {\n" +
                "\t\tattr POSTAL_CODE typ MIDDLETEXT; // hello\n" +
                "\t\tattr CITY typ MIDDLETEXT;\n" +
                "\t\tattr STREET_ADDRESS typ MIDDLETEXT;\n" +
                "\t\tattr STATE_PROVINCE typ MIDDLETEXT;\n" +
                "\t\tattr COUNTRY_CODE typ NATIVE(\"VARCHAR2(3)\") references COUNTRY_CODE;\n" +
                "\t}\n" +
                "}\n" +
                "\n" +
                "entity JOB {\n" +
                "\tdatagroup MAIN {\n" +
                "\t\tattr JOB_TITLE typ MIDDLETEXT;\n" +
                "\t\tattr MIN_SALARY typ MONEY;\n" +
                "\t\tattr MAX_SALARY typ MONEY;\t\t\n" +
                "\t}\n" +
                "}\n" +
                "\n" +
                "entity DEPARTMENT {\n" +
                "\tdatagroup MAIN {\n" +
                "\t\tattr DEPARTMENT_NAME typ MIDDLETEXT;\n" +
                "\t}\n" +
                "}\n" +
                "\n" +
                "entity EMPLOYEE {\n" +
                "\tdatagroup MAIN {\n" +
                "\t\tattr FIRST_NAME typ MIDDLETEXT;\n" +
                "\t\tattr LAST_NAME typ MIDDLETEXT;\n" +
                "\t\tattr EMAIL typ MIDDLETEXT;\n" +
                "\t\tattr PHONE_NUMBER typ MIDDLETEXT;\n" +
                "\t\tattr HIRE_DATE typ DATE;\n" +
                "\t\tattr SALARY typ MONEY;\n" +
                "\t\tattr COMMISSION_PCT typ PERCENTAGE;\t\t\n" +
                "\t}\n" +
                "}\n" +
                "\n" +
                "link DEPARTMENT_LOCATION between DEPARTMENT and LOCATION;\n" +
                "\n" +
                "link DEPARTMENT_MANAGER between DEPARTMENT and EMPLOYEE;\n" +
                "\n" +
                "link EMPLOYEE_MANAGER between EMPLOYEE as MANAGER and EMPLOYEE;\n" +
                "\n" +
                "link EMPLOYEE_DEPARTMENT between EMPLOYEE and DEPARTMENT;\n" +
                "\n" +
                "link EMPLOYEE_JOB between EMPLOYEE and JOB;");
        DataModel dataModel = vauDataModelParser.parse();

    }
}