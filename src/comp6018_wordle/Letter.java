/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comp6018_wordle;

/**
 *
 * @author south
 */
public class Letter {
    private char letter;
    private boolean correct;
    private boolean incorrect;
    private boolean incorrectPos;
   
    public Letter(char letter){
        this.letter = letter;
        this.correct = false;
        this.incorrect = false;
        this.incorrectPos = false;
    }
    
    public void setCorrect(){}
    public void setIncorrrect(){}
    public void setIncorrrectPos(){}
    public boolean getCorrect(){return this.correct;}
    public boolean getIncorrect(){return this.incorrect;}
    public boolean getIncorrectPos(){return this.incorrectPos;}
}
