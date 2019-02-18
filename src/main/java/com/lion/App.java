package com.lion;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class App {
    public JPanel panelName;
    private JLabel plusString;
    private JTextField textField1;
    private JButton okButton;
    private JTabbedPane tabbedPane1;
    private JRadioButton upgradeRadioButton;
    private JRadioButton unlockRadioButton;
    private JRadioButton lockRadioButton;
    private JTextField textField2;
    private JRadioButton closeRadioButton;
    private CatfishCommands catfishCommands;

    public App() {
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                catfishCommands = new CatfishCommands(textField1.getText(), textField2.getText());
                if (unlockRadioButton.isSelected()) {
                    catfishCommands.unlockAccount();
                } else if (lockRadioButton.isSelected()) {
                    catfishCommands.lockAccount();
                } else if (upgradeRadioButton.isSelected()){
                    catfishCommands.upgradeAccount();
                } else if (closeRadioButton.isSelected()){
                    catfishCommands.closeAccount();
                }
            }
        });
    }

}
