package model;

public class MonteCarlo {

    public static boolean debug = false;

    protected Stanja trenutnoStanjeIgre;
    private int[] rezultati;

    public MonteCarlo(Igra2048 trenutnaIgra) {
    	
        trenutnoStanjeIgre = Stanja.konstruktujStanja(trenutnaIgra);
        rezultati = new int[4];
    }

    private int simulirajJednom(Smerovi startSmer) {
    	
        Igra2048 simIgru = Igra2048.konstruisiIgru(trenutnoStanjeIgre);
       
        boolean pomeren = simIgru.pomeri(startSmer);
        
        if (!pomeren) 
        	return -1;

        while (!simIgru.jeGotov()) {        	
            Smerovi smer = Smerovi.getRandomSmer();
            simIgru.pomeri(smer);
        }
        
        //vrati razliku rezultata simuliranog i trenutnog
        return simIgru.getRezultat() - trenutnoStanjeIgre.getRezultat();
    }

    private void simuliraj(int brojSim) {
    	
    	//gleda gde je sve moguce pomeriti se
        Smerovi[] validanSmer = Igra2048.konstruisiIgru(trenutnoStanjeIgre).getValidniPomeraji();

        //npr: validni pomeraji su: levo, desno, gore
        for (Smerovi sm: validanSmer) {
        	//prvo gleda levo ;   levo je dvojka => rezultatIndex=2
            int rezultatIndex = Smerovi.izSmeraUInt(sm);	
            int sim = 0;
            
            //simuliraj onoliko puta koliko smo zadali
            while (sim < brojSim) {
                int rezultat = simulirajJednom(sm);
                if (rezultat == -1) 
                	continue;

                sim++;
                rezultati[rezultatIndex] += rezultat;
            }
        }
    }

    private Smerovi findNajveciRezultat() { 
    	
        int maxIndex = -1;
        int maxRezultat = -1;
        
        for (int i = 0; i < 4; i++)
            if (rezultati[i] > maxRezultat) {
                maxIndex = i;
                maxRezultat = rezultati[i];
            }

        return Smerovi.izIntUSmer(maxIndex);
    }

    
    public Smerovi getNajboljiSmer(int brojSim) {
    	
        Igra2048 igra = Igra2048.konstruisiIgru(trenutnoStanjeIgre);
        if (igra.jeGotov())
            return null;
        
        simuliraj(brojSim);
        return findNajveciRezultat();
    }

}
