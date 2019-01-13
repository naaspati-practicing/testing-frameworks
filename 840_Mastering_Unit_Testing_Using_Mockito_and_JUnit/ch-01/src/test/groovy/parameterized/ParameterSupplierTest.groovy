package parameterized
import static org.junit.Assert.*

import org.junit.experimental.theories.ParametersSuppliedBy
import org.junit.experimental.theories.Theories
import org.junit.experimental.theories.Theory
import org.junit.runner.RunWith

import main.Adder

@RunWith(Theories)
class ParameterSupplierTest {
	
	@Theory
	void testSomething(
		@ParametersSuppliedBy(NumberSupplier) Number n1,
		@ParametersSuppliedBy(NumberSupplier) Number n2
		) {
		println "$n1  $n2"
	}
	@Theory
	void test2(
		@ParametersSuppliedBy(NumberSupplier) Number n1,
		@ParametersSuppliedBy(NumberSupplier) Number n2
		) {
		double expected = n1.doubleValue() + n2.doubleValue()
		Adder adder = new Adder()
		double actual = adder.add(n1, n2)
		
		assertEquals expected, actual, 0.01
	}
}
	