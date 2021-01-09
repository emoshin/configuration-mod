package by.emoshin.lookup;

import by.emoshin.exception.VariableNotFoundException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ConfigurationInterpolator {

    private static final String VAR_START = "${";
    private static final String VAR_END = "}";
    private static final Map<String, Lookup> defaultLookups;

    static {
        Map<String, Lookup> lookups = new HashMap<>();
        for (DefaultLookups lookup : DefaultLookups.values()) {
            lookups.put(lookup.getPrefix(), lookup.getLookup());
        }
        defaultLookups = Collections.unmodifiableMap(lookups);
    }

    public static Map<String, Lookup> getDefaultPrefixLookups() {
        return defaultLookups;
    }

    public void registerLookup(String rawPrefix, Lookup rawLookup) {
        String prefix = Objects.requireNonNull(rawPrefix, "Prefix for lookup must not be null!!");
        Lookup lookup = Objects.requireNonNull(rawLookup, "Lookup must not be null!!");
        defaultLookups.put(prefix, lookup);
    }

    public boolean deregisterLookupForPrefix(String prefix) {
        return defaultLookups.remove(prefix) != null;
    }

    public Lookup fetchLookupForPrefix(String prefix) {
        return defaultLookups.get(prefix);
    }

    public Object interpolate(Object value) {
        Object result = null;
        if (value instanceof String) {
            String strVal = String.valueOf(value);
            if (isVariable(strVal)) {
                strVal = extractVariableName(strVal);
                result = resolve(strVal);
            }
        }
        return result;
    }

    private Object resolve(String strVal) {
        if (null == strVal) return null;
        String lookupPrefix = null;
        if (strVal.contains(".")) {
            lookupPrefix = strVal.substring(0, strVal.indexOf(".") + 1);
        }
        if (null != lookupPrefix) {
            Lookup lookup = defaultLookups.get(lookupPrefix);
            strVal = strVal.substring(lookupPrefix.length());
            try {
                Object obj = lookup.lookup(strVal);
                if (null != obj) {
                    return obj;
                }
            }catch (VariableNotFoundException ex){

            }
        }
        return null;
    }

    private boolean isVariable(String variable) {
        return variable.startsWith(VAR_START) && variable.endsWith(VAR_END);
    }

    private String extractVariableName(String strVal) {
        return strVal.substring(VAR_START.length(), strVal.length() - VAR_END.length());
    }

}
