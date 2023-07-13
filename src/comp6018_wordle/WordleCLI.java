/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comp6018_wordle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author 18020581
 */
public class WordleCLI {
    public static void main(String[] args) throws IOException {
        Model wordle = new Model(true, true, true);
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter a word");
        while(!wordle.getGameWon() && wordle.getGuesses() < 6){
            int turn  = 6 - wordle.getGuesses();
            System.out.println("You have " + turn + " guesses left!");
            ArrayList<String> correctLetters = new ArrayList<>();
            ArrayList<String> incorrectLetters = new ArrayList<>();
            ArrayList<String> letterLocationWrong = new ArrayList<>();
            if(wordle.getAnswerVisible()){
                System.out.println("Correct Word: " + wordle.getChosenWord());
            } 
            String word = scan.nextLine();            
            if(word.length() == 5){
                for(int i = 0; i < word.length();i++){
                    int letter = (int)word.charAt(i);
                    wordle.addLetter(letter);
                }                
                if(wordle.enterGuess()){
                    int turnIndex = wordle.getGuesses() - 1;
                    int [][][] guessedWords = wordle.getGuessedWords();
                    for (int[] guessedWord : guessedWords[turnIndex]) {
                        int classification = guessedWord[1];
                        String letter = String.valueOf((char) guessedWord[0]);
                        switch(classification){  
                            case 1: {
                                if(!correctLetters.contains(letter)){
                                    correctLetters.add(letter);
                                }
                                break;
                            }
                            case 2:{
                                if(!letterLocationWrong.contains(letter)){
                                    letterLocationWrong.add(letter);
                                }
                                break;
                            }
                            case 3:{
                                if(!letterLocationWrong.contains(letter)){                            
                                    incorrectLetters.add(letter);
                                }                  
                                break;

                            }
                        }
                    }
                    System.out.println("Correct Letters: " + correctLetters);
                    System.out.println("Incorrect Location: " + letterLocationWrong);
                    System.out.println("Incorrect Letters: " + incorrectLetters);
                }
                else{
                    System.out.println("Error message: Unable to find word");
                    for(int i=0;i<word.length();i++){
                        wordle.removeLetter();
                    }
                }
                
            }
            else{
                System.out.println("Error message: Word not correct length");
            }
        }  
        if(wordle.getGameWon()){
            System.out.println("Congratulations! You've guessed the word!");
        }
        else{
            System.out.println("You've not guessed the word!");
        }
    }                
}
        
        

      
    

