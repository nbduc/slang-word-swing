/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import model.History;
import utils.HistoryUtils;

/**
 *
 * @author duc
 */
public class HistoryDialog extends JDialog{
    private JList historyList;
    private final JButton cancelButton = new JButton("Cancel");
    private String returnedValue;
    
    public String getReturnedValue(){
        return returnedValue;
    }
    
    private void createAndShowGUI(){
        //set history list
        historyList = new JList(HistoryUtils.getHistoryList().toArray());
        historyList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    // Double-click detected
                    History history = (History)historyList.getSelectedValue();
                    returnedValue = history.getWord();
                    dispose();
                }
            }
        });
        
        //set history list scroll pane
        JScrollPane historyListScrollPane = new JScrollPane(historyList);
        
        //set cancel button
        cancelButton.addActionListener((ActionEvent e) -> {
            dispose();
        });
        
        //set cancel button wrapper
        JPanel cancelPanel = new JPanel();
        cancelPanel.setLayout(new FlowLayout());
        cancelPanel.add(cancelButton);
        
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(historyListScrollPane, BorderLayout.CENTER);
        getContentPane().add(cancelPanel, BorderLayout.SOUTH);
        setSize(250, 350);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    
    public HistoryDialog(JFrame parent, String title){
        super(parent, title, ModalityType.APPLICATION_MODAL);
        createAndShowGUI();
    }
}
