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
    public List<Event> getUserEvents() {
        List<Event> userEvents = new ArrayList<>();
        for (var event: eventService.getUserEvents()) {
            Event newEvent = new Event();
            newEvent.setTitle(event.getTitle());
            newEvent.setStart(event.getStart());
            newEvent.setEnd(event.getEnd());
            newEvent.setDescription(event.getDescription());
            for (var group: event.getGroups()) {
                Group newGroup = new Group(group.getGroup_name(), group.getGroup_description());
                newEvent.getGroups().add(newGroup);
            }
            userEvents.add(newEvent);
        }
        return userEvents;
    }

    @PostMapping
    public void addNewEvent(@RequestBody ObjectNode objectNode) {
        String title = objectNode.get("title").asText();
        LocalDateTime start = LocalDateTime.parse(objectNode.get("start").asText());
        LocalDateTime end = LocalDateTime.parse(objectNode.get("end").asText());
        String description = objectNode.get("description").asText();
        List<String> listOfGroupTitles = new ArrayList<>();
        if (objectNode.get("groups") != null) {
            Integer i = 0;
            while (i < objectNode.get("groups").size()) {
                listOfGroupTitles.add(objectNode.get("groups").get(i).asText());
                i += 1;
            }
        }
        eventService.addNewEvent(title, start, end, description, listOfGroupTitles);
    }

    @DeleteMapping
    public void deleteEvent(@RequestBody ObjectNode objectNode) {
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
        String newTitle = null;
        LocalDateTime newStart = null;
        LocalDateTime newEnd = null;
        String newDescription = null;
        List<String> newListOfGroupTitles = new ArrayList<>();
        if (objectNode.get("new_title") != null) {
            newTitle = objectNode.get("new_title").asText();
        }
        if (objectNode.get("new_start") != null) {
            newStart = LocalDateTime.parse(objectNode.get("new_start").asText());
        }
        if (objectNode.get("new_end") != null) {
            newEnd = LocalDateTime.parse(objectNode.get("new_end").asText());
        }
        if (objectNode.get("new_description") != null) {
            newDescription = objectNode.get("new_description").asText();
        }
        if (objectNode.get("new_groups") != null) {
            Integer i = 0;
            while (i < objectNode.get("new_groups").size()) {
                newListOfGroupTitles.add(objectNode.get("new_groups").get(i).asText());
                i += 1;
            }
        }
        eventService.updateEvent(title, start, end, description, newTitle,
                newStart, newEnd, newDescription, newListOfGroupTitles);
    }
}
