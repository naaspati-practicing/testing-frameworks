import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AssertionsDemo {

	class Person {
		final String firstName, lastName;

		public Person(String firstName, String lastName) {
			this.firstName = firstName;
			this.lastName = lastName;
		}
		public String getFirstName() {
			return firstName;
		}
		public String getLastName() {
			return lastName;
		}
	}

	private Person person;

	@BeforeEach
	void init() {
		person = new Person("John", "Doe");
	}

	@Test
	void standardAssertions() {
		assertEquals(2, 2);
		assertEquals(4, 4, "The optional assertion message is now the last parameter.");
		assertTrue('a' < 'b', () -> String.join(" -- ", "Assertion messages can be lazily evaluated "," to avoid constructing complex messages unnecessarily."));
	}

	@Test
	void groupedAssertions() {
		assertAll("person",
				() -> assertEquals("John", person.getFirstName()),
				() -> assertEquals("Doe", person.getLastName())
				);
	}

	@Test
	void dependentAssertions() {
		// Within a code block, if an assertion fails the
		// subsequent code in the same block will be skipped.
		assertAll("properties",
				() -> {
					String firstName = person.getFirstName();
					assertNotNull(firstName);

					// Executed only if the previous assertion is valid.
					assertAll("first name",
							() -> assertTrue(firstName.startsWith("J")),
							() -> assertTrue(firstName.endsWith("n"))
							);
				},
				() -> {
					// Grouped assertion, so processed independently
					// of results of first name assertions.
					String lastName = person.getLastName();
					assertNotNull(lastName);

					// Executed only if the previous assertion is valid.
					assertAll("last name",
							() -> assertTrue(lastName.startsWith("D")),
							() -> assertTrue(lastName.endsWith("e"))
							);
				}
				);
	}

	@Test
	void exceptionTesting() {
		Throwable exception = assertThrows(IllegalArgumentException.class, () -> { throw new IllegalArgumentException("a message"); });
		assertEquals("a message", exception.getMessage());
	}
	@Test
	void timeoutNotExceeded() {
		// The following assertion succeeds.
		assertTimeout(Duration.ofMinutes(2), () -> { });
	}
	@Test
	void timeoutNotExceededWithResult() {
		// The following assertion succeeds, and returns the supplied object.
		String actualResult = assertTimeout(Duration.ofMinutes(2), () -> { return "a result"; });
		assertEquals("a result", actualResult);
	}
	@Test
	void timeoutNotExceededWithMethod() {
		// The following assertion invokes a method reference and returns an object.
		String actualGreeting = assertTimeout(Duration.ofMinutes(2), this::greeting);
		assertEquals("Hello, World!", actualGreeting);
	}

	public String greeting() {
		return "Hello, World!";
	}
	@Test
	void timeoutExceeded() {
		// The following assertion fails with an error message similar to:
		// execution exceeded timeout of 10 ms by 91 ms
		assertTimeout(Duration.ofMillis(10), () -> {
			// Simulate task that takes more than 10 ms.
			Thread.sleep(100);
		});
	}
	@Test
	void timeoutExceededWithPreemptiveTermination() {
		// The following assertion fails with an error message similar to:
		// execution timed out after 10 ms
		assertTimeoutPreemptively(Duration.ofMillis(10), () -> {
			// Simulate task that takes more than 10 ms.
			Thread.sleep(100);
		});
	}
}
