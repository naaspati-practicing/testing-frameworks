import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Random;
import java.util.logging.Logger;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import sam.pkg.Adder;

public class ParameterizedTestDemo {
	
	@ParameterizedTest
	@MethodSource("intStream")
	public void adder_test(int a, int b) {
		Number actual = new Adder().add(a, b);
		Double expected = Double.sum(a, b);
		
		System.err.printf("%12s + %12s = %s\n", a,b, expected);
		
		assertEquals(expected, actual);
	}
	
	static Stream<Arguments> intStream() {
		Random r = new Random();
		return Stream.generate(() -> Arguments.arguments(r.nextInt(), r.nextInt()))
		.limit(10 + r.nextInt(30));
		
	}
}
