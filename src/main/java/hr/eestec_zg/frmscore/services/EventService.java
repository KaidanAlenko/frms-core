package hr.eestec_zg.frmscore.services;

import hr.eestec_zg.frmscore.domain.models.Event;

import java.util.List;

public interface EventService {
    void createEvent(Event event);
    void updateEvent(Event event);
    void deleteEvent(Long eventId);
    Event getEventById(Long id);
    Event getEventByName(String name);
    List<Event> getEventsByYear(String year);
    List<Event> getEvents();
}
