import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class IntGraphMatrixGetNumEdgesTest {
    @Parameters(name="Graph: {0}, Number of edges = {1}")
    public static Object[][] data() {
        return new Object[][] { //
            { TestUtilities.createCompleteGraph(new IntGraphMatrix(4)), 6 }, //
            { TestUtilities.createCompleteGraph(new IntGraphMatrix(10)), 45 }, //
            { TestUtilities.createSlidesGraph(new IntGraphMatrix(12)), 19 }, // 
//            { new IntGraphList(), 0 }, // 
        };
    }
    
    private IntGraph graph;
    
    private int numEdges;

    public IntGraphMatrixGetNumEdgesTest(IntGraph graph, int numEdges) {
        this.graph = graph;
        this.numEdges = numEdges;
    }
    
    @Test
    public void test() {
        Method getNumEdgesMethod = TestUtilities.getPublicInstanceMethod("getNumEdges", IntGraphMatrix.class, Integer.TYPE);
        int actual = (int) TestUtilities.invoke(getNumEdgesMethod, graph);
        assertEquals(numEdges, actual);
    }
}
