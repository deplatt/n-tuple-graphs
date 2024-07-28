import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

/*
 * Class containing static methods to create standard graphs
 */

public class Generators {
	
	// Returns a standard k-Cycle
	public static Graph kCycle(int k) {
		Graph graph = new SingleGraph(k + "-Cycle");
		if (k == 1 || k == 2) {
			return graph;
		}
		
		for (int i = 1; i <= k; i++) {
			graph.addNode("" + i);
		}
		
		for (int i = 1; i <= k; i++) {
			String other;
			
			if (i == k) {
				other = "1";
			}
			else {
				other = "" + (i + 1);
			}
			
			graph.addEdge(i + " " + other, "" + i, other);
		}
				
		return graph;
	}
	
	// Returns a k-complete graph
	public static Graph kComplete(int k) {
		Graph graph = new SingleGraph(k + "-Complete");

		for (int i = 1; i <= k; i++) {
			graph.addNode("" + i);
		}
		
		for (int i = 1; i <= k; i++) {
			for (int j = i + 1; j <= k; j++) {
				graph.addEdge(i + " " + j, "" + i, "" + j);
			}
		}
		
		return graph;
	}
	
	// Returns a star graph of order k
	public static Graph kStar(int k) {
		Graph g = new SingleGraph(k + "-Star");
		
		g.addNode("1");
		for (int i = 2; i <= k; i++) {
			g.addNode("" + i);
			g.addEdge("1" + i, "1", "" + i);
		}
		
		return g;
	}
	
	// Returns a path of order k
	public static Graph kPath(int k) {
		Graph g = new SingleGraph(k + "-Path");
		
		for (int i = 1; i <= k; i++) {
			g.addNode("" + i);
		}
		
		for (int i = 1; i <= k - 1; i++) {
			g.addEdge("" + i + (i + 1), "" + i, "" + (i + 1));
		}
		
		return g;
	}
	
	// Returns a complete bipartite graph
	public static Graph kBipartite(int p, int q) {
		Graph g = new SingleGraph(p + ", " + q + "-Bipartite");
		for (int i = 1; i <= p + q; i++) {
			g.addNode("" + i);
		}
		
		for (int i = 1; i <= p; i++) {
			for (int j = p + 1; j <= p + q; j++) {
				g.addEdge("" + i + j, "" + i, "" + j);
			}
		}
		
		return g;
	}
}