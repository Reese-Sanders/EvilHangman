package hangman;

import java.io.File;
import java.io.IOException;
import java.util.*;


public class EvilHangmanGame implements IEvilHangmanGame {
    private Set<String> dict;
    private int wordLength;
    private SortedSet<Character> guessedLetters;
    private StringBuilder potWord;

    public EvilHangmanGame(){
        dict = new HashSet<>();
        guessedLetters = new TreeSet<Character>();
        potWord = new StringBuilder();
    }

    @java.lang.Override
    public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {
        Scanner scan = new Scanner(dictionary);
        this.wordLength = wordLength;

        for (int i = 0; i < wordLength; i++){
            potWord.append('_');
        }

        while (scan.hasNext()) {
            String toDict = scan.next().toLowerCase(Locale.ROOT);
            if (toDict.length() == this.wordLength) {
                dict.add(toDict);
            }
        }
        if (dict.size() == 0){
            throw new EmptyDictionaryException();
        }
        //System.out.println(dict.toString());
    }

    @java.lang.Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
        guess = Character.toLowerCase(guess);
        if (guessedLetters.contains(guess)){
            throw new GuessAlreadyMadeException();
        }
        guessedLetters.add(guess);
        return evalSets(genPotWordsMap(guess), guess);

    }

    private void updatePotWord(String update) {
        for (int i = 0; i < update.length(); ++i){
            //System.out.println(potWord.toString() + " and i = " + i);
            if (update.charAt(i) != '_'){
                potWord.setCharAt(i, update.charAt(i));
            }
        }
        //System.out.println(potWord.toString());
    }

    private Set<String> evalSets(Map<String, Set<String>> potWords, char guess){
        int maxCount = -1;
        Set<String> maxSet = new HashSet<>();
        String maxKey = null;
        for (Map.Entry<String, Set<String>> i : potWords.entrySet()){
            //System.out.println("Checking set " + i.getValue().toString() + "While max is set " + maxSet.toString());
            //If this word key set size is larger than max
            if (i.getValue().size() > maxCount) {
                maxCount = i.getValue().size();
                maxSet = i.getValue();
                maxKey = i.getKey();
            }
            //If this word key is equal
            else if (i.getValue().size() == maxCount) {

                //finds the least amount of word and last index
                Deque<Integer> countI = new ArrayDeque<Integer>();
                Deque<Integer> countM = new ArrayDeque<Integer>();
                for (int j = 0; j < wordLength; j++){
                    if (i.getKey().charAt(j) == guess) {
                        countI.add(j);
                    }
                }
                for (int j = 0; j < wordLength; j++){
                    if (maxKey.charAt(j) == guess) {
                        countM.add(j);
                    }
                }
                // if there are fewer instances of guess char than max
                if (countI.size() < countM.size()) {
                    maxCount = i.getValue().size();
                    maxSet = i.getValue();
                    maxKey = i.getKey();
                    // if there are equal instances of guess char and max
                } else if (countI.size() == countM.size()) {
                    // if the last index of guess char is later in max word
                    if (countI.getLast() > countM.getLast()) {
                        maxCount = i.getValue().size();
                        maxSet = i.getValue();
                        maxKey = i.getKey();
                        //if last index is the same, check which has the right most letters
                    } else if (countI.getLast() == countM.getLast()){
                        while (countI.getLast() == countM.getLast()) {
                            countI.pop();
                            countM.pop();
                        }
                        if (countI.getLast() > countM.getLast()) {
                            maxCount = i.getValue().size();
                            maxSet = i.getValue();
                            maxKey = i.getKey();
                        }
                    }
                }
            }
        }
        //System.out.println(maxSet.toString());
        updatePotWord(maxKey);
        dict = maxSet;
        return maxSet;
    }

    private Map<String, Set<String>> genPotWordsMap(char guess) {
        Map<String, Set<String>> potWords = new HashMap<String, Set<String>>();
        for (String word : dict) {
            int index = word.indexOf(Character.toLowerCase(guess));
            if (index > -1){
                StringBuilder key = new StringBuilder();
                for (int i = 0; i < word.length(); i++){
                    if (guess == word.charAt(i)){
                        key.append(guess);
                    } else {
                        key.append('_');
                    }
                }
                if (potWords.containsKey(key.toString())){
                    potWords.get(key.toString()).add(word);
                } else {
                    Set<String> nArray = new TreeSet<String>();
                    nArray.add(word);
                    potWords.put(key.toString(), nArray);
                }
            } else {
                if (potWords.containsKey(potWord.toString())){
                    potWords.get(potWord.toString()).add(word);
                } else {
                    Set<String> nArray = new TreeSet<String>();
                    nArray.add(word);
                    potWords.put(potWord.toString(), nArray);
                }
            }
        }
        //System.out.println(potWords.toString());
        return potWords;
    }

    @java.lang.Override
    public SortedSet<Character> getGuessedLetters() {
        return guessedLetters;
    }
}
