package rules

import static org.junit.Assert.*

import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestName

class TestNameTest {
	@Rule
	public TestName name = new TestName()

	@Test
	public void test_santosh() {
		assertEquals('test_santosh', name.methodName)
	}
	@Test
	public void test_ganesh() {
		assertEquals('test_ganesh', name.methodName)
	}

}
