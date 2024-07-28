import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.SwingUtilities;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.ui.graphicGraph.GraphicElement;
import org.graphstream.ui.view.View;

/*
 * Mouse Listener for editing a graph
 * 
 * Controls:
 * - Right click on a node to select or deselect it
 * - With a node selected, right click on another to
 * 	create or delete an edge between them
 * - Use buttons to add or remove nodes
 * - Right click off a node to deselect the current node
 * - Only one node can be selected at a time
 */

public class CreateML implements MouseListener {

	private Trapper t;
	
	private View view;
	private Graph graph;
	
	private Node selected;
	
	public CreateML(Trapper t) {
		graph = t.getG().getGraph();
		view = t.getG().getViewer().getDefaultView();
		
		this.t = t;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {			
		/*
		 * If it is a right click on a node:
		 * 	Case 1: No node is selected
		 * 		select the clicked node
		 *	Case 2: Clicked node is already selected
		 *		deselect the clicked node
		 *	Case 3: There is a node selected but it is not the clicked node
		 *		If the two nodes have an edge, remove it
		 *		Otherwise add one between them
		 *		Then deselect the previously selected node
		 */
		
		if (SwingUtilities.isRightMouseButton(e)) {
			GraphicElement element = view.findNodeOrSpriteAt(e.getX(), e.getY());
			if (element != null) {
				Node n = graph.getNode(element.getId());
				if (selected == null) {
					selected = n;
					Visualizations.highlight(selected);
				}
				else if (selected.equals(n)) {
					reset();
				}
				else {
					if (selected.hasEdgeBetween(n)) {
						t.removeEdge(selected, n);
					}
					else {
						t.addEdge(selected, n);
					}
					reset();
				}
			}
			else {
				reset();
			}
		}
	}
	
	public void reset() {
		if (selected != null) {
			Visualizations.unHighlight(selected);
		}
		selected = null;
	}
	
	public class AddButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			t.addNode();
		}
	}
	
	public class DeleteButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (selected != null) {
				t.removeNode(selected);
				selected = null;
			}
		}
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
