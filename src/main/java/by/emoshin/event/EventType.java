package by.emoshin.event;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class EventType<T extends Event> implements Serializable {

    private final EventType<? super T> parentType;

    private final String eventName;

    public EventType(EventType<? super T> eventSuperType, String typeName) {
        this.parentType = eventSuperType;
        this.eventName = typeName;
    }

    public EventType<? super T> getSuperType() {
        return parentType;
    }

    public String getEventName() {
        return eventName;
    }

    @Override
    public String toString() {
        return String.format("%s [ %s ]", getClass().getSimpleName(), getEventName());
    }

    public static Set<EventType<?>> fetchEventSuperTypes(EventType<?> eventType) {
        Set<EventType<?>> types = new HashSet<>();
        EventType<?> currentType = eventType;
        if (currentType != null) {
            types.add(currentType);
            currentType = currentType.getSuperType();
        }
        return types;
    }
}
