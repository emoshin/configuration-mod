package by.emoshin.lookup;

public enum DefaultLookups {

    SYSTEM("sys.", new SystemPropertiesLookup()),
    ENVIRONMENT("env.", new EnvironmentLookup());

    private final String prefix;
    private final Lookup lookup;

    DefaultLookups(String prefix, Lookup lookup) {
        this.lookup = lookup;
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    public Lookup getLookup() {
        return lookup;
    }
}
