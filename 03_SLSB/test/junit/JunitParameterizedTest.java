package junit;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
 
/**
 * JUnit Parameterized Test
 * @author mkyong
 */

@RunWith(value = Parameterized.class)
public class JunitParameterizedTest {
 
   private int number;
 
   public JunitParameterizedTest(int number) {
      this.number = number;
   }
 
   @Parameters
   public static Collection<Object[]> data() {
     Object[][] data = new Object[][] { { 1 }, { 2 }, { 3 }, { 4 } };
     return Arrays.asList(data);
   }
 
   @Test
   public void pushTest() {
     System.out.println("Parameterized Number is : " + number);
   }
 
}