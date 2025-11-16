package main;

import model.Product;
import java.util.ArrayList;
import java.util.List;

public class Main {
    
    public static void main(String[] args) {
        List<Product> products = createProductsArray();
                printAllProducts(products);
        System.out.println();
        
        List<Product> productsByName = getProductsByName(products, "Молоко");
        printProducts(productsByName);
        System.out.println();
        List<Product> productsByNameAndPrice = getProductsByNameAndMaxPrice(products, "Хліб", 30.00);
        printProducts(productsByNameAndPrice);
        System.out.println();
        
        System.out.println("=== ЗАВДАННЯ В: Товари з терміном зберігання > 10 днів ===");
        List<Product> productsByShelfLife = getProductsByMinShelfLife(products, 10);
        printProducts(productsByShelfLife);
    }
    

    public static List<Product> createProductsArray() {
        List<Product> products = new ArrayList<>();
        
        products.add(new Product(1, "Молоко", "Гадячсир", 45.50, 5, 100));
        products.add(new Product(2, "Хліб", "Хлібзавод №1", 25.00, 3, 50));
        products.add(new Product(3, "Молоко", "Ферма", 55.00, 7, 80));
        products.add(new Product(4, "Сир", "Молочний край", 120.00, 14, 30));
        products.add(new Product(5, "Хліб", "Домашній", 28.50, 2, 40));
        products.add(new Product(6, "Масло", "Гадячсир", 65.00, 21, 60));
        products.add(new Product(7, "Йогурт", "Ферма", 32.00, 10, 70));
        products.add(new Product(8, "Хліб", "Батьківський", 35.00, 4, 45));
        products.add(new Product(9, "Ковбаса", "Мясокомбінат", 150.00, 30, 25));
        products.add(new Product(10, "Молоко", "Селянське", 40.00, 6, 90));
        
        return products;
    }
    

    public static void printAllProducts(List<Product> products) {
        for (Product product : products) {
            System.out.println(product);
        }
    }
    public static void printProducts(List<Product> products) {
        if (products.isEmpty()) {
            System.out.println("Товари не знайдено.");
        } else {
            for (Product product : products) {
                System.out.println(product);
            }
        }
    }
    public static List<Product> getProductsByName(List<Product> products, String name) {
        List<Product> result = new ArrayList<>();
        for (Product product : products) {
            if (product.getName().equalsIgnoreCase(name)) {
                result.add(product);
            }
        }
        return result;
    }
    
    public static List<Product> getProductsByNameAndMaxPrice(List<Product> products, String name, double maxPrice) {
        List<Product> result = new ArrayList<>();
        for (Product product : products) {
            if (product.getName().equalsIgnoreCase(name) && product.getPrice() <= maxPrice) {
                result.add(product);
            }
        }
        return result;
    }
    
    public static List<Product> getProductsByMinShelfLife(List<Product> products, int minShelfLife) {
        List<Product> result = new ArrayList<>();
        for (Product product : products) {
            if (product.getShelfLife() > minShelfLife) {
                result.add(product);
            }
        }
        return result;
    }
}
