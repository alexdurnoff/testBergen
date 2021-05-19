package ru.durnov.testTask;

import com.github.dockerjava.zerodep.shaded.org.apache.hc.client5.http.classic.methods.HttpPost;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.http.io.entity.StringEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import ru.durnov.testTask.dao.JMSRepository;
import ru.durnov.testTask.requestbody.JsonMessage;
import ru.durnov.testTask.requestbody.JsonMessages;

import java.io.FileNotFoundException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Testcontainers
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(initializers = {TestTaskApplicationTests.Initializer.class})
@Slf4j
class TestTaskApplicationTests {



	@Container
	public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13")
			.withDatabaseName("test")
			.withUsername("alexej")
			.withPassword("alexej");

	@Configuration
	public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
		@Override
		public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
			TestPropertyValues.of(
					"spring.datasource.url="+postgreSQLContainer.getJdbcUrl(),
					"spring.datasource.username=" + postgreSQLContainer.getUsername(),
					"spring.datasource.password=" + postgreSQLContainer.getPassword(),
					"spring.activemq.in-memory=false"
			).applyTo(configurableApplicationContext.getEnvironment());
		}
	}

	@Test
	public void testIsRunning(){
		Assertions.assertTrue(postgreSQLContainer.isRunning());
	}

	@Test
	public void testSaveSingleMessage(){

	}

	@Test
	public void testSaveHttpRequest() throws JsonProcessingException, FileNotFoundException, ExecutionException, InterruptedException {
		JsonMessage jsonMessage = new JsonMessage(4, "localhost", "Hello, world!");
		JsonMessages jsonMessages = new JsonMessages(jsonMessage);
		ObjectMapper objectMapper = new ObjectMapper();
		String json = new String(objectMapper.writeValueAsBytes(jsonMessages));
		log.debug("uri is " + "http://" + postgreSQLContainer.getHost());
		HttpRequest httpRequest = HttpRequest.newBuilder()
				.uri(URI.create("http://" + postgreSQLContainer.getHost()))
				.timeout(Duration.ofMinutes(1))
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(json))
				.build();
		HttpClient client = HttpClient.newBuilder()
				.version(HttpClient.Version.HTTP_1_1)
				.followRedirects(HttpClient.Redirect.NORMAL)
				.connectTimeout(Duration.ofSeconds(20))
				.build();
		CompletableFuture<Integer> integerCompletableFuture = client.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
				.thenApply(HttpResponse::statusCode);
		Integer code = integerCompletableFuture.get();
		Assertions.assertEquals(code, HttpStatus.CREATED);
	}


}
