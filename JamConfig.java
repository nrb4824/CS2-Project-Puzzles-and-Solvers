package puzzles.jam.model;

import puzzles.common.solver.Configuration;
import puzzles.hoppers.model.HoppersConfig;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class JamConfig implements Configuration{

    public final static String EMPTY = ".";

    /** The board for the configuration */
    public String[][] field;
    /** the row dimension of the board */
    public static int rowDim;
    /** the col dimension of the board */
    public static int colDim;
    /** An array list with all the types of valid characters on the board */

    HashMap<String, Car> cars = new HashMap<String, Car>();


    public JamConfig(String filename) throws IOException{
        try(BufferedReader in = new BufferedReader(new FileReader(filename))) {
            String[] rowcol = new String[2];
            rowcol = in.readLine().split(" ");
            this.rowDim = Integer.parseInt(rowcol[0]);
            this.colDim = Integer.parseInt(rowcol[1]);
            this.field = new String[rowDim][colDim];
            int numcars = Integer.parseInt(in.readLine());
            while(in.readLine() != null) {
                String[] c = new String[5];
                c = in.readLine().split(" ");
                Car car = new Car(Integer.parseInt(c[1]), Integer.parseInt(c[2]), Integer.parseInt(c[3]), Integer.parseInt(c[4]), c[0]);
                cars.put(c[0], car);
            }
            for(int row = 0; row<rowDim; ++row){
                for(int col = 0; col<colDim; ++col){
                    for(Car v: cars.values()){
                        if(v.startRow == row && v.startCol == col){
                            if(v.startRow == v.endRow){
                                int i = col;
                                while(i <= v.endCol){
                                    this.field[row][i] = v.getName();
                                }
                            }
                            if(v.startCol == v.endCol){
                                int i = row;
                                while(i<= v.endRow){
                                    this.field[i][col] = v.getName();
                                }
                            }
                        }
                    }
                    if(this.field[row][col] == null){
                        this.field[row][col] = EMPTY;
                    }
                    col += 1;
                }
                row +=1;
            }
        }
    }
    private JamConfig(JamConfig other){
        this.field = new String[rowDim][colDim];
        for(int row=0; row<rowDim; ++row){
            System.arraycopy(other.field[row], 0, this.field[row], 0, colDim);
        }
    }
    @Override
    public boolean equals(Object o){
        if(o instanceof JamConfig){
            JamConfig other = (JamConfig) o;
            return Arrays.deepEquals(this.field,other.field);
        }else{
            return false;
        }
    }

    @Override
    public int hashCode(){
        return Arrays.deepHashCode(this.field);
    }

    @Override
    public Collection<Configuration> getSuccessors(){
        Collection<Configuration> successors = new LinkedList<>();
        for(int row =0; row<rowDim; ++row){
            for(int col = 0; col<colDim; ++col){
                if(!this.field[row][col].equals(EMPTY)){
                    String key = this.field[row][col];
                    if(cars.containsKey(key)){
                        Car car = cars.get(key);
                        if(car.isVertical()){
                            successors.addAll(vertical(row, col, this));
                        }
                        else{
                            successors.addAll(horizontal(row, col, this));
                        }
                    }
                }
            }
        }
        return successors;
    }

    private Collection<Configuration> vertical(int row, int col, JamConfig current){
        Collection<Configuration> successors = new LinkedList<>();
        Car car = cars.get(this.field[row][col]);
        //Generates successors for positive vertical move
        int i = row;
        while(this.field[i+1][col].equals(EMPTY) && i+1 < rowDim){
            JamConfig successor= new JamConfig(current);
            successor.field[car.getStartRow()][col] = EMPTY;
            successor.field[car.getEndRow()+1][col] = car.getName();
            car.setStartRow(car.getStartRow() + 1);
            car.setEndRow(car.getEndRow() + 1);
            successors.add(successor);
        }
        //generates successors for negative horizontal move
        while(this.field[i-1][col].equals(EMPTY) && i-1 >= 0){
            JamConfig successor = new JamConfig(current);
            successor.field[car.getEndRow()][col] = EMPTY;
            successor.field[car.getStartRow() - 1][col] = car.getName();
            car.setStartRow(car.getStartRow() - 1);
            car.setEndRow(car.getEndRow() - 1);
            successors.add(successor);
        }
        return successors;
    }


    private Collection<Configuration> horizontal(int row, int col, JamConfig current){
        Collection<Configuration> successors = new LinkedList<>();
        Car car = cars.get(this.field[row][col]);
        //Generates successors for positive horizontal move
        int i = col;
        while(this.field[row][i + 1].equals(EMPTY) && i+1 < rowDim){
            JamConfig successor= new JamConfig(current);
            successor.field[row][car.getStartCol()] = EMPTY;
            successor.field[row][car.getEndCol() + 1] = car.getName();
            car.setStartCol(car.getStartCol() + 1);
            car.setEndCol(car.getEndCol() + 1);
            successors.add(successor);
        }
        while(this.field[i-1][col].equals(EMPTY) && i-1 >= 0){
            JamConfig successor = new JamConfig(current);
            successor.field[row][car.getEndCol()] = EMPTY;
            successor.field[row][car.getStartCol() - 1] = car.getName();
            car.setStartCol(car.getStartCol() - 1);
            car.setEndCol(car.getEndCol() - 1);
            successors.add(successor);
        }
        return successors;
    }
    @Override
    public boolean isGoal() {
        if( this.field[rowDim-1][colDim-1].equals("X")){
            return true;
        }
        else{
            return false;
        }
    }

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
