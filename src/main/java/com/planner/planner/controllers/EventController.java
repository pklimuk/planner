package com.planner.planner.controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.planner.planner.services.EventService;
import com.planner.planner.models.Group;
import com.planner.planner.models.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "api/user/events")
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<Event> getUserEvents(){
        List<Event> user_events = new ArrayList<>();
        for (var event: eventService.getUserEvents()) {
            Event new_event = new Event();
            new_event.setTitle(event.getTitle());
            new_event.setStart(event.getStart());
            new_event.setEnd(event.getEnd());
            new_event.setDescription(event.getDescription());
            for (var group: event.getGroups()) {
                Group new_group = new Group(group.getGroup_name(), group.getGroup_description());
                new_event.getGroups().add(new_group);
            }
            user_events.add(new_event);
        }
        return user_events;
    }
//    TODO: Implement null checking in description(in Deadline too)
    @PostMapping
    public void addNewEvent(@RequestBody ObjectNode objectNode){
        String title = objectNode.get("title").asText();
        LocalDateTime start = LocalDateTime.parse(objectNode.get("start").asText());
        LocalDateTime end = LocalDateTime.parse(objectNode.get("end").asText());
        String description = objectNode.get("description").asText();
        List<String> list_of_group_titles = new ArrayList<>();
        if (objectNode.get("groups") != null) {
            Integer i = 0;
            while (i < objectNode.get("groups").size()) {
                list_of_group_titles.add(objectNode.get("groups").get(i).asText());
                i += 1;
            }
        }
        eventService.addNewEvent(title, start, end, description, list_of_group_titles);
    }

    @DeleteMapping
    public void deleteEvent(@RequestBody ObjectNode objectNode){
        String title = objectNode.get("title").asText();
        LocalDateTime start = LocalDateTime.parse(objectNode.get("start").asText());
        LocalDateTime end = LocalDateTime.parse(objectNode.get("end").asText());
        String description = objectNode.get("description").asText();
        eventService.deleteEvent(title, start, end, description);
    }

    @PutMapping
    public void updateEvent(@RequestBody ObjectNode objectNode) {
        String title = objectNode.get("title").asText();
        LocalDateTime start = LocalDateTime.parse(objectNode.get("start").asText());
        LocalDateTime end = LocalDateTime.parse(objectNode.get("end").asText());
        String description = objectNode.get("description").asText();
        String new_title = null;
        LocalDateTime new_start = null;
        LocalDateTime new_end = null;
        String new_description = null;
        List<String> new_list_of_group_titles = new ArrayList<>();
        if (objectNode.get("new_title") != null){
            new_title = objectNode.get("new_title").asText();
        }
        if (objectNode.get("new_start") != null){
            new_start = LocalDateTime.parse(objectNode.get("new_start").asText());
        }
        if (objectNode.get("new_end") != null){
            new_end = LocalDateTime.parse(objectNode.get("new_end").asText());
        }
        if (objectNode.get("new_description") != null){
            new_description = objectNode.get("new_description").asText();
        }
        if (objectNode.get("new_groups") != null){
            Integer i = 0;
            while (i < objectNode.get("new_groups").size()) {
                new_list_of_group_titles.add(objectNode.get("new_groups").get(i).asText());
                i += 1;
            }
        }
        eventService.updateEvent(title, start, end, description, new_title,
                new_start, new_end, new_description, new_list_of_group_titles);
    }
}
