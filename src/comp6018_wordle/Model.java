/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comp6018_wordle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author 18020581
 */
public class Model extends Observable {
    private final boolean WORD_ERROR;
    private final boolean ANSWER_VISIBLE;
    private final boolean RANDOM_WORDS;    
    private boolean gameWon;
    private static final  int TOTAL_GUESSES = 6;
    private int guesses;
    private final ArrayList<String> availableWords;
    private final ArrayList<String> correctWords;
    private String chosenWord;
    private int[][][] guessedWords;    
    
    //@ invariant guesses >= 0 && guesses <= TOTAL_GUESSES && chosenWord.length() == 5 && !availableWords.isEmpty() && !correctWords.isEmpty()
    public Model(boolean wordError, boolean answerVisible, boolean randomWords) throws IOException{
        this.WORD_ERROR = wordError;
        this.ANSWER_VISIBLE = answerVisible;
        this.RANDOM_WORDS = randomWords;        
        this.guesses = 0;
        this.gameWon = false;
        //guessed words contains all words over the 6 guesses, each with 5 letters and each letter has two properties. 
        //The first property is the letter in number format, the second is the state of that letter.
        this.guessedWords = new int[TOTAL_GUESSES][5][2];
        this.availableWords = new ArrayList<>();
        this.correctWords = new ArrayList<>();
        loadWords();
        setChosenWord();
        assert(guesses >= 0 && guesses <= TOTAL_GUESSES && chosenWord.length() == 5 && !availableWords.isEmpty() && !correctWords.isEmpty());
        
    }
    
    public boolean invariant(){ 
        return guesses >= 0 && guesses <= TOTAL_GUESSES && !availableWords.isEmpty() && !correctWords.isEmpty();
    }
    
    /*@pre textWords.exists == true && textCommon.exists() == true(there is a common and words text document available.)      
    * @post wordsCount + commonCount == availableWords.size() && commonCount == correctWords.size() && invariant() == true
    *(available words list contains all words from the word text document and the correctWords list contains all words from the common text document)   */    
    private void loadWords() throws FileNotFoundException, IOException{ 
        File textWords = new File("words.txt");
        File textCommon = new File("common.txt");
        assert(textWords.exists() && textCommon.exists());
        FileInputStream words = new FileInputStream(textWords);
        FileInputStream common = new FileInputStream(textCommon);        
        Scanner scan = new Scanner(words);
        //add all words in the words text document to the available words list
        int wordsCount = 0;
        int commonCount = 0;
        while(scan.hasNextLine()){
            availableWords.add(scan.nextLine());
            wordsCount++;
        }
        words.close();
        scan = new Scanner(common);
        //add all words from the common text document to both the correctWords and availableWords lists
        while(scan.hasNextLine()){
            String word = scan.nextLine();
            correctWords.add(word);
            availableWords.add(word);
            commonCount++;
        }        
        common.close();
        assert(commonCount + wordsCount == availableWords.size() && commonCount == correctWords.size() && invariant());
    }
    
    /*@pre guesses != 0 && invariant() == true (game should not be able to reset on first turn)      
    * @post guesses == 0 & gameWon == false && invariant() == true  */    
    public boolean resetGame(){
        assert (guesses != 0 && invariant());        
        if(guesses > 0){
            guesses = 0;
            gameWon = false;
            guessedWords = new int[TOTAL_GUESSES][5][2];            
            setChosenWord();
            setChanged();
            notifyObservers(); 
            assert(guesses == 0 && gameWon == false && invariant());
            return true;
        }
        else{
           assert(invariant() == true);
           return false; 
        }
    }
    
    /*@pre invariant() == true (check guesses are within permitted range)      
    * @post invariant() == true && guesses == guess + 1 
    *(check guesses are within permitted range. if the word is valid check that guesses have increased by correct amount) */    
    public boolean enterGuess(){
        assert(invariant());
        int guess = guesses;
        //enter guess after making sure word is valid
        if(validateWord()){
            compareWord(); 
            if(guesses < TOTAL_GUESSES){
                guesses++; 
                assert(invariant() && guesses == guess + 1);
            }            
            setChanged();
            notifyObservers();
            return true;
        }    
        else{
            return false;
        }
    }
    

    
    /*@pre guessedWords[guesses].length >= 0 && guessedWords[guesses].length <= 5 (number of letters in guessed word is between  0 and 5)     
    * @post letterCount >= 0 && letterCount <= 5 (number of letters found in word is between  0 and 5) */
    public int checkLetterCount(){
        assert(guessedWords[guesses].length >= 0 && guessedWords[guesses].length <= 5);
        int letterCount = 0;
        for (int[] guessedWord : guessedWords[guesses]) {
            if (guessedWord[0] != 0) {
                letterCount++;
            }
        } 
        assert letterCount >=0 && letterCount <= 5;
        return letterCount;
    }
    
    /*@pre guessedWord[0] >= 97 && guessedWord[0] <= 122 (each number in guessed word should be between 65 and 90)     
    * @post  guess.length() == 5 && index <= availableWords.size()(word must have a length of 5 and index should not exceed the size of the available words list) */
    public boolean validateWord(){     
        String guess = "";
        //go through each letter of entered word and convert from number to letter to create guessed word
        for (int[] guessedWord : guessedWords[guesses]) {
            if(guessedWord[0] != 0){
                assert(guessedWord[0] >= 97 && guessedWord[0] <= 122);
                guess += String.valueOf((char) guessedWord[0]);   
            }
        }
        if(guess.length() == 5){
            if(WORD_ERROR){
                int index = 0;
                //compare guessed word to words in a list
                while(index < availableWords.size() -1 && !(guess.equals(availableWords.get(index)))){
                    index++;        
                }
                assert(guess.length() == 5 && index <= availableWords.size());
                //return if word is present in list or not
                return guess.equals(availableWords.get(index));  
            }   
            else{
                //return true only if WORD_ERROR flag is set to false allowing any 5 letter word to be entered
                return true;
            }
        }
        else{
            //return false if word is not 5 letters long
            return false;
        }
    }
    
    /*@pre checkLetterCount() == 5 (length of word must be 5)      
    * @post lettersCorrect <= 5 && lettersCorrect >= 0 && guessedWords[guesses][i][1] > 0 && guessedWords[guesses][i][1] < 4 
            (correct letters must be between range of 0 and 5 and each letter in guessed word must be assigned a value between 1 and 3) */
    private void compareWord(){         
        assert checkLetterCount() == 5;        
        int lettersCorrect = 0;        
        //go through each letter of the word chosen for that guess
        for(int i=0;i<guessedWords[guesses].length;i++){            
            char currentLetter = (char)guessedWords[guesses][i][0];
            //search for letter in chosen word
            int index = chosenWord.indexOf(currentLetter);
            if(index > -1){
                if(i == index || chosenWord.charAt(i) == currentLetter){
                    //set letter state as green (letter in correct position in word)
                    guessedWords[guesses][i][1] = 1;
                    lettersCorrect++;
                }
                else {
                    //set letter state as yellow (correct letter in word but in incorrect position)
                    guessedWords[guesses][i][1] = 2;
                }                
            }              
            else{
                //set letter state as grey (not found in word)
                guessedWords[guesses][i][1] = 3;                
            }
            assert (guessedWords[guesses][i][1] > 0 && guessedWords[guesses][i][1] < 4);
        }
        //set game as won if all letters in correct positions in word
        if(lettersCorrect == 5){
            gameWon = true;
        }
        assert (lettersCorrect >=0 && lettersCorrect <= 5);
    }   

    /*@pre letter >= 97 && letter <= 122 && gameWon == false && guesses < 6 && invariant() == true
    *(letter must be within letter ascii range. game must be not won. letter must be within letter count limit of word)     
    * @post guessedWords[guesses][count][0] != 0 && count < 5 && invariant() == true(letter must be added to end of word in list and no letters added after number of letters exceed 5 in word)   */    
    public void addLetter(int letter){               
        if(letter >= 97 && letter <= 122 && gameWon == false && guesses < 6){
            assert(letter >= 97 && letter <= 122 && gameWon == false && guesses < 6 && invariant()); 
            int count = 0;
            //find letter position in word
            while(count < 5 && guessedWords[guesses][count][0] != 0 ){
                count++;
            }
            //add letter to guessed word
            if(count < 5){
                guessedWords[guesses][count][0] = letter; 
                assert(guessedWords[guesses][count][0] != 0 && count < 5 && invariant());
                setChanged();
                notifyObservers();      
            }
        }
    }
    
    
    /*@pre checkLetterCount > 0 && invariant() == true(there is a letter to remove from word)      
    * @post guessedWords[guesses][count - 1][0] == 0 && checkLetterCount() == count && && invariant() == true(letter has been removed from word)   */
    public void removeLetter(){        
        int count = checkLetterCount(); 
        if(count > 0){
            assert(checkLetterCount() > 0 && invariant()); 
            guessedWords[guesses][count - 1][0] = 0; 
            assert guessedWords[guesses][count - 1][0] == 0;
            assert(invariant());
            setChanged();
            notifyObservers(); 
        }
        
    }

    /*@pre guesses >=0 && guesses <= totalGuesses   */
    public int getGuesses(){        
        assert guesses >=0 && guesses <= TOTAL_GUESSES;
        return guesses;}
    
    /*@pre !chosenWord.isEmpty() == true && chosenWord.length == 5 */
    public String getChosenWord(){        
        assert(!chosenWord.isEmpty() && chosenWord.length() == 5);
        return chosenWord;
    }
    
    /*@pre ANSWER_VISIBLE == false || ANSWER_VISIBLE == true (wordVisiblity must be either true or false) */
    public boolean getAnswerVisible(){
        assert(ANSWER_VISIBLE == false || ANSWER_VISIBLE == true);
        return ANSWER_VISIBLE;}
    
    /*@pre WORD_ERROR == false || WORD_ERROR == true (WORD_ERROR must be either true or false)*/
    public boolean getWordError(){
        assert(WORD_ERROR == false || WORD_ERROR == true);
        return WORD_ERROR;}
    
    /*@pre 
      @pre guessedWords.length == TOTAL_GUESSES && guessedWords[i].length == 5 && guessedWords[i][j].length == 2 
    *(guessed words must have dimensions of 6 turns 5 letters and 2 letter properties) */
    public int[][][] getGuessedWords(){     
        assert(guessedWords.length == TOTAL_GUESSES);
        for (int i = 0; i<guessedWords.length; i++) {
            assert (guessedWords[i].length == 5);
            for (int j = 0; j<guessedWords[i].length; j++) {
                assert (guessedWords[i][j].length == 2);                                
            }
        }  
        return guessedWords;}    
    
    /*@pre gameWon != null (gameWon must be either true or false) */
    public boolean getGameWon(){
        assert(gameWon == false || gameWon == true);
        return gameWon;}
    
    
    public ArrayList<String> getAvailableWords(){
        return availableWords;
    }
    
    
    /*@pre !correctWords.isEmpty() && invariant() == true (the correct words list is greater than 0)     
    * @post chosenWord.length() == 5 && chosenWord.matches("[a-z]") && invariant() == true(chosen word has 5 letters and the chosen word contains only letters)   */
    private void setChosenWord(){
        assert(!correctWords.isEmpty() && invariant());
        if(RANDOM_WORDS == true){
            Random rand = new Random();
            int randomise = rand.nextInt(correctWords.size());
            this.chosenWord = correctWords.get(randomise);
            setChanged();
            notifyObservers();
        }
        else{
            this.chosenWord = correctWords.get(1);
        }
        
        assert(chosenWord.length() == 5 && chosenWord.matches("[a-z]+") && invariant());
    }
    
}
