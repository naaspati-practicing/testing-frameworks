package sam.learn.spock.fire

import static org.junit.Assert.*

import org.junit.Test

import spock.lang.Specification

class FireEarlyWarningSpec extends Specification {
	def 'If all sensors are inactive everything is ok'() {
		
		given:'That all fire sensors are off'
		FireEarlyWarning few = new FireEarlyWarning()
		int triggeredSensors = 0
		
		when:'we ask status of fire control'
		few.feedData(triggeredSensors)
		WarningStatus ws = few.currentStatus
		
		then:'no alarm/notification should be triggered'
		!ws.alarmActive
		!ws.fireDepartmentNotified
	}
	def 'If only one sensor is active the alarm should ring as a precaution'() {
		
		given:'that one sensor is active'
		FireEarlyWarning few = new FireEarlyWarning()
		int triggeredSensors = 1
		
		when:'we ask status of fire control'
		few.feedData(triggeredSensors)
		WarningStatus ws = few.currentStatus
		
		then:'only the alarm should be active'
		ws.alarmActive
		!ws.fireDepartmentNotified
	}
	def 'If more than one sensor is active then we have a fire'() {
		
		given:'that two sensors is active'
		FireEarlyWarning few = new FireEarlyWarning()
		int triggeredSensors = 2
		
		when:'we ask status of fire control'
		few.feedData(triggeredSensors)
		WarningStatus ws = few.currentStatus
		
		then:'alarm is triggered and fire department is notified'
		ws.alarmActive
		ws.fireDepartmentNotified
	}
}
