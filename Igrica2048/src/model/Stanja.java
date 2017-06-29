package model;

public class Stanja {

    private int[][] grid;
    private int rezultat;
    private int brojPomeraja;
    private boolean jePobedio;

    private final int GRID_SIZE = Igra2048.GRID_SIZE;

    public static Stanja konstruktujStanja(Igra2048 igra) {
    	
        Stanja stanjeIgrice = new Stanja();
        stanjeIgrice.grid = igra.getGrid();
        stanjeIgrice.rezultat = igra.getRezultat();
        stanjeIgrice.brojPomeraja = igra.getBrojPomeraja();
        stanjeIgrice.jePobedio = igra.jePobedio();

        return stanjeIgrice;
    }

    public int[][] getGrid() {
    	
        int[][] gridKopija = new int[GRID_SIZE][GRID_SIZE];
        for (int red = 0; red < GRID_SIZE; red++)
            for (int kolona = 0; kolona < GRID_SIZE; kolona++)
                gridKopija[red][kolona] = grid[red][kolona];

        return gridKopija;
    }

    public int getRezultat() {
    	
        return rezultat;
    }

    public boolean jePobedio() {
    	
        return jePobedio;
    }

}
