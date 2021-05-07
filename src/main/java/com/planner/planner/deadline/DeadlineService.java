package com.planner.planner.deadline;

import com.planner.planner.user.User;
import com.planner.planner.user.UserService;
import com.planner.planner.userProfile.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class DeadlineService {

    private final DeadlineRepository deadlineRepository;
    private final UserService userService;

    @Autowired
    public DeadlineService(DeadlineRepository deadlineRepository, UserService userService) {
        this.deadlineRepository = deadlineRepository;
        this.userService = userService;
    }

    public List<Deadline> getUserDeadlines(){
        User deadline_user = userService.getUserByUsername(userService.getLoggedUserUserName());
        return deadline_user.getDeadlines();
    }

    public void addNewDeadline(Deadline deadline){
        User deadline_user = userService.getUserByUsername(userService.getLoggedUserUserName());
        deadline.setUser(deadline_user);
        deadlineRepository.save(deadline);
    }

    public void deleteDeadline(String title, LocalDateTime time, String description){
        List<Optional<Deadline>> list_of_deadlines = deadlineRepository.findListOfDeadlineByTitle(title);
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
            throw new IllegalStateException("Can not delete this deadline");
        }
    }

    @Transactional
    public void updateDeadline(String title, LocalDateTime time, String description){
        List<Optional<Deadline>> list_of_deadlines = deadlineRepository.findListOfDeadlineByTitle(title);
        Deadline deadline_to_update = null;
        if(list_of_deadlines.size() == 1) {
            deadline_to_update = list_of_deadlines.get(0).get();
        }
        if(list_of_deadlines.size() > 1) {
            for (var deadline: list_of_deadlines) {
                if (Objects.equals(deadline.get().getDeadline_time(), time)
                        & Objects.equals(deadline.get().getDescription(), description)) {
                    deadline_to_update = list_of_deadlines.get(0).get();
                    break;
                }
            }
        }


        if (title != null && title.length() > 0 && !Objects.equals(deadline_to_update.getTitle(), title)) {
            deadline_to_update.setTitle(title);
        }
        if (time != null && !Objects.equals(deadline_to_update.getDeadline_time(), time)) {
            deadline_to_update.setDeadline_time(time);
        }
        if (!Objects.equals(deadline_to_update.getDescription(), description)) {
            deadline_to_update.setDescription(description);
        }
    }


    // TODO: Delete test_func()
    public List<Optional<Deadline>> test_func(String title){
        return deadlineRepository.findListOfDeadlineByTitle(title);
    }
}
