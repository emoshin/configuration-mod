package by.emoshin.event;

public class ConfigurationEvent extends Event {

    public static final EventType<ConfigurationEvent> ANY = new EventType<>(Event.ANY, "CONFIGURATION_UPDATE");
    public static final EventType<ConfigurationEvent> ADD_PROPERTY = new EventType<>(Event.ANY, "ADD_PROPERTY");
    public static final EventType<ConfigurationEvent> SET_PROPERTY = new EventType<>(Event.ANY, "SET_PROPERTY");
    public static final EventType<ConfigurationEvent> CLEAR_PROPERTY = new EventType<>(Event.ANY, "CLEAR_PROPERTY");
    public static final EventType<ConfigurationEvent> CLEAR = new EventType<>(Event.ANY, "CLEAR");
    public static final EventType<ConfigurationEvent> GET_PROPERTY = new EventType<>(Event.ANY, "GET_PROPERTY");
    public static final EventType<ConfigurationEvent> REMOVE_PROPERTY = new EventType<>(Event.ANY, "REMOVE_PROPERTY");

    private final String propertyName;
    private final Object propertyValue;
    private final boolean beforeUpdate;

    public ConfigurationEvent(Object source, EventType<? extends ConfigurationEvent> type, String propertyName, Object propertyValue, boolean beforeUpdate) {
        super(source, type);
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
        this.beforeUpdate = beforeUpdate;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public Object getPropertyValue() {
        return propertyValue;
    }

    public boolean isBeforeUpdate() {
        return beforeUpdate;
    }
}
