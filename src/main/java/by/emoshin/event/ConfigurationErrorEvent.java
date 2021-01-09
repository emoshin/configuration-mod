package by.emoshin.event;

public class ConfigurationErrorEvent extends Event {

    public static final EventType<ConfigurationErrorEvent> ANY = new EventType<>(Event.ANY, "ERROR");
    public static final EventType<ConfigurationErrorEvent> READ = new EventType<>(Event.ANY, "READ_ERROR");
    public static final EventType<ConfigurationErrorEvent> WRITE = new EventType<>(Event.ANY, "WRITE_ERROR");

    private final EventType<?> errorOpType;
    private final String propertyName;
    private final Object propertyValue;
    private final Throwable cause;

    public ConfigurationErrorEvent(Object source, EventType<? extends ConfigurationErrorEvent> eventType, EventType<?> operationType, String propName, Object propertyValue, Throwable cause) {
        super(source, eventType);
        this.errorOpType = operationType;
        this.propertyName = propName;
        this.propertyValue = propertyValue;
        this.cause = cause;
    }

    public EventType<?> getErrorOpType() {
        return errorOpType;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public Object getPropertyValue() {
        return propertyValue;
    }

    public Throwable getCause() {
        return cause;
    }
}
