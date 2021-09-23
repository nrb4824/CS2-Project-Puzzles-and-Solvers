package puzzles.hoppers.model;


import puzzles.common.solver.Configuration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

/**
 * @author Nathan Borkholder
 */

public class HoppersConfig implements Configuration{
    //Input Constants
    /** A green frog*/
    public final static char GREENFROG = 'G';
    /** A red frog */
    public final static char REDFROG = 'R';
    /** An empty space that a frog can jump to */
    public final static char EMPTY  = '.';
    /** An invalid space that a frog cannot jump to */
    public final static char INVALID = '*';

    /** The board for the configuration */
    public char[][] field;
    /** the row dimension of the board */
    public static int rowDim;
    /** the col dimension of the board */
    public static int colDim;
    /** An array list with all the types of valid characters on the board */
    private Character[] types = new Character[4];
    {types[0]=GREENFROG;
    types[1]=REDFROG;
    types[2]=EMPTY;
    types[3]=INVALID;}

    /**
     * The constructor for HoppersConfig
     * @param filename the file being read
     * @throws IOException When the input is an improper character
     */
    public HoppersConfig(String filename) throws IOException {
        try(BufferedReader in = new BufferedReader(new FileReader(filename))) {
            String[] rowCol = new String[2];
            rowCol= in.readLine().split(" ");
            this.rowDim = Integer.parseInt(rowCol[0]);
            this.colDim = Integer.parseInt(rowCol[1]);
            this.field = new char[rowDim][colDim];
            for(int row = 0; row<rowDim; ++row){
                for(int col = 0; col<colDim; ++col){
                    this.field[row][col] = (char)in.read(); // read next character
                    if(this.field[row][col]!=types[0] && this.field[row][col]!=types[1] && this.field[row][col]!=types[2] && this.field[row][col]!=types[3]){
                        throw new IOException("Error: unaccepted cell input");
                    }
                    in.read();// ignore next whitespace
                }
            }
        }
    }

    /**
     * The copy constructor for HoppersConfig
     * @param other the parent configuration
     */
    private HoppersConfig(HoppersConfig other){
        this.field = new char[rowDim][colDim];
        for(int row=0; row<rowDim; ++row){
            System.arraycopy(other.field[row], 0, this.field[row], 0, colDim);
        }
    }

    /**
     * Checks to see if two configurations are equal
     * @param o the configuration to be compared to this configuration
     * @return true if the two configurations are equal
     */
    @Override
    public boolean equals(Object o){
        if(o instanceof HoppersConfig){
            HoppersConfig other = (HoppersConfig)o;
            return Arrays.deepEquals(this.field,other.field);
        }else{
            return false;
        }
    }

    /**
     * Creates a unique hashcode for each configuration
     * @return returns and integer hashcode
     */
    @Override
    public int hashCode(){
        return Arrays.deepHashCode(this.field);
    }

    /**
     * Generates all possible successors for each parent configuration
     * @return A linkedlist of possible successors
     */
    @Override
    public Collection<Configuration> getSuccessors(){
        Collection<Configuration> successors = new LinkedList<>();
        for(int row =0; row<rowDim; ++row){
            for(int col = 0; col<colDim; ++col){
                if(this.field[row][col]==GREENFROG || this.field[row][col]==REDFROG){
                    if(row % 2 ==0 && col % 2 == 0){
                        successors.addAll(even(row,col,this));
                    }
                    else if(row% 2 != 0 && col % 2 !=0){
                        successors.addAll(odd(row,col,this));
                    }
                }
            }
        }
        return successors;
    }

    /**
     * Generates all the configurations for an even space
     * @param row The row position of the current space
     * @param col The col position of the current space
     * @param current The current configuration
     * @return A linkedlist of all the configurations
     */
    private Collection<Configuration> even(int row,int col,HoppersConfig current){
        Collection<Configuration> successors = new LinkedList<>();
        Character frog = this.field[row][col];
        //Generates successor for row+4,col
        if(isValid(row+4,col) && frogCheck(row+2,col)) {
            HoppersConfig successor= new HoppersConfig(current);
            successor.field[row + 2][col] = EMPTY;
            successor.field[row + 4][col] = frog;
            successor.field[row][col]=EMPTY;
            successors.add(successor);
        }
        //Generates successor for row-4,col
        if(isValid(row-4,col) && frogCheck(row-2,col)) {
            HoppersConfig successor= new HoppersConfig(current);
            successor.field[row - 2][col] = EMPTY;
            successor.field[row - 4][col] = frog;
            successor.field[row][col]=EMPTY;
            successors.add(successor);
        }
        //Generates successor for row,col-4
        if(isValid(row,col-4) && frogCheck(row,col-2)) {
            HoppersConfig successor= new HoppersConfig(current);
            successor.field[row][col - 2] = EMPTY;
            successor.field[row][col - 4] = frog;
            successor.field[row][col]=EMPTY;
            successors.add(successor);
        }
        //Generates successor for row,col+4
        if(isValid(row,col+4) && frogCheck(row,col+2)) {
            HoppersConfig successor= new HoppersConfig(current);
            successor.field[row][col + 2] = EMPTY;
            successor.field[row][col + 4] = frog;
            successor.field[row][col]=EMPTY;
            successors.add(successor);
        }
        //Generates successor for row+2,col+2
        if(isValid(row+2,col+2) && frogCheck(row+1,col+1)) {
            HoppersConfig successor= new HoppersConfig(current);
            successor.field[row + 1][col + 1] = EMPTY;
            successor.field[row + 2][col + 2] = frog;
            successor.field[row][col]=EMPTY;
            successors.add(successor);
        }
        //Generates successor for row-2,col+2
        if(isValid(row-2,col+2) && frogCheck(row-1,col+1)) {
            HoppersConfig successor= new HoppersConfig(current);
            successor.field[row - 1][col + 1] = EMPTY;
            successor.field[row - 2][col + 2] = frog;
            successor.field[row][col]=EMPTY;
            successors.add(successor);
        }
        //Generates successor for row-2,col-2
        if(isValid(row-2,col-2) && frogCheck(row-1,col-1)) {
            HoppersConfig successor= new HoppersConfig(current);
            successor.field[row - 1][col - 1] = EMPTY;
            successor.field[row - 2][col - 2] = frog;
            successor.field[row][col]=EMPTY;
            successors.add(successor);
        }
        //Generates successor for row+2,col-2
        if(isValid(row+2,col-2) && frogCheck(row+1,col-1)) {
            HoppersConfig successor= new HoppersConfig(current);
            successor.field[row + 1][col - 1] = EMPTY;
            successor.field[row + 2][col - 2] = frog;
            successor.field[row][col]=EMPTY;
            successors.add(successor);
        }
        return successors;
    }

    /**
     * Generates all of the configurations for an odd position
     * @param row the row position of the current space
     * @param col the col position of the current space
     * @param current the parent configuration
     * @return a linkedlist of all the configurations
     */
    private Collection<Configuration> odd(int row, int col, HoppersConfig current){
        Collection<Configuration> successors = new LinkedList<>();
        char frog = this.field[row][col];
        //Generates successor for row+2,col+2
        if(isValid(row+2,col+2) && frogCheck(row+1,col+1)){
            HoppersConfig successor = new HoppersConfig(current);
            successor.field[row+1][col+1]=EMPTY;
            successor.field[row+2][col+2]=frog;
            successor.field[row][col]=EMPTY;
            successors.add(successor);
            }
        //Generates successor for row-2,col+2
        if(isValid(row-2,col+2) && frogCheck(row-1,col+1)){
            HoppersConfig successor = new HoppersConfig(current);
            successor.field[row-1][col+1]=EMPTY;
            successor.field[row-2][col+2]=frog;
            successor.field[row][col]=EMPTY;
            successors.add(successor);
            }
        //Generates successor for row-2,col-2
        if(isValid(row-2,col-2) && frogCheck(row-1,col-1)){
            HoppersConfig successor = new HoppersConfig(current);
            successor.field[row-1][col-1]=EMPTY;
            successor.field[row-2][col-2]=frog;
            successor.field[row][col]=EMPTY;
            successors.add(successor);
            }
        //Generates successor for row+2,col-2
        if(isValid(row+2,col-2) && frogCheck(row+1, col-1)){
            HoppersConfig successor = new HoppersConfig(current);
            successor.field[row+1][col-1]=EMPTY;
            successor.field[row+2][col-2]=frog;
            successor.field[row][col]=EMPTY;
            successors.add(successor);
            }
        return successors;
    }

    /**
     * Checks to see if there is a frog in a certain spot to jump over
     * @param row the row position of the potential frog
     * @param col the col position of the potential frog
     * @return true if there is a frog at the position
     */
    private boolean frogCheck(int row, int col){
        if(row>=0 && col>=0 && row< rowDim && col<colDim && this.field[row][col]==GREENFROG){
            return true;
        }
        return false;
    }

    /**
     * Checks to see if the potential move position is on the board
     * @param row the row position to move to
     * @param col the col position to move to
     * @return true if the position is on the board
     */
    private boolean isValid(int row, int col){
        if(row>=0 && col>=0 && row< rowDim && col<colDim && this.field[row][col]==EMPTY){
            return true;
        }
        return false;
    }

    /**
     * checks to see if the configuration is the solution to the puzzle
     * @return true if there is one red frog on the board and no green frogs.
     */
    @Override
    public boolean isGoal(){
        int redCount=0;
        int greenCount=0;
        for(int row = 0; row<rowDim; ++row){
            for(int col = 0; col<colDim; ++col){
                if(this.field[row][col]==REDFROG){
                    redCount+=1;
                }
                else if(this.field[row][col]==GREENFROG){
                    greenCount+=1;
                }
            }
        }
        if(greenCount==0 && redCount==1){
            return true;
        }
        return false;
    }

    /**
     * Creates a string version of the field configuration
     * @return a string 2d array
     */
    @Override
    public String toString(){
        StringBuilder result = new StringBuilder();
        result.append("\n");
        for(int row = 0;row<rowDim; ++row){
            for(int col = 0; col<colDim; ++col){
                result.append(this.field[row][col] + " ");
            }
            result.append("\n");
        }
        return result.toString();
    }
}
