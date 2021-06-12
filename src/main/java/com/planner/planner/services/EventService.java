package com.planner.planner.services;

import com.planner.planner.models.Group;
import com.planner.planner.repositories.GroupRepository;
import com.planner.planner.models.Event;
import com.planner.planner.repositories.EventRepository;
import com.planner.planner.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final UserService userService;
    private final GroupRepository groupRepository;

    @Autowired
    public EventService(EventRepository eventRepository, UserService userService,
                        GroupRepository groupRepository) {
        this.eventRepository = eventRepository;
        this.userService = userService;
        this.groupRepository = groupRepository;
    }

    public Boolean checkIfEventTimeIsCorrect(LocalDateTime startTime, LocalDateTime endTime) {
        boolean eventTimeIsCorrect = false;
        boolean endTimeIsCorrect = false;
        if (endTime.isAfter(startTime)) {
            endTimeIsCorrect = true;
        }
        else {
            throw new IllegalStateException("Provided time is not correct");
        }
        if (endTimeIsCorrect) {
            eventTimeIsCorrect = true;
        }
        return eventTimeIsCorrect;
    }

    public User getCurrentUser() {
        return userService.getUserByUsername(userService.getLoggedUserUserName());
    }

    public List<Event> getUserEvents() {
        User user = getCurrentUser();
        return user.getEvents();
    }

    public void addGroupsFromListOfString(Event eventToUpdate,
                                          List<String> listOfGroupsTitles,
                                          User user){
        Set<Group> eventGroups = new HashSet<>();
        for (var groupTitle : listOfGroupsTitles) {
            Group group = groupRepository.findGroupByNameAndUserId(groupTitle, user.getId()).orElseThrow(() ->
                    new IllegalStateException("Group with name " + groupTitle + " does not exists"));
            eventGroups.add(group);
            group.getEvents().add(eventToUpdate);
        }
        eventToUpdate.setGroups(eventGroups);
        eventRepository.save(eventToUpdate);
    }

    public void addNewEvent(String title, LocalDateTime start, LocalDateTime end,
                            String description, List<String> listOfGroupTitles) {
        checkIfEventTimeIsCorrect(start, end);
        User user = getCurrentUser();
        Event event = new Event(title, start, end, description);
        event.setUser(user);
        if (!listOfGroupTitles.isEmpty()) {
            addGroupsFromListOfString(event, listOfGroupTitles, user);
        }
        else {
            eventRepository.save(event);
        }
    }

    public void deleteEvent(String title, LocalDateTime start,
                            LocalDateTime end, String description) {
        User user = getCurrentUser();
        List<Optional<Event>> listOfEvents = eventRepository.findListOfEventsByTitleAndUserId(title, user.getId());
        try {
            if (listOfEvents.size() == 1) {
                eventRepository.deleteById(listOfEvents.get(0).get().getId());
            }
            if (listOfEvents.size() > 1) {
                for (var event: listOfEvents) {
                    if (Objects.equals(event.get().getStart(), start)
                            & Objects.equals(event.get().getEnd(), end)
                            & Objects.equals(event.get().getDescription(), description)) {
                        eventRepository.deleteById(listOfEvents.get(0).get().getId());
                        break;
                    }
                }
            }
        }
        catch (Exception e) {
            throw new IllegalStateException("Can not delete event with title: " + title);
        }
    }

    @Transactional
    public void updateEvent(String title, LocalDateTime start, LocalDateTime end,
                            String description, String newTitle, LocalDateTime newStart,
                            LocalDateTime newEnd, String newDescription,
                            List<String> newListOfGroupTitles) {
        User user = getCurrentUser();
        List<Optional<Event>> listOfEvents = eventRepository.findListOfEventsByTitleAndUserId(title, user.getId());
        Event eventToUpdate = null;
        if (listOfEvents.size() == 1) {
            eventToUpdate = listOfEvents.get(0).get();
        }
        if (listOfEvents.size() > 1) {
            for (var event : listOfEvents) {
                if (Objects.equals(event.get().getStart(), start)
                        & Objects.equals(event.get().getEnd(), end)
                        & Objects.equals(event.get().getDescription(), description)) {
                    eventToUpdate = listOfEvents.get(0).get();
                    break;
                }
            }
        }
        Set<Group> currentGroups = eventToUpdate.getGroups();
        if (newTitle != null && newTitle.length() > 0 && !Objects.equals(eventToUpdate.getTitle(), newTitle)) {
            eventToUpdate.setTitle(newTitle);
        }
        if (newStart != null && !Objects.equals(eventToUpdate.getStart(), newStart)) {
            checkIfEventTimeIsCorrect(newStart, eventToUpdate.getEnd());
            eventToUpdate.setStart(newStart);
        }
        if (newEnd != null && !Objects.equals(eventToUpdate.getEnd(), newEnd)) {
            checkIfEventTimeIsCorrect(eventToUpdate.getStart(), newEnd);
            eventToUpdate.setEnd(newEnd);
        }
        if (newDescription != null && !Objects.equals(eventToUpdate.getDescription(), newDescription)) {
            eventToUpdate.setDescription(newDescription);
        }
        if (newListOfGroupTitles.size() != 0 && !newListOfGroupTitles.contains("CLEAR ALL")) {
            Event newEvent = new Event(eventToUpdate.getTitle(), eventToUpdate.getStart(),
                    eventToUpdate.getEnd(), eventToUpdate.getDescription());
            newEvent.setUser(eventToUpdate.getUser());
            eventRepository.deleteEventById(eventToUpdate.getId());
            addGroupsFromListOfString(newEvent, newListOfGroupTitles, user);
            eventRepository.save(newEvent);
        }
        else if (newListOfGroupTitles.size() == 1 && newListOfGroupTitles.contains("CLEAR ALL")) {
            Event newEvent = new Event(eventToUpdate.getTitle(), eventToUpdate.getStart(),
                    eventToUpdate.getEnd(), eventToUpdate.getDescription());
            newEvent.setUser(eventToUpdate.getUser());
            eventRepository.deleteEventById(eventToUpdate.getId());
            eventRepository.save(newEvent);
        }
    }
}
