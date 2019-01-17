import static org.junit.jupiter.api.Assertions.fail;

import java.util.logging.Logger;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class StandardTests {
	private static final Logger LOGGER = Logger.getLogger(StandardTests.class.getName());
	
	@BeforeAll
	static void initAll() {
		LOGGER.info("@BeforeAll static void initAll(){}");
	}
	@BeforeEach 
	void init() {
		LOGGER.info("@BeforeEach void init(){}");
	}
	@Test
	void succeedingTest() {
		LOGGER.info("@Test succeedingTest() {}");
	}
	@Test
	void failingTest() {
		LOGGER.info("@Test failingTest() {}");
		fail("a failing test");
	}
	@Test
	@Disabled
	void disabledTest() {
		LOGGER.info("@Test disabledTest() {}");
		fail("disabled test");
	}
	
	@AfterAll
	static void tearDownAll() {
		LOGGER.info("@AfterAll static void tearDownAll(){}");
	}
	@AfterEach 
	void tearDown() {
		LOGGER.info("@AfterEach void tearDown(){}");
	}
	

}
