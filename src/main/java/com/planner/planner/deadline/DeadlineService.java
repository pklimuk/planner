package com.planner.planner.deadline;

import com.planner.planner.group.Group;
import com.planner.planner.group.GroupRepository;
import com.planner.planner.user.User;
import com.planner.planner.user.UserRepository;
import com.planner.planner.user.UserService;
import com.planner.planner.userProfile.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

//@Service
//public class DeadlineService {
//
//    private final DeadlineRepository deadlineRepository;
//    private final UserService userService;
//
//    @Autowired
//    public DeadlineService(DeadlineRepository deadlineRepository, UserService userService) {
//        this.deadlineRepository = deadlineRepository;
//        this.userService = userService;
//    }
//
//    public List<Deadline> getUserDeadlines(){
//        User deadline_user = userService.getUserByUsername(userService.getLoggedUserUserName());
//        return deadline_user.getDeadlines();
//    }
//
//    public void addNewDeadline(Deadline deadline){
//        User deadline_user = userService.getUserByUsername(userService.getLoggedUserUserName());
//        deadline.setUser(deadline_user);
//        deadlineRepository.save(deadline);
//    }
//
//    public void deleteDeadline(String title, LocalDateTime time, String description){
//        List<Optional<Deadline>> list_of_deadlines = deadlineRepository.findListOfDeadlineByTitle(title);
//        try {
//            if(list_of_deadlines.size() == 1) {
//                deadlineRepository.deleteById(list_of_deadlines.get(0).get().getId());
//            }
//            if(list_of_deadlines.size() > 1) {
//                for (var deadline: list_of_deadlines) {
//                    if (Objects.equals(deadline.get().getDeadline_time(), time)
//                            & Objects.equals(deadline.get().getDescription(), description)) {
//                        deadlineRepository.deleteById(list_of_deadlines.get(0).get().getId());
//                        break;
//                    }
//                }
//            }
//        }
//        catch (Exception e){
//            throw new IllegalStateException("Can not delete this deadline");
//        }
//    }
//
//    @Transactional
//    public void updateDeadline(String title, LocalDateTime time, String description){
//        List<Optional<Deadline>> list_of_deadlines = deadlineRepository.findListOfDeadlineByTitle(title);
//        Deadline deadline_to_update = null;
//        if(list_of_deadlines.size() == 1) {
//            deadline_to_update = list_of_deadlines.get(0).get();
//        }
//        if(list_of_deadlines.size() > 1) {
//            for (var deadline: list_of_deadlines) {
//                if (Objects.equals(deadline.get().getDeadline_time(), time)
//                        & Objects.equals(deadline.get().getDescription(), description)) {
//                    deadline_to_update = list_of_deadlines.get(0).get();
//                    break;
//                }
//            }
//        }
//
//
//        if (title != null && title.length() > 0 && !Objects.equals(deadline_to_update.getTitle(), title)) {
//            deadline_to_update.setTitle(title);
//        }
//        if (time != null && !Objects.equals(deadline_to_update.getDeadline_time(), time)) {
//            deadline_to_update.setDeadline_time(time);
//        }
//        if (!Objects.equals(deadline_to_update.getDescription(), description)) {
//            deadline_to_update.setDescription(description);
//        }
//    }
//
//
//    // TODO: Delete test_func()
//    public List<Optional<Deadline>> test_func(String title){
//        return deadlineRepository.findListOfDeadlineByTitle(title);
//    }

@Service
public class DeadlineService {

    private final DeadlineRepository deadlineRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    @Autowired
    public DeadlineService(DeadlineRepository deadlineRepository, UserService userService,
                           UserRepository userRepository, GroupRepository groupRepository) {
        this.deadlineRepository = deadlineRepository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
    }

    public List<Deadline> getUserDeadlines(){
        User deadline_user = userService.getUserByUsername(userService.getLoggedUserUserName());
        return deadline_user.getDeadlines();
    }

//    public void add_groups_from_list_of_string(Deadline deadline_to_update, List<String> list_of_groups_titles, User user) {
//        Set<Group> deadline_groups = new HashSet<>();
//        for (var group_title : list_of_groups_titles) {
//            Group group = groupRepository.findGroupByNameAndUserId(group_title, user.getId()).orElseThrow(() ->
//                    new IllegalStateException("Group with name " + group_title + " does not exists"));
//            deadline_groups.add(group);
////            deadline_to_update.setGroups(deadline_groups);
//        }
//        deadline_to_update.setGroups(deadline_groups);
//        deadlineRepository.save(deadline_to_update);
//    }

    public void addNewDeadline(String title, LocalDateTime time, String description, List<String> list_of_group_titles) {
        User user = userService.getUserByUsername(userService.getLoggedUserUserName());
        Deadline deadline = new Deadline(title, time, description);
        deadline.setUser(user);
        Set<Group> deadline_groups = new HashSet<>();
        if (list_of_group_titles.size() > 0) {
            for (var received_group : list_of_group_titles) {
                Group group = groupRepository.findGroupByNameAndUserId(received_group, user.getId()).orElseThrow(() ->
                        new IllegalStateException("Group with name " + received_group + " does not exists"));
                deadline_groups.add(group);
                group.getDeadlines().add(deadline);
            }

        }
        deadline.setGroups(deadline_groups);
        deadlineRepository.save(deadline);
    }

    public void deleteDeadline(String title, LocalDateTime time, String description){
        User user = userService.getUserByUsername(userService.getLoggedUserUserName());
        List<Optional<Deadline>> list_of_deadlines = deadlineRepository.findListOfDeadlineByTitleAndUserId(title, user.getId());
        try {
            if(list_of_deadlines.size() == 1) {
                deadlineRepository.deleteById(list_of_deadlines.get(0).get().getId());
            }
            if(list_of_deadlines.size() > 1) {
                for (var deadline: list_of_deadlines) {
                    if (Objects.equals(deadline.get().getDeadline_time(), time)
                            & Objects.equals(deadline.get().getDescription(), description)) {
                        deadlineRepository.deleteById(list_of_deadlines.get(0).get().getId());
                        break;
                    }
                }
            }
        }
        catch (Exception e){
            throw new IllegalStateException("Can not delete deadline with title: " + title);
        }
    }

    @Transactional
    public void updateDeadline(String title, LocalDateTime time, String description,
                               String new_title, LocalDateTime new_time,
                               String new_description, List<String> new_list_of_group_titles) {
        User user = userService.getUserByUsername(userService.getLoggedUserUserName());
        List<Optional<Deadline>> list_of_deadlines = deadlineRepository.findListOfDeadlineByTitleAndUserId(title, user.getId());
        Deadline deadline_to_update = null;
        if (list_of_deadlines.size() == 1) {
            deadline_to_update = list_of_deadlines.get(0).get();
        }
        if (list_of_deadlines.size() > 1) {
            for (var deadline : list_of_deadlines) {
                if (Objects.equals(deadline.get().getDeadline_time(), time)
                        & Objects.equals(deadline.get().getDescription(), description)) {
                    deadline_to_update = list_of_deadlines.get(0).get();
                    break;
                }
            }
        }
        if (new_title != null && new_title.length() > 0 && !Objects.equals(deadline_to_update.getTitle(), new_title)) {
            deadline_to_update.setTitle(new_title);
        }
        if (new_time != null && !Objects.equals(deadline_to_update.getDeadline_time(), new_time)) {
            deadline_to_update.setDeadline_time(new_time);
        }
        if (new_description != null && !Objects.equals(deadline_to_update.getDescription(), new_description)) {
            deadline_to_update.setDescription(new_description);
        }


        if (new_list_of_group_titles != null && !new_list_of_group_titles.contains("delete ALL")) {
            Deadline new_deadline = new Deadline(deadline_to_update.getTitle(), deadline_to_update.getDeadline_time(),
                    deadline_to_update.getDescription());
            new_deadline.setUser(deadline_to_update.getUser());
            deadlineRepository.deleteById(deadline_to_update.getId());

            Set<Group> deadline_groups = new HashSet<>();
            for (var received_group : new_list_of_group_titles) {
                Group group = groupRepository.findGroupByNameAndUserId(received_group, user.getId()).orElseThrow(() ->
                        new IllegalStateException("Group with name " + received_group + " does not exists"));
                deadline_groups.add(group);
                new_deadline.setGroups(deadline_groups);
            }
            deadlineRepository.save(new_deadline);
        }

        else if (new_list_of_group_titles.size() == 1 && !new_list_of_group_titles.contains("delete ALL")){
            Deadline new_deadline = new Deadline(deadline_to_update.getTitle(), deadline_to_update.getDeadline_time(),
                    deadline_to_update.getDescription());
            new_deadline.setUser(deadline_to_update.getUser());
            deadlineRepository.deleteById(deadline_to_update.getId());
            deadlineRepository.save(new_deadline);
            }
//            Set<Group> current_deadline_groups = deadline_to_update.getGroups();
//            List<String> titles_of_groups_to_remove = new ArrayList<>();
//            if (!current_deadline_groups.isEmpty()) {
//                for (var current_group : current_deadline_groups) {
//                    String current_group_title = current_group.getGroup_name();
//                    if (new_list_of_group_titles.contains(current_group_title)) {
//                        new_list_of_group_titles.remove(current_group_title);
//                    }
//                    else {
//                        titles_of_groups_to_remove.add(current_group_title);
//                        new_list_of_group_titles.remove(current_group_title);
//                    }
//                }
//                if (!titles_of_groups_to_remove.isEmpty()) {
//                    for (var group_title : titles_of_groups_to_remove) {
//                        Group group = groupRepository.findGroupByNameAndUserId(group_title, user.getId()).orElseThrow(() ->
//                                new IllegalStateException("Group with name " + group_title + " does not exists"));
//                        deadline_to_update.getGroups().remove(group);
//                    }
//                }
//                if (!new_list_of_group_titles.isEmpty()) {
//                    Set<Group> deadline_groups = new HashSet<>();
//                    for (var received_group : new_list_of_group_titles) {
//                        Group group = groupRepository.findGroupByNameAndUserId(received_group, user.getId()).orElseThrow(() ->
//                                new IllegalStateException("Group with name " + received_group + " does not exists"));
//                        deadline_groups.add(group);
//                        deadline_to_update.setGroups(deadline_groups);
//                    }
//                } else {
//                    Set<Group> deadline_groups = new HashSet<>();
//                    for (var received_group : new_list_of_group_titles) {
//                        Group group = groupRepository.findGroupByNameAndUserId(received_group, user.getId()).orElseThrow(() ->
//                                new IllegalStateException("Group with name " + received_group + " does not exists"));
//                        deadline_groups.add(group);
//                        deadline_to_update.setGroups(deadline_groups);
//                    }
//                    deadlineRepository.save(deadline_to_update);
//
//                }
//            }

    }


    // TODO: Delete test_func()
//    public List<Optional<Deadline>> test_func(String title){
//        return deadlineRepository.findListOfDeadlineByTitle(title);
//    }


    public void test_func_2(String group_name, String group_description){
        User my_user = userService.getUserByUsername(userService.getLoggedUserUserName());
        Group user_group = new Group(group_name, group_description);
        my_user.addGroup(user_group);
        groupRepository.save(user_group);
        userRepository.save(my_user);
    }

    public void test_func_3(){
        User my_user = userService.getUserByUsername(userService.getLoggedUserUserName());
        List<Deadline> user_deadlines = my_user.getDeadlines();
        user_deadlines.get(0).setGroups(new HashSet<>());
        deadlineRepository.save(user_deadlines.get(0));
        userRepository.save(my_user);
    }
}
