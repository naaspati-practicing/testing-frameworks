import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth8.*;
import static com.google.common.truth.Truth.*;


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


}
