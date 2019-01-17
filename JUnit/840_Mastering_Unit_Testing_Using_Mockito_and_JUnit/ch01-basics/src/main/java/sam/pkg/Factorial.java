package sam.pkg;

public class Factorial {
	public long factorial(long number) {
		if(number < 0)
			throw new IllegalArgumentException("cannot be lessThan 0");
		if(number < 2)
			return 1;
		
		return number * factorial(number - 1);
	}
}
