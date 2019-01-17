package sam.learn.spock.fire;

import static org.junit.Assert.*;

import org.junit.Test;

public class FireEarlyWarningTest {

	@Test
	public void fireAlarmScenario() {
		FireEarlyWarning few = new FireEarlyWarning();
		int triggeredSensors = 1;
		
		few.feedData(triggeredSensors);
		WarningStatus ws = few.getCurrentStatus();
		
		assertTrue("Alarm sounds", ws.isAlarmActive());
		assertFalse("No Notifications", ws.isFireDepartmentNotified());
	}
}
