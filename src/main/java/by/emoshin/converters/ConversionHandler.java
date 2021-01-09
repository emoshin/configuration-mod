package by.emoshin.converters;

public interface ConversionHandler<T> {

    <T> T to(Object src, Class<T> cls);

}
