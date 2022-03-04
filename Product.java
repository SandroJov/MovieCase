
import java.util.ArrayList;


public class Product {    
    private String name;
    private String year;
    private ArrayList<String> genre;
    private double rating;
    private int price;

    public Product(String name, String year, ArrayList<String> genre, 
            double rating, int price) {        
        this.name = name;
        this.year = year;
        this.genre = genre;
        this.rating = rating;
        this.price = price;
    }
 

    public double getRating() {
        return rating;
    }

    public ArrayList<String> getGenre() {
        return genre;
    }
              
    
    @Override
    public String toString() {
        return name + " - " + year + " - " + genre + " - " + "Rating: "+ rating
                + " - " + "Price: " + price;
    }
              
}
