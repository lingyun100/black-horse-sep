package com.example.blackhorse;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseIntegrationTest {
  private static final Integer MOCK_SERVER_PORT = 8081;
  private static ClientAndServer clientAndServer;
  protected MockServerClient mockServerClient = new MockServerClient("localhost", MOCK_SERVER_PORT);

  @BeforeAll
  static void beforeAll() {
    clientAndServer = ClientAndServer.startClientAndServer(MOCK_SERVER_PORT);
  }

  @AfterAll
  static void afterAll() {
    clientAndServer.stop();
  }
}
