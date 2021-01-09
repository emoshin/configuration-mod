package by.emoshin;

import java.util.Objects;

public class ConfigurationManager {

    private static Environment currentEnvironment;

    public enum Environment {
        UAT,
        OSA,
        PROD;

        private static Environment fromString(String strEnv) {
            Environment result = null;
            for (Environment environment : values()) {
                if (environment.name().equalsIgnoreCase(strEnv)) {
                    result = environment;
                    break;
                }
            }
            Objects.requireNonNull(result, "<" + strEnv + "> was not found in configured Environments!!");
            return result;
        }
    }

    private ConfigurationManager() {
    }

    public static Environment getEnvironment() {
        if (currentEnvironment == null) {
            String sysProp = System.getProperty("environment");
            if (sysProp == null) {
                throw new IllegalArgumentException("Environment must be configured!!");
            } else {
                currentEnvironment = Environment.fromString(sysProp);
            }
        }
        return Objects.requireNonNull(currentEnvironment, "Current environment must be configured before use!");
    }

    public static void setEnvironment(Environment env) {
        currentEnvironment = Objects.requireNonNull(env, "Environment must be not null!!");
    }

    public static Configuration getConfiguration() {
        return null;
    }
}
