package com.planner.planner.controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.planner.planner.services.DeadlineService;
import com.planner.planner.models.Group;
import com.planner.planner.models.Deadline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "api/user/deadlines")
public class DeadlineController {

    private final DeadlineService deadlineService;

    @Autowired
    public DeadlineController(DeadlineService deadlineService) {
        this.deadlineService = deadlineService;
    }


    @DeleteMapping
    public void deleteDeadline(@RequestBody ObjectNode objectNode) {

        String title = objectNode.get("title").asText();
        LocalDateTime time = LocalDateTime.parse(objectNode.get("deadline_time").asText());
        String description = objectNode.get("description").asText();
        deadlineService.deleteDeadline(title, time, description);
    }

    @GetMapping
    public List<Deadline> getUserDeadlines() {
        List<Deadline> userDeadlines = new ArrayList<>();
        for (var deadline: deadlineService.getUserDeadlines()){
            Deadline newDeadline = new Deadline();
                newDeadline.setTitle(deadline.getTitle());
                newDeadline.setDeadline_time(deadline.getDeadline_time());
                newDeadline.setDescription(deadline.getDescription());
                for (var group: deadline.getGroups()){
                    Group newGroup = new Group(group.getGroup_name(), group.getGroup_description());
                    newDeadline.getGroups().add(newGroup);
                }
            userDeadlines.add(newDeadline);
        }

        return userDeadlines;
    }

    @PostMapping
    public void addNewDeadline(@RequestBody ObjectNode objectNode) {
        String title = objectNode.get("title").asText();
        LocalDateTime time = LocalDateTime.parse(objectNode.get("deadline_time").asText());
        String description = objectNode.get("description").asText();
        List<String> listOfGroupTitles = new ArrayList<>();
        if (objectNode.get("groups") != null) {
            Integer i = 0;
            while (i < objectNode.get("groups").size()) {
                listOfGroupTitles.add(objectNode.get("groups").get(i).asText());
                i += 1;
            }
        }
        deadlineService.addNewDeadline(title, time, description, listOfGroupTitles);
    }

    @PutMapping
    public void updateDeadline(@RequestBody ObjectNode objectNode) {
        String title = objectNode.get("title").asText();
        LocalDateTime time = LocalDateTime.parse(objectNode.get("deadline_time").asText());
        String description = objectNode.get("description").asText();
        String newTitle = null;
        LocalDateTime newTime = null;
        String newDescription = null;
        List<String> newListOfGroupTitles = new ArrayList<>();
        if (objectNode.get("new_title") != null) {
            newTitle = objectNode.get("new_title").asText();
        }
        if (objectNode.get("new_time") != null) {
            newTime = LocalDateTime.parse(objectNode.get("new_time").asText());
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
        deadlineService.updateDeadline(title, time, description,
                newTitle, newTime, newDescription, newListOfGroupTitles);
    }
}
