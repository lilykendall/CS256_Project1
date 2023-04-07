import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Scanner;

// stores all of the dictionary data and stores methods for solving the puzzle
public class Letterman {

    private Config c;
    private ArrayList<WordInfo> dictionary;
    private int beginLocation, endLocation;

    public Letterman(Config con){
        this.c = con;

    }


    // read in the dictionary
    public void readDictionary(){
        Scanner scan = new Scanner(System.in);

        // get number of words in dictionary
        int count = scan.nextInt();
        scan.nextLine();

        dictionary = new ArrayList<>(count);

        while(scan.hasNextLine()){
            String line = scan.nextLine();

            //check for a blank line
            if(line.length() == 0){
                break;
            }

            // check for comments
            if(line.charAt(0) == '/'){
                continue;
            }

            // check if this is the begin word
            if(line.equals(c.getStartWord())){
                beginLocation = dictionary.size();
            }
            if(line.equals(c.getEndWord())){
                endLocation = dictionary.size();
            }
            if(c.getStartWord().length() != line.length()){
                if(!c.isLength()){
                    continue;
                }
            }
            dictionary.add(new WordInfo(line));
        } // while has next line
        //print the size of the dictionary
        System.out.println("Words in dictionary: " + count);



    } // readDictionary


    // output all words in dictionary
    public void printDictionary(){
        for(WordInfo el: dictionary){
            System.out.println(el.text);
        }
    }

    // method to check if a word is in the dictionary
    public boolean checkWord(String word){
        for(WordInfo el: dictionary){
            if(el.text.equals(word)){
                return true;
            }
        }
        return false;
    }

    // output the modification required to change string a to string b
    // a is the starting word and b is the ending word
    private void modificationOutput(String a, String b){
        // need to find first difference between a and b
        int pos = 0;

        // length of shorter string - 1
        int maxPosition = Math.min(a.length(), b.length());

        while(pos < maxPosition){
            //check for difference
            if(a.charAt(pos) != b.charAt(pos)){
                // we have found the position of the change
                break;
            }
            pos++;
        }
        // pos is either the position of the change or pos is index of last character in longer string
        // change, swap , insert or delete
        if(a.length() == b.length()){
            // for change: c, <position>, <letter>
            // for swap: s,<position>
            if(pos < maxPosition - 1 && a.charAt(pos +1) != b.charAt(pos +1)){
                System.out.println("s," + pos);
            }
            else{
                System.out.println("c," + pos + "," + b.charAt(pos));
            }


        }
        else if(a.length() < b.length()){
            // is insert
            // i, <position>, <letter>
            // string b will be longer
            System.out.println("i," + pos + "," + b.charAt(pos));

        }
        else if(a.length() > b.length()){
            // is remove
            // d, <position>
            System.out.println("d," + pos);

        }


    }

    // search from a beginning word to an end word
    public void search(){

        // deque to keep track of reachable collection
        // stores the index of the word being processed
        ArrayDeque<Integer> reachable_collection = new ArrayDeque<>();


        // processing the beginning word
        dictionary.get(beginLocation).setProcessed();
        dictionary.get(beginLocation).setPrev(-1);


        // adding the start index to the deque based on queue or stack mode
        if(c.isStackMode()){
            reachable_collection.addFirst(beginLocation);
        }
        else {
            reachable_collection.addLast(beginLocation);
        }

        if(c.isCheckpoint2()){
            System.out.println("  adding " + dictionary.get(beginLocation).text);
        }

        // keeps track of the number of steps taken for checkpoint 2
        int step = 0;

        // keeps track of the words that have been processed;
        // starting at 1 because the starting word has been processed already.
        int wordsChecked = 1;


        // while the reachable collection is not empty and the end word is not found
        while(!reachable_collection.isEmpty() && !dictionary.get(endLocation).isProcessed()){

            // current index being processed
            int currentIndex = reachable_collection.removeFirst();
            // current word being processed
            WordInfo currentWord = dictionary.get(currentIndex);
            // add a step every time something is removed
            step ++;

            if(c.isCheckpoint2()){
                System.out.println(step + ": processing " + currentWord.text);
            }

            // loop through every word in the dictionary
            for(int i = 0; i < dictionary.size(); i++){

                // skip ourselves
                if(currentIndex == i){
                    continue;
                }

                // other is the word at index i in the dictionary
                WordInfo other = dictionary.get(i);

                // if the word at the index is not yet processed and it is sufficiently similar to the current word...
                if(!other.isProcessed() && sufficientlySimilar(currentWord.text, other.text)){
                    //set the word being checked as processed
                    other.setProcessed();
                    other.setPrev(currentIndex);

                    // add it to the reachable collection based on the routing mode
                    if(c.isStackMode()){
                        reachable_collection.addFirst(i);
                    }

                    else{
                        reachable_collection.addLast(i);
                    }

                    // every time a word is added to the reachable collection, that counts as a word checked
                    wordsChecked ++;

                    if(c.isCheckpoint2()){
                        System.out.println("  adding " + other.text);
                    }

                    // if the word that was added was the end word, then we can break out of the loop
                    if(i == endLocation){
                        break;
                    }
                } //if word is not processed and is sufficiently similar

            } // for loop
        } // while loop

        // print that the solution has been found if the end word was found
        if(dictionary.get(endLocation).isProcessed()){
            System.out.println("Solution, " + wordsChecked + " words checked.");

            // track the path taken after the answer has been found
            ArrayList<Integer> answer = new ArrayList<>();
            answer.add(endLocation);
            int currWord = endLocation;
            while(dictionary.get(currWord).getPrev() != -1){
                currWord = dictionary.get(currWord).getPrev();
                answer.add(currWord);
            }

            // print how many words were used to find the end word
            System.out.println("Words in morph: " + answer.size());

            if(c.isWordMode()){
                for(int el = answer.size() - 1; el >= 0; el = el - 1){
                    System.out.println(dictionary.get(answer.get(el)).text);
                }
            }
            // modification output
            if(!c.isWordMode()){
                // print out the first word
                System.out.println(dictionary.get(answer.get(answer.size() - 1)).text);
                // then print out the one letter changes
                for(int idx = answer.size() - 2; idx >= 0; idx = idx - 1){
                    modificationOutput(dictionary.get(answer.get(idx + 1)).text, dictionary.get(answer.get(idx)).text);
                }
            }
        } // if the end word is processed

        // if not, then there is no solution
        if(!dictionary.get(endLocation).isProcessed()){
            System.out.println("No solution, " + wordsChecked + " words checked.");
        }


    } // search

    //checks if two words are similar to each other based on what morph mode is set
    private boolean sufficientlySimilar(String a, String b) {

        // variable to keep track of the difference in letters
        int charDifference = 0;
        // variable to keep track of where the difference in characters are
        int differenceIndex = 0;

        // if a and b are the same length then it can only be swap mode or change mode
        if(a.length() == b.length()){

            // check each letter in a and b
            for(int i = 0 ; i < a.length() ; i ++){

                // if the letter at index i in a and b do not match, add one to charDifference
                if(a.charAt(i) != b.charAt(i)){

                    charDifference ++;
                    differenceIndex = i;

                }
            } // for loop

            // if change mode is set and there is only one character difference
            if(c.isChange()){
                if(charDifference <= 1) {
                    return true;
                }
            }

            // if swap is set and the letters are swapped

            if(c.isSwap()){
                if(charDifference == 2){
                    if(a.charAt(differenceIndex) == b.charAt(differenceIndex - 1) && a.charAt(differenceIndex - 1) == b.charAt(differenceIndex)) {
                        return true;
                    }
                }
            }
        } // if (a length == b length)

        // else length must be set
        if(c.isLength()){

            // if a is longer than b
            if(a.length() - b.length() == 1){
                int j = 0;
                // check each letter in a and b
                for(int i = 0 ; i < b.length() ; i ++){
                    // if the character at index i is not the same for a and b
                    if(b.charAt(i) != a.charAt(i + j)){
                        // if there is already a char difference there cannot be another
                        if(j == 1){
                            return false;
                        }
                        charDifference ++;
                        j++;
                        // if the letter after index i is different they can't be the same
                        if(a.charAt(i + 1) != b.charAt(i)){
                            return false;
                        }
                    }
                }// for loop
                if(charDifference <= 1){
                    return true;
                }
            }
            // is b is longer than a
            if(b.length() - a.length() == 1){
                // same checking as above...
                int j =0;
                for(int i = 0 ; i < a.length() ; i ++){

                    if(b.charAt(i + j) != a.charAt(i)){
                        if(j==1){
                            return false;
                        }
                        charDifference ++;
                        j ++;
                        if(b.charAt(i + 1) != a.charAt(i)){
                            return false;
                        }
                      }
                    } // for loop
                if(charDifference <= 1){
                    return true;
                    }
            } // if a is longer than b

            } // if isLength

         // if nothing else works
        return false;

    } // sufficientlysimilar


    // inner class to store dictionary words
    private static class WordInfo{
        String text;
        boolean processed;
        int prev;

        // wordInfo constructor
        public WordInfo(String word){
            this.text = word;
            processed = false;
            this.prev = 0;
        }

        public boolean isProcessed() {
            return processed;
        }

        public void setProcessed(){
            processed = true;
        }

        public void setPrev(int lastnum){
            this.prev = lastnum;
        }

        public int getPrev(){
            return prev;
        }
    }


} // letterman class
