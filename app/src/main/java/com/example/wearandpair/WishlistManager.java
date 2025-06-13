package com.example.wearandpair;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class WishlistManager {

    private static WishlistManager instance;
    private final SharedPreferences prefs;
    private final Gson gson;

    private static final String PREF_NAME_PREFIX = "Wishlist_";
    private static final String KEY_PRODUCTS = "key_products";
    private static final String KEY_OUTFITS = "key_outfits";


    private WishlistManager(Context context) {
        SessionManager sessionManager = new SessionManager(context);
        String userEmail = sessionManager.getUserEmail();
        String prefName = PREF_NAME_PREFIX + (userEmail != null ? userEmail : "GUEST");

        prefs = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public static synchronized WishlistManager getInstance(Context context) {
        if (instance == null) {
            instance = new WishlistManager(context.getApplicationContext());
        }
        return instance;
    }

    /**
     * --- NEW METHOD ---
     * Resets the singleton instance. This should be called on user logout.
     */
    public static void resetInstance() {
        instance = null;
    }

    // --- Product Methods ---

    public void addProduct(Product product) {
        List<Product> products = getProducts();
        if (!products.stream().anyMatch(p -> p.getId() == product.getId())) {
            products.add(product);
            saveProducts(products);
        }
    }

    public void removeProduct(Product product) {
        List<Product> products = getProducts();
        products.removeIf(p -> p.getId() == product.getId());
        saveProducts(products);
    }

    public List<Product> getProducts() {
        String json = prefs.getString(KEY_PRODUCTS, null);
        if (json == null) {
            return new ArrayList<>();
        }
        Type type = new TypeToken<ArrayList<Product>>() {}.getType();
        return gson.fromJson(json, type);
    }

    private void saveProducts(List<Product> products) {
        String json = gson.toJson(products);
        prefs.edit().putString(KEY_PRODUCTS, json).apply();
    }

    // --- Outfit Methods ---
    public void addOutfit(Outfit outfit) {
        List<Outfit> outfits = getOutfits();
        if (!outfits.stream().anyMatch(o -> o.getId().equals(outfit.getId()))) {
            outfits.add(outfit);
            saveOutfits(outfits);
        }
    }

    public void removeOutfit(Outfit outfit) {
        List<Outfit> outfits = getOutfits();
        outfits.removeIf(o -> o.getId().equals(outfit.getId()));
        saveOutfits(outfits);
    }

    public List<Outfit> getOutfits() {
        String json = prefs.getString(KEY_OUTFITS, null);
        if (json == null) {
            return new ArrayList<>();
        }
        Type type = new TypeToken<ArrayList<Outfit>>() {}.getType();
        return gson.fromJson(json, type);
    }

    private void saveOutfits(List<Outfit> outfits) {
        String json = gson.toJson(outfits);
        prefs.edit().putString(KEY_OUTFITS, json).apply();
    }

}