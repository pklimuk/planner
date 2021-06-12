package com.planner.planner.services;

import com.planner.planner.models.Group;
import com.planner.planner.repositories.GroupRepository;
import com.planner.planner.models.User;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserService userService;
    private final DeadlineService deadlineService;
    private final EventService eventService;

    @Autowired
    public GroupService(GroupRepository groupRepository, UserService userService,
                        DeadlineService deadlineService, EventService eventService) {
        this.groupRepository = groupRepository;
        this.userService = userService;
        this.deadlineService = deadlineService;
        this.eventService = eventService;
    }

    public User getCurrentUser() {
        return userService.getUserByUsername(userService.getLoggedUserUserName());
    }

    public List<Group> getUserGroups() {
        User groupUser = getCurrentUser();
        return groupUser.getGroups();
    }

    public void addNewGroup(Group group) {
        User user = getCurrentUser();
        group.setUser(user);
        Optional<Group> groupOptional =
                groupRepository.findGroupByNameAndUserId(group.getGroup_name(), user.getId());
        if (groupOptional.isPresent()) {
            throw new IllegalStateException("Group: '" + group.getGroup_name() + "' has been already registered");
        }
        else {
            groupRepository.save(group);
        }
    }

    public void deleteGroup(String name) {
        User user = getCurrentUser();
        Optional<Group> groupOptional =
                groupRepository.findGroupByNameAndUserId(name, user.getId());
        if (!groupOptional.isPresent()) {
            throw new IllegalStateException("Group: '" + name + "' does not exist");
        }
        else {
            Group groupToDelete = groupOptional.get();
            if (!groupToDelete.getDeadlines().isEmpty()) {
                for (var deadline: groupToDelete.getDeadlines()) {
                    Set<Group> deadlineGroups = deadline.getGroups();
                    if (deadlineGroups.remove(groupToDelete)){
                        List<String> leftGroupsTitles = new ArrayList<>();
                        for (var leftGroup: deadlineGroups) {
                            leftGroupsTitles.add(leftGroup.getGroup_name());
                        }
                        deadlineService.updateDeadline(deadline.getTitle(), deadline.getDeadline_time(),
                                deadline.getDescription(), null, null, null,
                                leftGroupsTitles);
                    }
                }
            }
            if (!groupToDelete.getEvents().isEmpty()) {
                for (var event: groupToDelete.getEvents()) {
                    Set<Group> eventGroups = event.getGroups();
                    if (eventGroups.remove(groupToDelete)) {
                        List<String> leftGroupsTitles = new ArrayList<>();
                        for (var leftGroup: eventGroups) {
                            leftGroupsTitles.add(leftGroup.getGroup_name());
                        }
                        eventService.updateEvent(event.getTitle(), event.getStart(), event.getEnd(),
                                event.getDescription(), null, null, null,
                                null, leftGroupsTitles);
                    }
                }
            }
            groupRepository.deleteById(groupToDelete.getId());
        }
    }
    @Transactional
    public void updateGroup(String name, String newName, String newDescription) {
        User user = getCurrentUser();
        Optional<Group> groupOptional =
                groupRepository.findGroupByNameAndUserId(name, user.getId());
        if (!groupOptional.isPresent()) {
            throw new IllegalStateException("Group: '" + name + "' does not exist");
        }
        else {
            Group groupToUpdate = groupOptional.get();
            if (newName != null && newName.length() > 0
                   && !Objects.equals(groupToUpdate.getGroup_name(), newName)) {
                groupToUpdate.setGroup_name(newName);
            }
            if (newDescription != null && newDescription.length() > 0
                    && !Objects.equals(groupToUpdate.getGroup_description(), newDescription)) {
                groupToUpdate.setGroup_description(newDescription);
            }
            groupRepository.save(groupToUpdate);
        }
    }

    public String getGroupDeadlinesandEvents(String groupName) {
        User user = getCurrentUser();
        String json = null;
        Optional<Group> groupOptional =
                groupRepository.findGroupByNameAndUserId(groupName, user.getId());
        if (!groupOptional.isPresent()) {
            throw new IllegalStateException("Group: '" + groupName + "' does not exist");
        }
        else {
            Group group = groupOptional.get();
            List<String> groupDeadlinesTitles = new ArrayList<>();
            List<String> groupEventsTitles = new ArrayList<>();
            for (var event: group.getEvents()) {
                groupEventsTitles.add(event.getTitle());
            }
            for (var deadline: group.getDeadlines()) {
                groupDeadlinesTitles.add(deadline.getTitle());
            }
            json = new JSONObject()
                .put("groupName", group.getGroup_name())
                .put("events", groupEventsTitles)
                .put("deadlines", groupDeadlinesTitles)
                .toString();
        }
        return json;
    }
}
