package sam.learn.spock.coolant

import static org.junit.Assert.*

import org.junit.Test

import groovy.test.GroovyAssert
import spock.lang.Specification

class TemperatureReaderSpec extends Specification {
    def 'if current temperature difference is within limits everything is ok'() {
        given:'that temprature readings are within limits'
        TemperatureReadings prev = new TemperatureReadings(20, 40, 80)
        TemperatureReadings current = new TemperatureReadings(30, 45, 73)
        TemperatureReader reader = Stub(TemperatureReader)
        
        reader.getCurrentReadings() >>> [prev, current] // Instructing the dummy interface to return premade readings
        
        TemperatureMonitor monitor = new TemperatureMonitor(reader)
        
        when:'we ask the status of temperature control'
        monitor.readSensor()
        monitor.readSensor()
        
        then: 'everything shold be ok'
        monitor.isTemperatureNormal()  // Assertion after two subsequent calls 
    }
    
    def 'if current temperature difference is more than 20 degrees the alarm should sound'() {
        given:'that temperature difference is not within limits'
        TemperatureReadings prev = new TemperatureReadings(20, 40, 80)
        TemperatureReadings current = new TemperatureReadings(30, 10, 73)
        
        // Spock, behind the scenes, creates a dummy implementation of this interface.
        // By default the implementation does nothing, so it must be instructed how to react, which 
        // is done with the second important line, the >>> operator:
        TemperatureReader reader = Stub(TemperatureReader)
        reader.getCurrentReadings() >>> [prev, current] // Instructing the dummy interface to return premade readings, on first call return prev, on second call return current
        
        TemperatureMonitor monitor = new TemperatureMonitor(reader)
        
        when:'we ask the status of temperature control'
        monitor.readSensor()
        monitor.readSensor()
        
        then: 'the alarm should sound'
        !monitor.isTemperatureNormal()
    }
}
