package gui;

import javax.swing.*;
import java.awt.*;



public class MainFrame extends JFrame {

	private ControlPanel controlPanel;
    public GridPanel gridPanel;
	private StatusPanel statusPanel;

    private final ImageIcon IKONICA = new ImageIcon(getClass().getResource("graphics/logo.png"));

    public MainFrame() {
    	
        setLayout(new BorderLayout(16, 32));
        setTitle("2048 simulator igrice");
        setIconImage(IKONICA.getImage());
        setLocation(200, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        statusPanel = new StatusPanel();
        gridPanel = new GridPanel(statusPanel);
        controlPanel = new ControlPanel(gridPanel);
        
        add(controlPanel, BorderLayout.NORTH);
        add(gridPanel, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);
        
        pack();
    }
}
