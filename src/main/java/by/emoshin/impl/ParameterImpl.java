package by.emoshin.impl;

import by.emoshin.Parameter;

import java.util.Objects;

public class ParameterImpl<T> implements Parameter<T> {

    private final String paramName;
    private final T paramValue;

    private ParameterImpl(String paramName, T paramValue) {
        this.paramName = paramName;
        this.paramValue = paramValue;
    }

    @Override
    public String getName() {
        return paramName;
    }

    @Override
    public T getValue() {
        return paramValue;
    }

    @Override
    public String toString() {
        return "ParameterImpl{" +
                "paramName='" + paramName + '\'' +
                ", paramValue=" + paramValue +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParameterImpl<?> parameter = (ParameterImpl<?>) o;
        return Objects.equals(paramName, parameter.paramName) && Objects.equals(paramValue, parameter.paramValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paramName, paramValue);
    }

    public static <T> ParameterImpl<T> createParameter(String paramName, T paramValue) {
        return new ParameterImpl<>(paramName, paramValue);
    }
}
