package com.example.wearandpair;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class WishlistFragment extends Fragment implements WishlistAdapter.OnItemRemovedListener {

    private RecyclerView wishlistRecyclerView;
    private WishlistAdapter adapter;
    private WishlistManager wishlistManager;
    private List<Object> wishlistItems = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wishlistManager = WishlistManager.getInstance(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);

        wishlistRecyclerView = view.findViewById(R.id.wishlist_recycler_view);
        wishlistRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadWishlistItems();

        return view;
    }

    private void loadWishlistItems() {
        wishlistItems.clear();
        // TODO: Update WishlistManager to get outfits as well
        wishlistItems.addAll(wishlistManager.getProducts());
        wishlistItems.addAll(wishlistManager.getOutfits());

        adapter = new WishlistAdapter(getContext(), wishlistItems, this);
        wishlistRecyclerView.setAdapter(adapter);

        if (wishlistItems.isEmpty()) {
            Toast.makeText(getContext(), "Your wishlist is empty.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onProductRemoved(Product product) {
        // TODO: Update WishlistManager to include a removeProduct method
        wishlistManager.removeProduct(product);
        int position = wishlistItems.indexOf(product);
        if (position != -1) {
            wishlistItems.remove(position);
            adapter.notifyItemRemoved(position);
            Toast.makeText(getContext(), product.getName() + " removed from wishlist.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onOutfitRemoved(Outfit outfit) {
        // TODO: Update WishlistManager to include a removeOutfit method
        wishlistManager.removeOutfit(outfit);
        int position = wishlistItems.indexOf(outfit);
        if (position != -1) {
            wishlistItems.remove(position);
            adapter.notifyItemRemoved(position);
            Toast.makeText(getContext(), outfit.getName() + " removed from wishlist.", Toast.LENGTH_SHORT).show();
        }
    }
}