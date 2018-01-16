package hu.karsany.vau;

import hu.karsany.vau.project.Project;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class AppTest {

    @Test
    public void oracleHrExampleTest() throws IOException {
        App app = new App();
        app.app("clean", "compile", "-d", "../vau-others/examples/oracle-hr");

        Project projectModel = app.getProjectModel();

        Assert.assertNotNull(projectModel);
        Assert.assertEquals("Oracle HR database Data Vault Example", projectModel.getConfiguration().getName());

    }
}