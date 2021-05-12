package com.planner.planner.group;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.json.JSONArray;
import org.json.JSONObject;
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
    public List<Group> getUserGroups(){
        List<Group> user_groups = new ArrayList<>();
        for (var group: groupService.getUserGroups()) {
            Group new_group = new Group();
            new_group.setGroup_name(group.getGroup_name());
            new_group.setGroup_description(group.getGroup_description());
            user_groups.add(new_group);
        }
        return user_groups;
    }

    @GetMapping("/deadlines_and_events")
    public String getUserGroupsandDeadlines(@RequestBody ObjectNode objectNode){
        String group_name = objectNode.get("group_name").asText();
        return groupService.getGroupDeadlinesandEvents(group_name);
    }

    @PostMapping
    public void addNewGroup(@RequestBody Group group){
        groupService.addNewGroup(group);
    }

    @DeleteMapping
    public void deleteGroup(@RequestBody ObjectNode objectNode){
        String group_name = objectNode.get("group_name").asText();
        groupService.deleteGroup(group_name);
    }

    @PutMapping
    public void updateGroup(@RequestBody ObjectNode objectNode){
        String group_name = objectNode.get("group_name").asText();
        String new_group_name = null;
        String new_group_description = null;
        if (objectNode.get("new_group_name") != null){
            new_group_name = objectNode.get("new_group_name").asText();
        }
        if (objectNode.get("new_group_description") != null){
            new_group_description = objectNode.get("new_group_description").asText();
        }
        groupService.updateGroup(group_name, new_group_name, new_group_description);
    }
}
