import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.TestInfo;

public class RepeatedTestsDemo {
	private static final Logger LOGGER = Logger.getLogger(RepeatedTestsDemo.class.getName());
	
	@BeforeEach
	void beforeEach(TestInfo testInfo, RepetitionInfo repetitionInfo) {
		LOGGER.info(String.format("(beforeEach()) About to execute repetition %d of %d for %s",
				repetitionInfo.getCurrentRepetition(),
				repetitionInfo.getTotalRepetitions(),
				testInfo.getTestMethod().get().getName()
				));
	}
	
	@RepeatedTest(10)
	void repeatedTest() {
		LOGGER.info("repeatedTest()");
	}
	@RepeatedTest(value=2,name= "{displayName}/{currentRepetition/{totalRepetitions}")
	@DisplayName("custom repeat name!")
	void customDisplayNameTest() {
		LOGGER.info("customDisplayNameTest()");
	}
	@RepeatedTest(value=2,name= RepeatedTest.LONG_DISPLAY_NAME)
	void customDefaultDisplayNameTest() {
		LOGGER.info("customDisplayNameTest()");
	}
}
