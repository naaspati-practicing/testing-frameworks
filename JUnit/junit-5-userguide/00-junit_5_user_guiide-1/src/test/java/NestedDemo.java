import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.EmptyStackException;
import java.util.Stack;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("a stack")
public class NestedDemo {
	private static final Logger LOGGER = Logger.getLogger(NestedDemo.class.getName());

	Stack<Object> stack;

    @Test
    @DisplayName("is instantiated with new Stack()")
    void isInstantiatedWithNew() {
        new Stack<>();
        LOGGER.info("void isInstantiatedWithNew() {}");
    }
    
    @Nested
    @DisplayName("when new")
    class WhenNew {
    	
    	@BeforeEach
    	void createNewStack() {
    		LOGGER.info("@BeforeEach void WhenNew.createNewStack() {}"); 
    		stack = new Stack<>();
    	}
    	@Test
    	@DisplayName("is empty")
    	void isEmpty() {
    		LOGGER.info("@Test void WhenNew.isEmpty() {}");
    		assertThat(stack).isEmpty();
    	}
    	@Test
    	@DisplayName("throws EmptyStackException when popped")
    	void throwsExceptionWhenPopped() {
    		LOGGER.info("@Test void WhenNew.throwsExceptionWhenPopped() {}");
    		assertThrows(EmptyStackException.class, stack::pop);
    	}
    	@Test
    	@DisplayName("throws EmptyStackException when Peeked")
    	void throwsExceptionWhenPeeked() {
    		LOGGER.info("@Test void WhenNew.throwsExceptionWhenPeeked() {}");
    		assertThrows(EmptyStackException.class, () -> stack.peek());
    	}
    	
    	@Nested
    	@DisplayName("after pushing an element")
    	class AfterPushing {
    		private final String new_element = "new element";
    		
    		@BeforeEach
        	void pushNewElment() {
        		LOGGER.info("@BeforeEach void WhenNew.AfterPushing.pushNewElment() {}"); 
        		stack.push(new_element);
        	}
        	@Test
        	@DisplayName("is not empty")
        	void isNotEmpty() {
        		LOGGER.info("@Test void WhenNew.AfterPushing.isNotEmpty() {}");
        		assertThat(stack).isNotEmpty();
        	}
        	@Test
        	@DisplayName("return element when popped")
        	void returnElementPopped() {
        		LOGGER.info("@Test void WhenNew.AfterPushing.returnElementPopped() {}");
        		Object obj = stack.pop();
        		assertSame(new_element, obj);
        		assertThat(stack).isEmpty();
        	}
        	@Test
        	@DisplayName("return element when peeked")
        	void returnElementWhenPeeked() {
        		LOGGER.info("@Test void WhenNew.AfterPushing.returnElementWhenPeeked() {}");
        		Object obj = stack.peek();
        		
        		assertSame(new_element, obj);
        		assertThat(stack).isNotEmpty();
        	}	
    	}
    }
}
