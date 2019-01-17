
import static com.google.common.truth.Truth.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;


public class GoogleTruthTest {
	
	@Test
	void verify_multiple_values() {
		double marks = 100;
		
		assertThat(marks).isAnyOf(100D, 90.9);
		assertThat(marks).isNoneOf(99.99, 60);
	}
	@Test
	void verify_collection_values() {
		List<Double> salary = Arrays.asList(50d, 200d, 500d);
		
		assertThat(salary).contains(50.0);
		assertThat(salary).containsAllOf(50.0, 200.0);
		assertThat(salary).doesNotContain(1.0);
	}
	
	@Test
	void verify_Strings() {
		String name = "John Jr Dale";
		
		assertThat(name).startsWith("John");
		assertThat(name).contains("Jr");
		assertThat(name).endsWith("Dale");
	}
	
	@Test
	public void lessthanOrEquals_custom_matcher() throws Exception {
		int actualGoalScored = 2;
		assertThat(actualGoalScored).isAtLeast(1);
		assertThat(actualGoalScored).isAtMost(4);
		
		double originalPI = 3.14;
		assertThat(originalPI).isAtMost(Double.valueOf(9));
		String authorName = "Sujoy";
		assertThat(authorName).isAtMost("Sujoy1");
		
		int maxInt = Integer.MAX_VALUE;
		assertThat(maxInt).isAtLeast(Integer.valueOf(Integer.MIN_VALUE));
	}


}
