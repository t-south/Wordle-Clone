/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package comp6018_wordle;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author 18020581
 */
public class WordleGUI {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        //model argument flags are: display error messages and compare to list of  known words, display correct answer, implement randomised word 
        Model wordleModel = new Model(true, true, true);
        GraphicalController controller = new GraphicalController(wordleModel);
        GraphicalView view = new GraphicalView(wordleModel, controller);    

    }
    
}
