package model;

import java.util.Random;


public enum Smerovi {
	
    GORE (-1, -1),
    DOLE (-1, 1),
    LEVO (1, -1),
    DESNO (1, 1);

    public final int RED_ILI_KOLONA;
    public final int OFFSET;

    public static final int jeRed = 1;
    public static final int jeKolona = -1;
    public static final Smerovi[] SMEROVI = Smerovi.values();

    private static Random rand = new Random();

    Smerovi(int redIliKolona, int offset) {
    	
        this.RED_ILI_KOLONA = redIliKolona;
        this.OFFSET = offset;
    }

    public static Smerovi getRandomSmer() {   	
        return SMEROVI[rand.nextInt(SMEROVI.length)];
    }

    public static int izSmeraUInt(Smerovi sm) {
    	
        if (sm == GORE) 
        	return 0;
        if (sm == DOLE) 
        	return 1;
        if (sm == LEVO) 
        	return 2;
        return 3;
    }

    public static Smerovi izIntUSmer(int n) {
    	
        if (n < 0 || n > 3) 
        	return null;
        if (n == 0) 
        	return GORE;
        if (n == 1) 
        	return DOLE;
        if (n == 2) 
        	return LEVO;
        return DESNO;
    }
}


