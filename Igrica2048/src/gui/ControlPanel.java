package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.atomic.AtomicBoolean;



public class ControlPanel extends JPanel{
    
	private GridPanel gridPanel;
    private InfoPanel infoPanel;
    private ButtonsPanel buttonsPanel;

    public ControlPanel(GridPanel gridPanel) {
    	
        this.gridPanel = gridPanel;
        infoPanel = new InfoPanel();
        buttonsPanel= new ButtonsPanel(infoPanel, gridPanel);

        setLayout(new BorderLayout(32, 0));
        setPreferredSize(new Dimension(GridPanel.size, 128));
        add(infoPanel, BorderLayout.NORTH);
        add(buttonsPanel, BorderLayout.SOUTH);
    }

    private class InfoPanel extends JPanel {
    	
        private JLabel uputstvoLabel;
        private JLabel brojSimLabel;
        private JTextField brojSimField;

        public InfoPanel() {
        	
            setLayout(new FlowLayout(FlowLayout.CENTER, 16, 10));
            setPreferredSize(new Dimension(GridPanel.size, 80));
            
            uputstvoLabel = new JLabel("Za samostalnu kontrolu koristite strelice na tastaturi.");
            uputstvoLabel.setFont(new Font("Arial", Font.BOLD, 16));
            add(uputstvoLabel);

            brojSimLabel = new JLabel("Unesite broj simulacija/pomeraja: ");
            brojSimLabel.setFont(new Font("Arial", Font.BOLD, 16));
            add(brojSimLabel);

            brojSimField = new JTextField(6);
            brojSimField.setText("300");
            brojSimLabel.requestFocus();
            add(brojSimField);
        }

        public int getBrojSim() {
            int brojSim = -1;
            try {
                String text = brojSimField.getText().trim();
                brojSim = Integer.parseInt(text);
            } catch (NumberFormatException ex) {
                System.out.println(ex);
            }
            return brojSim;
        }
    }

    private class ButtonsPanel extends JPanel {

        InfoPanel infoPanel;
        GridPanel gridPanel;

        private JButton pokreni;
        private JButton restart;

       
        private AtomicBoolean pauzirano;
        private final Thread threadObject;

        public ButtonsPanel(final InfoPanel infoPanel, final GridPanel gridPanel) {
        	
            setLayout(new FlowLayout(FlowLayout.CENTER, 32, 0));
            setPreferredSize(new Dimension(gridPanel.size, 48));

            this.infoPanel = infoPanel;
            this.gridPanel = gridPanel;

            
            pauzirano = new AtomicBoolean(true);
            pokreni = new JButton("Pokrenite simulaciju");
            pokreni.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!pauzirano.get()) {
                        pokreni.setText("Pokreni simulaciju");
                        pauzirano.set(true);
                    } else {
                        pokreni.setText("Zaustavite");
                        pauzirano.set(false);

                        
                        synchronized (threadObject) {
                            threadObject.notify();
                        }
                    }
                }
            });
            add(pokreni, BorderLayout.CENTER);

            
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    while (true)
                    	// koliko puta je kliknuto na dugme
                        for (int i = 0; i < Integer.MAX_VALUE; i++) {
                            if (pauzirano.get())
                                synchronized (threadObject) {
                                    try {
                                        threadObject.wait();
                                    } catch (InterruptedException ex) {
                                        System.out.println(ex);
                                    }
                                }

                            int brojSim = infoPanel.getBrojSim();
                            if (brojSim != -1) {
                                gridPanel.runAIOnce(brojSim);
                                if (gridPanel.gameOver()) {
                                    pokreni.setText("Pokrenite simulaciju");
                                    pauzirano.set(true);
                                }
                            }

                            try {
                                if (brojSim < 200)
                                    Thread.sleep(5);
                            } catch (InterruptedException ex) {
                                System.out.println(ex);
                            }
                        }
                }
            };
         
            threadObject = new Thread(runnable);
            threadObject.start();

            restart = new JButton("Restartujte");
            restart.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    gridPanel.restart();
                }
            });
            add(restart, BorderLayout.EAST);
        }
    }
}
