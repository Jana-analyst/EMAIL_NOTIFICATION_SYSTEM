package com.example.CapStoneProject;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ActiveProfiles("test") // This links to application-test.properties
class DemoApplicationTests {

	@Test
	void contextLoads() {
	}

}
