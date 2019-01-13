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
class TestWatcherTest {
	public static StringBuilder dog = new StringBuilder()
	
	@Rule
	public TestWatcher watcher = new TestWatcher() {
		
			@Override
			public Statement apply(Statement base, Description description) {
				return super.apply(base, description);
			}

			/** */		
			@Override
			protected void succeeded(Description description) {
				dog << description.displayName << ' ' << 'success\n' 
			}
		
			@Override
			protected void failed(Throwable e, Description description) {
				dog << description.displayName << ' ' << e.class.simpleName  <<'\n'
			}
		
			@Override
			protected void skipped(AssumptionViolatedException e, Description description) {
				super.skipped(e, description);
			}
		
			@Override
			protected void skipped(org.junit.internal.AssumptionViolatedException e, Description description) {
				super.skipped(e, description);
			}
		
			@Override
			protected void starting(Description description) {
				super.starting(description);
			}
		
			@Override
			protected void finished(Description description) {
				super.finished(description);
			}
			
		}
	

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



