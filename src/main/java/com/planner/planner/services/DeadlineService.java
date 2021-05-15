package com.planner.planner.services;

import com.planner.planner.models.Group;
import com.planner.planner.repositories.GroupRepository;
import com.planner.planner.models.Deadline;
import com.planner.planner.repositories.DeadlineRepository;
import com.planner.planner.models.User;
import com.planner.planner.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

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

    public Boolean checkIfDeadlineTimeIsCorrect(LocalDateTime time) {
        boolean deadline_time_is_correct = false;
        if (time.isAfter(LocalDateTime.now())) {
            deadline_time_is_correct = true;
        } else {
            throw new IllegalStateException("Provided time is not correct");
        }
        return deadline_time_is_correct;
    }

    public List<Deadline> getUserDeadlines() {
        User deadline_user = userService.getUserByUsername(userService.getLoggedUserUserName());
        return deadline_user.getDeadlines();
    }

    public void add_groups_from_list_of_string(Deadline deadline_to_update, List<String> list_of_groups_titles, User user) {
        Set<Group> deadline_groups = new HashSet<>();
        for (var group_title : list_of_groups_titles) {
            Group group = groupRepository.findGroupByNameAndUserId(group_title, user.getId()).orElseThrow(() ->
                    new IllegalStateException("Group with name " + group_title + " does not exists"));
            deadline_groups.add(group);
            group.getDeadlines().add(deadline_to_update);
        }
        deadline_to_update.setGroups(deadline_groups);
        deadlineRepository.save(deadline_to_update);
    }

    public void addNewDeadline(String title, LocalDateTime time, String description, List<String> list_of_group_titles) {
        User user = userService.getUserByUsername(userService.getLoggedUserUserName());
        checkIfDeadlineTimeIsCorrect(time);
        Deadline deadline = new Deadline(title, time, description);
        deadline.setUser(user);
        if (!list_of_group_titles.isEmpty()) {
            add_groups_from_list_of_string(deadline, list_of_group_titles, user);
        } else {
            deadlineRepository.save(deadline);
        }
    }

    public void deleteDeadline(String title, LocalDateTime time, String description) {
        User user = userService.getUserByUsername(userService.getLoggedUserUserName());
        List<Optional<Deadline>> list_of_deadlines = deadlineRepository.findListOfDeadlineByTitleAndUserId(title, user.getId());
        try {
            if (list_of_deadlines.size() == 1) {
                deadlineRepository.deleteById(list_of_deadlines.get(0).get().getId());
            }
            if (list_of_deadlines.size() > 1) {
                for (var deadline : list_of_deadlines) {
                    if (Objects.equals(deadline.get().getDeadline_time(), time)
                            & Objects.equals(deadline.get().getDescription(), description)) {
                        deadlineRepository.deleteById(list_of_deadlines.get(0).get().getId());
                        break;
                    }
                }
            }
        } catch (Exception e) {
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
            checkIfDeadlineTimeIsCorrect(new_time);
            deadline_to_update.setDeadline_time(new_time);
        }
        if (new_description != null && !Objects.equals(deadline_to_update.getDescription(), new_description)) {
            deadline_to_update.setDescription(new_description);
        }


        if (new_list_of_group_titles.size() != 0 && !new_list_of_group_titles.contains("CLEAR ALL")) {
            Deadline new_deadline = new Deadline(deadline_to_update.getTitle(), deadline_to_update.getDeadline_time(),
                    deadline_to_update.getDescription());
            new_deadline.setUser(deadline_to_update.getUser());
            deadlineRepository.deleteDeadlineById(deadline_to_update.getId());
            add_groups_from_list_of_string(new_deadline, new_list_of_group_titles, user);
            deadlineRepository.save(new_deadline);
        } else if (new_list_of_group_titles.size() == 1 && new_list_of_group_titles.contains("CLEAR ALL")) {
            Deadline new_deadline = new Deadline(deadline_to_update.getTitle(), deadline_to_update.getDeadline_time(),
                    deadline_to_update.getDescription());
            new_deadline.setUser(deadline_to_update.getUser());
            deadlineRepository.deleteDeadlineById(deadline_to_update.getId());
            deadlineRepository.save(new_deadline);
        }
    }
}
