import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class IntGraphListDegreeTest {
    @Parameters(name="Graph: {0}")
    public static Object[][] data() {
        return new Object[][] { //
            { TestUtilities.createCompleteGraph(new IntGraphList(4)),
                TestUtilities.createMapSingleValue(4, 3) }, //
            { TestUtilities.createCompleteGraph(new IntGraphList(10)),
                    TestUtilities.createMapSingleValue(10, 9) }, //
            { TestUtilities.createSlidesGraph(new IntGraphList(12)),
                        TestUtilities.createMap(2, 4, 3, 3, 6, 3, 4, 3, 3, 2, 2, 3) }, // 
        };
    }
    
    private IntGraphList graph;
    
    private Map<Integer, Integer> degreeMap;
    
    public IntGraphListDegreeTest(IntGraphList graph, Map<Integer, Integer> degreeMap) {
        this.graph = graph;
        this.degreeMap = degreeMap;
    }
    
    @Test
    public void testDegree() {
        Method degreeMethod = TestUtilities.getPublicInstanceMethod("getDegree", IntGraphList.class, Integer.TYPE, Integer.TYPE);
        for (int v : graph.getVertices()) {
            int actual = (int) TestUtilities.invoke(degreeMethod, graph, v);
            assertEquals((int)degreeMap.get(v), actual);
        }
    }
}
