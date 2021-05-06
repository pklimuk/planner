package com.planner.planner;

import com.github.javafaker.Faker;
import com.planner.planner.deadline.Deadline;
import com.planner.planner.deadline.DeadlineRepository;
import com.planner.planner.event.Event;
import com.planner.planner.event.EventRepository;
import com.planner.planner.group.Group;
import com.planner.planner.group.GroupRepository;
import com.planner.planner.user.User;
import com.planner.planner.user.UserRepository;
import com.planner.planner.user.UserRole;
import com.planner.planner.userProfile.UserProfile;
import com.planner.planner.userProfile.UserProfileRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

//@EnableConfigurationProperties()
@SpringBootApplication
public class PlannerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlannerApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(
			UserProfileRepository userProfileRepository, UserRepository userRepository,
			EventRepository eventRepository, DeadlineRepository deadlineRepository, GroupRepository groupRepository) {

		return args -> {generateRandomUserProfiles(userProfileRepository, userRepository, eventRepository,
				deadlineRepository, groupRepository);};
//		eventRepository.deleteById(5L);};
	}

	private void generateRandomUserProfiles(UserProfileRepository userProfileRepository, UserRepository userRepository,
											EventRepository eventRepository, DeadlineRepository deadlineRepository,
											GroupRepository groupRepository) {
		Faker faker = new Faker();
		for (int i = 0; i < 10; i++) {
			//Creating group
			String group_name = faker.company().name();
			String group_description = faker.animal().name();
			Group user_group = new Group(group_name, group_description);
			groupRepository.save(user_group);

			//Creating user
			String firstName = faker.name().firstName();
			String lastName = faker.name().lastName();
			String email = String.format("%s.%s@pw.edu.pl", firstName, lastName);
			Integer year = faker.number().numberBetween(1950, 2020);
			Integer month = faker.number().numberBetween(1, 12);
			Integer day = faker.number().numberBetween(1, 27);
			LocalDate dob = LocalDate.of(year, month, day);
			String login = faker.crypto().md5();
			String password = faker.country().name();
			UserRole userRole = UserRole.USER;
			UserProfile userProfile = new UserProfile(
					firstName,
					lastName,
					email,
					dob);

			User user = new User(
					login,
					password,
					userRole,
					userProfile
			);
			//Creating an event
			for (int a = 0; a < faker.number().numberBetween(1, 3); a++){
				String event_title = faker.color().name();
				LocalDateTime event_start = LocalDateTime.now();
				LocalDateTime event_end = LocalDateTime.now().plusHours(faker.number().numberBetween(1, 10));
				String event_description = faker.company().name();
				Event new_event = new Event(event_title, event_start, event_end, event_description);
				user.addEvent(new_event);
			}
			//Creating a deadline
			for (int a = 0; a < faker.number().numberBetween(1, 3); a++) {
				String deadline_title = faker.color().name();
				LocalDateTime deadline_time = LocalDateTime.now().plusHours(faker.number().numberBetween(1, 10));
				String deadline_description = faker.company().name();
				Deadline new_deadline = new Deadline(deadline_title, deadline_time, deadline_description);
				user.addDeadline(new_deadline);
			}
			userRepository.save(user);

			// Adding events and deadlines to groups
			List<Event> user_events = user.getEvents();
			List<Deadline> user_deadlins = user.getDeadlines();
			user_group.getEvents().addAll(user_events);
			user_group.getDeadlines().addAll(user_deadlins);
			groupRepository.save(user_group);
		}

		String firstName = faker.name().firstName();
		String lastName = faker.name().lastName();
		String email = String.format("%s.%s@pw.edu.pl", firstName, lastName);
		Integer year = faker.number().numberBetween(1950, 2020);
		Integer month = faker.number().numberBetween(1, 12);
		Integer day = faker.number().numberBetween(1, 27);
		LocalDate dob = LocalDate.of(year, month, day);
		String login = faker.crypto().md5();
		String password = faker.country().name();
		UserProfile userProfile = new UserProfile(
				firstName,
				lastName,
				email,
				dob);
		userProfileRepository.save(userProfile);

//	User my_user = userRepository.findById(1L).get();
//	List<Event> user_events = my_user.getEvents();
//	List<Deadline> user_deadlines = my_user.getDeadlines();
//////		List<Group> user_groups = my_user.get
////		for (var i : user_events
////			 ) { System.out.println(i.toString()); }
////		System.out.println(user_deadlines);
	}

}