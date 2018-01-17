package hu.karsany.vau.project.datamodel.model;

import hu.karsany.vau.common.VauException;
import hu.karsany.vau.project.datamodel.parser.DataModelInitializer;
import org.junit.Assert;
import org.junit.Test;

public class LinkTest {

    @Test
    public void issue2_columnNamesAreUniqueValidation() {

        DataModelInitializer dataModelInitializer = new DataModelInitializer();
        try {
            dataModelInitializer.addModelDefinition("link EMPLOYEE_MANAGER between EMPLOYEE and EMPLOYEE;");
            Assert.assertFalse(true);
        } catch (VauException e) {
            Assert.assertEquals("Table LNK_EMPLOYEE_MANAGER contains the EMPLOYEE_ID. Specify an alias.", e.getMessage());
        }

    }

    @Test
    public void issue2_linkBetweenCanHaveAlias() {

        DataModelInitializer dataModelInitializer = new DataModelInitializer();
        dataModelInitializer.addModelDefinition("link EMPLOYEE_MANAGER between EMPLOYEE as MANAGER and EMPLOYEE;");

        DataModel dataModel = dataModelInitializer.getDataModel();
        Link link = dataModel.getLink("EMPLOYEE_MANAGER");
        Assert.assertEquals("Column names are unique", link.getColumns().size(), link.getColumns().stream().map(Column::getColumnName).distinct().count());
        Assert.assertTrue(link.getColumns().contains(new Column("MANAGER_ID", "NUMBER(20)", "Comment")));
        Assert.assertTrue(link.getColumns().contains(new Column("EMPLOYEE_ID", "NUMBER(20)", "Comment")));

    }

}