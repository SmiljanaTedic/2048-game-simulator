package model;

import java.util.HashMap;
import java.util.Random;


public class Grid2048 {

    private int[][] grid; // osnovna mreza
    private final int grid_size; // dimenzije mreze

    // verovatnoca da ce izaci cetvorka
    private final double verovZaCetvorku = 0.1;
    //konstante koje pokazuju da li je potez bio uspeh ili neuspeh
    public static final int POMERAJ_USPEH = 2048;
    public static final int POMERAJ_NEUSPEH = -2048;
    //mapa izmedju smerova i inicijalnih pozicija brojeva
    private HashMap<Smerovi, int[][]> initPozicijaTabele
    	= new HashMap<Smerovi,int[][]> (5, 1);


    public Grid2048(int velicina) {
    	
        this.grid_size = velicina;
        grid = new int[velicina][velicina];

        initPozicijaTabele.put(Smerovi.GORE, new int[][]
                {{3, 0}, {3, 1}, {3, 2}, {3, 3}} );
        initPozicijaTabele.put(Smerovi.DOLE, new int[][]
                {{0, 0}, {0, 1}, {0, 2}, {0, 3} });
        initPozicijaTabele.put(Smerovi.LEVO, new int[][]
                {{0, 3}, {1, 3}, {2, 3}, {3, 3} });
        initPozicijaTabele.put(Smerovi.DESNO, new int[][]
                {{0, 0}, {1, 0}, {2, 0}, {3, 0} });
    }

    private int[] spoji(int[] line) {
    	
        // rezultat[0...size - 1] == spojene linije
        // rezultat[size] = spojen rezultat
    	
        int[] rezultat = new int[grid_size + 1];
        boolean[] spojen = new boolean[grid_size];
        
        for (int i = 0; i < grid_size; i++) {
            spojen[i] = false;
        }
        
        int spojenRezultat = 0;

        // spoji brojeve
        
        //i=2, i=1, i=0
        for (int i = grid_size - 2; i >= 0; i--) {
        	//j=3, j=2, j=1
            int j = i + 1;
            
            //uslov: j < 3
            while (j < grid_size - 1 && line[j] == 0) 
            	j++;
            
            if (line[i] == line[j] && spojen[j] == false) {
                spojen[j] = true;
                line[j] = line[j] * 2;
                spojenRezultat = spojenRezultat + line[j];
                line[i] = 0;
                continue;
            }
        }

        // pomeri nule
        int k = grid_size - 1;
        for (int i = grid_size - 1; i >= 0; i--)
            if (line[i] != 0)
                rezultat[k--] = line[i];

        rezultat[grid_size] = spojenRezultat;

        return rezultat;
    }

    private boolean jePopunjen() {
        for (int red = 0; red < grid_size; red++)
            for (int kolona = 0; kolona < grid_size; kolona++)
                if (grid[red][kolona] == 0)
                    return false;
        return true;
    }

    //na koji nacin skliznu celije
    
    // dajuci smer pomeranja, konvertuj mrezu u linije da bi se spojile
    private int[][] gridULinije(Smerovi smer) {
    	
    	//npr. smer = GORE   (-1, -1)
    	
        int[][] linije = new int[grid_size][grid_size];
        
        //npr.  int[][] inicPozicijaBrojeva = {{3, 0}, {3, 1}, {3, 2}, {3, 3}, {0, 0} }
        int[][] inicPozicijaBrojeva = initPozicijaTabele.get(smer);

        int m = 0;
        int n;
        
        //nece uci jer je RED_ILI_KOLONA = -1
        // LEVO {{0, 3}, {1, 3}, {2, 3}, {3, 3} });
        if (smer.RED_ILI_KOLONA == Smerovi.jeRed)	//to je = 1
            
        	for (int[] pozicijaBroja : inicPozicijaBrojeva) {
                n = 0;
                for (int i = pozicijaBroja[1]; i >= 0 && i < grid_size; i += smer.OFFSET) {
                    linije[m][n++] = grid[pozicijaBroja[0]][i];
                }
                
                m++;
            }
        //uci ce ovde
        else if (smer.RED_ILI_KOLONA == Smerovi.jeKolona)
            for (int[] pozicijaBroja: inicPozicijaBrojeva) {
                n = 0;
                //i=3, i=3+(-1)=2, i = 2+(-1)=1, i=1+(-1)=0
                //i=3
                for (int i = pozicijaBroja[0]; i >= 0 && i < grid_size; i += smer.OFFSET) {
                    linije[m][n++] = grid[i][pozicijaBroja[1]];
                }
                
                m++;
            }

        return linije;
    }

    //dajuci smer kretanja, vrati spojene linije nazad na mrezu
    private void linijeUGrid(Smerovi smer, int[][] linije) {
    	
        int[][] inicijalnaPozicijaBroja = initPozicijaTabele.get(smer);

        int m = 0;
        int n;
        if (smer.RED_ILI_KOLONA == Smerovi.jeRed)
            for (int[] pozicijaBroja: inicijalnaPozicijaBroja) {
                n = 0;
                for (int i = pozicijaBroja[1]; i >= 0 && i < grid_size; i += smer.OFFSET)
                    grid[pozicijaBroja[0]][i] = linije[m][n++];
                m++;
            }
        else if (smer.RED_ILI_KOLONA == Smerovi.jeKolona)
            for (int[] pozicijaBroja: inicijalnaPozicijaBroja) {
                n = 0;
                for (int i = pozicijaBroja[0]; i >= 0 && i < grid_size; i += smer.OFFSET)
                    grid[i][pozicijaBroja[1]] = linije[m][n++];
                m++;
            }
    }

    private boolean jeIstaGrid(int[][] grid1, int[][] grid2) {
    	
        for (int red = 0; red < grid_size; red++)
            for (int kolona = 0; kolona < grid_size; kolona++)
                if (grid1[red][kolona] != grid2[red][kolona])
                    return false;
        return true;
    }

    //dodaj ili dvojku ili cetvorku na mrezu
    public void dodajNoviBroj() {
    	
        if (jePopunjen()) 
        	return;

        Random rand = new Random();
        int broj = (rand.nextDouble() > verovZaCetvorku)? 2 : 4;

        int x = rand.nextInt(grid_size);
        int y = rand.nextInt(grid_size);
        if (grid[x][y] != 0)
            while (grid[x][y] != 0) {
                x = rand.nextInt(grid_size);
                y = rand.nextInt(grid_size);
            }
        grid[x][y] = broj;
    }

    //pomeri mrezu u zadatom smeru
    public int[] pomeri(Smerovi smer) {
    	
    	//napravi kopiju trenutne mreze
        int[][] gridKopija = new int[grid_size][grid_size];
        
        for (int red = 0; red < grid_size; red++)
            for (int kolona = 0; kolona < grid_size; kolona++)
                gridKopija[red][kolona] = grid[red][kolona];

        //daj linije na spajanje
        int[][] linije = gridULinije(smer);

        //spoji linije
        int[] spojeneLinije;
        int rezultatSuma = 0;
        for (int[] line: linije) {
            spojeneLinije = spoji(line);
            rezultatSuma = rezultatSuma + spojeneLinije[grid_size];
            for (int i = 0; i < grid_size; i++) {
            	line[i] = spojeneLinije[i];
            }
        }

        //vrati linije u mrezu
        linijeUGrid(smer, linije);

        /* rezultat[2] imace sledeca dva clana niza:
           rezultat[0] = da li je uspesno pomeren
           rezultat[1] = suma rezultata  
        		(to je ono sto se ispisuje na ekranu kod 'Rezultat')
        */        
        int[] rezultat = new int[2];
        
        if (!jeIstaGrid(grid, gridKopija)) {
            dodajNoviBroj();
            rezultat[0] = POMERAJ_USPEH;
            rezultat[1] = rezultatSuma;
        } 
        
        else {
            rezultat[0] = POMERAJ_NEUSPEH;
            rezultat[1] = 0;
        }

        return rezultat;
    }

    //uzmi sadasnje stanje
    public int[][] getGrid() {
    	
        int[][] gridKopija = new int[grid_size][grid_size];
        for (int red = 0; red < grid_size; red++)
            for (int kolona = 0; kolona < grid_size; kolona++)
                gridKopija[red][kolona] = grid[red][kolona];

        return gridKopija;
    }

    //stavi mrezu u novu mrezu
    public void setGrid(int[][] noviGrid) {
    	
        for (int red = 0; red < grid_size; red++)
            for (int kolona = 0; kolona < grid_size; kolona++)
                grid[red][kolona] = noviGrid[red][kolona];
    }

    //u jednom trenutku u kojim je sve smerovima moguce pomeriti se 
    public Smerovi[] getValidnePomeraje() {
    	
        int[][] gridKopija = getGrid();
        int broj = 0;
        Smerovi[] smerovi = new Smerovi[4];

        for (Smerovi sm: Smerovi.values()) {
            pomeri(sm);
            if (!jeIstaGrid(grid, gridKopija))
                smerovi[broj++] = sm;
            grid = gridKopija;
            gridKopija = getGrid();
        }

        Smerovi[] pomeraji = new Smerovi[broj];
        
        for (int i = 0; i < broj; i++)
            pomeraji[i] = smerovi[i];
        
        return pomeraji;
    }

    public boolean jeGotovo() {
    	
        int[][] gridKopija = getGrid();
        for (Smerovi sm: Smerovi.values()) {
            pomeri(sm);
            if (!jeIstaGrid(gridKopija, grid)) {
                grid = gridKopija;
                return false;
            }
            else {
            	grid = gridKopija;		//staro stanje
            	gridKopija = getGrid();		//sadasnje stanje 
            }
        }
        return true;
    }

    public void reset() {
    	
        for (int red = 0; red < grid_size; red++)
            for (int kolona = 0; kolona < grid_size; kolona++)
                grid[red][kolona] = 0;
    }

    public int getMaksimalniBroj() {
    	
        int max = 2;
        for (int red = 0; red < grid_size; red++)
            for (int kolona = 0; kolona < grid_size; kolona++)
                if (grid[red][kolona] > max)
                    max = grid[red][kolona];
        return max;
    }

}
