package com.planner.planner.deadline;

import antlr.collections.impl.IntRange;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.planner.planner.group.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.ranges.Range;

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


//    @GetMapping("/test")
//    public List<Deadline> test_func(String title){
//        List<Deadline> user_deadlines = new ArrayList<>();
//        for (var deadline: deadlineService.test_func("Second deadline")){
//            Deadline new_deadline = new Deadline();
//            new_deadline.setId(deadline.get().getId());
//            new_deadline.setTitle(deadline.get().getTitle());
//            new_deadline.setDeadline_time(deadline.get().getDeadline_time());
//            new_deadline.setDescription(deadline.get().getDescription());
//            new_deadline.setGroups(deadline.get().getGroups());
//            user_deadlines.add(new_deadline);
//        }
//        return user_deadlines;
//    }

    @GetMapping("/test")
    public void test_func2(){
        deadlineService.test_func_2("My_group 2", "Simple Description");
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
    public void addNewDeadline(@RequestBody ObjectNode objectNode) {
//        String title = objectNode, LocalDateTime time, String description, List<String> list_of_group_titles
        String title = objectNode.get("title").asText();
        LocalDateTime time = LocalDateTime.parse(objectNode.get("deadline_time").asText());
        String description = objectNode.get("description").asText();
        List<String> list_of_group_titles = new ArrayList<>();
        Integer i = 0;
        if (objectNode.get("groups") != null) {
            System.out.println("IM IN THE IF LOOP");
            while (i < objectNode.get("groups").size()) {
                System.out.println("IM IN THE WHILE LOOP");
                list_of_group_titles.add(objectNode.get("groups").get(i).asText());
                i += 1;
            }
        }
        System.out.println("IM HAVE SENT DEADLINE TO SERVICE");
        deadlineService.addNewDeadline(title, time, description, list_of_group_titles);
    }

    @PutMapping
    public void updateUerProfile(@RequestBody ObjectNode objectNode) {

        String title = objectNode.get("title").asText();
        LocalDateTime time = LocalDateTime.parse(objectNode.get("deadline_time").asText());
        String description = objectNode.get("description").asText();
        deadlineService.updateDeadline(title, time, description);
    }
}
