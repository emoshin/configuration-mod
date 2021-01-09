package by.emoshin.lookup;

import by.emoshin.exception.VariableNotFoundException;

public class SystemPropertiesLookup extends ConfigurationInterpolator implements Lookup {

    @Override
    public String lookup(String variable) {
        String result;
        try {
            result = System.getProperty(variable);
        } catch (NullPointerException | IllegalArgumentException ex) {
            throw new VariableNotFoundException("Variable <" + variable + "> was not found in system properties!!");
        }
        return result;
    }
}
