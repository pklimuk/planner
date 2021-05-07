package com.planner.planner.deadline;

import com.planner.planner.user.User;
import com.planner.planner.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeadlineService {

    private final DeadlineRepository deadlineRepository;
    private final UserService userService;

    @Autowired
    public DeadlineService(DeadlineRepository deadlineRepository, UserService userService) {
        this.deadlineRepository = deadlineRepository;
        this.userService = userService;
    }

    public void addNewDeadline(Deadline deadline){
        User deadline_user = userService.getUserByUsername(userService.getLoggedUserUserName());
        deadline.setUser(deadline_user);
        deadlineRepository.save(deadline);
    }
}
