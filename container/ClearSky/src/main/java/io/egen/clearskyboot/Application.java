package io.egen.clearskyboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.AbstractEnvironment;

import io.egen.clearskyboot.config.SwaggerConfig;
import io.egen.clearskyboot.config.WebConfig;
import io.egen.clearskyboot.exceptions.RestResponseEntityExceptionHandler;

@SpringBootApplication
@Import({ WebConfig.class, SwaggerConfig.class, RestResponseEntityExceptionHandler.class })
public class Application {

	public static void main(String[] args) {
		//System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "prod");
		SpringApplication.run(Application.class, args);
	}
}