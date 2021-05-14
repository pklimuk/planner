package com.planner.planner.event;

import com.planner.planner.deadline.Deadline;
import com.planner.planner.group.Group;
import com.planner.planner.group.GroupRepository;
import com.planner.planner.user.User;
import com.planner.planner.user.UserService;
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

    public Boolean checkIfEventTimeIsCorrect(LocalDateTime start_time, LocalDateTime end_time){
        Boolean event_time_is_correct = false;
        Boolean start_time_is_correct = false;
        Boolean end_time_is_correct = false;
        if (start_time.isAfter(LocalDateTime.now())){
            start_time_is_correct = true;
        }
        else{
            throw new IllegalStateException("Provided start time is not correct");
        }
        if (end_time.isAfter(LocalDateTime.now()) && end_time.isAfter(start_time)){
            end_time_is_correct = true;
        }
        else{
            throw new IllegalStateException("Provided end time is not correct");
        }
        if(start_time_is_correct && end_time_is_correct){
            event_time_is_correct = true;
        }
        return event_time_is_correct;
    }

    public User getCurrentUser(){
        return userService.getUserByUsername(userService.getLoggedUserUserName());
    }

    public List<Event> getUserEvents(){
        User user = getCurrentUser();
        return user.getEvents();
    }

    public void add_groups_from_list_of_string(Event event_to_update,
                                               List<String> list_of_groups_titles,
                                               User user){
        Set<Group> event_groups = new HashSet<>();
        for (var group_title : list_of_groups_titles) {
            Group group = groupRepository.findGroupByNameAndUserId(group_title, user.getId()).orElseThrow(() ->
                    new IllegalStateException("Group with name " + group_title + " does not exists"));
            event_groups.add(group);
            group.getEvents().add(event_to_update);
        }
        event_to_update.setGroups(event_groups);
        eventRepository.save(event_to_update);
    }

    public void addNewEvent(String title, LocalDateTime start, LocalDateTime end,
                            String description, List<String> list_of_group_titles){
        checkIfEventTimeIsCorrect(start, end);
        User user = getCurrentUser();
        Event event = new Event(title, start, end, description);
        event.setUser(user);
        if (!list_of_group_titles.isEmpty()) {
            add_groups_from_list_of_string(event, list_of_group_titles, user);
        }
        else {
            eventRepository.save(event);
        }
    }

    public void deleteEvent(String title, LocalDateTime start,
                            LocalDateTime end, String description ){
        User user = getCurrentUser();
        List<Optional<Event>> list_of_events = eventRepository.findListOfEventsByTitleAndUserId(title, user.getId());
        try {
            if(list_of_events.size() == 1) {
                eventRepository.deleteById(list_of_events.get(0).get().getId());
            }
            if(list_of_events.size() > 1) {
                for (var event: list_of_events) {
                    if (Objects.equals(event.get().getStart(), start)
                            & Objects.equals(event.get().getEnd(), end)
                            & Objects.equals(event.get().getDescription(), description)) {
                        eventRepository.deleteById(list_of_events.get(0).get().getId());
                        break;
                    }
                }
            }
        }
        catch (Exception e){
            throw new IllegalStateException("Can not delete event with title: " + title);
        }
    }

    @Transactional
    public void updateEvent(String title, LocalDateTime start, LocalDateTime end,
                            String description, String new_title, LocalDateTime new_start,
                            LocalDateTime new_end, String new_description,
                            List<String> new_list_of_group_titles) {
        checkIfEventTimeIsCorrect(new_start, new_end);
        User user = getCurrentUser();
        List<Optional<Event>> list_of_events = eventRepository.findListOfEventsByTitleAndUserId(title, user.getId());
        Event event_to_update = null;
        if (list_of_events.size() == 1) {
            event_to_update = list_of_events.get(0).get();
        }
        if (list_of_events.size() > 1) {
            for (var event : list_of_events) {
                if (Objects.equals(event.get().getStart(), start)
                        & Objects.equals(event.get().getEnd(), end)
                        & Objects.equals(event.get().getDescription(), description)) {
                    event_to_update = list_of_events.get(0).get();
                    break;
                }
            }
        }
        Set<Group> current_groups = event_to_update.getGroups();
        if (new_title != null && new_title.length() > 0 && !Objects.equals(event_to_update.getTitle(), new_title)) {
            event_to_update.setTitle(new_title);
        }
        if (new_start != null && !Objects.equals(event_to_update.getStart(), new_start)) {
            event_to_update.setStart(new_start);
        }
        if (new_end != null && !Objects.equals(event_to_update.getEnd(), new_end)) {
            event_to_update.setEnd(new_end);
        }
        if (new_description != null && !Objects.equals(event_to_update.getDescription(), new_description)) {
            event_to_update.setDescription(new_description);
        }
        if (new_list_of_group_titles.size() != 0 && !new_list_of_group_titles.contains("CLEAR ALL")) {
            Event new_event = new Event(event_to_update.getTitle(), event_to_update.getStart(),
                    event_to_update.getEnd(), event_to_update.getDescription());
            new_event.setUser(event_to_update.getUser());
            eventRepository.deleteEventById(event_to_update.getId());
            add_groups_from_list_of_string(new_event, new_list_of_group_titles, user);
            eventRepository.save(new_event);
        }
        else if (new_list_of_group_titles.size() == 1 && new_list_of_group_titles.contains("CLEAR ALL")) {
            Event new_event = new Event(event_to_update.getTitle(), event_to_update.getStart(),
                    event_to_update.getEnd(), event_to_update.getDescription());
            new_event.setUser(event_to_update.getUser());
            eventRepository.deleteEventById(event_to_update.getId());
            eventRepository.save(new_event);
        }
//        else {
//            Event new_event = new Event(event_to_update.getTitle(), event_to_update.getStart(),
//                    event_to_update.getEnd(), event_to_update.getDescription());
//            new_event.setUser(event_to_update.getUser());
//            List<String> titles_of_current_groups = new ArrayList<>();
//            for (var group:current_groups) {
//                titles_of_current_groups.add(group.getGroup_name());
//            }
//            eventRepository.deleteEventById(event_to_update.getId());
//            add_groups_from_list_of_string(new_event, titles_of_current_groups, user);
//            eventRepository.save(new_event);
//        }
    }
}
