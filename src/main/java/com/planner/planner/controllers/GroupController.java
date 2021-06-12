package com.planner.planner.controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.planner.planner.services.GroupService;
import com.planner.planner.models.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "api/user/groups")
public class GroupController {

    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping
    public List<Group> getUserGroups() {
        List<Group> userGroups = new ArrayList<>();
        for (var group: groupService.getUserGroups()) {
            Group newGroup = new Group();
            newGroup.setGroup_name(group.getGroup_name());
            newGroup.setGroup_description(group.getGroup_description());
            userGroups.add(newGroup);
        }
        return userGroups;
    }

    @PostMapping("/deadlines_and_events")
    public String getUserGroupsandDeadlines(@RequestBody ObjectNode objectNode) {
        String groupName = objectNode.get("group_name").asText();
        return groupService.getGroupDeadlinesandEvents(groupName);
    }

    @PostMapping
    public void addNewGroup(@RequestBody Group group) {
        groupService.addNewGroup(group);
    }

    @DeleteMapping
    public void deleteGroup(@RequestBody ObjectNode objectNode) {
        String groupName = objectNode.get("group_name").asText();
        groupService.deleteGroup(groupName);
    }

    @PutMapping
    public void updateGroup(@RequestBody ObjectNode objectNode) {
        String groupName = objectNode.get("group_name").asText();
        String newGroupName = null;
        String newGroupDescription = null;
        if (objectNode.get("new_group_name") != null) {
            newGroupName = objectNode.get("new_group_name").asText();
        }
        if (objectNode.get("new_group_description") != null) {
            newGroupDescription = objectNode.get("new_group_description").asText();
        }
        groupService.updateGroup(groupName, newGroupName, newGroupDescription);
    }
}
