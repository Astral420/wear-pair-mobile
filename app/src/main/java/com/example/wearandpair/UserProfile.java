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
import android.widget.Button;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserProfile extends Fragment {

    private SessionManager sessionManager;
    private WishlistManager wishlistManager;
    private TextView userEmailText;
    private MaterialButton logoutButton;
    private RecyclerView profileWishlistRecyclerView;
    private ProfileWishlistAdapter adapter;

    public UserProfile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize managers
        sessionManager = new SessionManager(requireContext());
        wishlistManager = WishlistManager.getInstance(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Find views
        userEmailText = view.findViewById(R.id.user_email_text);
        logoutButton = view.findViewById(R.id.logout_button);
        profileWishlistRecyclerView = view.findViewById(R.id.profile_wishlist_recycler_view);

        // Set user email
        userEmailText.setText(sessionManager.getUserEmail());

        // Set up the logout button
        logoutButton.setOnClickListener(v -> {
            sessionManager.logoutUser();
        });

        // Set up RecyclerView
        setupWishlistPreview();
    }

    private void setupWishlistPreview() {
        profileWishlistRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Get all products and outfits
        List<Object> allItems = new ArrayList<>();
        allItems.addAll(wishlistManager.getProducts());
        allItems.addAll(wishlistManager.getOutfits());

        // For simplicity, we'll just show the first 3 items.
        // For a more robust solution, you would sort by a timestamp.
        List<Object> previewItems = new ArrayList<>();
        for(int i = 0; i < allItems.size() && i < 3; i++) {
            previewItems.add(allItems.get(i));
        }

        // Set up the adapter
        adapter = new ProfileWishlistAdapter(getContext(), previewItems);
        profileWishlistRecyclerView.setAdapter(adapter);
    }
}