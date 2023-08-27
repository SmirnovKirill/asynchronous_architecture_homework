package asynchomework.auth.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuthLauncher {
	//TODO валидатор прикрутить
	//TODO сделать oauth
	public static void main(String[] args) {
		SpringApplication.run(AuthLauncher.class, args);
	}
}
