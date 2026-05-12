package viva2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class InventoryManager {
    private final ArrayList<Product> items = new ArrayList<>();

    public void loadFromFile(String filename) {
        items.clear();
        File file = new File(filename);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }

                // Expected: ID,Name,Price,Stock
                String[] parts = line.split(",", 4);
                if (parts.length != 4) {
                    continue;
                }

                try {
                    int id = Integer.parseInt(parts[0].trim());
                    String name = parts[1].trim();
                    double price = Double.parseDouble(parts[2].trim());
                    int stock = Integer.parseInt(parts[3].trim());

                    // Ignore duplicates (keep first occurrence)
                    if (searchById(id) == null) {
                        items.add(new Product(id, name, price, stock));
                    }
                } catch (NumberFormatException ignored) {
                    // Skip invalid line
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading inventory file: " + e.getMessage());
        }
    }

    public void saveToFile(String filename) {
        File file = new File(filename);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            for (Product p : items) {
                writer.write(p.getId() + "," + p.getName() + "," + p.getPrice() + "," + p.getStock());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing inventory file: " + e.getMessage());
        }
    }

    public boolean addProduct(Product p) {
        if (p == null) {
            return false;
        }
        if (searchById(p.getId()) != null) {
            return false;
        }
        items.add(p);
        return true;
    }

    public boolean removeProduct(int id) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId() == id) {
                items.remove(i);
                return true;
            }
        }
        return false;
    }

    public Product searchById(int id) {
        for (Product p : items) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    public Product getProductById(int id) {
        return searchById(id);
    }

    public ArrayList<Product> searchByName(String nameQuery) {
        ArrayList<Product> result = new ArrayList<>();
        if (nameQuery == null) {
            return result;
        }
        String q = nameQuery.trim().toLowerCase();
        if (q.isEmpty()) {
            return result;
        }
        for (Product p : items) {
            String n = p.getName() == null ? "" : p.getName().toLowerCase();
            if (n.contains(q)) {
                result.add(p);
            }
        }
        return result;
    }

    public boolean updateStock(int id, int newStock) {
        Product p = searchById(id);
        if (p == null) {
            return false;
        }
        if (newStock < 0) {
            return false;
        }
        p.setStock(newStock);
        return true;
    }

    // PDF spec name
    public boolean isAvailable(int id, int requestedQty) {
        if (requestedQty <= 0) {
            return false;
        }
        Product p = searchById(id);
        return p != null && p.getStock() >= requestedQty;
    }

    // Backwards-compatible alias (if you called it elsewhere)
    public boolean isavailable(int id, int quantity) {
        return isAvailable(id, quantity);
    }

    public void displayAll() {
        if (items.isEmpty()) {
            System.out.println("Inventory is empty.");
            return;
        }

        System.out.println("\n=== Inventory ===");
        System.out.printf("%-8s %-25s %-10s %-10s%n", "ID", "Name", "Price", "Stock");
        System.out.println("-----------------------------------------------------------");
        for (Product p : items) {
            System.out.printf("%-8d %-25s %-10.2f %-10d%n", p.getId(), p.getName(), p.getPrice(), p.getStock());
        }
    }

    public int size() {
        return items.size();
    }
}