package ua.store.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class Category {

    private final int id;
    private final String name;
    private final String description;
    private final List<Product> products;

    private Category(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.description = builder.description;
        this.products = builder.products != null
            ? Collections.unmodifiableList(new ArrayList<>(builder.products))
            : Collections.emptyList();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Product> getProducts() {
        return products;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(Category category) {
        return new Builder()
                .id(category.id)
                .name(category.name)
                .description(category.description)
                .withProducts(category.products);
    }

    public static final class Builder implements CategoryBuilder {
        private int id;
        private String name;
        private String description;
        private List<Product> products;

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
        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder withProducts(List<Product> products) {
            this.products = products;
            return this;
        }

        @Override
        public Category build() {
            return new Category(this);
        }
    }

    public interface CategoryBuilder {
        CategoryBuilder id(int id);
        CategoryBuilder name(String name);
        CategoryBuilder description(String description);
        CategoryBuilder withProducts(List<Product> products);
        Category build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return id == category.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", productsCount=" + products.size() +
                '}';
    }
}
