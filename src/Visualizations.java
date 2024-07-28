import java.util.List;
import java.util.Set;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;

/*
 * Class containing static methods to style graphs
 */

public class Visualizations {
	
	public static void displayPaths(Set<List<Node>> paths) {
		double increment = 1.0 / paths.size();
		
		int i = 0;
		for (List<Node> path: paths) {
			int[] colors = numberToColor(i * increment);
			
			int pathLen = path.size();

			for (int j = 0; j < pathLen - 1; j++) {
				Node n1 = path.get(j);
				Node n2 = path.get(j + 1);

				Edge e = n1.getEdgeBetween(n2);
				String fillColor = String.format("fill-color: rgb(%d, %d, %d)", colors[0], colors[1], colors[2]);
				e.setAttribute("ui.style", fillColor + "; size: 5px;");
			}
			
			i++;
		}
	}
	
	private static int[] numberToColor(double w) {
		
		int scale = 180;

		double r = 0;
		double g = 0;
		double b = 0;
		
		int cat = (int) (w * 6);
		double dist = w * 6 - cat;
				
		switch (cat) {
		case 0:
			r = scale;
			g = 0;
			b = dist * scale;
			break;
		case 1:
			r = (1 - dist) * scale;
			g = 0;
			b = scale;
			break;
		case 2:
			r = 0;
			g = dist * scale;
			b = scale;
			break;
		case 3:
			r = 0;
			g = scale;
			b = (1 - dist) * scale;
			break;
		case 4:
			r = dist * scale;
			g = scale;
			b = 0;
			break;
		case 5:
			r = scale;
			g = (1 - dist) * scale;
			b = 0;
			break;
 		}
		
		int[] colors = {(int) r, (int) g, (int) b};
		return colors;
	}
	
	public static void highlight(Node n) {
		n.setAttribute("ui.style", "stroke-color: red; stroke-width: 5px;");
		n.setAttribute("highlighted", true);

	}
	
	public static void unHighlight(Node n) {
		n.setAttribute("ui.style", "stroke-color: black; stroke-width: 2px;");
		n.setAttribute("highlighted", false);
	}
	
	public static void renderEdge(Edge e) {
		String nodeStyle = "fill-color: black; size: 3px;";
		e.setAttribute("ui.style", nodeStyle);
	}	
}
