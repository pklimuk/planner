package com.planner.planner.deadline;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/user/deadlines")
public class DeadlineController {

    private final DeadlineService deadlineService;

    @Autowired
    public DeadlineController(DeadlineService deadlineService) {
        this.deadlineService = deadlineService;
    }

//    @PostMapping
//    public void addNewUser(@RequestBody ObjectNode objectNode) {
//        String firstName = objectNode.get("firstName").asText();
//        String lastName = objectNode.get("lastName").asText();


    @GetMapping("/test")
    public List<Deadline> test_func(String title){
        List<Deadline> user_deadlines = new ArrayList<>();
        for (var deadline: deadlineService.test_func("Second deadline")){
            Deadline new_deadline = new Deadline();
            new_deadline.setId(deadline.get().getId());
            new_deadline.setTitle(deadline.get().getTitle());
            new_deadline.setDeadline_time(deadline.get().getDeadline_time());
            new_deadline.setDescription(deadline.get().getDescription());
            new_deadline.setGroups(deadline.get().getGroups());
            user_deadlines.add(new_deadline);
        }
        return user_deadlines;
    }

    @DeleteMapping
    public void deleteDeadline(@RequestBody ObjectNode objectNode){

        String title = objectNode.get("title").asText();
        LocalDateTime time = LocalDateTime.parse(objectNode.get("deadline_time").asText());
        String description = objectNode.get("description").asText();
        deadlineService.deleteDeadline(title, time, description);
    }

    @GetMapping
    public List<Deadline> getUserDeadlines(){
        List<Deadline> user_deadlines = new ArrayList<>();
        for (var deadline: deadlineService.getUserDeadlines()){
            Deadline new_deadline = new Deadline();
                new_deadline.setTitle(deadline.getTitle());
                new_deadline.setDeadline_time(deadline.getDeadline_time());
                new_deadline.setDescription(deadline.getDescription());
                new_deadline.setGroups(deadline.getGroups());
            user_deadlines.add(new_deadline);
        }

        return user_deadlines;
    }

    @PostMapping
    public void addNewDeadline(@RequestBody Deadline deadline) {
        deadlineService.addNewDeadline(deadline);
    }

    @PutMapping
    public void updateUerProfile(@RequestBody ObjectNode objectNode) {

        String title = objectNode.get("title").asText();
        LocalDateTime time = LocalDateTime.parse(objectNode.get("deadline_time").asText());
        String description = objectNode.get("description").asText();
        deadlineService.updateDeadline(title, time, description);
    }
}
