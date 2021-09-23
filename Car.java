package puzzles.jam.model;

import java.util.LinkedList;

public class Car {

    public int startRow;

    public int startCol;

    public int endRow;

    public int endCol;

    public int length;

    public String name;

    LinkedList<String> car = new LinkedList<String>();

    public Car(int startRow, int startCol, int endRow, int endCol, String name){
        this.startRow = startRow;
        this.startCol = startCol;
        this.endRow = endRow;
        this.endCol = endCol;
        this.name = name;

        if(this.startRow == this.endRow){
            int i = startCol;
            int j = 0;
            while(i <= endCol){
                car.add(Integer.toString(startRow));
                car.add(Integer.toString(i));
                j += 1;
            }
            length = j;
        }

        else if(this.startCol == this.endCol){
            int i = startRow;
            int j = 0;
            while(i<= endRow){
                car.add(Integer.toString(i));
                car.add(Integer.toString(startCol));
                j+=1;
            }
            length = j;
        }
    }

    public boolean isVertical(){
        if(this.startCol == this.endCol){
            return true;
        }
        else{
            return false;
        }
    }

    public int getStartRow() {
        return startRow;
    }

    public int getEndRow() {
        return endRow;
    }

    public int getStartCol() {
        return startCol;
    }

    public int getEndCol() {
        return endCol;
    }

    public int getLength() {
        return length;
    }

    public String getName() {
        return name;
    }

    public LinkedList<String> getCar() {
        return car;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public void setStartCol(int startCol) {
        this.startCol = startCol;
    }

    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }

    public void setEndCol(int endCol) {
        this.endCol = endCol;
    }

    @Override
    public String toString() {
        return "Car{" +
                "startRow=" + startRow +
                ", startCol=" + startCol +
                ", endRow=" + endRow +
                ", endCol=" + endCol +
                ", length=" + length +
                ", name='" + name + '\'' +
                ", car=" + car +
                '}';
    }
}
