package by.emoshin.event;

import java.util.Objects;

public final class EventRegistrationData<T extends Event> {
    private final EventType<T> eventType;
    private final EventListener<? super T> listener;

    public EventRegistrationData(EventType<T> type, EventListener<? super T> listener) {
        this.eventType = Objects.requireNonNull(type, "EventType must be not null!!");
        this.listener = Objects.requireNonNull(listener, "Listener must be not null!!");
    }

    public EventType<T> getEventType() {
        return eventType;
    }

    public EventListener<? super T> getListener() {
        return listener;
    }

    @Override
    public int hashCode() {
        int result = eventType.hashCode();
        result = 31 * result + listener.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof EventRegistrationData)) {
            return false;
        }
        final EventRegistrationData<?> eventRegistrationData = (EventRegistrationData<?>) obj;
        return getListener() == eventRegistrationData.getListener()
                && getEventType().equals(eventRegistrationData.getEventType());
    }
}
