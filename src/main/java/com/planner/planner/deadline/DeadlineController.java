package com.planner.planner.deadline;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/user/deadlines")
public class DeadlineController {

    private final DeadlineService deadlineService;

    @Autowired
    public DeadlineController(DeadlineService deadlineService) {
        this.deadlineService = deadlineService;
    }


    @PostMapping
    public void addNewDeadline(@RequestBody Deadline deadline) {
        deadlineService.addNewDeadline(deadline);
    }
}
