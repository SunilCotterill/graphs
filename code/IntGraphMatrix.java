import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This class represents a graph stored using an adjacency
 * matrix.
 * 
 * @author Mark Hancock
 * @author <your name can go here>
 */
public class IntGraphMatrix extends IntGraph {
    /**
     * The adjacency matrix for this graph (represented as a 2D
     * array).
     */
    private boolean[][] adjMatrix;

    /**
     * Creates a new graph with vertices numbered 0 to
     * numVertices - 1.
     * 
     * @param numVertices the number of vertices to add to this
     *                    graph on creation
     */
    public IntGraphMatrix(int numVertices) {
        // the adjacency matrix should have the same number of rows
        // and columns, which represents the number of vertices
        adjMatrix = new boolean[numVertices][numVertices];
    }

    /**
     * Adds a vertex with a value to this graph.
     * 
     * @param value the integer value of this vertex
     * @throws IllegalArgumentException always, since adjacency
     *                                  matrices have nodes
     *                                  created at
     *                                  initialization and more
     *                                  can't be added after the
     *                                  fact.
     */
    @Override
    public void addVertex(int value) {
        throw new IllegalArgumentException(
                "Can't add vertices to an adjacency matrix");
    }

    /**
     * Adds an edge from first to second to this graph.
     * 
     * @param first  the first vertex in the edge
     * @param second the second vertex in the edge
     * 
     * @throws IllegalArgumentException if either first or
     *                                  second are outside the
     *                                  range 0 to size - 1
     */
    @Override
    public void addEdge(int first, int second) {
        checkVertex(first);
        checkVertex(second);
        adjMatrix[first][second] = true;
        adjMatrix[second][first] = true;
    }

    /**
     * Returns the number of vertices in this graph.
     * 
     * @return the number of vertices in this graph.
     */
    @Override
    public int getNumVertices() {
        return adjMatrix.length;
    }

    /**
     * Returns an adjacency list for vertex v.
     * 
     * @param v the vertex for which to retrieve the adjacency
     *          list
     * @return an adjacency list for vertex v.
     */
    @Override
    public List<Integer> getAdjacencyList(int v) {
        // This is using something called "streams", which you can
        // learn about in Chapter 19 of the Building Java Programs
        // textbook. This _could_ be implemented without using them,
        // this is just some nice clean code to do something useful
        // for testing.
        return IntStream.range(0, getNumVertices())
                .filter(i -> adjMatrix[v][i])
                .boxed()
                .collect(Collectors.toList());
    }

    /**
     * Returns the set of vertices in this graph.
     * 
     * @return the set of vertices in this graph.
     */
    @Override
    public Set<Integer> getVertices() {
        // This is using something called "streams", which you can
        // learn about in Chapter 19 of the Building Java Programs
        // textbook. This _could_ be implemented without using them,
        // this is just some nice clean code to do something useful
        // for testing.
        return IntStream.range(0, getNumVertices())
                .boxed()
                .collect(Collectors.toSet());
    }

    /**
     * Checks if this vertex is in the graph and returns true if
     * it is, false if it is not.
     * 
     * @param v the vertex to check
     * @return true if vertex v is in the graph, false
     *         otherwise.
     */
    @Override
    public boolean hasVertex(int v) {
        return v >= 0 && v < adjMatrix.length;
    }

    private void checkVertex(int v) {
        if (!hasVertex(v)) {
            throw new IllegalArgumentException(
                    "vertex " + v + " is not in the graph");
        }
    }

}
