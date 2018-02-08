package com.example.erdenebulgans.soh_appuser;

/**
 * Created by Erdenebulgan.S on 1/17/2018.
 */

public class RecyclerItem {

    private String title;
    private String description;

    public RecyclerItem(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
