package ecommerce.services;

import ecommerce.models.Dairy;
import ecommerce.models.Ebook;
import ecommerce.models.Electronic;
import ecommerce.models.Product;
import ecommerce.utils.NoEnoughProdcuts;
import ecommerce.utils.ProductNodeFound;
import ecommerce.utils.Trie;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;


public class Inventory {
    private final Trie store; // trie for searching and storing products
    public final Map<String, Product> products;
    public Inventory(){
        this.products = new HashMap<>();
        store = new Trie();

        // load the inventory here
        Dairy prod1 =  new Dairy("CHE201", "Feta", 45f, 50f, LocalDate.of(2025, 7, 7));
        Dairy prod2  = new Dairy("CHE202", "Cheddar",      50f, 60f, LocalDate.of(2023, 12, 1));  // expired
        Dairy prod3  = new Dairy("CHE203", "Gouda",        55f, 55f, LocalDate.of(2024, 11, 15)); // expired
        Dairy prod4  = new Dairy("CHE204", "Mozzarella",   60.32f, 45f, LocalDate.of(2025, 8, 20));
        Dairy prod5  = new Dairy("CHE205", "Parmesan",     65.4f, 40f, LocalDate.of(2025, 9, 5));
        Dairy prod6  = new Dairy("CHE206", "Brie",         58.7f, 48f, LocalDate.of(2022, 6, 15));  // expired
        Dairy prod7  = new Dairy("CHE207", "Camembert",    53f, 44f, LocalDate.of(2025, 7, 20));
        Dairy prod8  = new Dairy("CHE208", "Ricotta",      46.9f, 43f, LocalDate.of(2025, 6, 1));   // expired
        Dairy prod9  = new Dairy("CHE209", "Cream Cheese", 42.2f, 47f, LocalDate.of(2025, 10, 10));
        Dairy prod10 = new Dairy("CHE210", "Blue Cheese",  60.3f, 49f, LocalDate.of(2023, 1, 1));   // expired
        Dairy prod11 = new Dairy("CHE211", "Halloumi",     52.59f, 52f, LocalDate.of(2025, 11, 12));
        Dairy prod12 = new Dairy("CHE212", "Swiss",        59f, 41f, LocalDate.of(2024, 9, 30));  // expired
        Dairy prod13 = new Dairy("CHE213", "Provolone",    61f, 46f, LocalDate.of(2025, 12, 24));
        Dairy prod14 = new Dairy("CHE214", "Cottage",      40f, 38f, LocalDate.of(2024, 8, 10));  // expired
        Dairy prod15 = new Dairy("CHE215", "Mascarpone",   48f, 42f, LocalDate.of(2025, 7, 15));

        addToInventory(prod1, 2);
        addToInventory(prod2, 5);
        addToInventory(prod3, 3);
        addToInventory(prod4, 10);
        addToInventory(prod5, 6);
        addToInventory(prod6, 1);
        addToInventory(prod7, 8);
        addToInventory(prod8, 4);
        addToInventory(prod9, 7);
        addToInventory(prod10, 2);
        addToInventory(prod11, 9);
        addToInventory(prod12, 3);
        addToInventory(prod13, 5);
        addToInventory(prod14, 6);
        addToInventory(prod15, 4);


        // Ebooks
        Ebook ebook1 = new Ebook("BOK102", "Tale of Two Cities", 100.5f);
        Ebook ebook2 = new Ebook("BOK103", "1984", 85.0f);
        Ebook ebook3 = new Ebook("BOK104", "Pride and Prejudice", 95.25f);
        Ebook ebook4 = new Ebook("BOK105", "The Great Gatsby", 78.9f);
        Ebook ebook5 = new Ebook("BOK106", "Moby Dick", 110.0f);

        addToInventory(ebook1, 10);
        addToInventory(ebook2, 5);
        addToInventory(ebook3, 7);
        addToInventory(ebook4, 3);
        addToInventory(ebook5, 8);

        Electronic device1  = new Electronic("TV102", "LG TV Full HD",         3500.50f, 3999.99f);
        Electronic device2  = new Electronic("TV103", "Samsung Smart TV",      4200.00f, 4599.99f);
        Electronic device3  = new Electronic("PHN201", "iPhone 13 Pro",        28000.00f, 29999.99f);
        Electronic device4  = new Electronic("PHN202", "Samsung Galaxy S21",   25000.00f, 26999.99f);
        Electronic device5  = new Electronic("LPT301", "Dell XPS 13",          33000.00f, 34999.99f);
        Electronic device6  = new Electronic("LPT302", "MacBook Air M1",       36000.00f, 38999.99f);
        Electronic device7  = new Electronic("TAB401", "iPad 9th Gen",         15000.00f, 15999.99f);
        Electronic device8  = new Electronic("CAM501", "Canon DSLR 250D",      18000.00f, 19999.99f);
        Electronic device9  = new Electronic("WTC601", "Apple Watch SE",       10000.00f, 10999.99f);
        Electronic device10 = new Electronic("HDP701", "WD External HDD 1TB",   1200.00f, 1399.99f);

        addToInventory(device1, 3);
        addToInventory(device2, 4);
        addToInventory(device3, 2);
        addToInventory(device4, 5);
        addToInventory(device5, 1);
        addToInventory(device6, 3);
        addToInventory(device7, 6);
        addToInventory(device8, 2);
        addToInventory(device9, 4);
        addToInventory(device10, 7);
    }

    public Product findProduct(String productId){
        if (products.containsKey(productId)){
            return products.get(productId);
        }
        return null;
    }

    public void addToInventory(Product product, int quantity){
        store.insert(product, quantity);
        if (products.containsKey(product.getId())) return;
        products.put(product.getId(), product);
    }

    public void removeFromInventory(String productId, int quantity) throws NoEnoughProdcuts, ProductNodeFound {
        Product product = findProduct(productId);
        store.delete(product, quantity);
        if (store.amount(product) == 0) products.remove(product.getId());
    }

    public Map<Product, Integer> getAllProducts() {
        try {
            return store.startWith("");
        } catch (ProductNodeFound e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Map<Product, Integer> searchProducts(String prefix){
        try {
            return store.startWith(prefix);
        } catch (ProductNodeFound e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Integer checkAmount(String productId){
        Product product = this.findProduct(productId);
        if (product == null){
            return 0;
        }
        return this.store.amount(product);
    }
}
