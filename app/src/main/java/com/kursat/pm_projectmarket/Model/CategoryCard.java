package com.kursat.pm_projectmarket.Model;

public class CategoryCard {

    private String categoryId;
    private String imageUrl;
    private String categoryName;
    private String categoryName_tr;

    public CategoryCard(){}

    public CategoryCard(String categoryId, String imageUrl, String categoryName, String categoryName_tr) {
        this.categoryId = categoryId;
        this.imageUrl = imageUrl;
        this.categoryName = categoryName;
        this.categoryName_tr = categoryName_tr;
    }

    public String getCategoryName_tr() { return categoryName_tr; }

    public void setCategoryName_tr(String categoryName_tr) { this.categoryName_tr = categoryName_tr; }

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