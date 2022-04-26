package net.knowledgebase.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class KnowledgebaseApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(KnowledgebaseApplication.class, args);
	}

}
