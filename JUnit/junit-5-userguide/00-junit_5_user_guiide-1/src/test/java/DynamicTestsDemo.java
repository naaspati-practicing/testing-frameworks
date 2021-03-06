import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.DynamicContainer.dynamicContainer;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.ThrowingConsumer;

public class DynamicTestsDemo {

	@SafeVarargs
	private static <E> List<E> list(E...values) {
		return Arrays.<E>asList(values);
	}
	
	private static List<DynamicTest> defaultTests() {
		return list(
				dynamicTest("1st dynamic test", () -> assertTrue(true)),
				dynamicTest("2nd dynamic test", () -> assertEquals(5, 3+2))
				);
	}

	@TestFactory
	List<String> dynamicTestsWithInvalidReturnType() {
		return Collections.singletonList("hello");
	}
	Collection<DynamicTest> dynamicTestsFromCollection( ) {
		return defaultTests();
	}
	@TestFactory
	Iterable<DynamicTest> dynamicTestsFromIterable() {
		return defaultTests();
	}

	@TestFactory
	Iterator<DynamicTest> dynamicTestsFromIterator() {
		return defaultTests().iterator();
	}
	@TestFactory
	DynamicTest[] dynamicTestsFromArray() {
		return defaultTests().toArray(new DynamicTest[2]);
	}

	@TestFactory
	Stream<DynamicTest> dynamicTestsFromStream() {
		return Stream.of("A", "B", "C")
				.map(str -> dynamicTest("test" + str, () -> { /* ... */ }));
	}

	@TestFactory
	Stream<DynamicTest> dynamicTestsFromIntStream() {
		// Generates tests for the first 10 even integers.
		return IntStream.iterate(0, n -> n + 2).limit(10)
				.mapToObj(n -> dynamicTest("test" + n, () -> assertTrue(n % 2 == 0)));
	}

	@TestFactory
	Stream<DynamicTest> generateRandomNumberOfTests() {

		// Generates random positive integers between 0 and 100 until
		// a number evenly divisible by 7 is encountered.
		Iterator<Integer> inputGenerator = new Iterator<Integer>() {

			Random random = new Random();
			int current;

			@Override
			public boolean hasNext() {
				current = random.nextInt(100);
				return current % 7 != 0;
			}

			@Override
			public Integer next() {
				return current;
			}
		};

		// Generates display names like: input:5, input:37, input:85, etc.
		Function<Integer, String> displayNameGenerator = (input) -> "input:" + input;

		// Executes tests based on the current input value.
		ThrowingConsumer<Integer> testExecutor = (input) -> assertTrue(input % 7 != 0);

		// Returns a stream of dynamic tests.
		return DynamicTest.stream(inputGenerator, displayNameGenerator, testExecutor);
	}

	@TestFactory
	Stream<DynamicNode> dynamicTestsWithContainers() {
		return Stream.of("A", "B", "C")
				.map(input -> dynamicContainer("Container " + input, Stream.of(
						dynamicTest("not null", () -> assertNotNull(input)),
						dynamicContainer("properties", Stream.of(
								dynamicTest("length > 0", () -> assertTrue(input.length() > 0)),
								dynamicTest("not empty", () -> assertFalse(input.isEmpty()))
								))
						)));
	}
}
