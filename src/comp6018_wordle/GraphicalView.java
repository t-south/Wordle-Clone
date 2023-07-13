/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comp6018_wordle;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;
import java.util.Observer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author 18020581
 */
public class GraphicalView implements Observer{
    private Model gameModel;
    private GraphicalController gameController;
    private JFrame frame;
    private JPanel[] panels;    
    private static final String[][] KEYS = {{"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"}, {"A", "S", "D", "F", "G", "H", "J", "K", "L"}, {"Enter", "Z", "X", "C", "V", "B", "N", "M", "\u232b"}};
    private JPanel[] keyBoardPanels;
    private JLabel[][] board;   
    private JButton newGameButton;
    private JLabel chosenWord;
    private JLabel errorMessage;
    
    public GraphicalView(Model gameModel, GraphicalController gameController){
        //set up the GUI for use
        keyBoardPanels = new JPanel[KEYS.length];
        newGameButton = new JButton("New Game");
        chosenWord = new JLabel();
        errorMessage = new JLabel();        
        this.gameModel = gameModel;
        this.gameController = gameController;
        board = new JLabel[6][5];
        gameModel.addObserver(this);
        createDisplay();       
        chosenWord.setVisible(gameModel.getAnswerVisible());        
        errorMessage.setVisible(gameModel.getWordError());        
        chosenWord.setText("Correct Word: " + gameModel.getChosenWord());
        gameController.setView(this);        
    }

    private void createDisplay(){
        frame = new JFrame("Wordle");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1920,1080);
        frame.getContentPane().setBackground(Color.WHITE);        
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints constraint = new GridBagConstraints();
        JPanel displayPanel = new JPanel();
        displayPanel.setBorder(BorderFactory.createEmptyBorder(30,20,10,20));
        //create letter displays for the words, setting fonts, colors and spacing of boxes
        for(int i =0; i < board.length; i++){     
            for(int j =0; j < board[i].length; j++){  
                board[i][j] = new JLabel(" ", JLabel.CENTER);
                board[i][j].setFont(new Font("Neue Helvetica", Font.BOLD, 75));                
                board[i][j].setForeground(Color.BLACK);
                board[i][j].setBackground(Color.WHITE);
                board[i][j].setPreferredSize(new Dimension(100, 100));
                board[i][j].setOpaque(true);
                board[i][j].setBorder(BorderFactory.createLineBorder(Color.GRAY));
                constraint.fill = GridBagConstraints.HORIZONTAL;               
                constraint.gridy = i;
                constraint.gridx = j;
                //create spacing between the letter box displays
                constraint.insets = new Insets(3, 3, 3, 3);
                displayPanel.setLayout(layout);
                displayPanel.add(board[i][j], constraint);     
            }
        }        
        frame.getContentPane().add(BorderLayout.NORTH, displayPanel);
        createKeyboard();  
        newGameButton.setVisible(false);
        newGameButton.setFocusable(false);
        newGameButton.addActionListener((ActionEvent e) -> {gameController.resetGame();});
        errorMessage.setVisible(false);
        frame.getContentPane().add(BorderLayout.EAST, errorMessage);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(newGameButton);
        frame.getContentPane().add(BorderLayout.CENTER, buttonPanel);        
        chosenWord.setVisible(gameModel.getAnswerVisible()); 
        frame.getContentPane().add(BorderLayout.WEST, chosenWord);
        frame.setResizable(false);
        frame.setVisible(true);    
    }
    

    private void createKeyboard(){   
        JPanel keyboardPanel = new JPanel();
        //create keyboard using a double array listing keys and that keys appropriate row.
        for(int i =0;i< KEYS.length; i++){
            keyBoardPanels[i] = new JPanel();            
            for(String j: KEYS[i]){
                JButton button = new JButton(j);
                //set keyboard background color to light gray and set font
                button.setBackground(Color.decode("#d3d6da")); 
                button.setBorder(BorderFactory.createEmptyBorder());
                button.setFont(new Font("Inherit", Font.BOLD, 20)); 
                button.setPreferredSize(new Dimension(100,100));
                button.addKeyListener(new KeyListener(){
                    @Override
                    public void keyTyped(KeyEvent e) {
                        
                    }

                    @Override
                    public void keyPressed(KeyEvent e) {
                        
                    }

                    @Override
                    public void keyReleased(KeyEvent e) {                        
                        gameController.inputKey(e);                        
                    }
                });
                //remove text highlight on button
                button.setFocusPainted(false);
                keyBoardPanels[i].add(button);
            }
            keyboardPanel.setLayout(new GridLayout(3, 9));            
            keyboardPanel.add(keyBoardPanels[i]);
        }
        frame.getContentPane().add(BorderLayout.SOUTH, keyboardPanel);    
    }
    
    private void updateKeyboard(String letter, int color){
        //iterate through keyboard panels to find keyboard letter and update its color
        for(int i=0;i<KEYS.length;i++){
            for(int j=0;j<KEYS[i].length;j++){
                //iterate through the keyboard button using the KEYS as an index 
                JButton keyButton = (JButton) keyBoardPanels[i].getComponent(j);
                Color background = keyBoardPanels[i].getComponent(j).getBackground();
                //find specific key letter on keyboard and check that keyboard background is not green already
                if(keyButton.getText().equals(letter.toUpperCase()) && background != Color.decode("#6aaa64")){
                    keyBoardPanels[i].getComponent(j).setForeground(Color.WHITE);
                    switch(color){
                        case 1 ->  { 
                            //green
                            keyBoardPanels[i].getComponent(j).setBackground(Color.decode("#6aaa64"));                            
                        }
                    
                        case 2 ->  {
                            //yellow
                            keyBoardPanels[i].getComponent(j).setBackground(Color.decode("#c9b458"));
                        }
                        
                        case 3 -> {
                            //grey
                            keyBoardPanels[i].getComponent(j).setBackground(Color.decode("#787c7e"));
                        } 
                    }                 
                    
                }                
            }
        }
    }
 
    private void resetKeyboard(){
        //reset the keyboard to its beginning setting. 
        assert gameModel.getGuesses() == 0;
        for(int i=0;i<KEYS.length;i++){
            for(int j=0;j<KEYS[i].length;j++){           
                keyBoardPanels[i].getComponent(j).setBackground(Color.decode("#d3d6da"));   
                keyBoardPanels[i].getComponent(j).setForeground(Color.BLACK);
            }
        }
    }
   
    public void changeNewGameButton(boolean decision){
        newGameButton.setVisible(decision);
    }
    
    public void changeErrorMessage(boolean wordEnterOutcome){ 
        //set the chosenWord for when the answerVisible flag is true
        chosenWord.setText("Correct Word: " + gameModel.getChosenWord());      
        if(wordEnterOutcome){
            errorMessage.setText("");
        }
        else{
            errorMessage.setText("Error message: Unable to find word");
        }
    }

    @Override
    public void update(Observable o, Object o1) {        
        int [][][] display = gameModel.getGuessedWords(); 
        if(gameModel.getGuesses() == 0){
            resetKeyboard();
        } 
        //update the letter display
        for(int i = 0; i<display.length;i++){
            for(int j = 0;j<display[i].length;j++){ 
                String letter = String.valueOf((char)display[i][j][0]);
                board[i][j].setText(letter);
                int color = display[i][j][1];
                switch(color){
                    case 0: {
                        board[i][j].setBackground(Color.WHITE);
                        board[i][j].setForeground(Color.BLACK); 
                        break;
                    }
                    
                    case 1: {
                        //GREEN
                        board[i][j].setBackground(Color.decode("#6aaa64"));
                        board[i][j].setForeground(Color.WHITE); 
                        break;
                    }
                    
                    case 2: {
                        //YELLOW
                        board[i][j].setBackground(Color.decode("#c9b458"));
                        board[i][j].setForeground(Color.WHITE);
                        break;
                    }
                        
                    case 3: {
                        //GRAY
                        board[i][j].setBackground(Color.decode("#787c7e"));  
                        board[i][j].setForeground(Color.WHITE); 
                        break;
                    }                
                }
                if(display[i][j][1] > 0){
                    updateKeyboard(letter, color);
                }                
            }
        }
    }
    
}
