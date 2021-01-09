package by.emoshin.event;

import javax.swing.event.EventListenerList;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class BaseEventSource implements EventSource {

    private List<EventRegistrationData<?>> eventListeners;

    private int detailEvents;

    public BaseEventSource() {
        this.eventListeners = new CopyOnWriteArrayList<>();
    }

    @Override
    public <T extends Event> void addEventListener(EventType<T> eventType, EventListener<? super T> listener) {
        eventListeners.add(new EventRegistrationData<>(eventType, listener));
    }

    @Override
    public <T extends Event> void addEventListener(EventRegistrationData<T> regData) {
        EventRegistrationData<?> registrationData = Objects.requireNonNull(regData, "EventRegistrationData must be not null!!");
        eventListeners.add(registrationData);
    }

    @Override
    public <T extends Event> boolean removeEventListener(EventRegistrationData<T> regData) {
        return eventListeners.remove(Objects.requireNonNull(regData, "EventRegistrationData must be not null!!"));
    }

    @Override
    public <T extends Event> boolean removeEventListener(EventType<T> eventType, EventListener<? super T> listener) {
        return !(listener == null || eventType == null) && removeEventListener(new EventRegistrationData<>(eventType, listener));
    }

    public void fire(Event rawEvent) {
        final Event event = Objects.requireNonNull(rawEvent, "Event to be fired must not be null!!");
        for (EventListenerIterator<? extends Event> iterator = getEventListenerIterator(event.getEventType()); iterator.hasNext(); ) {
            iterator.invokeNextListenerUnchecked(event);
        }
    }

    protected <T extends ConfigurationEvent> void fireEvent(final EventType<T> type,
                                                            final String propName, final Object propValue, final boolean before) {
        if (checkDetailEvents(-1)) {
            final EventListenerIterator<T> it =
                    getEventListenerIterator(type);
            if (it.hasNext()) {
                final ConfigurationEvent event =
                        createEvent(type, propName, propValue, before);
                while (it.hasNext()) {
                    it.invokeNext(event);
                }
            }
        }
    }

    protected <T extends ConfigurationEvent> ConfigurationEvent createEvent(
            final EventType<T> type, final String propName, final Object propValue, final boolean before) {
        return new ConfigurationEvent(this, type, propName, propValue, before);
    }

    public <T extends ConfigurationErrorEvent> void fireError(
            final EventType<T> eventType, final EventType<?> operationType,
            final String propertyName, final Object propertyValue, final Throwable cause) {
        final EventListenerIterator<T> iterator =
                getEventListenerIterator(eventType);
        if (iterator.hasNext()) {
            final ConfigurationErrorEvent event =
                    createErrorEvent(eventType, operationType, propertyName,
                            propertyValue, cause);
            while (iterator.hasNext()) {
                iterator.invokeNext(event);
            }
        }
    }

    protected ConfigurationErrorEvent createErrorEvent(
            final EventType<? extends ConfigurationErrorEvent> type,
            final EventType<?> opType, final String propName, final Object propValue, final Throwable ex) {
        return new ConfigurationErrorEvent(this, type, opType, propName,
                propValue, ex);
    }

    public <T extends Event> Iterable<EventListener<? super T>> getEventListeners(EventType<T> eventType) {
        return () -> getEventListenerIterator(eventType);
    }

    public <T extends Event> EventListenerIterator<T> getEventListenerIterator(EventType<T> eventType) {
        return new EventListenerIterator<>(eventListeners.iterator(), eventType);
    }

    public List<EventRegistrationData<?>> getRegistrations() {
        return Collections.unmodifiableList(eventListeners);
    }

    public <T extends Event> List<EventRegistrationData<? extends T>> getRegistrationsForSuperType(EventType<T> eventType) {
        Map<EventType<?>, Set<EventType<?>>> superTypes = new HashMap<>();
        List<EventRegistrationData<? extends T>> results = new LinkedList<>();
        for (EventRegistrationData<?> reg : eventListeners) {
            Set<EventType<?>> base = superTypes.get(reg.getEventType());
            if (base == null) {
                base = EventType.fetchEventSuperTypes(reg.getEventType());
                superTypes.put(reg.getEventType(), base);
            }
            if (base.contains(eventType)) {
                @SuppressWarnings("unchecked")
                EventRegistrationData<? extends T> result = (EventRegistrationData<? extends T>) reg;
                results.add(result);
            }
        }
        return results;
    }

    public void clear() {
        eventListeners.clear();
    }

    public void setDetailEvents(boolean enable) {
        synchronized (BaseEventSource.class) {
            if (enable) {
                detailEvents++;
            } else {
                detailEvents--;
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static void callListener(EventListener<?> listener, Event event) {
        @SuppressWarnings("rawtypes")
        EventListener rowListener = listener;
        rowListener.onEvent(event);
    }

    private boolean checkDetailEvents(final int limit) {
        synchronized (BaseEventSource.class) {
            return detailEvents > limit;
        }
    }

    static final class EventListenerIterator<T extends Event> implements Iterator<EventListener<? super T>> {

        private final Iterator<EventRegistrationData<?>> lIterator;

        private final EventType<T> baseEventType;
        private final Set<EventType<?>> acceptedTypes;
        private EventListener<? super T> nextElement;

        private EventListenerIterator(Iterator<EventRegistrationData<?>> it, EventType<T> base) {
            this.lIterator = it;
            this.baseEventType = base;
            acceptedTypes = EventType.fetchEventSuperTypes(base);
            initNextElement();
        }

        @Override
        public boolean hasNext() {
            return nextElement != null;
        }

        @Override
        public EventListener<? super T> next() {
            if (nextElement == null) {
                throw new NoSuchElementException("No more event listeners!");
            }
            EventListener<? super T> result = nextElement;
            initNextElement();
            return result;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Removing elements is not supported!");
        }

        public void invokeNext(Event event) {
            validateEvent(event);
            invokeNextListenerUnchecked(event);
        }

        private void initNextElement() {
            nextElement = null;
            while (lIterator.hasNext() && nextElement == null) {
                EventRegistrationData<?> regData = lIterator.next();
                if (acceptedTypes.contains(regData.getEventType())) {
                    nextElement = castListener(regData);
                }
            }
        }

        private void validateEvent(Event event) {
            if (event == null || !EventType.fetchEventSuperTypes(event.getEventType()).contains(baseEventType)) {
                throw new IllegalArgumentException("Event incompatible with listener iterator: " + event);
            }
        }

        private void invokeNextListenerUnchecked(Event event) {
            EventListener<? super T> listener = next();
            callListener(listener, event);
        }

        @SuppressWarnings("unchecked")
        private EventListener<? super T> castListener(EventRegistrationData<?> regData) {
            @SuppressWarnings("rawtypes")
            EventListener listener = regData.getListener();
            return listener;
        }
    }
}
