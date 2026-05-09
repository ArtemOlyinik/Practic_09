package ua.store.model;

import java.util.Objects;

public final class Product {

    private final int id;
    private final String name;
    private final double price;
    private final int categoryId;
    private final Category category;

    private Product(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.price = builder.price;
        this.categoryId = builder.categoryId;
        this.category = builder.category;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public Category getCategory() {
        return category;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(Product product) {
        return new Builder()
                .id(product.id)
                .name(product.name)
                .price(product.price)
                .categoryId(product.categoryId)
                .category(product.category);
    }

    public static final class Builder implements ProductBuilder {
        private int id;
        private String name;
        private double price;
        private int categoryId;
        private Category category;

        @Override
        public Builder id(int id) {
            this.id = id;
            return this;
        }

        @Override
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        @Override
        public Builder price(double price) {
            this.price = price;
            return this;
        }

        @Override
        public Builder categoryId(int categoryId) {
            this.categoryId = categoryId;
            return this;
        }

        @Override
        public Builder category(Category category) {
            this.category = category;
            return this;
        }

        @Override
        public Product build() {
            return new Product(this);
        }
    }

    public interface ProductBuilder {
        ProductBuilder id(int id);
        ProductBuilder name(String name);
        ProductBuilder price(double price);
        ProductBuilder categoryId(int categoryId);
        ProductBuilder category(Category category);
        Product build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", categoryId=" + categoryId +
                '}';
    }
}
