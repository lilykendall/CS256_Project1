import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;

public class Config {


    // member variables for settings
    private String endWord = "";
    private String startWord = "";

    //checks morph modes
    private boolean Lset = false;
    private boolean Sset = false;
    private boolean Cset = false;

    // Checks for stack or queue mode
    private boolean isStack = false;
    private boolean routingSystem = false;

    // checks checkpoints
    private boolean checkpoint1;
    private boolean checkpoint2;

    // checks output mode;
    private boolean WordOutput = true;


    public Config(String[] args) {
        // getOpt processing

        //Creating command line options
        LongOpt[] CommandOpts = {
                new LongOpt("stack", LongOpt.NO_ARGUMENT, null, 's'),

                new LongOpt("queue", LongOpt.NO_ARGUMENT, null, 'q'),

                new LongOpt("change", LongOpt.NO_ARGUMENT, null, 'c'),

                new LongOpt("swap", LongOpt.NO_ARGUMENT, null, 'p'),

                new LongOpt("length", LongOpt.NO_ARGUMENT, null, 'l'),

                new LongOpt("output", LongOpt.REQUIRED_ARGUMENT, null, 'o'),

                new LongOpt("begin", LongOpt.REQUIRED_ARGUMENT, null, 'b'),

                new LongOpt("end", LongOpt.REQUIRED_ARGUMENT, null, 'e'),

                new LongOpt("help", LongOpt.NO_ARGUMENT, null, 'h'),

                new LongOpt("checkpoint1", LongOpt.NO_ARGUMENT, null, 'x'),

                new LongOpt("checkpoint2", LongOpt.NO_ARGUMENT, null, 'y')

        };

        Getopt opt = new Getopt("Project 1", args, "sqcplo:b:e:hxy", CommandOpts);
        opt.setOpterr(true);

        int choice;
        // looping through all arguments
        while ((choice = opt.getopt()) != -1) {
            switch (choice) {

                case 's':
                    //use stack routing
                    if (routingSystem) {
                        System.err.println("Error: Queue mode is already set.");
                        System.exit(1);
                    }
                        isStack = true;
                        routingSystem = true;

                    break;

                case 'q':
                    // use queue routing
                    if (routingSystem) {
                        System.err.println("Error: Stack mode is already set.");
                        System.exit(1);
                    }
                        isStack = false;
                        routingSystem = true;
                    break;

                case 'l':
                    // modify length
                    Lset = true;
                    break;

                case 'c':
                    // change one letter
                    Cset = true;
                    break;

                case 'p':
                    // swap two adjacent characters
                    Sset = true;
                    break;


                case 'o':
                    // must be followed by W or M
                    // output  file format
                    String outMode = opt.getOptarg();
                    if(!outMode.equals("M") && !outMode.equals("W")){
                        System.err.println("Error: unknown output mode. Only W and M are supported.");
                        System.exit(1);
                    }
                    WordOutput = outMode.equals("W");
                    break;

                case 'b':
                    // required argument
                    // starting word
                    startWord = opt.getOptarg();

                    break;

                case 'e':
                    // required argument
                    // ending word
                    endWord = opt.getOptarg();

                    break;

                case 'h':
                    // prints help message
                    System.out.println("This program turns a starting word into a specified end word with different methods.");
                    System.out.println("The --stack flag will use a stack system to complete the program.");
                    System.out.println("The --queue flag will use a queue system to complete the program.");
                    System.out.println("The --change flag will reach the end word by changing one letter.");
                    System.out.println("The --swap flag will reach the end word by swapping two letters that are next to each other.");
                    System.out.println("the --length flag will reach the end word by adding or deleting a letter.");
                    System.out.println("The --output flag defines whether the output file is in modification format or word output format.");
                    System.out.println("The --begin flag defines what the starting word is.");
                    System.out.println("The --end flag defines what the end word is");
                    System.out.println("The --checkpoint1 flag ends the program after checkpoint 1 is completed.");
                    System.out.println("The --checkpoint2 flag ends the program after checkpoint 2 is completed.");
                    System.exit(0);
                    break;

                case 'x':
                    // programs stops after checkpoint 1
                    checkpoint1 = true;
                    break;

                case 'y':
                    // program stops after checkpoint 2
                    checkpoint2 = true;
                    break;

                default:
                    System.err.println("Error: unknown command line argument");
                    System.exit(1);
            } // switch

        } // while loop

        if(!Lset && !Cset && !Sset){
            System.err.println("Error: no change, length or swap set in command line.");
            System.exit(1);
        }
        if(!routingSystem){
            System.err.println("Error: one routing scheme must be set");
            System.exit(1);
        }

        if(startWord.length() != endWord.length() && !Lset){
            System.err.println("Error: Impossible case");
            System.exit(1);
        }

        if(startWord.equals("") || endWord.equals("")){
            System.err.println("Error no start or end word specified.");
            System.exit(1);
        }


    } // Config constructor



    public boolean isSwap(){
        return Sset;
    }

    public boolean isChange(){
        return Cset;
    }

    public boolean isStackMode(){
        return isStack;
    }

    public boolean isLength() {
        return Lset;
    }

    public boolean isWordMode(){
        return WordOutput;
    }

    public String getStartWord(){
        return startWord;
    }

    public String getEndWord(){
        return endWord;
    }

    public boolean isCheckpoint1(){
        return checkpoint1;
    }

    public boolean isCheckpoint2(){
        return checkpoint2;
    }


}
