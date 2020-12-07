import java.lang.reflect.Method;

import org.junit.Test;

public class IntGraphListDegreeExceptionTest {
    @Test(expected = IllegalArgumentException.class)
    public void testException() {
        IntGraph graph = new IntGraphList();
        Method degreeMethod = TestUtilities.getPublicInstanceMethod("getDegree", IntGraphList.class, Integer.TYPE, Integer.TYPE);
        TestUtilities.invoke(degreeMethod, graph, 4);
    }

}
