/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package comp6018_wordle;

import java.io.IOException;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author south
 */
public class ModelTest {  

    /**
    * Test of addLetter method, of class Model.
    * Scenario 1 - test comparison of words for the list of available words while game model is set to find words in list
    * Scenario 2 - test words of different lengths while game model is set to find words in list and validate this works for each turn
    * Scenario 3 - test words of different lengths while game model is set to accept any 5 letters and validate this works for each turn
    * @post  guess.length() == 5 && index < availableWords.size() -1(word must have a length of 5 and index should not exceed the size of the available words list)
    */
    @Test
    public void testValidateWord() throws IOException {
        System.out.println("Validate word test");        
        //initialising model so that the word is validated against a list of known words
        Model firstModel = new Model(true, true, true);            
        ArrayList<String> availableWords = firstModel.getAvailableWords();
        for(int i=0;i< availableWords.size();i++){

            String word = availableWords.get(i);
            for(int j = 0; j < word.length();j++){
                // convert to int
                int letter = (int)word.charAt(j);
                firstModel.addLetter(letter);                
            }
            //check word is length 5
            assertEquals(firstModel.checkLetterCount(), 5);         
            assertTrue(firstModel.validateWord());
            //clear the word
            for(int j = 0; j < 6;j++){                
                firstModel.removeLetter();
            }
        }
              
        //initialising this model so that any combination of letters can be input as a word
        Model secondModel = new Model(false, true, true);
        //scenario 2 & 3
        // the word 'rebut'
        int correctWord[] = {114, 101, 98, 117, 116};
        for(int i=0;i<6;i++){
            for(int j=0;j<6;j++){ 
                //test that validateWord returns false for different word lengths other than 5
                if(j < 5){
                    //scenario 2
                    //test length of word matches
                    assertEquals(firstModel.checkLetterCount(), j);
                    assertFalse(firstModel.validateWord());                    
                    
                    //scenario 3
                    assertEquals(secondModel.checkLetterCount(), j);
                    assertFalse(secondModel.validateWord());
                    firstModel.addLetter(correctWord[j]);
                    secondModel.addLetter(97);
                    
                }
                else{
                    //scenario 2                    
                    assertEquals(firstModel.checkLetterCount(), 5);
                    assertTrue(firstModel.validateWord());
                    
                    //scenario 3
                    assertEquals(secondModel.checkLetterCount(), 5);
                    assertTrue(secondModel.validateWord());                    
                    
                }

            }
            firstModel.enterGuess();
            secondModel.enterGuess();
        } 
    }
    
    /**
     * Test of addLetter method, of class Model.
     * Scenario 1 - test that addletter() allows lowercase letters and check that non-letters are not added, while maintaining post conditions
     * Scenario 2 - test that addLetter() correctly updates on each turn, also on each turn test that any unentered words do not have letters replaced 
     * after adding a letter more than 5 times, while maintaining post conditions 
     * @post guessedWords[guesses][count][0] != 0 && count < 5 && invariant() == true
     * @post(letter must be added to end of word in list and no letters added after number of letters exceed 5 in word)
     */
    @Test
    public void testAddLetter() throws IOException {
        System.out.println("Add letter test");
        //initialising model so that any combination of letters can be input as a word
        Model testModel = new Model(false, true, true);        
        //scenario 1
        //test letters
        for(int i = 97; i<123; i++){
            testModel.addLetter(i);
            int word[][][] = testModel.getGuessedWords();
            //compare the letter stored in the word for that guess
            assertTrue(testModel.checkLetterCount() <= 5);            
            assertTrue(testModel.invariant());   
            assertEquals(word[0][testModel.checkLetterCount() - 1][0],i);
            testModel.removeLetter();
        }
        //test non-letters
        for(int i = 33; i<48; i++){
            testModel.addLetter(i);
            int word[][][] = testModel.getGuessedWords();
            assertTrue(testModel.checkLetterCount() <= 5);            
            assertTrue(testModel.invariant());            
            assertEquals(word[0][testModel.checkLetterCount()][0],0);            
        }
        //scenario 2
        for(int i=0;i<6;i++){
            for(int j=0;j<10;j++){    
                //add letters 5 times and test postconditions
                if(i < 6){
                    // enter 'a'
                    testModel.addLetter(97);
                    int word[][][] = testModel.getGuessedWords();                    
                    assertTrue(testModel.checkLetterCount() <= 5);
                    assertTrue(word[i][testModel.checkLetterCount() - 1][0] != 0);
                    assertTrue(testModel.invariant());
                }
                //test letters dont update after adding letters 5 times and test postconditions
                else{
                    //enter 'b'                
                    testModel.addLetter(98);
                    int word[][][] = testModel.getGuessedWords();
                    assertTrue(testModel.invariant());                    
                    assertTrue(testModel.checkLetterCount() <= 5);                
                    //compare to the 5th letter entered into the word
                    assertEquals(word[i][4][0], 97);
                }
            }  
            //enter guess to proceed to next turn
            testModel.enterGuess(); 
            assertTrue(testModel.invariant());
        }
    }

    /**
     * Test of removeLetter method, of class Model.
     * Scenario 1 - Test that that letter in a word cannot go below 0 and when removing each letter the model updates also test the previous for each turn of the game
     * @post guessedWords[guesses][count - 1][0] == 0 && checkLetterCount() == count && && invariant() == true  (letter has been removed from word)
     */
    @Test
    public void testRemoveLetter() throws IOException {
        System.out.println("Remove letter test");
        Model testModel = new Model(false, true, true);  
        //scenario 1
        for(int i=0;i<6;i++){
            //create a 5 letter word
            for(int j=0;j<6;j++){    
                testModel.addLetter(97);
                assertTrue(testModel.invariant());
            } 
            int letters = 5;
            for(int j=0;j<10;j++){    
                testModel.removeLetter();                
                int word[][][] = testModel.getGuessedWords();
                assertTrue(testModel.invariant());
                //test letter being removed from word
                assertEquals(word[i][testModel.checkLetterCount()][0], 0);
                //when there are no letter test that the letter count stays at 0
                if(j >= 5){                 
                    assertTrue(testModel.checkLetterCount() == 0);
                }
                else{
                    letters--;
                    //test letter count is the correct number when removing a letter
                    assertTrue(testModel.checkLetterCount() == letters);
                }
                
            } 
            testModel.enterGuess();
            assertTrue(testModel.invariant());
        }

        
        
    }
    
}
