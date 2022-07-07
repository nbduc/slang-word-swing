/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import model.SlangWord;

/**
 *
 * @author duc
 */
public class TheWordDialog extends JDialog{
    private boolean isEditDialog = false;
    private SlangWord oldSlangWord;
    private SlangWord returnedValue;
    private final JButton cancelButton = new JButton("Cancel");
    private final JButton okButton = new JButton("OK");
    private final JTextField wordTextField = new JTextField(23);
    private final JTextField definitionTextField = new JTextField(23);
    private final JButton addButton = new JButton("Add");
    private final JButton removeButton = new JButton("Remove");
    private final JList definitionList = new JList();
    private final ArrayList<String> defs = new ArrayList<>();
    
    public SlangWord getReturnedValue(){
        return returnedValue;
    }
    
    private void addNewDefinition(){
        DefaultListModel model = (DefaultListModel) definitionList.getModel();
        String newDef = definitionTextField.getText();
        model.add(defs.size(), newDef);
        defs.add(newDef);
        definitionTextField.setText("");
    }
    
    private void createAndShowGUI(){
        //set remove def button
        removeButton.setEnabled(false);
        removeButton.addActionListener((ActionEvent e) -> {
            DefaultListModel model = (DefaultListModel) definitionList.getModel();
            int index = definitionList.getSelectedIndex();
            model.remove(index);
            defs.remove(index);
        });
        
        //set add def button
        addButton.addActionListener((ActionEvent e) -> {
            addNewDefinition();
        });
        
        //set definition text field
        definitionTextField.addActionListener((ActionEvent e) -> {
            addNewDefinition();
        });
        
        //set definition list
        DefaultListModel model = new DefaultListModel();
        model.addAll(defs);
        definitionList.setModel(model);
        definitionList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        definitionList.addListSelectionListener((ListSelectionEvent e) -> {
            if(!definitionList.isSelectionEmpty()){
                removeButton.setEnabled(true);
            } else {
                removeButton.setEnabled(false);
            }
        });
        definitionList.setPreferredSize(new Dimension(255, -1));
        JScrollPane definitionListPane = new JScrollPane(definitionList);
        
        //set word text field
        if(isEditDialog){
            wordTextField.setText(oldSlangWord.getWord());
        }
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.EAST;
        c.insets = new Insets(3,5,3,5);
        mainPanel.add(new JLabel("Word:"), c);
        c.gridy += 1;
        mainPanel.add(new JLabel("Definition:"), c);
        c.gridx = 1;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        mainPanel.add(wordTextField, c);
        c.gridy = 1;
        mainPanel.add(definitionTextField, c);
        c.gridy = 2;
        mainPanel.add(definitionListPane, c);
        c.gridx = 2;
        c.gridy = 1;
        mainPanel.add(addButton, c);
        c.gridy = 2;
        mainPanel.add(removeButton, c);
        
        //set ok button
        okButton.addActionListener((ActionEvent e) -> {
            String word = wordTextField.getText().toUpperCase();
            if(!"".equals(word)){
                String[] defsArray = new String[defs.size()];
                returnedValue = new SlangWord(word, defs.toArray(defsArray));
                JOptionPane.showMessageDialog(null, "Successfully added the new word.", "About", 
                            JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(null, "The Word field cannot be empty.", "Error", 
                            JOptionPane.ERROR_MESSAGE);
            }
        });
        
        //set cancel button
        cancelButton.addActionListener((ActionEvent e) -> {
            dispose();
        });
        
        //set function button wrapper
        JPanel funcButtonPanel = new JPanel();
        funcButtonPanel.setLayout(new FlowLayout());
        funcButtonPanel.add(okButton);
        funcButtonPanel.add(cancelButton);
        
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(funcButtonPanel, BorderLayout.SOUTH);
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    
    public TheWordDialog(JFrame parent, String title, SlangWord oldSlangWord){
        super(parent, title, ModalityType.APPLICATION_MODAL);
        isEditDialog = true;
        this.oldSlangWord = oldSlangWord;
        defs.addAll(Arrays.asList(oldSlangWord.getDefinitionList()));
        createAndShowGUI();
    }
    
    public TheWordDialog(JFrame parent, String title) {
        super(parent, title, ModalityType.APPLICATION_MODAL);
        createAndShowGUI();
    }
}
