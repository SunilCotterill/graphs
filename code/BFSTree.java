import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class BFSTree {
	private IntGraph graph;
	private int source;
	private Map<Integer, Integer> distances = new HashMap<>(); // vertices to
																// integers
	private Map<Integer, Integer> parents = new HashMap<>();
	// TODO: consider what fields you might want
	// Hint: probably not (just) a graph and source vertex

	// TODO: implement constructor
	BFSTree(IntGraph graph, int source) {
		this.graph = graph;
		this.source = source;
		BFS();
	}

	private void BFS() {
		
		for (int node : graph.getVertices()) {
			// for every vertex
			distances.put(node, -1);
			// initialize distance and parent
			parents.put(node, null);
		}
		
		Queue<Integer> q = new LinkedList<>();
		this.distances.put(source, 0);
		q.add(source);
		while (!q.isEmpty()) {
			int u = q.remove();
			for (int v : graph.getAdjacencyList(u)) {

				if (distances.get(v) == -1) {

					distances.put(v, distances.get(u) + 1);
					parents.put(v, u);
					q.add(v);
				}
			}
		}

	}
	
	public int getDistanceTo(int v) {
		if(graph.getVertices().contains(v)) {
			return distances.get(v);
		}
		else return -1;
	}
// TODO: implement getDistanceTo method
}
