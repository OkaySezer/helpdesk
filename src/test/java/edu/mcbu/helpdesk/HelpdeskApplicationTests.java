package edu.mcbu.helpdesk;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * Smoke test: the application starts against a real PostgreSQL and the health check is UP.
 * Skipped automatically when Docker is not running (disabledWithoutDocker = true).
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Testcontainers(disabledWithoutDocker = true)
class HelpdeskApplicationTests {

	// Starts a throwaway PostgreSQL container; @ServiceConnection injects its URL/user/password into Spring.
	@Container
	@ServiceConnection
	static PostgreSQLContainer postgres = new PostgreSQLContainer(DockerImageName.parse("postgres:17"));

	@LocalServerPort
	int port;

	@Test
	void contextLoads() {
		// Passes if the Spring context starts without errors.
	}

	@Test
	void healthCheckReportsUp() {
		RestClient client = RestClient.builder().baseUrl("http://localhost:" + port).build();

		ResponseEntity<String> response = client.get().uri("/actuator/health").retrieve().toEntity(String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).contains("\"status\":\"UP\"");
	}

}
