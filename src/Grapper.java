import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

import org.graphstream.algorithm.measure.ConnectivityMeasure;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.Graphs;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Viewer;

/*
 * Wrapper class for a graph. Contains its panel and viewer.
 * Creates UI elements to manipulate the graph.
 */

public class Grapper {

	private Graph graph;
	private Viewer viewer;
	private ViewPanel graphPanel;

	private JPanel controlPanel;

	private JCheckBox layoutToggle;
	private JCheckBox labelToggle;
	private JLabel minDegree;
	private JButton connectivity;

	private JButton circulizer;
	private JButton refresh;
	private JButton bipartize;

	private Stylizer stylizer;

	public Grapper(Graph g) {
		graph = g;
		stylizer = new Stylizer(graph);

		stylizer.reset();

		viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);

		graphPanel = viewer.addDefaultView(false);

		createControlPanel();
	}

	// --------------- Setup ---------------
	private void createControlPanel() {
		GridBagLayout gbl = new GridBagLayout();
		controlPanel = new JPanel(gbl);
		controlPanel.setPreferredSize(new Dimension(400, 100));

		// ---------- Auto-Layout Toggle ----------
		layoutToggle = new JCheckBox("Auto-Layout");
		layoutToggle.addActionListener(new AutoLayoutListener());
		layoutToggle.doClick();
		gbl.addLayoutComponent(layoutToggle, Client.gbc(0, 0, 1, 1, 0.2, 0.5));
		controlPanel.add(layoutToggle);

		// ---------- Label Toggle ----------
		labelToggle = new JCheckBox("Show Labels");
		labelToggle.addActionListener(new LabelToggleListener());
		labelToggle.doClick();

		gbl.addLayoutComponent(labelToggle, Client.gbc(0, 1, 1, 1, 0.2, 0.5));
		controlPanel.add(labelToggle);

		// ---------- Minimum Degree ----------
		minDegree = new JLabel("Minimum degree: " + getMinDegree(), SwingConstants.CENTER);
		gbl.addLayoutComponent(minDegree, Client.gbc(1, 0, 1, 1, 0.2, 0.5));
		controlPanel.add(minDegree);

		// ---------- Connectivity ----------
		connectivity = new JButton("Connectivity: ?");
		connectivity.addActionListener(new ConnectivityListener());
		gbl.addLayoutComponent(connectivity, Client.gbc(1, 1, 1, 1, 0.2, 0.5));
		controlPanel.add(connectivity);

		// ---------- Refresh Button ----------
		refresh = new JButton(new ImageIcon("Icons/Controls/RefreshIcon.png"));
		refresh.addActionListener(new RefreshListener());
		gbl.addLayoutComponent(refresh, Client.gbc(4, 0, 1, 2, 0.2, 1));
		controlPanel.add(refresh);

		// ---------- Bipartize Button ----------
		bipartize = new JButton(new ImageIcon("Icons/Controls/BipartiteIcon.png"));
		bipartize.addActionListener(new BipartizeListener());
		gbl.addLayoutComponent(bipartize, Client.gbc(3, 0, 1, 2, 0.2, 1));
		controlPanel.add(bipartize);

		// ---------- Circulize Button ----------
		circulizer = new JButton(new ImageIcon("Icons/Controls/CircleIcon.png"));
		gbl.addLayoutComponent(circulizer, Client.gbc(2, 0, 1, 2, 0.2, 1));
		circulizer.addActionListener(new CirculizerListener());
		controlPanel.add(circulizer);
	}

	// --------------- Dynamically Changing the Graph ---------------

	public void changeGraph(Graph newG) {
		graph.clear();
		for (Node n : newG.getNodeSet()) {
			graph.addNode(n.getId());
		}
		for (Edge e : newG.getEdgeSet()) {
			graph.addEdge(e.getId(), e.getNode0().getId(), e.getNode1().getId());
		}
		defaults();
	}

	public void addNode() {
		for (int i = graph.getNodeCount() + 1; i > 0; i--) {
			if (graph.getNode("" + i) == null) {
				graph.addNode("" + i);
				defaults();
			}
		}
	}

	public void addEdge(Node n1, Node n2) {
		graph.addEdge(n1.getId() + n2.getId(), n1, n2);
		defaults();
	}

	public void removeNode(Node n) {
		graph.removeNode(n);
		defaults();
	}

	public void removeEdge(Node n1, Node n2) {
		graph.removeEdge(n1, n2);
		defaults();
	}

	public void defaults() {
		stylizer.reset();
		if (!layoutToggle.isSelected()) {
			circulize();
		}

		minDegree.setText("Minimum degree: " + getMinDegree());
		connectivity.setText("Connectivity: ?");
		bipartize.setIcon(new ImageIcon("Icons/Controls/BipartiteIcon.png"));
	}

	public int getMinDegree() {
		if (graph.getNodeCount() == 0) {
			return 0;
		}

		int min = Integer.MAX_VALUE;
		for (Node n : graph) {
			int degree = n.getDegree();
			if (degree < min) {
				min = degree;
			}
		}
		return min;
	}

	// --------------- Layouts ---------------

	private void circulize() {
		if (layoutToggle.isSelected()) {
			layoutToggle.doClick();
		}

		Collection<Node> nodes = graph.getNodeSet();
		int numNodes = nodes.size();

		int i = 0;
		for (Node n : nodes) {
			n.setAttribute("xyz", Math.cos(i * (2 * Math.PI / numNodes)), Math.sin(i * (2 * Math.PI / numNodes)), 0);
			i++;
		}
	}

	private boolean bipartize() {
		if (graph.getNodeCount() == 0) {
			return false;
		}

		Queue<Node> q = new LinkedList<>();

		Node start = graph.getNode(0);
		start.setAttribute("Part", "1");

		int num1 = 1;
		int num2 = 0;

		q.add(start);

		while (!q.isEmpty()) {
			Node n = q.poll();
			String nPart = n.getAttribute("Part");
			Iterator<Node> nIt = n.getNeighborNodeIterator();
			while (nIt.hasNext()) {
				Node m = nIt.next();
				if (m.hasAttribute("Part")) {
					if (m.getAttribute("Part").equals(nPart)) {
						for (Node node : graph) {
							node.removeAttribute("Part");
						}
						return false;
					}
				} else {
					if (nPart.equals("1")) {
						m.addAttribute("Part", "2");
						num2++;
					} else {
						m.addAttribute("Part", "1");
						num1++;
					}
					q.add(m);
				}
			}
		}

		// Display as bipartite

		if (layoutToggle.isSelected()) {
			layoutToggle.doClick();
		}

		String larger;
		double inc;

		int x1 = 0;
		int x2;

		int count1 = 0;
		int count2 = 1;

		if (num1 == num2) {
			larger = "1";
			inc = 1;
			x2 = num1 - 1;
			count2 = 0;
		} else if (num1 > num2) {
			larger = "1";
			inc = ((double) num1) / (num2 + 2);
			x2 = num1 - 1;

		} else {
			larger = "2";
			inc = ((double) num2) / (num1 + 2);
			x2 = num2 - 1;
		}

		for (Node node : graph) {
			if (node.getAttribute("Part").equals(larger)) {
				node.setAttribute("xyz", x1, count1, 0);
				count1++;
			} else {
				node.setAttribute("xyz", x2, count2 * inc, 0);
				count2++;
			}

			node.removeAttribute("Part");
		}
		return true;
	}

	// --------------- Listeners ---------------

	private class AutoLayoutListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JToggleButton source = (JToggleButton) e.getSource();
			if (source.isSelected()) {
				viewer.enableAutoLayout();
			} else {
				viewer.disableAutoLayout();
			}
		}
	}

	private class LabelToggleListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JToggleButton source = (JToggleButton) e.getSource();
			stylizer.setLabels(source.isSelected());
		}
	}

	private class CirculizerListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (layoutToggle.isSelected()) {
				layoutToggle.doClick();
			}
			circulize();
		}
	}

	private class RefreshListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!layoutToggle.isSelected()) {
				layoutToggle.doClick();
			}
			changeGraph(Graphs.clone(graph));
		}
	}

	private class ConnectivityListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton source = (JButton) e.getSource();

			// First, check if the graph is connected
			int c;

			if (graph.getNodeCount() == 0) {
				c = 0;
			} else {
				int count = 0;
				Iterator<Node> it = graph.getNode(0).getBreadthFirstIterator();

				while (it.hasNext()) {
					count++;
					it.next();
				}

				if (count == graph.getNodeCount()) {
					c = ConnectivityMeasure.getVertexConnectivity(graph);

					int m = getMinDegree();
					if (m < c) {
						c = m;
					}
				} else {
					c = 0;
				}
			}

			source.setText("Connectivity: " + c);
		}
	}

	private class BipartizeListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton source = (JButton) e.getSource();
			if (!bipartize()) {
				source.setIcon(new ImageIcon("Icons/Controls/NotBipartiteIcon.png"));
			}
		}
	}

	// --------------- Getters ---------------

	public Graph getGraph() {
		return graph;
	}

	public Viewer getViewer() {
		return viewer;
	}

	public ViewPanel getPanel() {
		return graphPanel;
	}

	public JPanel getControlPanel() {
		return controlPanel;
	}

	public Stylizer getStylizer() {
		return stylizer;
	}
}
