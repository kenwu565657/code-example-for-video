package org.example;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter your product price: ");
            String productPriceString = scanner.nextLine();
            double productPrice = Double.parseDouble(productPriceString);

            System.out.print("Enter your product have discount or not(y only for yes): ");
            String haveDiscountString = scanner.nextLine();
            boolean haveDiscount = "y".equals(haveDiscountString);

            Product product = new Product(productPrice, haveDiscount);
            var productDiscountIdentifier = new ProductDiscountIdentifier();
            var discount = productDiscountIdentifier.identify(product);
            String messageTemplate = "Your product have a discount of %.0f percent. \n";
            System.out.printf(messageTemplate, discount);
        } catch (NumberFormatException e) {
            System.out.print("Invalid number input!");
        }
    }
}
