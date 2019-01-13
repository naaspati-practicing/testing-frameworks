package rules
import static org.junit.Assert.*

import org.junit.Rule
import org.junit.Test
import org.junit.rules.Timeout

class TimeoutRuleTest {
	@Rule
	public org.junit.rules.Timeout timeout = new Timeout(20)

	/*
	 * for induvidual test timeout, {@code @Test(timeout=10)} can be used.
	 * but The timeout rule applies the same timeout to all the test methods in a class.
	 */
	
	@Test
	void testLongTime() {
		sleep(1000)
	}
}
