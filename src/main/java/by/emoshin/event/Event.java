package by.emoshin.event;

import java.util.EventObject;
import java.util.Objects;

public class Event extends EventObject {

    public static final EventType<Event> ANY = new EventType<>(null, "ANY");

    private final EventType<? extends Event> eventType;


    public Event(Object source, EventType<? extends Event> evnType) {
        super(source);
        this.eventType = Objects.requireNonNull(evnType, "EventType must be not null!!");
    }

    public EventType<? extends Event> getEventType() {
        return eventType;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(256);
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append(String.format(" %s=%s", "source", getSource()));
        sb.append(String.format(" %s=%s", "eventType", getEventType()));
        sb.append(" ]");
        return sb.toString();
    }
}
