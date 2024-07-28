import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

/*
 * Class for building nTuple Graphs
 */

public class NTuple {
	// Returns an n-Tuple graph of g
	public static Graph create(Graph g, int n) {

		Graph nTuple = new SingleGraph(n + "-Tuple");
		if (n == 0) {
			return nTuple;
		}
		nTuple.setAttribute("baseGraph", g);
		nTuple.setAttribute("n", n);

		// --------------- VERTICES ---------------

		// Create a binary string of length d(g)
		for (int i = 0; i < Math.pow(2, g.getNodeCount()); i++) {
			String binStr = Integer.toBinaryString(i);

			// If the string isn't long enough, add leading 0s
			while (binStr.length() < g.getNodeCount()) {
				binStr = "0" + binStr;
			}

			// Check if the binary string has n 1s
			if (binStrValid(binStr, n)) {
				// Translate the binary string into a subset
				Set<Node> vertexTuple = new HashSet<>();

				for (int j = 0; j < binStr.length(); j++) {
					if (binStr.charAt(j) == '1') {
						vertexTuple.add(g.getNode(j));
					}
				}

				String nodeId = vertexTuple.toString();
				nodeId = nodeId.substring(1, nodeId.length() - 1);
				nodeId = "{" + nodeId + "}";

				Node node = nTuple.addNode(nodeId);
				node.setAttribute("tuple", vertexTuple);
			}
		}

		// --------------- EDGES ---------------

		for (int i = 0; i < nTuple.getNodeCount(); i++) {
			for (int j = i + 1; j < nTuple.getNodeCount(); j++) {
				@SuppressWarnings("unchecked")
				Set<Node> n1 = new HashSet<Node>((Set<Node>) nTuple.getNode(i).getAttribute("tuple"));
				@SuppressWarnings("unchecked")
				Set<Node> n2 = new HashSet<Node>((Set<Node>) nTuple.getNode(j).getAttribute("tuple"));

				Set<Node> intersection = new HashSet<>(n1);
				intersection.retainAll(n2);

				if (intersection.size() == n - 1) {
					n1.removeAll(intersection);
					n2.removeAll(intersection);

					Node alpha = null;
					Node beta = null;

					for (Node node : n1) {
						alpha = node;
					}
					for (Node node : n2) {
						beta = node;
					}

					if (alpha.hasEdgeBetween(beta)) {
						nTuple.addEdge(nTuple.getNode(i).toString() + " " + nTuple.getNode(j).toString(),
								nTuple.getNode(i).toString(), nTuple.getNode(j).toString());
					}
				}
			}
		}

		return nTuple;
	}

	private static boolean binStrValid(String binStr, int n) {
		int total = 0;
		for (char c : binStr.toCharArray()) {
			total += Integer.parseInt("" + c);
		}
		return total == n;
	}
	
	
	
	// This is an alternate n-Tuple graph generator that
	// iterates through g's edges instead of nodes.
	public static Graph create2(Graph g, int n) {
		Graph nTuple = new SingleGraph(n + "-Tuple");
		if (n == 0) {
			return nTuple;
		}
		nTuple.setAttribute("baseGraph", g);
		nTuple.setAttribute("n", n);

		// --------------- VERTICES ---------------

		
		HashMap<String, Node> map = new HashMap<>();
		
		for (int i = 0; i < Math.pow(2, g.getNodeCount()); i++) {
			String binStr = Integer.toBinaryString(i);

			// If the string isn't long enough, add leading 0s
			while (binStr.length() < g.getNodeCount()) {
				binStr = "0" + binStr;
			}

			// Check if the binary string has n 1s
			if (binStrValid(binStr, n)) {
				// Translate the binary string into a subset
				Set<Node> vertexTuple = new HashSet<>();

				for (int j = 0; j < binStr.length(); j++) {
					if (binStr.charAt(j) == '1') {
						vertexTuple.add(g.getNode(j));
					}
				}

				String nodeId = vertexTuple.toString();
				nodeId = nodeId.substring(1, nodeId.length() - 1);
				nodeId = "{" + nodeId + "}";

				Node node = nTuple.addNode(nodeId);
				node.setAttribute("tuple", vertexTuple);
				map.put(binStr, node);
			}
		}
		
		// --------------- EDGES ---------------
		for (Edge e: g.getEdgeSet()) {
			Node n1 = e.getNode0();
			Node n2 = e.getNode1();
			
			int ind1 = n1.getIndex();
			int ind2 = n2.getIndex();
			
			for (int i = 0; i < Math.pow(2, g.getNodeCount() - 2); i++) {
				String binStr = Integer.toBinaryString(i);
				
				while (binStr.length() < g.getNodeCount() - 2) {
					binStr = "0" + binStr;
				}
				
				if (binStrValid(binStr, n - 1)) {
					String[] strs = inserts(binStr, ind1, ind2);
					nTuple.addEdge(strs[0] + strs[1], map.get(strs[0]), map.get(strs[1]));
				}
			}
		}
		return nTuple;
	}
	
	private static String[] inserts(String binStr, int ind1, int ind2) {		
		StringBuffer s1 = new StringBuffer(binStr);
		StringBuffer s2 = new StringBuffer(binStr);
		
		// Make ind1 the lower indexs
		if (ind1 > ind2) {
			int temp = ind1;
			ind1 = ind2;
			ind2 = temp;
		}
		
		if (ind1 > binStr.length()) {
			s1.append("01");
			s2.append("10");
		}
		else if (ind2 > binStr.length()) {
			s1.insert(ind1, '0');
			s2.insert(ind1, '1');
			
			s1.append('1');
			s2.append('0');
		}
		else {
			s1.insert(ind1, '0');
			s2.insert(ind1, '1');
			
			s1.insert(ind2, '1');
			s2.insert(ind2, '0');
		} 

		String[] strs = {s1.toString(), s2.toString()};
		System.out.println(ind1 + " " + ind2 + ": " + binStr + " -> " + s1.toString() + " " + s2.toString());
		return strs;
		
	}
}
