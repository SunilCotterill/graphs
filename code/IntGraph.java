import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * This is an abstract class representing a graph with
 * vertices and edges. An abstract class is something we
 * haven't covered in detail in MSCI 121 or MSCI 240, so you
 * don't need to worry about how this works, it is just used
 * for testing.
 * 
 * If curious, Chapter 9 of the Building Java Programs
 * textbook describes it in detail.
 * 
 * @author Mark Hancock
 *
 */
public abstract class IntGraph {
    /**
     * Adds a vertex with a value to this graph.
     * 
     * @param value the integer value of this vertex
     * @throws IllegalArgumentException if this vertex value has
     *                                  been added before
     */
    public abstract void addVertex(int value);

    /**
     * Adds an edge from first to second to this graph.
     * 
     * @param first  the first vertex in the edge
     * @param second the second vertex in the edge
     * 
     * @throws IllegalArgumentException if either first or
     *                                  second are not vertices
     *                                  in the graph.
     */
    public abstract void addEdge(int first, int second);

    /**
     * Returns the number of vertices in this graph.
     * 
     * @return the number of vertices in this graph.
     */
    public abstract int getNumVertices();

    /**
     * Returns an adjacency list for vertex v.
     * 
     * @param v the vertex for which to retrieve the adjacency
     *          list
     * @return an adjacency list for vertex v.
     */
    public abstract List<Integer> getAdjacencyList(int v);

    /**
     * Returns the set of vertices in this graph.
     * 
     * @return the set of vertices in this graph.
     */
    public abstract Set<Integer> getVertices();

    /**
     * Checks if this vertex is in the graph and returns true if
     * it is, false if it is not.
     * 
     * @param v the vertex to check
     * @return true if vertex v is in the graph, false
     *         otherwise.
     */
    public abstract boolean hasVertex(int v);

    /**
     * @return a string representation of this graph of the
     *         form: "Vertices: [...], Edges: [(...),(...),...]"
     */
    @Override
    public String toString() {
        Set<UndirectedEdge> edges = new LinkedHashSet<>();
        for (int u : getVertices()) {
            for (int v : getAdjacencyList(u)) {
                edges.add(new UndirectedEdge(u, v));
            }
        }
        return String.format("Vertices: %s, Edges: %s",
                this.getVertices(), edges);
    }

    /**
     * This is a helper class with the sole purpose of being
     * able to nicely print out the list of edges in an
     * undirected graph.
     * 
     * @author Mark Hancock
     *
     */
    private class UndirectedEdge {
        /**
         * The two vertices in the undirected edge. The smallest one
         * is always a.
         */
        private int a, b;

        /**
         * Creates an undirected edge from a to b. The two
         * parameters can be supplied in any order and will be
         * stored internally in order (smallest first).
         * 
         * @param a one of two vertices in the edge.
         * @param b one of two vertices in the edge.
         */
        public UndirectedEdge(int a, int b) {
            this.a = Math.min(a, b);
            this.b = Math.max(a, b);
        }

        /**
         * @return a string representation of this edge as "(a,b)",
         *         where a is the smaller of the two vertices.
         */
        @Override
        public String toString() {
            return String.format("(%d,%d)", a, b);
        }

        /**
         * Checks for equality of this edge with another.
         */
        @Override
        public boolean equals(Object other) {
            // Source:
            // https://www.sitepoint.com/implement-javas-equals-method-correctly/
            // self check
            if (this == other)
                return true;

            // null check
            if (other == null)
                return false;

            // type check and cast
            if (getClass() != other.getClass())
                return false;

            UndirectedEdge edge = (UndirectedEdge) other;

            // field comparison (only check degree for equality)
            return (a == edge.a && b == edge.b);
        }

        /**
         * Ensures that using a HashMap or HashSet on UndirectedEdge
         * objects matches the equals method.
         */
        @Override
        public int hashCode() {
            return Objects.hash(a, b);
        }
    }
}