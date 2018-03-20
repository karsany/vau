package hu.karsany.vau.project.configuration;

import com.thoughtworks.xstream.converters.ConversionException;
import org.junit.Assert;
import org.junit.Test;

public class ConfigurationTest {

    @Test
    public void loadConfiguration_1() {
        Configuration configuration = Configuration.loadConfiguration(getClass().getClassLoader().getResourceAsStream("loadConfiguration_1.xml"));
        Assert.assertEquals("template_base.vm", configuration.getTemplate().getTemplateName());
        Assert.assertEquals("procedure", configuration.getTemplate().getTemplateType());
    }

    @Test
    public void loadConfiguration_2() {
        try {
            Configuration configuration = Configuration.loadConfiguration(getClass().getClassLoader().getResourceAsStream("loadConfiguration_2.xml"));
            Assert.assertTrue(false);
        } catch (ConversionException e) {
            Assert.assertTrue(true);
        }
    }
}