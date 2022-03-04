
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;
import java.util.stream.Collectors;

public class UI {

    private HashMap<String, User> users;
    private HashMap<String, Product> products;
    private Scanner scanner;

    public UI(HashMap<String, User> users, HashMap<String, Product> products,
            Scanner scanner) {
        this.users = users;
        this.products = products;
        this.scanner = scanner;
    }

    public void start() {
        Scanner scan = new Scanner(System.in);
        this.users = readUsersFromFile();
        this.products = readProductsFromFile();
        HashMap<String, String> current = readCurrentSessionFile();
        while (true) {
            System.out.println("1 - Latest popular movies (User rating)");
            System.out.println("2 - Latest popular movies (Purchase rate)");
            System.out.println("3 - Recommendations for users currently online");
            System.out.println("end - End program");
            System.out.println("");
            String input = scan.nextLine();
            if (input.equals("end")) {
                break;
            }
            if (input.equals("1")) {
                System.out.println("Latest popular movies (User rating):");
                ArrayList<Product> topThree = popularByRating(this.products);
                for (Product movie : topThree) {
                    System.out.println(movie);
                }
                System.out.println("");
            }
            if (input.equals("2")) {
                System.out.println("Latest popular movies (Purchase rate):");
                printMovies(popularByPurchase(this.users));
                System.out.println("");
            }
            if (input.equals("3")) {
                recommended(current);
                System.out.println("");
            }
        }
    }

    public HashMap<String, User> readUsersFromFile() {
        HashMap<String, User> user = new HashMap<>();
        try ( Scanner reader = new Scanner(Paths.get("Users.txt"))) {
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] parts = line.split(", ");

                String id = parts[0];

                String name = parts[1];

                ArrayList<String> viewed = new ArrayList<>();
                String[] partsViewed = parts[2].split(";");
                for (int i = 0; i < partsViewed.length; i++) {
                    viewed.add(partsViewed[i]);
                }

                ArrayList<String> purchased = new ArrayList<>();
                String[] partsPurchased = parts[3].split(";");
                for (int i = 0; i < partsPurchased.length; i++) {
                    purchased.add(partsPurchased[i]);
                }

                user.put(id, new User(name, viewed, purchased));
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return user;
    }

    public HashMap<String, Product> readProductsFromFile() {
        HashMap<String, Product> product = new HashMap<>();
        try ( Scanner reader = new Scanner(Paths.get("Products.txt"))) {
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] parts = line.split(",");

                String id = parts[0];

                String name = parts[1].trim();

                String year = parts[2];

                ArrayList<String> genre = new ArrayList<>();
                for (int i = 3; i < 8; i++) {
                    if (!(parts[i].trim().isEmpty())) {
                        genre.add(parts[i].trim());
                    }
                }

                double rating = Double.valueOf(parts[8]);

                int price = Integer.valueOf(parts[9]);

                product.put(id, new Product(name, year, genre, rating, price));
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return product;
    }

    public ArrayList<Product> popularByRating(HashMap<String, Product> hashMap){
        ArrayList<Product> topThree = hashMap.values().stream()                
                .sorted(Comparator.comparingDouble(Product::getRating).reversed())
                .limit(3)
                .collect(Collectors.toCollection(ArrayList::new));

        return topThree;
    }

    public ArrayList<String> popularByPurchase(HashMap<String, User> hashMap) {
        
        ArrayList<String> allPurchases = new ArrayList<>();
        for (User user : hashMap.values()) {
            allPurchases.addAll(user.getPurchased());
        }

        HashMap<String, Integer> multiples = new HashMap<>();
        for (String movies : allPurchases) {
            if (multiples.containsKey(movies)) {
                multiples.put(movies, multiples.get(movies) + 1);
            } else {
                multiples.put(movies, 1);
            }
        }
        ArrayList<Integer> recurring = new ArrayList<>();        
        for (HashMap.Entry<String, Integer> entry : multiples.entrySet()) {
            recurring.add(entry.getValue());
        }
        
        Collections.sort(recurring);
                
        ArrayList<String> topThree = multiples.keySet().stream()
                .filter(key -> multiples.get(key) == recurring.get(recurring.size() - 1) ||
                               multiples.get(key) == recurring.get(recurring.size() - 2) ||
                               multiples.get(key) == recurring.get(recurring.size() - 3))        
                .collect(Collectors.toCollection(ArrayList::new));
       
        return topThree;
    }

    public void printMovies(ArrayList<String> list) {
        for (String all : list) {
            System.out.println(products.get(all));
        }
    }

    public HashMap<String, String> readCurrentSessionFile() {
        HashMap<String, String> current = new HashMap<>();
        try (Scanner reader = new Scanner(Paths.get("CurrentUserSession.txt"))){
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] parts = line.split(", ");

                String userId = parts[0];

                String productId = parts[1].trim();

                current.put(userId, productId);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return current;
    }

    public void recommended(HashMap<String, String> hashMap) {
        for (String currentUsers : hashMap.keySet()) {
            
            ArrayList<String> currentGenres = 
                    this.products.get(hashMap.get(currentUsers)).getGenre();
            String currentUser = this.users.get(currentUsers).getName();
            ArrayList<Product> recommendedMovies = new ArrayList<>();
            try {
            this.products.values().stream()
                    .filter(s -> s.getGenre().contains(currentGenres.get(0)))
                    .filter(s -> s.getGenre().contains(currentGenres.get(1)) ||
                                 s.getGenre().contains(currentGenres.get(2)))
                    .limit(5)
                    .collect(Collectors.toCollection(() -> recommendedMovies));
            
            }catch (IndexOutOfBoundsException e) {                
            }
            System.out.println("Recommended for " + currentUser + ":");
            System.out.println("");
            for (Product movies : recommendedMovies) {
                System.out.println(movies);
            }
            System.out.println("");
        }
    }
}