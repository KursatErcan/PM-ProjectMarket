package com.kursat.pm_projectmarket.Model;

public class CategoryCard {

    private int Id;
    private String imageUrl;
    private String categoryName;

    public CategoryCard(){}

    public CategoryCard(int id, String imageUrl, String categoryName) {
        Id = id;
        this.imageUrl = imageUrl;
        this.categoryName = categoryName;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
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
            return categoryCard.getId()==(this.getId());
        }

        return false;
    }
}



/*
public class CategoryType {
    private String categoryName;
    private String categoryImageUrl;

    public CategoryType(String category, String categoryImageUrl) {
        this.categoryName = category;
        this.categoryImageUrl = categoryImageUrl;
    }
    public CategoryType(){}
    public String getCategoryName() {
        return categoryName;
    }

    public CategoryType setCategoryName(String category) {
        this.categoryName = category;
        return this;
    }

    public String getCategoryImageUrl() {
        return categoryImageUrl;
    }

    public CategoryType setCategoryImageUrl(String categoryImageUrl) {
        this.categoryImageUrl = categoryImageUrl;
        return this;
    }
}
*/