package puzzles.water;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.lang.reflect.Array;
import java.util.*;

/**
 * @author Nathan Borkholder
 * @author Clara Pitkins
 */

public class Water implements Configuration {
    public int amount;
    public static HashMap<Integer,Integer> buckets = new HashMap<>();
    public int[] amounts;
    public int bucketCounter = 0;

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println(("Usage: java Water amount bucket1 bucket2 ..."));
        }
        Water water = new Water(args);
        Solver solver =  new Solver(water);
        solver.solve(true);
    }

    /**
     * the consturcor for the water puzzle
     * @param args
     */
    public Water(String[] args){
        this.amounts = new int[args.length];
        for(int i = 0; i < args.length; ++i){
            int num = Integer.parseInt(args[i]);
            if(i==0){
                this.amount=num;
            }else{
                buckets.put(bucketCounter,num);
            }
            ++bucketCounter;
        }
        System.out.println("Amount: "+ amount + ", Buckets: [" + buckets.keySet() + "]");
    }

    /**
     * The copy constructor for water
     * @param water the previous config
     * @param bucketPored the first bucket
     * @param bucketFilled the second bucket
     */
    public Water(Water water,int bucketPored, int bucketFilled){
        this.amount=water.amount;
        this.amounts=new int[water.amounts.length];
        for(int i=0;i<amounts.length; ++i){
            this.amounts[i]= water.amounts[i];
        }
        if(bucketPored==0){
            amounts[bucketFilled-1]=buckets.get(bucketFilled);
        }
        else if(bucketFilled==0){
            amounts[bucketPored-1] = 0;
        }else{
            if(amounts[bucketFilled-1]< buckets.get(bucketFilled)){
                int change = Math.min(buckets.get(bucketFilled)-amounts[bucketFilled-1],amounts[bucketPored-1]);

                if(amounts[bucketPored-1]<=change){
                    amounts[bucketPored-1]=0;
                    amounts[bucketFilled-1]+= change;
                }
                else if(amounts[bucketPored-1]>change){
                    amounts[bucketPored-1]-=change;
                    amounts[bucketFilled-1]=buckets.get(bucketFilled);
                }
            }
        }
    }

    /**
     * Checks to see if two objects are equal
     * @param o the passed object
     * @return true if the objects are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Water water = (Water) o;
        return Arrays.equals(amounts, water.amounts);
    }

    /**
     * Creates the hash code for the configuration
     * @return an int that is the hashcode
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(amounts);
    }

    /**
     * Gets all the possible children configuration
     * @return a list of successors
     */
    @Override
    public Collection<Configuration> getSuccessors(){
        Collection<Configuration> successors =new LinkedList<>();
        for(int bucketNum: buckets.keySet()){
            for(int bucketNum2: buckets.keySet()) {
                Water successor1 = new Water(this,bucketNum, bucketNum2);
                successors.add(successor1);
            }
            Water successor2 = new Water(this,bucketNum,0);
            Water successor3 = new Water(this,0,bucketNum);
            successors.add(successor2);
            successors.add(successor3);
        }
        return successors;
    }

    /**
     * Checks to see if the goal is met by the configuration
     * @return true if it is the goal
     */
    @Override
    public boolean isGoal(){
        for(int i=0; i<this.amounts.length;i++){
            if(amounts[i]==this.amount){
                return true;
            }
        }
        return false;
    }

    /**
     * Creates the info necesary to print
     * @return a string arraylist
     */
    @Override
    public String toString(){
        StringBuilder result = new StringBuilder();
        result.append("[");
        for(int i=0; i<buckets.size(); ++i){
            if(i==buckets.size()-1){
                result.append(" " + amounts[i]);
            }
            else{
                result.append(amounts[i] + ",");
            }
        }
        result.append("]");
        return result.toString();
    }
}
