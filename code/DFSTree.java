import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DFSTree {
	private IntGraph graph;
	private int source;
	private Set<Integer> visited;
	private Map<Integer, Integer> parents;
	// TODO: consider what fields you might want
	// Hint: probably not (just) a graph and source vertex

	// TODO: create the DFSTree constructor
	public DFSTree(IntGraph graph, int source) {
		this.graph = graph;
		this.source = source;
		visited = new HashSet<>();
		parents = new HashMap<>();

		for (int v : graph.getVertices()) {
			dfsVisit(v, visited, parents);
		}
	}

	private void dfsVisit(int v, Set<Integer> visited,
			Map<Integer, Integer> parents) {
		visited.add(v);
		for (int u : graph.getAdjacencyList(v)) {
			if (!visited.contains(u)) {
				parents.put(u, v);
				dfsVisit(v, visited, parents);
			}
		}
	}

	// TODO: create the isConnected method
	
	public boolean isConnected(int v) {
		return visited.contains(v);
	}
}
