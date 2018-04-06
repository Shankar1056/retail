package com.bigappcompany.retailtest.model;

/**
 * Created by shankar on 6/4/18.
 */

public class UserDetailsModel {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getReputation() {
        return reputation;
    }

    public void setReputation(String reputation) {
        this.reputation = reputation;
    }

    private String name, image, reputation, paginationCount;

    public String getPaginationCount() {
        return paginationCount;
    }

    public void setPaginationCount(String paginationCount) {
        this.paginationCount = paginationCount;
    }

    public UserDetailsModel(String name, String image, String reputation, String paginationCount){
        this.name = name;
        this.image = image;
        this.reputation = reputation;
        this.paginationCount = paginationCount;
    }
}
