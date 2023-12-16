package backend.VocaProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class VocaProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(VocaProjectApplication.class, args);
	}

}
