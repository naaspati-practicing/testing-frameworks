import static org.junit.Assert.*

import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

class SanityTest extends GroovyTestCase {
	@Before
	void setUp() {
		println 'setUp'
	}

	@After
	void tearDown() {
		println 'tearDown'
	}
	void testSomething() {
		println 'done'
	}

}
