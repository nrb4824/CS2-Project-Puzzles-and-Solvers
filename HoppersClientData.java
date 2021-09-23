package puzzles.hoppers.model;


/**
 * @author Nathan Borkholder
 */
public class HoppersClientData{
    public String string;

    /**
     * The constructor for HoppersClientData
     * @param string the string to be passed
     */
    public HoppersClientData(String string){
        this.string = string;
    }

    /**
     * returns the string(already a string before passing into toString)
     * @return a string
     */
    public String toString(){
        return string;
    }
}
