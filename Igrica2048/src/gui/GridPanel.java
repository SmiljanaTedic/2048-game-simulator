package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import model.*;


public class GridPanel extends JPanel{
	
    private Igra2048 igra;
    private StatusPanel statusPanel;

    private static final int RAM_DIMENZIJE = 10;
    private static final int GRID_SIZE = Igra2048.GRID_SIZE;
    private static final int BROJ_DIMENZIJE = 128;
    public static final int size = GRID_SIZE * BROJ_DIMENZIJE + RAM_DIMENZIJE * 2;

    private Image POZADINA_SLIKA;
    private Image BROJEVI_SLIKA;
    private Image RAM_SLIKA;

    public GridPanel(StatusPanel statusPanel) {
    	
        setPreferredSize(new Dimension(size, size));
        POZADINA_SLIKA = new ImageIcon(getClass().getResource("graphics/pozadina.png")).getImage();
        BROJEVI_SLIKA = new ImageIcon(getClass().getResource("graphics/brojevi.png")).getImage();
        RAM_SLIKA = new ImageIcon(getClass().getResource ("graphics/ram.png")).getImage();

        igra = new Igra2048();
        igra.start();
        this.statusPanel = statusPanel;

        setKeyStrokeAction();
        setFocusable(true);
        requestFocusInWindow();
    }

    //tastatura: gore, dole, levo, desno strelice
    private void setKeyStrokeAction() {
    	
        Action goreAkcija = new PomeriAkcija(Smerovi.GORE);
        Action doleAkcija = new PomeriAkcija(Smerovi.DOLE);
        Action levoAkcija = new PomeriAkcija(Smerovi.LEVO);
        Action desnoAkcija = new PomeriAkcija(Smerovi.DESNO);

        InputMap i_map = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        i_map.put(KeyStroke.getKeyStroke("UP"), "pomeri_se_gore");
        i_map.put(KeyStroke.getKeyStroke("DOWN"), "pomeri_se_dole");
        i_map.put(KeyStroke.getKeyStroke("LEFT"), "pomeri_se_levo");
        i_map.put(KeyStroke.getKeyStroke("RIGHT"), "pomeri_se_desno");

        ActionMap a_map = getActionMap();
        a_map.put("pomeri_se_gore", goreAkcija);
        a_map.put("pomeri_se_dole", doleAkcija);
        a_map.put("pomeri_se_levo", levoAkcija);
        a_map.put("pomeri_se_desno", desnoAkcija);
    }

    @Override
    public void paintComponent(Graphics g) {
    	
        nacrtajRam(g);

        int[][] grid = igra.getGrid();
        for (int red = 0; red < GRID_SIZE; red++)
            for (int kolona = 0; kolona < GRID_SIZE; kolona++)
                nacrtajBroj(red, kolona, grid[red][kolona], g);
    }

    private int deliDoDva(int broj) {
        if (broj == 2) 
        	return 1;
        else 
        	return deliDoDva(broj / 2) + 1;
    }

    //kako pronalazi lokaciju broja na slici
    private int[] pozicijaBrojaNaSlici(int nasBroj) {
    	
        if (nasBroj == 0) 
        	return null;

        int redosled = deliDoDva(nasBroj);
        int red = ((redosled - 1) / 4) * BROJ_DIMENZIJE;
        int kolona = ((redosled - 1) % 4) * BROJ_DIMENZIJE;

        int[] lokacija = {red, kolona};
        return lokacija;
    }

    private void nacrtajBroj(int red, int kolona, int brojKojiCrtamo, Graphics g) {
    	
        int dx = kolona * BROJ_DIMENZIJE + RAM_DIMENZIJE;
        int dy = red * BROJ_DIMENZIJE + RAM_DIMENZIJE;
        int[] lokacija = pozicijaBrojaNaSlici(brojKojiCrtamo);

        if (lokacija == null)
            g.drawImage(POZADINA_SLIKA, dx, dy, dx + BROJ_DIMENZIJE, dy + BROJ_DIMENZIJE, 0, 0,
                    BROJ_DIMENZIJE, BROJ_DIMENZIJE, null);
        else {
            int sx = lokacija[1];
            int sy = lokacija[0];
            g.drawImage(BROJEVI_SLIKA, dx, dy, dx + BROJ_DIMENZIJE, dy + BROJ_DIMENZIJE,
                    sx, sy, sx + BROJ_DIMENZIJE, sy + BROJ_DIMENZIJE, null);
        }
    }

    private void nacrtajRam(Graphics g) {
    	
        int size = GRID_SIZE * BROJ_DIMENZIJE + RAM_DIMENZIJE * 2;
        g.drawImage(RAM_SLIKA, 0, 0, size, size, 0, 0, size, size, null);
    }

    private class PomeriAkcija extends AbstractAction {

        public PomeriAkcija(model.Smerovi sm) {
            putValue("smer", sm);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Smerovi sm = (Smerovi) getValue("smer");
            if (igra.pomeri(sm)) {
                statusPanel.updateStatus(igra);
                statusPanel.updateLabels();
                repaint();
            }
        }
    }

    public void restart() {
        igra.restart();
        statusPanel.resetStatus();
        statusPanel.updateLabels();
        repaint();
    }

    public void runAIOnce(int brojSim) {
        if (igra.jeGotov()) 
        	return;

        MonteCarlo mcAI = new MonteCarlo(igra);
        Smerovi sm = mcAI.getNajboljiSmer(brojSim);

        igra.pomeri(sm);
        statusPanel.updateStatus(igra);
        statusPanel.updateLabels();
        repaint();
    }

    public boolean gameOver() {
        return igra.jeGotov();
    }
}
