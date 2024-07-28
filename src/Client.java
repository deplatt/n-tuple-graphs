import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatIntelliJLaf;

public class Client {
	public static void main(String[] args) {
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		
		// Set look and feel
		try {
		    UIManager.setLookAndFeel( new FlatIntelliJLaf() );
		} catch( Exception ex ) {
		    System.err.println( "Failed to initialize LaF" );
		}
		
		UIManager.getLookAndFeelDefaults().put("defaultFont", new Font("AR Bonnie", Font.PLAIN, 14));
		
				
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		GridBagLayout gbl = new GridBagLayout();
		JPanel p = new JPanel(gbl) {
			private static final long serialVersionUID = 1L;

			@Override
			public Dimension getPreferredSize() {
				return new Dimension(1470, 845);
			}
		};

		p.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		
		Grapper g = new Grapper(Generators.kCycle(5));
		
		Trapper graphs = new Trapper(g);
		Grapper nG = graphs.getNG();
		
		JSlider nSlider = graphs.getNSlider();
		JButton clearButton = graphs.getClearButton();
		

		JPanel selectModes = graphs.getSelectModes();
		JPanel generators = graphs.getGenerators();
		JPanel gControls = g.getControlPanel();
		JPanel nControls = nG.getControlPanel();
				
		JPanel editButtons = graphs.getEditButtons();
		

		
		
		gbl.addLayoutComponent(generators, gbc(0, 0, 1, 3, 0.1, 1));
		gbl.addLayoutComponent(selectModes, gbc(0, 3, 1, 4, 0.1, 0.3));
		gbl.addLayoutComponent(editButtons, gbc(0, 7, 1, 1, 0.01, 0.02));
		gbl.addLayoutComponent(clearButton, gbc(0, 8, 1, 1, 0.1, 0.2));
		
		gbl.addLayoutComponent(g.getPanel(), gbc(1, 0, 6, 7, 1, 1));
		gbl.addLayoutComponent(nG.getPanel(), gbc(7, 0, 6, 7, 1, 1));

		gbl.addLayoutComponent(gControls, gbc(1, 7, 6, 1, 0.4, 0.01));
		gbl.addLayoutComponent(nControls, gbc(7, 7, 6, 1, 0.4, 0.01));

		gbl.addLayoutComponent(nSlider, gbc(1, 8, 12, 1, 1, 0.2));


		
		p.add(g.getPanel());
		p.add(nG.getPanel());

		
		p.add(nSlider);
		p.add(clearButton);
		p.add(selectModes);
		p.add(generators);
		
		p.add(gControls);
		p.add(nControls);
		
		p.add(editButtons);

		frame.add(p);
		frame.pack();
		frame.setVisible(true);

	}

	public static GridBagConstraints gbc(int gridx, int gridy, int gridwidth, int gridheight, double weightx,
			double weighty) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = gridx;
		gbc.gridy = gridy;
		gbc.gridheight = gridheight;
		gbc.gridwidth = gridwidth;
		gbc.weightx = weightx;
		gbc.weighty = weighty;

		gbc.fill = GridBagConstraints.BOTH;

		return gbc;
	}
}
