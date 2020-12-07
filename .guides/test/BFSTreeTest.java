import static org.junit.Assert.assertEquals;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class BFSTreeTest {
    @Parameters(name="Graph: {0}, Source: {1}, Distances: {2}")
    public static Object[][] data() {
        return new Object[][] { //
            {TestUtilities.createSlidesGraph(new IntGraphList(12)), 2,
                TestUtilities.createMap(1, 1, 0, 1, 2, 3, 3, 3, 4, 4, 4, 3)}, //
            {TestUtilities.createDisconnectedGraph(new IntGraphList(12)), 2,
                TestUtilities.createMap(1, 1, 0, 1, -1, -1, -1, -1, -1, -1, -1, -1)}, //
            {TestUtilities.createCompleteGraph(new IntGraphList(4)), 0,
                TestUtilities.createMap(0, 1, 1, 1)}, //
        };
    }
    
    private IntGraph graph;
    
    private int source;
    
    private Map<Integer, Integer> distances;
    
    public BFSTreeTest(IntGraph graph, int source,
            Map<Integer, Integer> distances) {
        this.graph = graph;
        this.source = source;
        this.distances = distances;
    }

    @Test
    public void test() {
        Constructor<BFSTree> constr =  TestUtilities.getConstructor(BFSTree.class, IntGraph.class, Integer.TYPE);
        BFSTree tree = TestUtilities.newInstance(constr, graph, source);
        
        for (int v : distances.keySet()) {
            Method getDistanceMethod = TestUtilities.getPublicInstanceMethod("getDistanceTo", BFSTree.class, Integer.TYPE, Integer.TYPE);
            int actual = (int) TestUtilities.invoke(getDistanceMethod, tree, v);
            assertEquals((int)distances.get(v), actual);
        }
    }
}
