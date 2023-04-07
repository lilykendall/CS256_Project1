import static java.lang.System.nanoTime;

public class Main {

    public static void main(String[] args){

        Config c = new Config(args);

        Letterman l = new Letterman(c);

        l.readDictionary();

        if(!l.checkWord(c.getStartWord()) || !l.checkWord(c.getEndWord())){
            System.err.println("Error: Start word and/or end word not in dictionary");
            System.exit(1);
        }

        if(c.isCheckpoint1()){
            l.printDictionary();
            System.exit(0);
        }

        if(c.isCheckpoint2()) {
            l.search();
            System.exit(0);
        }
        l.search();


    }
}
