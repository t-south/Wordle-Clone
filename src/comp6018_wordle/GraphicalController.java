/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comp6018_wordle;

import java.awt.event.KeyEvent;

/**
 *
 * @author 18020581
 */
public class GraphicalController {
    private Model gameModel;
    private GraphicalView view;
    
    public GraphicalController(Model gameModel){
        this.gameModel = gameModel;    
    }

    public void resetGame(){
        //controller enabling and disabling buttons on the view
        view.changeNewGameButton(!gameModel.resetGame());
        //removes any error messages on game reset
        view.changeErrorMessage(true);
    }
    
    public void setView(GraphicalView view){this.view = view;}
    
    public void inputKey(KeyEvent e){        
        if(gameModel.getGameWon() == false && gameModel.getGuesses() < 6){
            //if the input is the enter key
            if (e.getKeyCode() == 10){
                boolean wordEntryOutcome = gameModel.enterGuess();
                //controller alters view for enabling and disabling new game button
                view.changeNewGameButton(gameModel.getGuesses() > 0); 
                view.changeErrorMessage(wordEntryOutcome);
            }
            //if the input is a backspace
            else if(e.getKeyCode() == 8){
                gameModel.removeLetter();
            }
            //if the input is a letter
            else if(e.getKeyCode() >= 65 && e.getKeyCode() <= 90 && gameModel.checkLetterCount() < 5){
                //change from uppercase to lowercase
                int letter = e.getKeyCode() + 32;
                gameModel.addLetter(letter);
            }
        }
    
    }
}
