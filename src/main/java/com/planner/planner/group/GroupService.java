package com.planner.planner.group;

import com.planner.planner.deadline.DeadlineService;
import com.planner.planner.user.User;
import com.planner.planner.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserService userService;
    private final DeadlineService deadlineService;

    @Autowired
    public GroupService(GroupRepository groupRepository, UserService userService,
                        DeadlineService deadlineService) {
        this.groupRepository = groupRepository;
        this.userService = userService;
        this.deadlineService = deadlineService;
    }

    public User getCurrentUser(){
        return userService.getUserByUsername(userService.getLoggedUserUserName());
    }

    public List<Group> getUserGroups(){
        User group_user = getCurrentUser();
        return group_user.getGroups();
    }

    public void addNewGroup(Group group){
        User user = getCurrentUser();
        group.setUser(user);
        Optional<Group> groupOptional =
                groupRepository.findGroupByNameAndUserId(group.getGroup_name(), user.getId());
        if (groupOptional.isPresent()){
            throw new IllegalStateException("Group: '" + group.getGroup_name() + "' has been already registered");
        }
        else {
            groupRepository.save(group);
        }
    }

    public void deleteGroup(String name){
        User user = getCurrentUser();
        Optional<Group> groupOptional =
                groupRepository.findGroupByNameAndUserId(name, user.getId());
        if (!groupOptional.isPresent()){
            throw new IllegalStateException("Group: '" + name + "' does not exist");
        }
        else {
            Group group_to_delete = groupOptional.get();
            if(!group_to_delete.getDeadlines().isEmpty()){
                for (var deadline: group_to_delete.getDeadlines()) {
                    Set<Group> deadline_groups = deadline.getGroups();
                    if (deadline_groups.remove(group_to_delete)){
                        List<String> left_groups_titles = new ArrayList<>();
                        for (var left_group: deadline_groups) {
                            left_groups_titles.add(left_group.getGroup_name());
                        }
                        deadlineService.updateDeadline(deadline.getTitle(),deadline.getDeadline_time(),
                                deadline.getDescription(), null, null, null,
                                left_groups_titles);
                    }
                }
            }
            if(!group_to_delete.getEvents().isEmpty()){
                //TODO: Implement this part after full EVENT implementation
            }
            groupRepository.deleteById(group_to_delete.getId());
        }
    }
    @Transactional
    public void updateGroup(String name, String new_name, String new_description){
        User user = getCurrentUser();
        Optional<Group> groupOptional =
                groupRepository.findGroupByNameAndUserId(name, user.getId());
        if (!groupOptional.isPresent()){
            throw new IllegalStateException("Group: '" + name + "' does not exist");
        }
        else {
            Group group_to_update = groupOptional.get();
            if (new_name != null && new_name.length() > 0 &&
                    !Objects.equals(group_to_update.getGroup_name(), new_name)) {
                group_to_update.setGroup_name(new_name);
            }
            if (new_description != null && new_description.length() > 0 &&
                    !Objects.equals(group_to_update.getGroup_description(), new_description)) {
                group_to_update.setGroup_description(new_description);
            }
            groupRepository.save(group_to_update);
        }
    }
}
