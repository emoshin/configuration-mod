package by.emoshin.lookup;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LookupTests {

    private Lookup lookup;

    @Test
    public void environmentLookupTest() {
        lookup = new EnvironmentLookup();
        String pathVariable = String.valueOf(lookup.lookup("PATH"));
        assertThat(pathVariable).as("PATH exists in system environment variable").isNotEmpty();
        assertThat(pathVariable).as("PATH contains JAVA_HOME variable").contains("JAVA_HOME");
    }

    @Test
    public void systemPropertiesLookupTest() {
        lookup = new SystemPropertiesLookup();
        String jvmVendorVariable = String.valueOf(lookup.lookup("java.vm.vendor"));
        assertThat(jvmVendorVariable).as("java.vm.vendor exists in system properties").isNotEmpty();
    }
}
