import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;
import java.util.Map;

import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class IntGraphMatrixDegreeTest {
    @Parameters(name="Graph: {0}")
    public static Object[][] data() {
        return new Object[][] { //
            { TestUtilities.createCompleteGraph(new IntGraphMatrix(4)),
                TestUtilities.createMapSingleValue(4, 3) }, //
            { TestUtilities.createCompleteGraph(new IntGraphMatrix(10)),
                    TestUtilities.createMapSingleValue(10, 9) }, //
            { TestUtilities.createSlidesGraph(new IntGraphMatrix(12)),
                        TestUtilities.createMap(2, 4, 3, 3, 6, 3, 4, 3, 3, 2, 2, 3) }, // 
        };
    }
    
    private IntGraph graph;
    
    private Map<Integer, Integer> degreeMap;
    
    private Method degreeMethod;
    
    public IntGraphMatrixDegreeTest(IntGraph graph, Map<Integer, Integer> degreeMap) {
        this.graph = graph;
        this.degreeMap = degreeMap;
        this.degreeMethod = TestUtilities.getPublicInstanceMethod("getDegree", IntGraphMatrix.class, Integer.TYPE, Integer.TYPE);
    }
    
    @Test
    public void test() {
        Assume.assumeFalse(graph.getVertices().isEmpty());
        for (int v : graph.getVertices()) {
            int actual = (int) TestUtilities.invoke(degreeMethod, graph, v);
            assertEquals((int)degreeMap.get(v), actual);
        }
    }
    
}
