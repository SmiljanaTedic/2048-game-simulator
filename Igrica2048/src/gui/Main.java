package gui;

import java.awt.*;

import gui.*;



public class Main {
    public static void main(String args[]) {
    	
        EventQueue.invokeLater(new Runnable() {
        	
            @Override
            public void run() {
            	
                new MainFrame().setVisible(true);
            }
        });
    }
}
