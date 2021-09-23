package puzzles.hoppers.ptui;

import puzzles.common.Observer;
import puzzles.hoppers.model.HoppersClientData;
import puzzles.hoppers.model.HoppersModel;

import java.io.IOException;
import java.util.Scanner;

/**
 * @author Nathan Borkholder
 */

public class HoppersPTUI implements Observer<HoppersModel, HoppersClientData> {
    /**The model for view and controller */
    private HoppersModel model;

    /**
     * Construct the PTUI
     */
    public HoppersPTUI(String filename) throws IOException {
        this.model=new HoppersModel(filename);
        initializeView();
    }


    /**
     * Initialize the view
     */
    private void initializeView(){
        this.model.addObserver(this);
        update(this.model,null);
    }

    /**
     * Prints out the board with the rows and columns numbered
     */
    private void displayBoard(){
        int colCounter=0;
        StringBuilder result = new StringBuilder();
        result.append("  ");
        for(int i = 0; i<this.model.currentConfig.rowDim; ++i) {
            result.append(" " + i);
        }
        result.append("\n");
        result.append("  ");
        for(int d=0;d<2*this.model.currentConfig.rowDim;++d) {
            result.append("-");
        }
        result.append("\n");
        for ( int row = 0; row<this.model.currentConfig.rowDim; ++row) {
            result.append(colCounter + "|");
            for (int col = 0; col < this.model.currentConfig.colDim; ++col){
                result.append(" " + this.model.currentConfig.field[row][col]);
            }
            colCounter+=1;
            result.append("\n");
        }
        result.append("\n");
        System.out.print(result);
    }


    /**
     * Print on standard out help for the game.
     */
    private void displayHelp(){
        System.out.println( "h(int)              -- hint next move");
        System.out.println("l(oad) filename     -- load new puzzle file");
        System.out.println("s(elect) r c        -- select cell at r, c");
        System.out.println("q(uit)              -- quit the game");
        System.out.println("r(eset)             -- reset the current game");
    }

    /**
     * Updates the model after each move, displays the board and the command given
     * @param model the current model
     * @param data optional data the server.model can send to the observer
     *
     */
    @Override
    public void update(HoppersModel model, HoppersClientData data) {
        if(data != null){
            System.out.println(data.toString());
        }
        displayBoard();
        if(model.currentConfig.isGoal()){
            System.out.println("You Win!");
        }
    }

    /**
     * Reads a command and executes in a loop.
     * @throws IOException when the file is in wrong format
     */
    public void run() {
        Scanner in = new Scanner( System.in );
        for ( ; ; ) {
            String line = in.nextLine();
            String[] words = line.split( "\\s+" );
            if ( words.length > 0 ) {
                if ( words[ 0 ].startsWith( "h" ) ) {
                    this.model.hint();
                }
                else if ( words[ 0 ].startsWith( "l" ) ) {
                    String file = words[1];
                        this.model.loadFile(file);
                }
                else if ( words[ 0 ].startsWith( "s" ) ) {
                    int n = Integer.parseInt( words[ 1 ] );
                    int m = Integer.parseInt( words[ 2 ] );
                    this.model.select( n, m );
                }
                else if ( words[ 0 ].startsWith( "q" ) ) {
                    break;
                }
                else if ( words[ 0 ].startsWith( "r" ) ) {
                    this.model.reset();
                }
                else {
                    System.out.println("Invalid input");
                    displayHelp();
                }
            }
        }
    }

    /**
     * Runs the main method for PTUI, creates the PTUI, displays the initial board and help, and then runs the program.
     * @param args the inputed file
     * @throws IOException when the file is in the wrong format.
     */
    public static void main(String[] args)  {
        if (args.length != 1) {
            System.out.println("Usage: java HoppersPTUI filename");
        }
        try{
            HoppersPTUI ptui = new HoppersPTUI(args[0]);
            ptui.displayHelp();
            ptui.run();
        }
        catch(IOException e){
            System.out.println("Failed to read first file");

        }

    }
}
