import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import javax.swing.SwingUtilities;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.ui.graphicGraph.GraphicElement;
import org.graphstream.ui.view.View;

/*
 * Mouse Listener that displays paths between nodes
 * 
 * Controls:
 * - Right click a node to select or deselect it
 * - Right click a second node to see all internally 
 * 	disjoint paths between them
 * - Right click off a node to deselect all nodes
 */

public class PathingML implements MouseListener {

	private Grapper g;
	private Graph graph;
	private View view;
	
	public List<Node> selected;
	
	public PathingML(Grapper g) {
		this.g = g;
		graph = g.getGraph();
		view = g.getViewer().getDefaultView();
		
		selected = new ArrayList<>();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
		/*
		 * If it is a right click on a node:
		 * 	Case 1: No node is selected:
		 * 		Select the clicked node
		 * 	Case 2: One node is selected:
		 * 		Select the clicked node
		 * 		Draw paths between the two selected nodes
		 * 	Case 3: Two nodes are selected:
		 * 		Deselect all nodes
		 * 		Select the clicked node
		 * If it is a right click off a node:
		 * 	Deslect all nodes
		 */
		
		
		if (SwingUtilities.isRightMouseButton(e)) {
			GraphicElement element = view.findNodeOrSpriteAt(e.getX(), e.getY());
			if (element != null) {
				Node n = graph.getNode(element.getId());
				int size = selected.size();
				
				switch (size) {
				case 0:
					selected.add(n);
					break;
				case 1:
					Node n1 = selected.get(0); 
					if (n1.equals(n)) {
						reset();
					}
					else {
						selected.add(n);
						Set<List<Node>> paths = allPaths(graph, selected.get(0), n);
						Visualizations.displayPaths(paths);
						
						break;
					}
				default:
					reset();
					selected.add(n);
					break;
				}
				
				Visualizations.highlight(n);

			}
			else {
				reset();
			}
		}
	}
	
	public void reset() {
		for (Node s: selected) {
			Visualizations.unHighlight(s);
		}
		selected.clear();
		g.getStylizer().resetEdges();
	}
	
	public static Set<List<Node>> allPaths(Graph g, Node a, Node b) {
		Set<List<Node>> paths = new HashSet<>();
		Set<Node> ignore = new HashSet<>();
		
		paths = allPaths(paths, ignore, a, b);
		
		if (a.hasEdgeBetween(b)) {
			List<Node> direct = new ArrayList<>();
			direct.add(a);
			direct.add(b);
			paths.add(direct);
		}
		
		return paths;
		
		
		// Repeatedly do breadth-first search, but remove nodes from the working graph each time
		
		
	}
	
	private static Set<List<Node>> allPaths(Set<List<Node>> paths, Set<Node> ignore, Node a, Node b) {
		List<Node> path = new ArrayList<Node>();
		
		Queue<Node> s = new LinkedList<>();
		HashMap<Node, Node> seen = new HashMap<>();
		seen.put(a, null);
		
		Iterator<Node> neighbors = a.getNeighborNodeIterator();
		while (neighbors.hasNext()) {
			Node nbr = neighbors.next();
			if (!ignore.contains(nbr) && !nbr.equals(b)) {
				s.add(nbr);
				seen.put(nbr, a);
			}
		}
		
		boolean found = false;
		while (!s.isEmpty() && !found) {
			Node n = s.poll();
			// If n is b, work backwards to create a path
			// If n is not b, add all of n's unseen neighbors to seen with n as the back node
			if (n.equals(b)) {
				path = createPath(seen, n, a);
				paths.add(path);
				found = true;
			}
			else {
				Iterator<Node> nNeighbors = n.getNeighborNodeIterator();

				while (nNeighbors.hasNext()) {
					Node nbr = nNeighbors.next();
					if (!seen.containsKey(nbr) && !ignore.contains(nbr)) {
						seen.put(nbr, n);
						s.add(nbr);
					}
				}
			}
		}
		
		if (!found) {
			return paths;
		}
		
		for (Node n: path) {
			if (!n.equals(a) && !n.equals(b)) {
				ignore.add(n);
			}
		}
		return allPaths(paths, ignore, a, b);
	}
	
	private static List<Node> createPath(Map<Node, Node> seen, Node start, Node end) {
		Node current = start;
		List<Node> path = new ArrayList<>();
		while (!current.equals(end)) {
			path.add(current);
			current = seen.get(current);
		}
		path.add(end);
		
		return path;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
	
	

}
