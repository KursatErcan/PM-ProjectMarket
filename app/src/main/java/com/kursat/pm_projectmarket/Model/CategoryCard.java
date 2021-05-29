package com.kursat.pm_projectmarket.Model;

public class CategoryCard {

    private String categoryId;
    private String imageUrl;
    private String categoryName;

    public CategoryCard(){}

    public CategoryCard(String categoryId, String imageUrl, String categoryName) {
        this.categoryId = categoryId;
        this.imageUrl = imageUrl;
        this.categoryName = categoryName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


    @Override()
    public boolean equals(Object other) {
        // This is unavoidable, since equals() must accept an Object and not something more derived
        if (other instanceof CategoryCard) {
            // Note that I use equals() here too, otherwise, again, we will check for referential equality.
            // Using equals() here allows the Model class to implement it's own version of equality, rather than
            // us always checking for referential equality.
            CategoryCard categoryCard = (CategoryCard) other;
            return categoryCard.getCategoryId().equals(this.getCategoryId());
        }

        return false;
    }
}