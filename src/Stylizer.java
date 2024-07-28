import java.util.Set;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

/*
 * Class to style the nodes and edges of a graph
 */

public class Stylizer {

	private Graph graph;
	private boolean nTup;
	private boolean labels;
	
	public Stylizer(Graph graph) {
		this.graph = graph;
		this.labels = true;
		
		nTup = graph.hasAttribute("baseGraph");
	}
	
	public void resetNodes() {
		if (!nTup) {
			resetCircleNodes();
		}
		else {
			resetBoxNodes();
		}
	}
	
	private void resetCircleNodes() {
		for (Node n: graph.getNodeSet())  {
			
			String style;
			if (labels) {
				int width = 10 * n.getId().length();

				if (width < 30) {
					width = 30;
				}

				style = "size: " + width + "px, 30px;"
						+ "shape: circle;"
						+ "fill-color: gray;"
						+ "stroke-mode: plain;"
						+ "text-size: 15;"
						+ "stroke-width: 2px;"
						+ "stroke-color: black;";
				
				n.setAttribute("ui.label", n.getId());
			}
			else {
				style = "size: 10px;"
						+ "shape: circle;"
						+ "fill-color:black;"
						+ "stroke-mode: plain;"
						+ "stroke-width: 2px;"
						+ "stroke-color: black;"
						+ "text-size: 0;";
			}
			
			n.setAttribute("ui.style", style);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void resetBoxNodes() {
		for (Node n: graph.getNodeSet())  {
			String style;
			
			if (labels) {
				int width = 10 * n.getId().length();
				
				if (width < 30) {
					width = 30;
				}
				
				if (n.hasAttribute("tuple")) {
					width = 15 * ((Set<Node>) n.getAttribute("tuple")).size() + 30;
				}
				
				style = "size: " + width + "px, 30px; "
						+ "shape: box;"
						+ "fill-color: gray;"
						+ "stroke-mode: plain;"
						+ "stroke-color: black;"
						+ "stroke-width: 2px;"
						+ "text-color: black;"
						+ "text-size: 15;";
			
				n.setAttribute("ui.label", n.getId());
			}
			else {
				style = "size: 10px;"
						+ "shape: box;"
						+ "fill-color:black;"
						+ "stroke-mode: plain;"
						+ "stroke-width: 2px;"
						+ "stroke-color:black;"
						+ "text-size: 0;";
			}
			
			n.setAttribute("ui.style", style);
		}
	}
	
	public void resetEdges() {
		for (Edge e: graph.getEdgeSet()) {
			String style = "fill-color: black; size: 2px; stroke-mode: plain;";
			e.setAttribute("ui.style", style);
		}
	}
	
	public void reset() {
		resetNodes();
		resetEdges();
	}
	
	
	
	public void setLabels(boolean val) {
		labels = val;
		resetNodes();
		
		for (Node n: graph) {
			Object highlighted = n.getAttribute("highlighted");
			if (highlighted != null && (boolean) highlighted) {
				Visualizations.highlight(n);
			}
		}
	}
}
