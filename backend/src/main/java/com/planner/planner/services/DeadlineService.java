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
    private Boolean enableTimeChecking = false;

    @Autowired
    public DeadlineService(DeadlineRepository deadlineRepository, UserService userService,
                           UserRepository userRepository, GroupRepository groupRepository) {
        this.deadlineRepository = deadlineRepository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
    }

    public Boolean checkIfDeadlineTimeIsCorrect(LocalDateTime time) {
        boolean deadlineTimeIsCorrect = false;
        if (time.isAfter(LocalDateTime.now())) {
            deadlineTimeIsCorrect = true;
        } else {
            throw new IllegalStateException("Provided time is not correct");
        }
        return deadlineTimeIsCorrect;
    }

    public List<Deadline> getUserDeadlines() {
        User deadlineUser = userService.getUserByUsername(userService.getLoggedUserUserName());
        return deadlineUser.getDeadlines();
    }

    public void addGroupsFromListOfString(Deadline deadlineToUpdate, List<String> listOfGroupsTitles, User user) {
        Set<Group> deadlineGroups = new HashSet<>();
        for (var groupTitle : listOfGroupsTitles) {
            Group group = groupRepository.findGroupByNameAndUserId(groupTitle, user.getId()).orElseThrow(() ->
                    new IllegalStateException("Group with name " + groupTitle + " does not exists"));
            deadlineGroups.add(group);
            group.getDeadlines().add(deadlineToUpdate);
        }
        deadlineToUpdate.setGroups(deadlineGroups);
        deadlineRepository.save(deadlineToUpdate);
    }

    public void addNewDeadline(String title, LocalDateTime time, String description, List<String> listOfGroupTitles) {
        User user = userService.getUserByUsername(userService.getLoggedUserUserName());
        if (enableTimeChecking) {
            checkIfDeadlineTimeIsCorrect(time);
        }
        Deadline deadline = new Deadline(title, time, description);
        deadline.setUser(user);
        if (!listOfGroupTitles.isEmpty()) {
            addGroupsFromListOfString(deadline, listOfGroupTitles, user);
        } else {
            deadlineRepository.save(deadline);
        }
    }

    public void deleteDeadline(String title, LocalDateTime time, String description) {
        User user = userService.getUserByUsername(userService.getLoggedUserUserName());
        List<Optional<Deadline>> listOfDeadlines = deadlineRepository.findListOfDeadlineByTitleAndUserId(title, user.getId());
        try {
            if (listOfDeadlines.size() == 1) {
                deadlineRepository.deleteById(listOfDeadlines.get(0).get().getId());
            }
            if (listOfDeadlines.size() > 1) {
                for (var deadline : listOfDeadlines) {
                    if (Objects.equals(deadline.get().getDeadline_time(), time)
                            & Objects.equals(deadline.get().getDescription(), description)) {
                        deadlineRepository.deleteById(listOfDeadlines.get(0).get().getId());
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
                               String newTitle, LocalDateTime newTime,
                               String newDescription, List<String> newListOfGroupTitles) {
        User user = userService.getUserByUsername(userService.getLoggedUserUserName());
        List<Optional<Deadline>> listOfDeadlines = deadlineRepository.findListOfDeadlineByTitleAndUserId(title, user.getId());
        Deadline deadlineToUpdate = null;
        if (listOfDeadlines.size() == 1) {
            deadlineToUpdate = listOfDeadlines.get(0).get();
        }
        if (listOfDeadlines.size() > 1) {
            for (var deadline : listOfDeadlines) {
                if (Objects.equals(deadline.get().getDeadline_time(), time)
                        & Objects.equals(deadline.get().getDescription(), description)) {
                    deadlineToUpdate = listOfDeadlines.get(0).get();
                    break;
                }
            }
        }
        if (newTitle != null && newTitle.length() > 0 && !Objects.equals(deadlineToUpdate.getTitle(), newTitle)) {
            deadlineToUpdate.setTitle(newTitle);
        }
        if (newTime != null && !Objects.equals(deadlineToUpdate.getDeadline_time(), newTime)) {
            if (enableTimeChecking) {
                checkIfDeadlineTimeIsCorrect(newTime);
            }
            deadlineToUpdate.setDeadline_time(newTime);
        }
        if (newDescription != null && !Objects.equals(deadlineToUpdate.getDescription(), newDescription)) {
            deadlineToUpdate.setDescription(newDescription);
        }


        if (newListOfGroupTitles.size() != 0 && !newListOfGroupTitles.contains("CLEAR ALL")) {
            Deadline newDeadline = new Deadline(deadlineToUpdate.getTitle(), deadlineToUpdate.getDeadline_time(),
                    deadlineToUpdate.getDescription());
            newDeadline.setUser(deadlineToUpdate.getUser());
            deadlineRepository.deleteDeadlineById(deadlineToUpdate.getId());
            addGroupsFromListOfString(newDeadline, newListOfGroupTitles, user);
            deadlineRepository.save(newDeadline);
        } else if (newListOfGroupTitles.size() == 1 && newListOfGroupTitles.contains("CLEAR ALL")) {
            Deadline newDeadline = new Deadline(deadlineToUpdate.getTitle(), deadlineToUpdate.getDeadline_time(),
                    deadlineToUpdate.getDescription());
            newDeadline.setUser(deadlineToUpdate.getUser());
            deadlineRepository.deleteDeadlineById(deadlineToUpdate.getId());
            deadlineRepository.save(newDeadline);
        }
    }
}
