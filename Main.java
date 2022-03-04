
import java.util.HashMap;
import java.util.Scanner;


public class Main {
    
    public static void main(String[] args) {       
        Scanner scanner = new Scanner(System.in);
        HashMap<String, User> users = new HashMap<>();
        HashMap<String, Product> products = new HashMap<>();
        UI ui = new UI(users, products, scanner);
        ui.start();      
       
    }
}
