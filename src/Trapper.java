import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

/*
 * Wrapper class for a Grapper and it's n-Tuple Grapper.
 * Creates UI elements that can manipulate these graphs.
 */

public class Trapper {
	private Grapper g;
	private Grapper nG;

	private JPanel generators;
	private JPanel selectModes;
	private JPanel editButtons;
	private JSlider nSlider;
	private JButton clearButton;

	private JComboBox<ImageIcon> genBox;
	private JSpinner kSpinner;
	private JSpinner pSpinner;
	private JSpinner qSpinner;
	private GridBagLayout genGbl;

	private JToggleButton highlightMode;
	private JToggleButton createMode;
	private JToggleButton pathingMode;
	private JButton addButton;
	private JButton deleteButton;

	private CreateML cml;
	private HighlightML hmlG;
	private HighlightML hmlN;
	private PathingML pmlG;
	private PathingML pmlN;

	public Trapper(Grapper g) {
		this.g = g;

		nG = new Grapper(NTuple.create(g.getGraph(), 2));
		nG.getPanel().setBorder(BorderFactory.createLineBorder(Color.blue, 5));

		createSlider();
		createClearButton();
		createGenerators();
		createSelectModes();
		createEditButtons();
	}

	// --------------- Setup ---------------

	private void createSlider() {
		// ---------- Slider to control n ----------
		nSlider = new JSlider(0, g.getGraph().getNodeCount(), 2);
		nSlider.addChangeListener(new NSliderListener());

		nSlider.setPaintTicks(true);
		nSlider.setMajorTickSpacing(1);
		nSlider.setPaintLabels(true);
		nSlider.setLabelTable(nSlider.createStandardLabels(1));
	}

	private void createClearButton() {
		// ---------- Clear button ----------
		clearButton = new JButton();
		clearButton.addActionListener(new ClearButtonListener());
		clearButton.setText("Clear");
	}

	private void createGenerators() {
		genGbl = new GridBagLayout();
		generators = new JPanel(genGbl);

		// ---------- Spinner for size of generated graphs ----------
		kSpinner = new JSpinner();
		kSpinner.addChangeListener(new KSpinnerListener());
		kSpinner.setModel(new SpinnerNumberModel(5, 1, 20, 1));

		genGbl.addLayoutComponent(kSpinner, Client.gbc(0, 2, 2, 1, 1, 0.2));
		generators.add(kSpinner);

		// ---------- Alternate Spinners ----------
		pSpinner = new JSpinner();
		pSpinner.addChangeListener(new KSpinnerListener());
		pSpinner.setModel(new SpinnerNumberModel(3, 1, 10, 1));

		qSpinner = new JSpinner();
		qSpinner.addChangeListener(new KSpinnerListener());
		qSpinner.setModel(new SpinnerNumberModel(2, 1, 10, 1));

		// ---------- Menu for selecting graph to generate ----------
		genBox = new JComboBox<>();
		genBox.addActionListener(new GenBoxListener());

		genBox.addItem(new ImageIcon("Icons/Generators/P5.png"));
		genBox.addItem(new ImageIcon("Icons/Generators/C5.png"));
		genBox.addItem(new ImageIcon("Icons/Generators/K5.png"));
		genBox.addItem(new ImageIcon("Icons/Generators/Star5.png"));
		genBox.addItem(new ImageIcon("Icons/Generators/K32.png"));

		genGbl.addLayoutComponent(genBox, Client.gbc(0, 1, 2, 1, 1, 0.6));
		generators.add(genBox);

		// ---------- Panel Label ----------
		JLabel genLabel = new JLabel(" ------- Generators -------", SwingConstants.CENTER);
		genGbl.addLayoutComponent(genLabel, Client.gbc(0, 0, 2, 1, 1, 0.2));
		generators.add(genLabel);

		generators.setPreferredSize(new Dimension(130, 100));
		generators.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
	}

	private void createSelectModes() {
		cml = new CreateML(this);
		hmlG = new HighlightML(g);
		hmlN = new HighlightML(nG);
		pmlG = new PathingML(g);
		pmlN = new PathingML(nG);

		GridBagLayout gbl = new GridBagLayout();
		selectModes = new JPanel(gbl);
		
		// ---------- Panel Label ----------
		JLabel label = new JLabel(" ------ Select Mode ------", SwingConstants.CENTER);
		gbl.addLayoutComponent(label, Client.gbc(0, 0, 1, 1, 1, 0.3));
		selectModes.add(label);

		// ---------- Create Mode ----------
		createMode = new JToggleButton(new ImageIcon("Icons/Modes/EditIcon.png"));
		createMode.setFocusPainted(false);
		createMode.addActionListener(new modeListener());
		gbl.addLayoutComponent(createMode, Client.gbc(0, 3, 1, 1, 1, 0.3));
		selectModes.add(createMode);

		// ---------- Highlight Mode ----------
		highlightMode = new JToggleButton(new ImageIcon("Icons/Modes/HighlightIcon.png"));
		highlightMode.setFocusPainted(false);
		highlightMode.addActionListener(new modeListener());
		gbl.addLayoutComponent(highlightMode, Client.gbc(0, 1, 1, 1, 1, 0.3));
		selectModes.add(highlightMode);
		
		// ---------- Pathing Mode ----------
		pathingMode = new JToggleButton(new ImageIcon("Icons/Modes/PathingIcon.png"));
		pathingMode.setFocusPainted(false);
		pathingMode.addActionListener(new modeListener());
		gbl.addLayoutComponent(pathingMode, Client.gbc(0, 2, 1, 1, 1, 0.3));
		selectModes.add(pathingMode);
	}

	private void createEditButtons() {
		editButtons = new JPanel(new GridLayout(1, 2));

		// ---------- Add Node ----------
		addButton = new JButton(new ImageIcon("Icons/Modes/PlusIcon.png"));
		addButton.setEnabled(false);
		addButton.setFocusPainted(false);
		addButton.addActionListener(cml.new AddButtonListener());
		editButtons.add(addButton);
		
		// ---------- Remove Node ----------
		deleteButton = new JButton(new ImageIcon("Icons/Modes/TrashIcon.png"));
		deleteButton.setEnabled(false);
		deleteButton.setFocusPainted(false);
		deleteButton.addActionListener(cml.new DeleteButtonListener());
		editButtons.add(deleteButton);
	}

	// --------------- Dynamically Changing the Graphs ---------------

	public void changeG(Graph newG) {
		g.changeGraph(newG);
		nG.changeGraph(NTuple.create(newG, nSlider.getValue()));

		nSlider.setMaximum(newG.getNodeCount());
	}

	public void changeN(int n) {
		nG.changeGraph(NTuple.create(g.getGraph(), n));
	}

	public void addEdge(Node n1, Node n2) {
		g.addEdge(n1, n2);
		nG.changeGraph(NTuple.create(g.getGraph(), nSlider.getValue()));
	}

	public void removeEdge(Node n1, Node n2) {
		g.removeEdge(n1, n2);
		nG.changeGraph(NTuple.create(g.getGraph(), nSlider.getValue()));
	}

	public void addNode() {
		g.addNode();
		nSlider.setMaximum(g.getGraph().getNodeCount());
		nG.changeGraph(NTuple.create(g.getGraph(), nSlider.getValue()));
	}

	public void removeNode(Node n) {
		g.removeNode(n);
		nSlider.setMaximum(g.getGraph().getNodeCount());
		nG.changeGraph(NTuple.create(g.getGraph(), nSlider.getValue()));
	}

	public void defaultVis() {
		g.getStylizer().reset();
		nG.getStylizer().reset();
	}

	// --------------- Listeners ---------------

	private class NSliderListener implements ChangeListener {
		public void stateChanged(ChangeEvent e) {
			JSlider source = (JSlider) e.getSource();
			if (!source.getValueIsAdjusting()) {
				changeN(source.getValue());
			}
		}
	}

	private class GenBoxListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {

			@SuppressWarnings("unchecked")
			JComboBox<ImageIcon> source = (JComboBox<ImageIcon>) e.getSource();

			Graph newG;

			int ind = source.getSelectedIndex();

			int k = (int) kSpinner.getValue();

			generators.remove(pSpinner);
			generators.remove(qSpinner);
			generators.remove(kSpinner);

			if (ind == 4) {

				genGbl.addLayoutComponent(pSpinner, Client.gbc(0, 2, 1, 1, 0.5, 0.2));
				genGbl.addLayoutComponent(qSpinner, Client.gbc(1, 2, 1, 1, 0.5, 0.2));
				generators.add(pSpinner);
				generators.add(qSpinner);

				kSpinner.setValue((int) pSpinner.getValue() + (int) qSpinner.getValue());

				newG = Generators.kBipartite((int) pSpinner.getValue(), (int) qSpinner.getValue());
			} else {
				genGbl.addLayoutComponent(kSpinner, Client.gbc(0, 2, 2, 1, 1, 0.2));
				generators.add(kSpinner);

				if (k % 2 == 1) {
					pSpinner.setValue(k / 2 + 1);
					qSpinner.setValue(k / 2);
				} else {
					pSpinner.setValue(k / 2);
					qSpinner.setValue(k / 2);
				}

				switch (ind) {
				case 0:
					newG = Generators.kPath(k);
					break;
				case 1:
					newG = Generators.kCycle(k);
					break;
				case 2:
					newG = Generators.kComplete(k);
					break;
				case 3:
					newG = Generators.kStar(k);
					break;
				default:
					newG = new SingleGraph("");
				}
			}

			generators.repaint();

			changeG(newG);

		}
	}

	private class modeListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JToggleButton source = (JToggleButton) e.getSource();

			if (source.equals(createMode)) {
				if (source.isSelected()) {
					g.getViewer().getDefaultView().addMouseListener(cml);
					deleteButton.setEnabled(true);
					addButton.setEnabled(true);
				} else {
					g.getViewer().getDefaultView().removeMouseListener(cml);
					deleteButton.setEnabled(false);
					addButton.setEnabled(false);
					cml.reset();
				}
			} else if (source.equals(highlightMode)) {
				if (source.isSelected()) {
					g.getViewer().getDefaultView().addMouseListener(hmlG);
					nG.getViewer().getDefaultView().addMouseListener(hmlN);

				} else {
					g.getViewer().getDefaultView().removeMouseListener(hmlG);
					nG.getViewer().getDefaultView().removeMouseListener(hmlN);
					hmlG.reset();
					hmlN.reset();
				}
			} else {
				if (source.isSelected()) {
					g.getViewer().getDefaultView().addMouseListener(pmlG);
					nG.getViewer().getDefaultView().addMouseListener(pmlN);
				} else {
					g.getViewer().getDefaultView().removeMouseListener(pmlG);
					nG.getViewer().getDefaultView().removeMouseListener(pmlN);
					pmlG.reset();
					pmlN.reset();
				}
			}

			if (source.isSelected()) {
				if (createMode.isSelected() && !source.equals(createMode)) {
					createMode.doClick();
				}
				if (highlightMode.isSelected() && !source.equals(highlightMode)) {
					highlightMode.doClick();
				}
				if (pathingMode.isSelected() && !source.equals(pathingMode)) {
					pathingMode.doClick();
				}
			} else {
				defaultVis();
			}
		}
	}

	private class KSpinnerListener implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent e) {
			genBox.setSelectedIndex(genBox.getSelectedIndex());

		}
	}

	private class ClearButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			changeG(new SingleGraph(""));
		}
	}

	// --------------- Getters ---------------

	public Grapper getG() {
		return g;
	}

	public Grapper getNG() {
		return nG;
	}

	public JSlider getNSlider() {
		return nSlider;
	}

	public JButton getClearButton() {
		return clearButton;
	}

	public JPanel getSelectModes() {
		return selectModes;
	}

	public JPanel getGenerators() {
		return generators;
	}

	public JPanel getEditButtons() {
		return editButtons;
	}
}
