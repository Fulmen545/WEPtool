package com.lion;

import javax.swing.*;

public class Main {
    public static void main(String[] args){
        JFrame frame = new JFrame("WEP tool");
        frame.setContentPane(new App().panelName);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();;
        frame.setVisible(true);
    }
}
