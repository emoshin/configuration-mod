package by.emoshin.impl;

import by.emoshin.Configuration;
import by.emoshin.Parameter;
import by.emoshin.event.BaseEventSource;
import by.emoshin.event.ConfigurationEvent;
import by.emoshin.exception.VariableNotFoundException;
import by.emoshin.lookup.ConfigurationInterpolator;
import by.emoshin.properties.PropertyReader;
import by.emoshin.sync.LockMode;
import by.emoshin.sync.NoOpSynchronizer;
import by.emoshin.sync.Synchronizer;

import java.util.List;
import java.util.NoSuchElementException;

import static by.emoshin.properties.PropertyReader.GLOBAL_PROP;
import static java.util.stream.Collectors.toList;

public abstract class AbstractConfiguration extends BaseEventSource implements Configuration {

    private Synchronizer currentSynchronizer;
    private static final ConfigurationInterpolator interpolator = new ConfigurationInterpolator();
    private PropertyReader environmentBasedPropertyReader;

    public void setPropertyReader(PropertyReader reader) {
        this.environmentBasedPropertyReader = reader;
    }

    @Override
    public <T> void setParameter(String paramName, T paramValue) {
        getSynchronizer().beginWrite();
        try {
            fireEvent(ConfigurationEvent.SET_PROPERTY, paramName, paramValue, true);
            setParameterInternal(paramName, paramValue);
            fireEvent(ConfigurationEvent.SET_PROPERTY, paramName, paramValue, false);
        } finally {
            getSynchronizer().endWrite();
        }
    }

    @Override
    public List<Parameter<?>> getParameters() {
        getSynchronizer().beginRead();
        try {
            return getParametersInternal();
        } finally {
            getSynchronizer().endRead();
        }
    }

    protected abstract List<Parameter<?>> getParametersInternal();

    @Override
    public List<String> getKeys() {
        return getParameters().stream()
                .map(Parameter::getName)
                .collect(toList());
    }

    @Override
    public Parameter<?> getParameter(String paramName) {
        getSynchronizer().beginRead();
        try {
            fireEvent(ConfigurationEvent.GET_PROPERTY, paramName, null, true);
            Parameter<?> result = null;
            for (Parameter<?> parameter : getParameters()) {
                if (parameter.getName().equals(paramName)) {
                    result = parameter;
                    break;
                }
            }
            if (null == result) {
                try {
                    result = environmentBasedPropertyReader.getParameter(paramName);
                } catch (VariableNotFoundException ex) {
                    result = GLOBAL_PROP.getParameter(paramName);
                }
                //TODO add logs
                addParameter(paramName, result);//Add parameter due to unexistence
            }
            fireEvent(ConfigurationEvent.GET_PROPERTY, paramName, result.getValue(), false);
            return result;
        } finally {
            getSynchronizer().endRead();
        }
    }

    @Override
    public <T> Parameter<?> getOrDefault(String paramName, T defaultValue) {
        try {
            return getParameter(paramName);
        } catch (NoSuchElementException ignore) {
            return ParameterImpl.createParameter(paramName, defaultValue);
        }
    }

    @Override
    public <T> void addParameter(String paramName, T paramValue) {
        getSynchronizer().beginWrite();
        try {
            fireEvent(ConfigurationEvent.ADD_PROPERTY, paramName, paramValue, true);
            addParameterInternal(paramName, interpolate(paramValue));
            fireEvent(ConfigurationEvent.ADD_PROPERTY, paramName, paramValue, false);
        } finally {
            getSynchronizer().endWrite();
        }
    }

    protected abstract <T> void addParameterInternal(String key, T value);

    @Override
    public void removeParameter(String paramName) {
        getSynchronizer().beginWrite();
        try {
            fireEvent(ConfigurationEvent.REMOVE_PROPERTY, paramName, null, true);
            removeParameterInternal(paramName);
            fireEvent(ConfigurationEvent.REMOVE_PROPERTY, paramName, null, false);
        } finally {
            getSynchronizer().endWrite();
        }
    }

    protected abstract void removeParameterInternal(String paramName);

    protected <T> void setParameterInternal(String key, T value) {
        setDetailEvents(false);
        try {
            addParameter(key, value);
        } finally {
            setDetailEvents(true);
        }
    }

    @Override
    public Synchronizer getSynchronizer() {
        Synchronizer sync = currentSynchronizer;
        return sync == null ? NoOpSynchronizer.INSTANCE : sync;
    }

    @Override
    public void setSynchronizer(Synchronizer synchronizer) {
        this.currentSynchronizer = synchronizer;
    }

    @Override
    public void lock(LockMode lockMode) {
        switch (lockMode) {
            case READ:
                getSynchronizer().beginRead();
                break;
            case WRITE:
                getSynchronizer().beginWrite();
                break;
            default:
                throw new IllegalArgumentException("Unsupported mode: " + lockMode + "!!");
        }
    }

    @Override
    public void unlock(LockMode lockMode) {
        switch (lockMode) {
            case READ:
                getSynchronizer().endRead();
                break;
            case WRITE:
                getSynchronizer().endWrite();
                break;
            default:
                throw new IllegalArgumentException("Unsupported mode: " + lockMode + "!!");
        }
    }

    @SuppressWarnings("unchecked")
    protected <T> T interpolate(T value) {
        //TODO add conversion handlers
        T result = (T) interpolator.interpolate(value);
        return result == null ? value : result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Parameter<?> param : getParameters()) {
            sb.append(param.getName()).append(" = ").append(param.getValue()).append("\n");
        }
        return sb.toString();
    }
}
