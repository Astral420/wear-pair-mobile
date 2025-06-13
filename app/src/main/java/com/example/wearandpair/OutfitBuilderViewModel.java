package com.example.wearandpair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class OutfitBuilderViewModel extends ViewModel {

    // Use MutableLiveData to hold the selected products.
    // This allows the UI to observe changes and update automatically.
    private final MutableLiveData<Product> top = new MutableLiveData<>();
    private final MutableLiveData<Product> bottom = new MutableLiveData<>();
    private final MutableLiveData<Product> shoes = new MutableLiveData<>();
    private final MutableLiveData<Product> accessory = new MutableLiveData<>();

    // Public setters that will be called from the Fragment
    public void setTop(Product product) {
        top.setValue(product);
    }

    public void setBottom(Product product) {
        bottom.setValue(product);
    }

    public void setShoes(Product product) {
        shoes.setValue(product);
    }

    public void setAccessory(Product product) {
        accessory.setValue(product);
    }

    // Public getters that the Fragment will observe
    public LiveData<Product> getTop() {
        return top;
    }

    public LiveData<Product> getBottom() {
        return bottom;
    }

    public LiveData<Product> getShoes() {
        return shoes;
    }

    public LiveData<Product> getAccessory() {
        return accessory;
    }
}