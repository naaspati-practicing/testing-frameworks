package rules
import static org.junit.Assert.*

import org.junit.Test
import org.junit.rules.ExpectedException


/**
 * The ExpectedException rule is an important rule for handling exceptions. It allows
 * you to assert the expected exception type and the exception message, for example,
 * your code may throw a generic exception (such as IllegalStateException) for
 * all failure conditions, but you can assert the generic exception message to verify the
 * exact cause.
 * 
 * for individual test(s) use {@code @Test(expected=Exception class)} to test the error conditions.
 *
 */
class ExpectedExceptionRuleTest {
	public ExpectedException thrown = ExpectedException.none()

	@Test
	void throwsNothing() {
	}
	
	@Test
	void throwsNullpointer() {
		thrown.expect(NullPointerException)
		throw new NullPointerException()
	}
	
	@Test
	void throwsIllegalStateExceptionWithMsg() {
		thrown.expect(IllegalStateException)
		thrown.expectMessage('This is legal state?')
		throw new IllegalStateException('This is legal state?')
	}

}
