package hangman;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Set;
import java.util.Iterator;

public class EvilHangman {

    public static void main(String[] args) {
        String fileName = args[0];
        File f = new File(fileName);
        int wordLength = Integer.parseInt(args[1]);
        int guesses = Integer.parseInt(args[2]);
        StringBuilder potWord = new StringBuilder();
        for (int i = 0; i < wordLength; i++){
            potWord.append('-');
        }

        IEvilHangmanGame test = new EvilHangmanGame();
        try {
            test.startGame(f, wordLength);
        } catch (EmptyDictionaryException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

            Scanner userIn = new Scanner(System.in);
            while (guesses > 0) {
                try {
                    System.out.println("You have " + guesses + " left");
                    System.out.println("Used Letters: " + test.getGuessedLetters().toString());
                    System.out.println("Word: " + potWord.toString());

                    System.out.println("Guess a letter");
                    String guess = userIn.nextLine();
                    if (guess.length() > 1) {
                        System.out.println("Please only guess 1 letter at a time");
                        continue;
                    }
                    if ((int) Character.toLowerCase(guess.charAt(0)) < 96 || Character.toLowerCase(guess.charAt(0)) > 123){
                        System.out.println("Please type a letter and not a symbol");
                        continue;
                    }
                    Set<String> potWords = test.makeGuess(guess.charAt(0));
                    Iterator<String> it = potWords.iterator();
                    String word = it.next();
                    String orig = potWord.toString();
                    for (int i = 0; i < wordLength; i++){
                        if (guess.charAt(0) == word.charAt(i)){
                            potWord.setCharAt(i, guess.charAt(0));
                        }
                    }
                    if (orig.equals(potWord.toString())){
                        guesses--;
                        System.out.println("Sorry, there are no " + guess.charAt(0) + "'s");
                        if (guesses == 0) {
                            System.out.println("You lose\nThe word was: " + word);
                        }
                    }
                    if (!potWord.toString().contains("-")) {
                        System.out.println("You win!\nThe word was: " + potWord.toString());
                        break;
                    }
                } catch (GuessAlreadyMadeException e){
                    System.out.println("You've already guess this letter, try again");
                }
            }

        //(int) guess < 97 || (int) guess < 123)
    }

}
