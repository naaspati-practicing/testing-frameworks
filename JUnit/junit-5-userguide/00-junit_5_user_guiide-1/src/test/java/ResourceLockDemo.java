import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.api.parallel.ResourceAccessMode;
import org.junit.jupiter.api.parallel.ResourceLock;

import static org.junit.jupiter.api.Assertions.*;
import static com.google.common.truth.Truth8.*;
import static com.google.common.truth.Truth.*;
import static org.junit.jupiter.api.parallel.ResourceAccessMode.*;

@Execution(ExecutionMode.CONCURRENT)
public class ResourceLockDemo {

	private Properties backup;
	private static final String SYSTEM_PROPERTIES = "SYSTEM_PROPERTIES";  

	@BeforeEach
	void backup() {
		backup = new Properties();
		backup.putAll(System.getProperties());
	}
	@AfterEach
	void restore() {
		System.setProperties(backup);
	}

	@Test
	@ResourceLock(mode=READ, value=SYSTEM_PROPERTIES)
	void customPropertyIsNotSetByDefault() {
		assertNull(System.getProperty("my.prop"));
	}
	@Test
	@ResourceLock(value = SYSTEM_PROPERTIES, mode = READ_WRITE)
	void canSetCustomPropertyToFoo() {
		System.setProperty("my.prop", "foo");
		assertEquals("foo", System.getProperty("my.prop"));
	}
	@Test
    @ResourceLock(value = SYSTEM_PROPERTIES, mode = READ_WRITE)
    void canSetCustomPropertyToBar() {
        System.setProperty("my.prop", "bar");
        assertEquals("bar", System.getProperty("my.prop"));
    }
	
}

