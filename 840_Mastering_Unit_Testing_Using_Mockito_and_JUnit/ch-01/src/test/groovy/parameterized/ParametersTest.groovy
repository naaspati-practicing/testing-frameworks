package parameterized
import static org.junit.Assert.*

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameter
import org.junit.runners.Parameterized.Parameters

import groovy.transform.TupleConstructor

@RunWith(Parameterized)
class ParametersTest {
	int number
	int expectedResult 

	@Parameters
	static factorialData() {
		([
			[0,1],
			[1,1],
			[2,2],
			[3,6],
			[4,24],
			[5,120],
			[6,720]
			] as Object[][]).collect()
	}
	
	public ParametersTest(int number, int expectedResult) {
		this.number = number;
		this.expectedResult = expectedResult;
	}
	
	@Test
	void toast() {
		assertEquals expectedResult, Factorial.factorial(number)
	}
}
	