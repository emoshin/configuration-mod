package by.emoshin.properties;

import by.emoshin.ConfigurationManager;
import by.emoshin.Parameter;
import by.emoshin.commonutils.ResourceUtils;
import by.emoshin.commonutils.exceptions.ResourceException;
import by.emoshin.exception.VariableNotFoundException;
import by.emoshin.impl.ParameterImpl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public class PropertyReader {

    public static final PropertyReader GLOBAL_PROP = new PropertyReader("global.properties");
    private static final PropertyReader OSAT_PROP = new PropertyReader("osat.properties");
    private static final PropertyReader PROD_PROP = new PropertyReader("prod.properties");
    private static final PropertyReader UAT_PROP = new PropertyReader("uat.properties");

    private final String fileName;
    private final Properties properties;

    private PropertyReader(final String fileName) {
        this.fileName = fileName;
        this.properties = parseProperties();
    }

    public String getPropertyValue(final String rawPropName) {
        String propName = Objects.requireNonNull(rawPropName, "Property name must be not null!!");
        String foundPropName = properties.stringPropertyNames().stream().filter(p -> p.equals(propName)).findFirst()
                .orElseThrow(() -> new VariableNotFoundException("Variable <" + propName + "> was not found in predefined properties!!"));
        return properties.getProperty(foundPropName);
    }

    public Parameter<?> getParameter(String propName) {
        String propertyVal = getPropertyValue(propName);
        return ParameterImpl.createParameter(propName, propertyVal);
    }

    @SuppressWarnings("unchecked")
    public <T> T getPropertyValueOrDefault(String propName, T defaultValue) {
        T result = null;
        try {
            //TODO add conversion
            result = (T) getPropertyValue(propName);
        } catch (VariableNotFoundException ex) {
            result = defaultValue;
        }
        return result;
    }

    public String getPropertyValueOrDefaultString(String propName, String defaultValue) {
        return getPropertyValueOrDefault(propName, defaultValue);
    }

    private Properties parseProperties() {
        InputStream fileInputStream;
        Properties props = new Properties();
        try {
            fileInputStream = ResourceUtils.getResourceUrl("configurations/" + fileName).openStream();
            props.load(fileInputStream);
        } catch (IOException e) {
            throw new ResourceException(e);
        }
        return props;
    }

    public static PropertyReader getPropertyReaderForEnvironment(ConfigurationManager.Environment rawEnv) {
        ConfigurationManager.Environment env = Objects.requireNonNull(rawEnv, "Environment must be not null!!");
        PropertyReader reader;
        switch (env) {
            case OSA:
                reader = OSAT_PROP;
                break;
            case UAT:
                reader = UAT_PROP;
                break;
            case PROD:
                reader = PROD_PROP;
                break;
            default:
                reader = GLOBAL_PROP;
        }
        return reader;
    }
}
