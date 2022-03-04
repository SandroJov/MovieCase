
import java.util.ArrayList;


public class User {    
    private String name;
    private ArrayList<String> viewed;
    private ArrayList<String> purchased;
    
        
    public User(String name, ArrayList<String> viewed, 
            ArrayList<String> purchased) {        
        this.name = name;
        this.viewed = viewed;
        this.purchased = purchased;
    }

    public String getName() {
        return name;
    }
    
    
    public ArrayList<String> getPurchased() {
        return purchased;
    }
        

    @Override
    public String toString() {
        return this.name + " - " + this.viewed + " - " + this.purchased;
    }            
        
}
