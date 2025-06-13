package com.example.wearandpair;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView; // Import TextView
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class Home extends Fragment {

    public interface OnHomeNavigationListener {
        void onNavigateToProducts();
    }

    private OnHomeNavigationListener navigationListener;
    private SessionManager sessionManager; // Add SessionManager

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnHomeNavigationListener) {
            navigationListener = (OnHomeNavigationListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnHomeNavigationListener");
        }
        // Initialize SessionManager here
        sessionManager = new SessionManager(context);
    }

    public Home() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // --- FIX: Update the welcome message ---
        TextView usernameTextView = view.findViewById(R.id.username);
        String username = sessionManager.getUsername();
        // Capitalize the first letter for a nice format
        String formattedUsername = username.substring(0, 1).toUpperCase() + username.substring(1);
        usernameTextView.setText("Welcome, " + formattedUsername + "!");

        MaterialButton shopNowButton = view.findViewById(R.id.shopNowButton);
        shopNowButton.setOnClickListener(v -> navigateToProducts("summer"));

        MaterialButton viewAllPopularButton = view.findViewById(R.id.viewAllButton);
        viewAllPopularButton.setOnClickListener(v -> navigateToProducts("popular"));

        MaterialButton wishlistButton = view.findViewById(R.id.wishlistButton);
        wishlistButton.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new WishlistFragment())
                    .addToBackStack(null)
                    .commit();
        });

        setupCategoryClickListeners(view);
    }

    private void setupCategoryClickListeners(View view) {
        // This method remains the same
        MaterialCardView categoryTShirts = view.findViewById(R.id.categoryTShirts);
        categoryTShirts.setOnClickListener(v -> navigateToProducts("Shirts"));
        MaterialCardView categoryJeans = view.findViewById(R.id.categoryJeans);
        categoryJeans.setOnClickListener(v -> navigateToProducts("Lowers"));
        MaterialCardView categoryDresses = view.findViewById(R.id.categoryDresses);
        categoryDresses.setOnClickListener(v -> navigateToProducts("Dresses"));
        MaterialCardView categoryHoodies = view.findViewById(R.id.categoryHoodies);
        categoryHoodies.setOnClickListener(v -> navigateToProducts("Hoodies & Jackets"));
        MaterialCardView categoryShoes = view.findViewById(R.id.categoryShoes);
        categoryShoes.setOnClickListener(v -> navigateToProducts("Shoes"));
        MaterialCardView categoryBlazers = view.findViewById(R.id.categoryBlazers);
        categoryBlazers.setOnClickListener(v -> navigateToProducts("Blazers"));
        MaterialCardView categorySwimwear = view.findViewById(R.id.categorySwimwear);
        categorySwimwear.setOnClickListener(v -> navigateToProducts("Swimwear"));
        MaterialCardView categoryAccessories = view.findViewById(R.id.categoryAccessories);
        categoryAccessories.setOnClickListener(v -> navigateToProducts("Accessories"));
    }

    private void navigateToProducts(String filterType) {
        // This method remains the same
        ProductsFragment productsFragment = new ProductsFragment();
        Bundle args = new Bundle();
        args.putString("filter_type", filterType);
        productsFragment.setArguments(args);

        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, productsFragment)
                .addToBackStack(null)
                .commit();

        if (navigationListener != null) {
            navigationListener.onNavigateToProducts();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        navigationListener = null;
    }
}