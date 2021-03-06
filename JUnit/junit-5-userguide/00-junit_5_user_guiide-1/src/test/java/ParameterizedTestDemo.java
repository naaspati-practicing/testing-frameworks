import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Formatter;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.JavaTimeConversionPattern;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.EnumSource.Mode;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

public class ParameterizedTestDemo {
	private static final Logger LOGGER = Logger.getLogger(ParameterizedTestDemo.class.getName());

	@ParameterizedTest
	@ValueSource(strings={ "racecar", "radar", "able was I ere I saw elba"})
	void palindromes(String s) {
		StringBuilder sb = new StringBuilder();
		sb.append("\ns = \"").append(s).append("\"\n");
		Formatter fm = new Formatter(sb);

		for (int i = 0; i < s.length()/2; i++) {
			int index = i;
			int last = s.length() - 1 - i;
			assertEquals(s.charAt(i), s.charAt(last), () -> "index: "+index);
			fm.format("  s[%d] = %c, s[%d] = %c\n", i, s.charAt(i), last, s.charAt(last));
		}

		fm.close();
		LOGGER.info(sb.toString());
	}

	@ParameterizedTest
	@ValueSource(ints = { 1, 2, 3 })
	void testWithValueSource(int argument) {
		assertTrue(argument > 0 && argument < 4);
	}
	@ParameterizedTest
	@EnumSource(TimeUnit.class)
	void testWithEnumSource(TimeUnit timeUnit) {
		assertNotNull(timeUnit);
	}

	@ParameterizedTest
	@EnumSource(value = TimeUnit.class, names = { "DAYS", "HOURS" })
	void testWithEnumSourceInclude(TimeUnit timeUnit) {
		assertTrue(EnumSet.of(TimeUnit.DAYS, TimeUnit.HOURS).contains(timeUnit));
	}
	@ParameterizedTest
	@EnumSource(value = TimeUnit.class, mode = Mode.EXCLUDE, names = { "DAYS", "HOURS" })
	void testWithEnumSourceExclude(TimeUnit timeUnit) {
		assertFalse(EnumSet.of(TimeUnit.DAYS, TimeUnit.HOURS).contains(timeUnit));
		assertTrue(timeUnit.name().length() > 5);
	}
	@ParameterizedTest
	@EnumSource(value = TimeUnit.class, mode = Mode.MATCH_ALL, names = "^(M|N).+SECONDS$")
	void testWithEnumSourceRegex(TimeUnit timeUnit) {
		String name = timeUnit.name();
		assertTrue(name.startsWith("M") || name.startsWith("N"));
		assertTrue(name.endsWith("SECONDS"));
	}

	@ParameterizedTest
	@MethodSource("stringProvider")
	void testWithSimpleMethodSource(String argument) {
		assertNotNull(argument);
	}

	static Stream<String> stringProvider() {
		return Stream.of("foo", "bar");
	}

	@ParameterizedTest
	//  JUnit will search for a factory method that has the same name as the current @ParameterizedTest method by convention. 
	@MethodSource
	void testWithSimpleMethodSourceHavingNoValue(String argument) {
		assertNotNull(argument);
	}

	static Stream<String> testWithSimpleMethodSourceHavingNoValue() {
		return Stream.of("foo", "bar");
	}

	/* TODO premititive Stream example */
	@ParameterizedTest
	@MethodSource("range")
	void testWithRangeMethodSource(int argument) {
		assertTrue(argument > 9);
	}

	static IntStream range() {
		return IntStream.range(0, 20).skip(10);
	}

	/* TODO Arguments.arguments example */

	@ParameterizedTest
	@MethodSource("stringIntAndListProvider")
	void testWithMultiArgMethodSource(String str, int num, List<String> list) {
		assertEquals(3, str.length());
		assertTrue(num >=1 && num <=2);
		assertEquals(2, list.size());
	}

	static Stream<Arguments> stringIntAndListProvider() {
		return Stream.of(
				Arguments.arguments("foo", 1, Arrays.asList("a", "b")),
				Arguments.arguments("bar", 2, Arrays.asList("x", "y"))
				);
	}

	/* TODO external method source example */
	
	@ParameterizedTest
	@MethodSource("StringsProviders#tinyStrings")
	void testWithExternalMethodSource(String tinyString) {
		assertNotNull(tinyString);
		assertTrue(tinyString.length() < 5);
	}

	@ParameterizedTest
	@CsvSource({ "foo, 1", "bar, 2", "'baz, qux', 3", "'', 15" })
	void testWithCsvSource(String first, int second) {
		assertNotNull(first);
		assertNotEquals(0, second);
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/two-column.csv", numLinesToSkip = 1)
	void testWithCsvFileSource(String first, int second) {
		assertNotNull(first);
		assertNotEquals(0, second);
	}

	@ParameterizedTest
	@ArgumentsSource(MyArgumentsProvider.class)
	void testWithArgumentsSource(String argument) {
		assertNotNull(argument);
	}


	/* TODO ARGUMENT CONVERSION */

	/* TODO Implicit Conversion */

	@ParameterizedTest
	@ValueSource(strings = "SECONDS")
	void testWithImplicitArgumentConversion(TimeUnit argument) {
		assertNotNull(argument.name());
	}

	/* TODO String-to-Object Conversion */

	@ParameterizedTest
	@ValueSource(strings = "1 + 2 + 4 + 5")
	void testWithExplictArgumentConversion(Sum argument) {
		assertEquals(1 + 2 + 4 + 5, argument.sum);
	}

	@ParameterizedTest
	@ValueSource(strings = "1 - 2 - 4 - 5")
	void testWithExplictArgumentConversion_2(Substract argument) {
		assertEquals(1 - 2 - 4 - 5, argument.result);
	}

	/* TODO explict conversion */
	
	@ParameterizedTest
	@EnumSource(TimeUnit.class)
	void testWithExplicitArgumentConversion( @ConvertWith(ToStringArgumentConverter.class) String argument) {
		assertNotNull(TimeUnit.valueOf(argument));
	}

	@ParameterizedTest
	@ValueSource(strings = { "01.01.2017", "31.12.2017" })
	void testWithExplicitJavaTimeConverter(@JavaTimeConversionPattern("dd.MM.yyyy") LocalDate argument) {
		assertEquals(2017, argument.getYear());
	}
	
	/* TODO Argument Aggregation */
	
	@ParameterizedTest
	@PersonCsv
	void testWithArgumentsAccessor(ArgumentsAccessor accessor) {
		Person person = PersonArgumentsAggregator.toPerson(accessor);
		assertThat(person).isAnyOf(Person.parse("Jane, Doe, F, 1990-05-20"), Person.parse("John, Doe, M, 1990-10-22"));
	}
	
	@ParameterizedTest
	@PersonCsv
	void testWithArgumentsAccessor_2(@AggregateWith(PersonArgumentsAggregator.class) Person person) {
		assertThat(person).isAnyOf(Person.parse("Jane, Doe, F, 1990-05-20"), Person.parse("John, Doe, M, 1990-10-22"));
	}
	
	@ParameterizedTest
	@PersonCsv
	void testWithArgumentsAccessor_3(@CsvToPerson Person person) {
		assertThat(person).isAnyOf(Person.parse("Jane, Doe, F, 1990-05-20"), Person.parse("John, Doe, M, 1990-10-22"));
	}
	
	
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@CsvSource({
    "Jane, Doe, F, 1990-05-20",
    "John, Doe, M, 1990-10-22"
})
@interface PersonCsv { }

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@AggregateWith(PersonArgumentsAggregator.class)
@interface CsvToPerson { }

class PersonArgumentsAggregator implements ArgumentsAggregator {

	@Override
	public Object aggregateArguments(ArgumentsAccessor accessor, ParameterContext context)
			throws ArgumentsAggregationException {
		return toPerson(accessor);
	}
	
	public static Person toPerson(ArgumentsAccessor arg) {
		return new Person(arg.getString(0), 
				arg.getString(1), 
				arg.get(2, Gender.class),
				arg.get(3, LocalDate.class));
	}
}

enum Gender {
	F, M
}

class Person {
	private final String firstName, lastName;
	private final Gender gender;
	private final LocalDate dateOfBirth;
	
	public Person(String firstName, String lastName, Gender gender, LocalDate dateOfBirth) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.dateOfBirth = dateOfBirth;
	}
	public static Person parse(String string) {
		String[] str = string.split(",");
		
		return new Person(
				str[0].trim(), 
				str[1].trim(), 
				Gender.valueOf(str[2].trim()), 
				LocalDate.parse(str[3].trim()));
	}
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public Gender getGender() {
		return gender;
	}
	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}
	@Override
	public int hashCode() {
		return Objects.hash(dateOfBirth, firstName, gender, lastName);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		Person other = (Person) obj;
		return Objects.equals(dateOfBirth, other.dateOfBirth) && 
				Objects.equals(firstName, other.firstName) && 
				gender == other.gender && 
				Objects.equals(lastName, other.lastName);
	}
	
}

class ToStringArgumentConverter extends SimpleArgumentConverter {

	@Override
	protected Object convert(Object source, Class<?> targetType) throws ArgumentConversionException {
		assertEquals(String.class, targetType, "Can only convert to String");
		return source.toString();
	}
}

class StringsProviders {
	static Stream<String> tinyStrings() {
		return Stream.of(".", "oo", "OOO");
	}
}

class MyArgumentsProvider implements ArgumentsProvider {

	@Override
	public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
		return Stream.of("foo", "bar").map(Arguments::of);
	}

}

class Sum {
	final int sum ;
	public Sum(int sum) {
		this.sum = sum;
	}

	public static Sum newInstance(String expression) {
		return new Sum(Pattern.compile("+", Pattern.LITERAL)
				.splitAsStream(expression)
				.map(String::trim)
				.mapToInt(Integer::parseInt)
				.sum());
	}
}
class Substract {
	final int result;
	public Substract(String expression) {
		this.result = Pattern.compile("-", Pattern.LITERAL)
				.splitAsStream(expression)
				.map(String::trim)
				.mapToInt(Integer::parseInt)
				.reduce((i, j) -> i - j)
				.orElse(0);
	}
}