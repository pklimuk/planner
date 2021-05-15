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
    public void deleteDeadline(@RequestBody ObjectNode objectNode){

        String title = objectNode.get("title").asText();
        LocalDateTime time = LocalDateTime.parse(objectNode.get("deadline_time").asText());
        String description = objectNode.get("description").asText();
        deadlineService.deleteDeadline(title, time, description);
    }

    @GetMapping
    public List<Deadline> getUserDeadlines(){
        List<Deadline> user_deadlines = new ArrayList<>();
        for (var deadline: deadlineService.getUserDeadlines()){
            Deadline new_deadline = new Deadline();
                new_deadline.setTitle(deadline.getTitle());
                new_deadline.setDeadline_time(deadline.getDeadline_time());
                new_deadline.setDescription(deadline.getDescription());
                for (var group: deadline.getGroups()){
                    Group new_group = new Group(group.getGroup_name(), group.getGroup_description());
                    new_deadline.getGroups().add(new_group);
                }
            user_deadlines.add(new_deadline);
        }

        return user_deadlines;
    }

    @PostMapping
    public void addNewDeadline(@RequestBody ObjectNode objectNode) {
        String title = objectNode.get("title").asText();
        LocalDateTime time = LocalDateTime.parse(objectNode.get("deadline_time").asText());
        String description = objectNode.get("description").asText();
        List<String> list_of_group_titles = new ArrayList<>();
        if (objectNode.get("groups") != null) {
            Integer i = 0;
            while (i < objectNode.get("groups").size()) {
                list_of_group_titles.add(objectNode.get("groups").get(i).asText());
                i += 1;
            }
        }
        deadlineService.addNewDeadline(title, time, description, list_of_group_titles);
    }

    @PutMapping
    public void updateDeadline(@RequestBody ObjectNode objectNode) {
        String title = objectNode.get("title").asText();
        LocalDateTime time = LocalDateTime.parse(objectNode.get("deadline_time").asText());
        String description = objectNode.get("description").asText();
        String new_title = null;
        LocalDateTime new_time = null;
        String new_description = null;
        List<String> new_list_of_group_titles = new ArrayList<>();
        if (objectNode.get("new_title") != null){
            new_title = objectNode.get("new_title").asText();
        }
        if (objectNode.get("new_time") != null){
            new_time = LocalDateTime.parse(objectNode.get("new_time").asText());
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
        deadlineService.updateDeadline(title, time, description,
                new_title, new_time, new_description, new_list_of_group_titles);
    }
}