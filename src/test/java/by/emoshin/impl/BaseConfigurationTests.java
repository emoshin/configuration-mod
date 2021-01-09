package by.emoshin.impl;

import by.emoshin.Configuration;
import by.emoshin.ConfigurationManager;
import by.emoshin.Parameter;
import by.emoshin.Parameters;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BaseConfigurationTests {

    static {
        ConfigurationManager.setEnvironment(ConfigurationManager.Environment.PROD);
    }

    @Test
    public void testBase() {
        Configuration config = new BaseConfiguration();
        Parameter<String> parameterToAdd = ParameterImpl.createParameter("my.third.param", "test3");
        config.addParameter(parameterToAdd);
        Parameter<?> systemEnvParameter = ParameterImpl.createParameter("docker.host", "${env.DOCKER_HOST}");
        config.addParameter(systemEnvParameter);
        Parameter<?> systemPropParameter = ParameterImpl.createParameter("java.vm.vendor", "${sys.java.vm.vendor}");
        Parameter<Boolean> booleanParameter = ParameterImpl.createParameter("boolean.parameter", true);
        config.addParameter(systemPropParameter);
        config.addParameter(booleanParameter);
        assertThat(config.getKeys()).hasSize(4);
        assertThat(config.getParameter("my.third.param")).isEqualTo(parameterToAdd);
        assertThat(config.getParameter("docker.host").getValue()).isEqualTo("tcp://192.168.56.101:2375");
        assertThat(config.getParameter("java.vm.vendor").getValue()).isEqualTo("BellSoft");
        assertThat(config.getParameter(Parameters.ENVIRONMENT).getValue()).isEqualTo("PROD");
    }
}
