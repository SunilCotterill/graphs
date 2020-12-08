import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class BFSTree {
	private IntGraph graph;
	private int source;
	private Map<Integer, Integer> distances;
	private Map<Integer, Integer> parents;
	
	public BFSTree(IntGraph graph, int source) {
		this.graph = graph;
		this.source = source;
        distances = new HashMap<>();
        parents = new HashMap<>(); 
        for (int node : graph.getVertices()) {
			distances.put(node, -1);
			parents.put(node, null);
		}
		
		Queue<Integer> q = new LinkedList<>();
		distances.put(source, 0);
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
		if (distances.containsKey(v)) {
			return distances.get(v);
		} else {
			return -1;
		}
	}
}
