package hr.eestec_zg.frmsbackend;

import hr.eestec_zg.frmscore.domain.models.Event;
import hr.eestec_zg.frmscore.exceptions.EventNotFoundException;
import hr.eestec_zg.frmscore.services.EventService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static hr.eestec_zg.frmsbackend.utils.TestDataUtils.DUMMY_VALUE;
import static hr.eestec_zg.frmsbackend.utils.TestDataUtils.TEST_EVENT_NAME_1;
import static hr.eestec_zg.frmsbackend.utils.TestDataUtils.TEST_EVENT_NAME_2;
import static hr.eestec_zg.frmsbackend.utils.TestDataUtils.TEST_EVENT_NAME_3;
import static hr.eestec_zg.frmsbackend.utils.TestDataUtils.TEST_EVENT_SHORT_NAME_1;
import static hr.eestec_zg.frmsbackend.utils.TestDataUtils.TEST_EVENT_SHORT_NAME_2;
import static hr.eestec_zg.frmsbackend.utils.TestDataUtils.TEST_EVENT_SHORT_NAME_3;
import static hr.eestec_zg.frmsbackend.utils.TestDataUtils.TEST_EVENT_YEAR_1;
import static hr.eestec_zg.frmsbackend.utils.TestDataUtils.TEST_EVENT_YEAR_3;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class EventServiceTest extends TestBase {

    @Autowired
    private EventService eventService;

    private Event testEvent1;
    private Event testEvent2;

    @Before
    public void setTestData() {
        testEvent1 = new Event(TEST_EVENT_NAME_1, TEST_EVENT_SHORT_NAME_1, TEST_EVENT_YEAR_1);
        Event testEvent = new Event(TEST_EVENT_NAME_2, TEST_EVENT_SHORT_NAME_2, TEST_EVENT_YEAR_1);

        eventRepository.createEvent(testEvent1);
        eventRepository.createEvent(testEvent);
    }

    @Test
    public void testGettingEventByName() {
        Event event3 = eventService.getEventByName(TEST_EVENT_NAME_1);

        assertNotNull(event3);
    }

    @Test
    public void testGettingEventById() {
        Event testEvent = eventService.getEventById(testEvent1.getId());

        assertEquals(testEvent1, testEvent);
    }

    @Test(expected = EventNotFoundException.class)
    public void testGettingNonExistingEventByName() {
        eventService.getEventByName(DUMMY_VALUE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeletingOfEventWithoutSendingId() {
        eventService.deleteEvent(null);
    }

    @Test(expected = EventNotFoundException.class)
    public void testUpdatingNonExistingEvent() {
        Event testEvent = new Event(DUMMY_VALUE, TEST_EVENT_SHORT_NAME_1, TEST_EVENT_YEAR_1);

        eventService.updateEvent(testEvent);
    }

    @Test
    public void testUpdatingEvent() {
        Event testEvent = eventService.getEventByName(TEST_EVENT_NAME_1);
        testEvent.setShortName("bla");

        eventService.updateEvent(testEvent);

        Event event = eventService.getEventByName(TEST_EVENT_NAME_1);

        String year = event.getYear();
        String shortName = event.getShortName();

        assertEquals(TEST_EVENT_YEAR_1, year);
        assertEquals("bla", shortName);
    }

    @Test
    public void testGettingEventByYear() {
        testEvent2 = new Event(TEST_EVENT_NAME_3, TEST_EVENT_SHORT_NAME_3, TEST_EVENT_YEAR_3);
        eventService.createEvent(testEvent2);

        List<Event> events = eventService.getEventsByYear(TEST_EVENT_YEAR_3);
        testEvent1 = eventService.getEventById(testEvent2.getId());

        assertEquals(1, events.size());
        assertEquals(testEvent2, testEvent1);
        assertTrue("There is no testEvent1 with name " + testEvent2.getName() + " stored", events.contains(testEvent2));
    }

    @Test
    public void testGettingEventsByNonExistingYear() {
        List<Event> events = eventService.getEventsByYear(DUMMY_VALUE);
        assertEquals(0, events.size());
    }

    @Test
    public void testGettingEvents() {
        List<Event> events = eventService.getEvents();
        assertEquals(2, events.size());
    }

    @Test
    public void testCreationAndDeletionOfEvent() {
        List<Event> events = eventService.getEventsByYear(TEST_EVENT_YEAR_1);
        assertEquals(2, events.size());

        Long a = testEvent1.getId();
        eventService.deleteEvent(a);
        a++;
        eventService.deleteEvent(a);

        events = eventService.getEventsByYear(TEST_EVENT_YEAR_1);
        assertEquals(0, events.size());

        testEvent2 = new Event(TEST_EVENT_NAME_3, TEST_EVENT_SHORT_NAME_3, TEST_EVENT_YEAR_1);
        eventService.createEvent(testEvent2);
        events = eventService.getEventsByYear(TEST_EVENT_YEAR_1);

        assertEquals(1, events.size());
        assertTrue("There is no testEvent1 with name " + testEvent2.getName() + " stored", events.contains(testEvent2));

    }

}