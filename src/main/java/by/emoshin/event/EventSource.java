package by.emoshin.event;

public interface EventSource {

    <T extends Event> void addEventListener(EventType<T> eventType, EventListener<? super T> listener);

    <T extends Event> void addEventListener(EventRegistrationData<T> regData);

    <T extends Event> boolean removeEventListener(EventType<T> eventType, EventListener<? super T> listener);

    <T extends Event> boolean removeEventListener(EventRegistrationData<T> regData);
}
