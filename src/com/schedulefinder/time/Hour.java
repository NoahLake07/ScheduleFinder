package com.schedulefinder.time;

public class Hour {

    private boolean isAvailable;

    public Hour(boolean isAvailable){
        this.isAvailable = isAvailable;
    }

    public Hour(){
        this.isAvailable = false;
    }

    public void setAvailable(boolean status){
        this.isAvailable = status;
    }

    public boolean isAvailable(){
        return isAvailable;
    }

}
