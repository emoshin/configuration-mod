package by.emoshin;

import by.emoshin.sync.SynchronizerSupport;

import java.util.List;

public interface Configuration extends SynchronizerSupport {

    List<Parameter<?>> getParameters();

    List<String> getKeys();

    Parameter<?> getParameter(String paramName);

    <T> Parameter<?> getOrDefault(String paramName, T defaultValue);

    default <T> void addParameter(Parameter<T> parameter) {
        addParameter(parameter.getName(), parameter.getValue());
    }

    <T> void addParameter(String paramName, T paramValue);

    default void removeParameter(Parameter<?> parameter) {
        removeParameter(parameter.getName());
    }

    void removeParameter(String paramName);

    default <T> void setParameter(Parameter<T> parameter) {
        setParameter(parameter.getName(), parameter.getValue());
    }

    <T> void setParameter(String paramName, T paramValue);

    default boolean containsParameterByName(String paramName) {
        return getParameters().stream().anyMatch(p -> p.getName().equals(paramName));
    }
}
