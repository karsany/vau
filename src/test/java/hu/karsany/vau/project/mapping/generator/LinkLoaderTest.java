package hu.karsany.vau.project.mapping.generator;

import hu.karsany.vau.project.datamodel.parser.DataModelInitializer;
import hu.karsany.vau.project.mapping.generator.loader.LinkLoader;
import hu.karsany.vau.project.mapping.generator.loader.LoaderParameter;
import org.junit.Assert;
import org.junit.Test;

public class LinkLoaderTest {

    @Test
    public void issue2_link_loader_generates_ok_code() {

        DataModelInitializer dmi = new DataModelInitializer();
        dmi.addModelDefinition("link EMPLOYEE_MANAGER between EMPLOYEE as MANAGER and EMPLOYEE;");

        LoaderParameter lp = new LoaderParameter();
        lp.setDataModel(dmi.getDataModel());
        lp.setLinkName("EMPLOYEE_MANAGER");
        lp.setSqlScript("Select employee_id employee_bk,\n" +
                "       manager_id  manager_bk\n" +
                "  From hr.employees\n" +
                " Where manager_id Is Not Null");
        lp.setSourceSystemName("HR");
        LinkLoader linkLoader = new LinkLoader(lp);
        String script = linkLoader.toString();
        System.out.println(script);

        Assert.assertFalse("Does not contain ${h.", script.contains("${h."));
        Assert.assertTrue("Contains employee_id string", script.toUpperCase().contains("EMPLOYEE_ID"));
        Assert.assertTrue("Contains manager_id string", script.toUpperCase().contains("MANAGER_ID"));
        Assert.assertTrue("Contains 'hub_employee manager' string", script.toUpperCase().contains("hub_employee manager".toUpperCase()));


    }

}