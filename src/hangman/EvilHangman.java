package hangman;

import java.io.File;

public class EvilHangman {

    public static void main(String[] args) {
        String fileName = args[0];
        File f = new File(fileName);
        int wordLength = Integer.parseInt(args[1]);
        int guesses = Integer.parseInt(args[2]);

        IEvilHangmanGame test = new EvilHangmanGame();
        test.startGame(f, wordLength);


    }

}
