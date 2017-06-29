package model;


public class Igra2048{

    private Grid2048 grid2048;
    public static final int GRID_SIZE = 4;
    private int brojPomeraja;
    private int rezultat;
    private boolean jePobedio;

    public Igra2048() {
    	
        grid2048 = new Grid2048(GRID_SIZE);
        rezultat = 0;
        jePobedio = false;
    }

    public static Igra2048 konstruisiIgru(Stanja stanja) {
    	
        Igra2048 igra = new Igra2048();
        igra.grid2048.setGrid(stanja.getGrid());
        igra.rezultat = stanja.getRezultat();
        igra.jePobedio = stanja.jePobedio();

        return igra;
    }

    public int[][] getGrid() {  	
        return grid2048.getGrid();
    }

    public boolean pomeri(Smerovi smer) {
    	
        int[] pomeriRezultat = grid2048.pomeri(smer);
        
        // pomeriRezultat[0] = da li je uspesno pomeren
        // pomeriRezultat[1] = suma rezultata 
        
        if (pomeriRezultat[0] == Grid2048.POMERAJ_USPEH) {
            brojPomeraja++;
            rezultat = rezultat + pomeriRezultat[1];
            if (jePobedio == false && grid2048.getMaksimalniBroj() >= 2048)
                jePobedio = true;
            return true;
        }
        // else if (pomeriRezultat[0] == Grid2048.POMERAJ_NEUSPEH)
        else
        	return false;
    }

    public void start() {
    	
        grid2048.dodajNoviBroj();
    }

    public void restart() {
    	
        grid2048.reset();
        grid2048.dodajNoviBroj();
        brojPomeraja = 0;
        rezultat = 0;
        jePobedio = false;
    }

    public int getRezultat() {
    	return rezultat;
    }

    public boolean jeGotov() {
    	return grid2048.jeGotovo();
    }

    public Smerovi[] getValidniPomeraji() {
        return grid2048.getValidnePomeraje();
    }

    public boolean jePobedio() {
        return jePobedio;
    }

    public int getMaxBroj() {
        return grid2048.getMaksimalniBroj();
    }

    public int getBrojPomeraja() {
        return brojPomeraja;
    }

}
