import static org.junit.Assert.assertEquals;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class DFSTreeTest {
    @Parameters(name="Graph: {0}, Source: {1}, Distances: {2}")
    public static Object[][] data() {
        return new Object[][] { //
            {TestUtilities.createSlidesGraph(new IntGraphList(12)), 2,
                TestUtilities.createMap(true, true, true, true, true, true, true, true, true, true, true, true)}, //
            {TestUtilities.createDisconnectedGraph(new IntGraphList(12)), 2,
                TestUtilities.createMap(true, true, true, true, false, false, false, false, false, false, false, false)}, //
            {TestUtilities.createCompleteGraph(new IntGraphList(4)), 0,
                TestUtilities.createMap(true, true, true, true)}, //
            {new IntGraphList(4), 0,
                TestUtilities.createMap(true, false, false, false)}, //
            {TestUtilities.createGraph(new IntGraphList(4), 0,1, 1,2, 2,3), 0,
                    TestUtilities.createMap(true, true, true, true)}, //
        };
    }
    
    private IntGraph graph;
    
    private int source;
    
    private Map<Integer, Boolean> connected;
    
    public DFSTreeTest(IntGraph graph, int source,
            Map<Integer, Boolean> connected) {
        this.graph = graph;
        this.source = source;
        this.connected = connected;
    }

    @Test
    public void test() {
        Constructor<DFSTree> constr =  TestUtilities.getConstructor(DFSTree.class, IntGraph.class, Integer.TYPE);
        DFSTree tree = TestUtilities.newInstance(constr, graph, source);
        
        for (int v : connected.keySet()) {
            Method isConnectedMethod = TestUtilities.getPublicInstanceMethod("isConnected", DFSTree.class, Boolean.TYPE, Integer.TYPE);
            boolean actual = (boolean) TestUtilities.invoke(isConnectedMethod, tree, v);
            assertEquals(connected.get(v), actual);
        }
    }
}
