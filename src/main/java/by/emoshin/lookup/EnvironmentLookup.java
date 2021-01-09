package by.emoshin.lookup;

import by.emoshin.exception.VariableNotFoundException;

public class EnvironmentLookup implements Lookup {

    @Override
    public String lookup(String variable) {
        String result = System.getenv(variable);
        if (result == null) {
            throw new VariableNotFoundException("Variable <" + variable + "> was not found in environment variables!!");
        }
        return result;
    }
}
