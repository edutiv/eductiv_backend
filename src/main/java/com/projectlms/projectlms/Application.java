package com.projectlms.projectlms;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.projectlms.projectlms.domain.dao.Role;
import com.projectlms.projectlms.domain.dao.RoleEnum;
import com.projectlms.projectlms.repository.RoleRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class Application {
	@Autowired
	private RoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	InitializingBean sendDatabase() {
    return () -> {
			Role checkAdmin = roleRepository.findByName(RoleEnum.ROLE_ADMIN).orElse(null);
				if(checkAdmin == null) {
					log.info("Set role admin");
					Role roleAdmin = new Role();
					roleAdmin.setName(RoleEnum.ROLE_ADMIN);
					roleRepository.save(roleAdmin);
				}
			Role checkMentor = roleRepository.findByName(RoleEnum.ROLE_MENTOR).orElse(null);
				if(checkMentor == null) {
					log.info("Set role mentor");
					Role roleMentor = new Role();
					roleMentor.setName(RoleEnum.ROLE_MENTOR);
					roleRepository.save(roleMentor);
				}
			Role checkUser = roleRepository.findByName(RoleEnum.ROLE_USER).orElse(null);
				if(checkUser == null) {
					log.info("Set role user");
					Role roleUser = new Role();
					roleUser.setName(RoleEnum.ROLE_USER);
					roleRepository.save(roleUser);
				}
			};
		}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedMethods("GET", "POST", "PUT", "DELETE")
						.allowCredentials(true)
						.allowedOrigins("http://localhost:3000", "https://edutiv-web.vercel.app", "https://edutiv-capstone.herokuapp.com");
			}    
		};
	}

}
