package rules

import static org.junit.Assert.*

import org.junit.AfterClass
import org.junit.AssumptionViolatedException
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runners.model.Statement

/**
 * 
 TestWatcher (and the deprecated TestWatchman) are base classes for rules that take note of the testing action, without modifying it. 
 *
 */
class TestWatcher_groovyfied_Test {
	public static StringBuilder dog = new StringBuilder()
	
	@Rule
	public TestWatcher watcher = [
			succeeded:{description -> dog << description.displayName << ' ' << 'success!\n'},
			failed:{error, description -> dog << description.displayName << ' ' << error.class.simpleName  <<'\n' }
			] as TestWatcher

	@Test
	void failTest() {
		fail("Not yet implemented")
	}
	@Test
	void passTest() {
	}
	
	@AfterClass
	static void afterClass() {
		println dog
	}
}



