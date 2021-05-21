package ru.durnov.testTask;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import ru.durnov.testTask.controller.JMSController;
import ru.durnov.testTask.requestbody.JsonDestination;
import ru.durnov.testTask.requestbody.JsonInterval;
import ru.durnov.testTask.requestbody.JsonMessage;
import ru.durnov.testTask.requestbody.JsonMessages;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Testcontainers
@SpringBootTest(classes = TestTaskApplication.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(initializers = {TestTaskApplicationTests.Initializer.class})
@AutoConfigureMockMvc
class TestTaskApplicationTests {
	@Autowired
	private MockMvc mockMvc;

	private static final ObjectMapper objectMapper = new ObjectMapper();

	/*@Container
	public static GenericContainer<?> artemisContainer = new GenericContainer<>("vromero/activemq-artemis")
			.withExposedPorts(61616);*/

	@Container
	public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13")
			.withDatabaseName("test")
			.withUsername("alexej")
			.withPassword("alexej")
			.withInitScript("data.sql");

	@Configuration
	public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
		@Override
		public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
			TestPropertyValues.of(
					"spring.datasource.url="+ postgreSQLContainer.getJdbcUrl(),
					"spring.datasource.username=" + postgreSQLContainer.getUsername(),
					"spring.datasource.password=" + postgreSQLContainer.getPassword(),
					"spring.datasource.driverClassName=org.postgresql.Driver",
					"embedded.postgresql.schema=data.sql",
					"org.apache.activemq=artemis-jms-server",
					"spring.jpa.database-platform=org.hibernate.dialect.PostgresPlusDialect",
					"server.port=8081"
			).applyTo(configurableApplicationContext.getEnvironment());
		}
	}

	@BeforeAll
	static void init(){
		postgreSQLContainer.start();
		/*artemisContainer.start();*/
	}

	@Test
	public void testIsRunning(){
		Assertions.assertTrue(postgreSQLContainer.isRunning());
		/*Assertions.assertTrue(artemisContainer.isRunning());*/
	}

	@Test
	public void testSaveHttpRequest() throws Exception {
		JsonMessages jsonMessages = new JsonMessages(new JsonMessage(4, "localhost", "Hello, world!"));
		mockMvc.perform(post("/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(jsonMessages)))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED.value()));
	}

	@Test
	public void testGetMessageById() throws Exception {
		mockMvc.perform(get("/2"))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FOUND.value()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.body").value("Пока, сосед!"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.destination").value("192.168.1.190.test.task"));
	}

	@Test
	public void testByDestination() throws Exception {
		mockMvc.perform(get("/search_by_destination")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(new JsonDestination("192.168.1.190.test.task"))))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FOUND.value()));
	}

	@Test
	public void testByInterval() throws Exception {
		JsonInterval jsonInterval = new JsonInterval(
				LocalDateTime.of(2021, 5, 1, 23, 15),
				LocalDateTime.of(2021, 5, 10, 19, 15)
		);
		mockMvc.perform(get("/search_by_interval")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(jsonInterval)))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.FOUND.value()));
	}

}
