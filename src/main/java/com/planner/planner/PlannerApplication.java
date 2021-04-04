package com.planner.planner;

import com.github.javafaker.Faker;
import org.apache.catalina.User;
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

//	@Bean
//	CommandLineRunner commandLineRunner(
//			UserProfileRepository userProfileRepository,
//			UserRepository userRepository) {
//		return args -> {
//			Faker faker = new Faker();
//
//			String firstName = faker.name().firstName();
//			String lastName = faker.name().lastName();
//			String email = String.format("%s.%s@amigoscode.edu", firstName, lastName);
//			LocalDate dob = LocalDate.of(2000, 6, 2);
//			UserProfile userProfile = new UserProfile(
//					firstName,
//					lastName,
//					email,
//					dob);
//
//			User user = new User(
//					"my_login",
//					"my_password",
//					userProfile);
//
//			userRepository.save(user);
//
//			userProfileRepository.findById(1L)
//					.ifPresent(System.out::println);
//
//			userRepository.findById(1L)
//					.ifPresent(System.out::println);
//
////			userProfileRepository.deleteById(1L);
//
//		};
//	}
//
//	private void generateRandomUserProfiles(UserProfileRepository userProfileRepository) {
//		Faker faker = new Faker();
//		for (int i = 0; i < 20; i++) {
//			String firstName = faker.name().firstName();
//			String lastName = faker.name().lastName();
//			String email = String.format("%s.%s@amigoscode.edu", firstName, lastName);
//			LocalDate dob = LocalDate.of(2000, 6, 2);
//			UserProfile userProfile = new UserProfile(
//					firstName,
//					lastName,
//					email,
//					dob);
//			userProfileRepository.save(userProfile);
//		}
//	}

	@Bean
	CommandLineRunner commandLineRunner(UserProfileRepository userProfileRepository) {
		return args -> {
			UserProfile maria = new UserProfile(
					"Maria",
					"Jones",
					"maria.jones@amigoscode.edu",
					LocalDate.of(1990, 10, 10)
			);

			UserProfile ahmed = new UserProfile(
					"Ahmed",
					"Ali",
					"ahmed.ali@amigoscode.edu",
					LocalDate.of(1995, 05, 05)
			);

			System.out.println("Adding maria and ahmed");
			userProfileRepository.saveAll(List.of(maria, ahmed));

		};
	}
}