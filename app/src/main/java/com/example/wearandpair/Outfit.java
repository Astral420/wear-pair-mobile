package com.example.wearandpair;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;
import java.util.UUID;

public class Outfit implements Parcelable {
    private final String id;
    private final String name;
    private final String description;
    private final List<Product> items; // The list of products in the outfit

    public Outfit(String name, String description, List<Product> items) {
        this.id = "outfit_" + UUID.randomUUID().toString(); // Generate a unique ID
        this.name = name;
        this.description = description;
        this.items = items;
    }

    protected Outfit(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        items = in.createTypedArrayList(Product.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeTypedList(items);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Outfit> CREATOR = new Creator<Outfit>() {
        @Override
        public Outfit createFromParcel(Parcel in) {
            return new Outfit(in);
        }

        @Override
        public Outfit[] newArray(int size) {
            return new Outfit[size];
        }
    };

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Product> getItems() {
        return items;
    }
}