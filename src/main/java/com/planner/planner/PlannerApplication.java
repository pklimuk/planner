package com.planner.planner;

import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
public class PlannerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlannerApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(
			UserProfileRepository userProfileRepository,
			UserRepository userRepository) {

			return args -> { generateRandomUserProfiles(userProfileRepository, userRepository);
			userProfileRepository.deleteById(15L);
		};
	}

	private void generateRandomUserProfiles(UserProfileRepository userProfileRepository, UserRepository userRepository) {
		Faker faker = new Faker();
		for (int i = 0; i < 20; i++) {
			String firstName = faker.name().firstName();
			String lastName = faker.name().lastName();
			String email = String.format("%s.%s@amigoscode.edu", firstName, lastName);
			LocalDate dob = LocalDate.of(2000, 6, 2);
			String login = faker.name().firstName();
			String password = faker.name().lastName();
			UserProfile userProfile = new UserProfile(
					firstName,
					lastName,
					email,
					dob);

			com.planner.planner.User user = new User(
					login,
					password,
					userProfile
			);

			userRepository.save(user);
		}
	}

}