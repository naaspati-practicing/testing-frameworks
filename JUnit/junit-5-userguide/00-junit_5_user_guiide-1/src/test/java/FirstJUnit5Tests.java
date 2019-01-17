

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


public class FirstJUnit5Tests {
    @Test 
    public void firstTest() {
        assertEquals(2, 1+1);
    }
    @Test 
    public void secondTest() {
        assertEquals(4, 1+1);
    }
}
