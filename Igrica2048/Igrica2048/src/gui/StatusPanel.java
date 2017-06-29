package gui;

import model.Igra2048;
import javax.swing.*;
import java.awt.*;


public class StatusPanel extends JPanel{

    private int rezultat;
    private int najboljiRezultat;
    private int brojPomeraja;
    private boolean pobedio;
    private boolean izgubio;

    private JLabel rezultatLabel;
    private JLabel najboljiRezultatLabel;
    private JLabel brojPomerajaLabel;
    private JLabel pobedioLabel;
    private JLabel izgubioLabel;
    
    
    public StatusPanel() {
    	
        setLayout(new GridLayout(1, 5));
        setPreferredSize(new Dimension(GridPanel.size, 24));

        inic_Statusa_I_Labela();
        updateLabels();
        
        add(rezultatLabel);
        add(najboljiRezultatLabel);
        add(brojPomerajaLabel);
        add(pobedioLabel);
        add(izgubioLabel);
    }

    public void updateLabels() {
    	
    	rezultatLabel.setText("  Rezultat: " + rezultat);
        najboljiRezultatLabel.setText("     Najbolji: " + najboljiRezultat);
        brojPomerajaLabel.setText("     Pomeraja: " + brojPomeraja);
        pobedioLabel.setText(pobedio? "" + "          Pobedili ste!" : "");
        izgubioLabel.setText(izgubio? "           Kraj igre." : "");
    }

    public void updateStatus(Igra2048 game) {
    	
        rezultat = game.getRezultat();
        if (rezultat > najboljiRezultat)
            najboljiRezultat = rezultat;
        brojPomeraja = game.getBrojPomeraja();
        pobedio = game.jePobedio();
        izgubio = game.jeGotov();
    }

    public void resetStatus() {
    	
        rezultat = 0;
        brojPomeraja = 0;
        pobedio = false;
        izgubio = false;
    }

    private void inic_Statusa_I_Labela() {
    	
    	resetStatus();
        najboljiRezultat = 0;

        rezultatLabel = new JLabel();
        najboljiRezultatLabel = new JLabel();
        brojPomerajaLabel = new JLabel();
        pobedioLabel = new JLabel();
        izgubioLabel = new JLabel();
    }

}
