package org.example;

import java.util.Optional;

public class ProductionDiscountIdentifier {
    /*
        this function return the discount % of a product.
        it means x % off if the return value is Optional of 10f.
        it means no any discount if the return value is Optional of 0f.
        it returns empty Optional if the input is null for safety.
     */
    public Optional<Float> safeIdentify(Product product) {
        if (null == product) {
            return Optional.empty();
        }

        float discount = 0f;
        if (product.haveDiscount()) {
            if (product.price() < 100) {
                discount = 10f;
            }
            else if (product.price() < 200) {
                discount = 20f;
            }
            else if (product.price() < 300) {
                discount = 40f;
            } else {
                discount = 50f;
            }
        }

        return Optional.of(discount);
    }

    public float identify(Product product) throws EmptyProductException {
        if (null == product) {
            throw new EmptyProductException();
        }

        float discount = 0f;
        if (product.haveDiscount()) {
            if (product.price() < 100) {
                discount = 10f;
            }
            else if (product.price() < 200) {
                discount = 20f;
            }
            else if (product.price() < 300) {
                discount = 40f;
            } else {
                discount = 50f;
            }
        }

        return discount;
    }
}
