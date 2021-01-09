package by.emoshin.impl;

import by.emoshin.ConfigurationManager;
import by.emoshin.Parameter;
import by.emoshin.properties.PropertyReader;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

public class BaseConfiguration extends AbstractConfiguration {

    private final List<Parameter<?>> store;

    public BaseConfiguration() {
        this.store = new CopyOnWriteArrayList<>();
        PropertyReader reader = PropertyReader.getPropertyReaderForEnvironment(ConfigurationManager.getEnvironment());
        setPropertyReader(reader);
    }

    @Override
    protected List<Parameter<?>> getParametersInternal() {
        return store;
    }

    @Override
    protected <T> void addParameterInternal(String key, T value) {
        store.add(ParameterImpl.createParameter(key, value));
    }

    @Override
    protected void removeParameterInternal(String paramName) {
        store.stream().filter(p -> p.getName().equals(paramName)).findFirst().map(p -> {
            store.remove(p);
            return store;
        });
    }
}
