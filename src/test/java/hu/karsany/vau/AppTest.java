package hu.karsany.vau;

import hu.karsany.vau.project.Project;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class AppTest {

    @Test
    public void oracleHrExampleTestCleanCompile() throws IOException, IllegalAccessException, InstantiationException {
        App app = new App();
        app.app("-d", "examples/oracle-hr", "clean", "compile");

        Project projectModel = ApplicationContext.getProject();

        Assert.assertNotNull(projectModel);
        Assert.assertEquals("Oracle HR database Data Vault Example", projectModel.getConfiguration().getName());
    }

    @Test
    public void oracleHrExampleTestDoc() throws IOException, IllegalAccessException, InstantiationException {
        App app = new App();
        app.app("-d", "examples/oracle-hr", "doc");
    }
    
}
