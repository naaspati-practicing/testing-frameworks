package sam.learn.spock.nuclear

import static org.junit.Assert.*

import org.junit.Test

import spock.lang.Specification

class NuclearReactorSpec extends Specification {

	def 'Complete test of all nuclear scenarios'(){
		given: 'a nuclear reactor and sensor data'
		NuclearReactorMonitor monitor = new NuclearReactorMonitor()
		
		when:'we examine the sensor data'
		monitor.feedFireSensorData(fireSensors)
		monitor.feedRadiationSensorData(radiation)
		monitor.feedPressureInBar(pressure)
		NuclearReactorStatus status = monitor.currentStatus
		
		then:'we act according to safety requirements'
		status.alarmActive == alarm
		status.shutDownNeeded == shutDown
		status.evacuationMinutes == evacuation
		
		where: 'Possible nuclear incidents are:'
		pressure | fireSensors | radiation              || alarm | shutDown | evacuation
		150      | 0           | []                     || false | false    | -1
		150      | 1           | []                     || true  | false    | -1
		150      | 3           | []                     || true  | true     | -1
		150      | 0           | [110.4f ,0.3f, 0.0f]   || true  | true     | 1
		150      | 0           | [45.3f ,10.3f, 47.7f]  || false | false    | -1
		155      | 0           | [0.0f ,0.0f, 0.0f]     || true  | false    | -1
		170      | 0           | [0.0f ,0.0f, 0.0f]     || true  | true     | 3
		180      | 0           | [110.4f ,0.3f, 0.0f]   || true  | true     | 1
		500      | 0           | [110.4f ,300f, 0.0f]   || true  | true     | 1
		30       | 0           | [110.4f ,1000f, 0.0f]  || true  | true     | 1
		155      | 4           | [0.0f ,0.0f, 0.0f]     || true  | true     | -1
		170      | 1           | [45.3f ,10.3f, 47.7f]  || true  | true     | 3
		
		// The || notation is used to split the inputs from outputs. 
	}

}
