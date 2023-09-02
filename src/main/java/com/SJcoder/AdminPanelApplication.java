
package com.SJcoder;
import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.SJcoder.model.applicationUsers;
import com.SJcoder.model.Role;
import com.SJcoder.repository.roleRepository;
import com.SJcoder.repository.userRepository;

@SpringBootApplication
public class AdminPanelApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdminPanelApplication.class, args);
	}
	
	@Bean
	CommandLineRunner run(userRepository userRepo, roleRepository roleRepo, PasswordEncoder encoder) {
		
		return args -> {
			
//			If admin role is present in the table then no need to create a separate admin role. For ddl-auto:update , it is used.
			if(roleRepo.findByAuthority("ADMIN").isPresent()) return;
			
//			Creating a admin role when the application executes if there is no admins.
			Role adminRole = roleRepo.save(new Role("ADMIN"));
			roleRepo.save(new Role("USER"));
			
			Set<Role> roles = new HashSet<>();
			roles.add(adminRole);
			
			applicationUsers admin = new applicationUsers(1,"admin","admin@gmail.com",encoder.encode("admin"),roles);
			userRepo.save(admin);
			
			};
		}

}