package io.egen.clearskyboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import io.egen.clearskyboot.config.SwaggerConfig;
import io.egen.clearskyboot.config.WebConfig;
import io.egen.clearskyboot.exceptions.RestResponseEntityExceptionHandler;

@SpringBootApplication
@Import({ WebConfig.class, SwaggerConfig.class, RestResponseEntityExceptionHandler.class })
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}