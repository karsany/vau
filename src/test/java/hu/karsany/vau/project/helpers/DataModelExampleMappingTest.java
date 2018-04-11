package hu.karsany.vau.project.helpers;

import hu.karsany.vau.project.datamodel.generator.other.DataModelExampleMapping;
import hu.karsany.vau.project.datamodel.model.Link;
import hu.karsany.vau.project.datamodel.parser.DataModelInitializer;
import org.junit.Assert;
import org.junit.Test;

public class DataModelExampleMappingTest {

    @Test
    public void issue2_example2_good_column_names() {
        DataModelInitializer dataModelInitializer = new DataModelInitializer();
        dataModelInitializer.addModelDefinition("link EMPLOYEE_MANAGER between EMPLOYEE as MANAGER and EMPLOYEE;");

        Link employeeManagerLink = dataModelInitializer.getDataModel().getLink("EMPLOYEE_MANAGER");

        String generatedScript = new DataModelExampleMapping(employeeManagerLink).toString();

        Assert.assertEquals("Generated script contains EMPLOYEE_BK one time", generatedScript.replaceAll("employee_bk", ""), generatedScript.replaceFirst("employee_bk", ""));
        Assert.assertTrue("Generated script contains EMPLOYEE_BK", generatedScript.toUpperCase().contains("EMPLOYEE_BK"));
        Assert.assertTrue("Generated script contains MANAGER_BK", generatedScript.toUpperCase().contains("MANAGER_BK"));

    }

}