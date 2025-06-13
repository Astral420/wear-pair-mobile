package com.example.wearandpair;
import android.os.Parcel;
import android.os.Parcelable;

// 1. Implement Parcelable
public class Product implements Parcelable {
    private final int id;
    private final String name;
    private final String imageLink;
    private final int drawableResId;
    private final String buyLink;
    private final String retailerLink;
    private final String category;
    private final boolean isSummerCollection;
    private final boolean isPopular;
    private final String prodDescription;

    // Constructor for loading from URL
    public Product(int id, String name, String imageLink, String buyLink, String retailerLink, String category, boolean isSummerCollection, boolean isPopular, String prodDescription) {
        this.id = id;
        this.name = name;
        this.imageLink = imageLink;
        this.drawableResId = 0;
        this.buyLink = buyLink;
        this.retailerLink = retailerLink;
        this.category = category;
        this.isSummerCollection = isSummerCollection;
        this.isPopular = isPopular;
        this.prodDescription = prodDescription;
    }

    // Constructor for loading from drawable
    public Product(int id, String name, int drawableResId, String buyLink, String retailerLink, String category, boolean isSummerCollection, boolean isPopular, String prodDescription) {
        this.id = id;
        this.name = name;
        this.imageLink = null;
        this.drawableResId = drawableResId;
        this.buyLink = buyLink;
        this.retailerLink = retailerLink;
        this.category = category;
        this.isSummerCollection = isSummerCollection;
        this.isPopular = isPopular;
        this.prodDescription = prodDescription;

    }

    protected Product(Parcel in) {
        id = in.readInt();
        name = in.readString();
        imageLink = in.readString();
        drawableResId = in.readInt();
        buyLink = in.readString();
        retailerLink = in.readString();
        category = in.readString();
        isSummerCollection = in.readByte() != 0;
        isPopular = in.readByte() != 0;
        prodDescription = in.readString();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(imageLink);
        dest.writeInt(drawableResId);
        dest.writeString(buyLink);
        dest.writeString(retailerLink);
        dest.writeString(category);
        dest.writeByte((byte) (isSummerCollection ? 1 : 0));
        dest.writeByte((byte) (isPopular ? 1 : 0));
        dest.writeString(prodDescription);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };



    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getImageLink() { return imageLink; }
    public int getDrawableResId() { return drawableResId; }
    public String getBuyLink() { return buyLink; }
    public String getRetailerLink() { return retailerLink; }
    public String getCategory() { return category; }
    public boolean isSummerCollection() { return isSummerCollection; }
    public boolean isPopular() { return isPopular; }
    public String getProdDescription() { return prodDescription; }
}