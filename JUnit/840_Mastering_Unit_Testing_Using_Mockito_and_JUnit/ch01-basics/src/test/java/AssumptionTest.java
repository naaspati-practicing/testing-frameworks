import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

public class AssumptionTest {
	
	@Test
	void very_critical_test() {
		assumeFalse(() -> 10 > System.currentTimeMillis());
		assertTrue(true);
	}
	

}
