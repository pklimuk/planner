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
    public DeadlineService(DeadlineRepository deadlineRepository, UserService userService, UserRepository userRepository, GroupRepository groupRepository) {
        this.deadlineRepository = deadlineRepository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
    }

    public List<Deadline> getUserDeadlines(){
        User deadline_user = userService.getUserByUsername(userService.getLoggedUserUserName());
        return deadline_user.getDeadlines();
    }

    public void addNewDeadline(String title, LocalDateTime time, String description, List<String> list_of_group_titles) {
        User user = userService.getUserByUsername(userService.getLoggedUserUserName());
        Deadline deadline = new Deadline(title, time, description);
        deadline.setUser(user);
        Set<Group> deadline_groups = new HashSet<>();
        if (list_of_group_titles.size() > 0) {
            for (var received_group : list_of_group_titles) {
                Group group = groupRepository.findGroupByNameAndUserId(received_group, user.getId().longValue()).orElseThrow(() ->
                        new IllegalStateException("Group with name " + received_group + " does not exists"));
                deadline_groups.add(group);
                group.getDeadlines().add(deadline);
            }

        }
        System.out.println("CREATED DEADLINE");
        deadline.setGroups(deadline_groups);
        deadlineRepository.save(deadline);
    }

    public void deleteDeadline(String title, LocalDateTime time, String description){
        User user = userService.getUserByUsername(userService.getLoggedUserUserName());
        List<Optional<Deadline>> list_of_deadlines = deadlineRepository.findListOfDeadlineByTitleAndUserId(title, user.getId().longValue());
        try {
            if(list_of_deadlines.size() == 1) {
//                list_of_deadlines.get(0).get().getGroups().clear();
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
            throw new IllegalStateException("Can not delete this deadline");
        }
    }

    @Transactional
    public void updateDeadline(String title, LocalDateTime time, String description){
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
}
