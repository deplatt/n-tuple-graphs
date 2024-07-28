import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.SwingUtilities;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.ui.graphicGraph.GraphicElement;
import org.graphstream.ui.view.View;

/*
 * Mouse Listener that allows the user to highlight nodes
 * 
 * Controls:
 * - Right click on a node to select or deselect it
 * - Right click off of a node to deselect all
 */

public class HighlightML implements MouseListener {
	
	private View view;
	private Graph graph;
	
	private Set<Node> selected;
	
	public HighlightML(Grapper g) {
		view = g.getViewer().getDefaultView();
		graph = g.getGraph();
		
		selected = new HashSet<>();
	}

	@Override
	public void mouseClicked(MouseEvent e) {		
		if (SwingUtilities.isRightMouseButton(e)) {
			GraphicElement element = view.findNodeOrSpriteAt(e.getX(), e.getY());
			if (element != null) {
				Node n = graph.getNode(element.getId());
				
				if (selected.contains(n)) {
					Visualizations.unHighlight(n);
					selected.remove(n);
				}
				else {
					Visualizations.highlight(n);
					selected.add(n);
				}
			}
			else {
				reset();
			}
		}
	}
	
	public void reset() {
		for (Node n: selected) {
			Visualizations.unHighlight(n);
		}
		selected.clear();
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
