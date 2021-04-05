package com.planner.planner;

import com.github.javafaker.Faker;
import com.planner.planner.deadline.Deadline;
import com.planner.planner.deadline.DeadlineRepository;
import com.planner.planner.event.Event;
import com.planner.planner.event.EventRepository;
import com.planner.planner.user.User;
import com.planner.planner.user.UserRepository;
import com.planner.planner.userProfile.UserProfile;
import com.planner.planner.userProfile.UserProfileRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootApplication
public class PlannerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlannerApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(
			UserProfileRepository userProfileRepository, UserRepository userRepository,
			EventRepository eventRepository, DeadlineRepository deadlineRepository) {

		return args -> {generateRandomUserProfiles(userProfileRepository, userRepository, eventRepository, deadlineRepository);};
//		eventRepository.deleteById(5L);};
	}

	private void generateRandomUserProfiles(UserProfileRepository userProfileRepository, UserRepository userRepository,
											EventRepository eventRepository, DeadlineRepository deadlineRepository) {
		Faker faker = new Faker();
		for (int i = 0; i < 10; i++) {
			String firstName = faker.name().firstName();
			String lastName = faker.name().lastName();
			String email = String.format("%s.%s@amigoscode.edu", firstName, lastName);
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

			User user = new User(
					login,
					password,
					userProfile
			);
			for (int a = 0; a < faker.number().numberBetween(1, 3); a++){
				String event_title = faker.color().name();
				LocalDateTime event_start = LocalDateTime.now();
				LocalDateTime event_end = LocalDateTime.now().plusHours(faker.number().numberBetween(1, 10));
				String event_description = faker.company().name();
				String event_group = faker.company().name();
				Event new_event = new Event(event_title, event_start, event_end, event_description, event_group);
				user.addEvent(new_event);
			}

			for (int a = 0; a < faker.number().numberBetween(1, 3); a++) {
				String deadline_title = faker.color().name();
				LocalDateTime deadline_time = LocalDateTime.now().plusHours(faker.number().numberBetween(1, 10));
				String deadline_description = faker.company().name();
				String deadline_group = faker.company().name();
				Deadline new_deadline = new Deadline(deadline_title, deadline_time, deadline_description, deadline_group);
				user.addDeadline(new_deadline);
			}
			userRepository.save(user);
		}

	}
}