package annotations

import javax.imageio.ImageIO

import org.junit.Test

import com.manning.spock.chapter8.nuker.CameraFeed
import com.manning.spock.chapter8.nuker.SmartHardDriveNuker

import spock.lang.Specification

class SpySpec extends Specification {
    
    // Listing 8.24 Creating a spy with Spock

    def 'automatic deletion of hard drive when agents are here'() {
        given:'a camera feed'
        CameraFeed feed = new CameraFeed()
        
        and:'the auto-nuker program'
        SmartHardDriveNuker nuker = Spy(SmartHardDriveNuker)
        nuker.deleteHardDriveNow() >> { println  'hard disk cleared'}
        
        when: 'agents are knoking the door'
        feed.currentFrame = ImageIO.read(ClassLoader.getSystemResourceAsStream('agents.jpg'))
        nuker.activate(feed)
        
        then:'all files of hard drive should be deleted'
        1 * nuker.deleteHardDriveNow()

        /**
         * Here you create a spy of your class under test. By default, after creation, all methods
         * are real and pass through to the real object.10 Then you specifically mock the method
         * that deletes the hard drive. But the method that employs the face-recognition logic is
         * still the real one.
         */
                
    }
    
}
