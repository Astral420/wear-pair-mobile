package com.example.wearandpair;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider; // Import this
import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import java.util.ArrayList;
import java.util.Arrays;

public class OutfitBuilder extends Fragment {

    // --- The ViewModel will now hold all the data ---
    private OutfitBuilderViewModel viewModel;

    // We still need references to the views
    private ImageView imageTop, imageBottom, imageShoes, imageAccessory;
    private String currentCategorySelection;

    public OutfitBuilder() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // --- Initialize the ViewModel ---
        // The ViewModelProvider ensures we get the same ViewModel instance even
        // if the fragment is re-created.
        viewModel = new ViewModelProvider(this).get(OutfitBuilderViewModel.class);

        // The result listener now updates the ViewModel, not a local variable
        getParentFragmentManager().setFragmentResultListener("product_selection_request", this, (requestKey, bundle) -> {
            Product selectedProduct = bundle.getParcelable("selected_product");
            if (selectedProduct != null) {
                updateProductForCategory(currentCategorySelection, selectedProduct);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_outfit_builder, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeViews(view);
        setupClickListeners(view);

        // --- NEW: OBSERVE THE VIEWMODEL FOR CHANGES ---
        // This code sets up observers that will automatically update the
        // ImageViews whenever the data in the ViewModel changes.
        // This runs when the view is first created and every time you come back to it.
        viewModelObservers();
    }

    private void viewModelObservers(){
        viewModel.getTop().observe(getViewLifecycleOwner(), product -> updateImageView(imageTop, product));
        viewModel.getBottom().observe(getViewLifecycleOwner(), product -> updateImageView(imageBottom, product));
        viewModel.getShoes().observe(getViewLifecycleOwner(), product -> updateImageView(imageShoes, product));
        viewModel.getAccessory().observe(getViewLifecycleOwner(), product -> updateImageView(imageAccessory, product));
    }

    private void initializeViews(@NonNull View view) {
        imageTop = view.findViewById(R.id.image_top);
        imageBottom = view.findViewById(R.id.image_bottom);
        imageShoes = view.findViewById(R.id.image_shoes);
        imageAccessory = view.findViewById(R.id.image_accessory);
    }

    // updateProductForCategory now calls the ViewModel's setters
    private void updateProductForCategory(String category, Product product) {
        if (category == null) return;

        switch (category) {
            case "Top":
                viewModel.setTop(product);
                break;
            case "Bottom":
                viewModel.setBottom(product);
                break;
            case "Shoes":
                viewModel.setShoes(product);
                break;
            case "Accessory":
                viewModel.setAccessory(product);
                break;
        }
    }

    // The saveOutfit method now gets the latest data directly from the ViewModel
    private void saveOutfit(String name, String description) {
        // Get the current products from the ViewModel's LiveData
        Product currentTop = viewModel.getTop().getValue();
        Product currentBottom = viewModel.getBottom().getValue();
        Product currentShoes = viewModel.getShoes().getValue();
        Product currentAccessory = viewModel.getAccessory().getValue();

        if (name.trim().isEmpty()) {
            Toast.makeText(getContext(), "Please enter an outfit name.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (currentTop == null || currentBottom == null || currentShoes == null) {
            Toast.makeText(getContext(), "Please select a Top, Bottom, and Shoes.", Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<Product> items = new ArrayList<>();
        items.add(currentTop);
        items.add(currentBottom);
        items.add(currentShoes);
        if (currentAccessory != null) {
            items.add(currentAccessory);
        }

        Outfit newOutfit = new Outfit(name, description, items);


        WishlistManager.getInstance(getContext()).addOutfit(newOutfit);
        Toast.makeText(getContext(), "Outfit '" + name + "' saved to wishlist!", Toast.LENGTH_SHORT).show();
    }


    // Helper method remains the same
    private void updateImageView(ImageView imageView, Product product) {
        if (imageView == null || getContext() == null) return;

        // If the product is null, reset to the 'add' icon
        if (product == null) {
            imageView.setImageResource(R.drawable.ic_add_photo);
            imageView.setImageTintList(getResources().getColorStateList(R.color.brown, null));
        } else {
            imageView.setImageTintList(null); // Clear tint
            Glide.with(getContext()).load(product.getDrawableResId()).into(imageView);
        }
    }

    // All navigation methods remain unchanged
    private void setupClickListeners(@NonNull View view) {
        MaterialCardView cardTop = view.findViewById(R.id.card_top);
        MaterialCardView cardBottom = view.findViewById(R.id.card_bottom);
        MaterialCardView cardShoes = view.findViewById(R.id.card_shoes);
        MaterialCardView cardAccessory = view.findViewById(R.id.card_accessory);
        EditText outfitNameInput = view.findViewById(R.id.outfit_name_input);
        EditText outfitDescriptionInput = view.findViewById(R.id.outfit_description_input);
        Button saveOutfitButton = view.findViewById(R.id.save_outfit_button);

        cardTop.setOnClickListener(v -> navigateToTopSelection());
        cardBottom.setOnClickListener(v -> navigateToSingleCategorySelection("Bottom", "Lowers"));
        cardShoes.setOnClickListener(v -> navigateToSingleCategorySelection("Shoes", "Shoes"));
        cardAccessory.setOnClickListener(v -> navigateToSingleCategorySelection("Accessory", "Accessories"));

        saveOutfitButton.setOnClickListener(v -> saveOutfit(
                outfitNameInput.getText().toString(),
                outfitDescriptionInput.getText().toString()
        ));
    }
    private void navigateToTopSelection() { /* ... unchanged ... */
        this.currentCategorySelection = "Top";
        ArrayList<String> topCategories = new ArrayList<>(Arrays.asList(
                "Shirts", "Dresses", "Blazers", "Hoodies & Jackets", "Swimwear"
        ));
        ProductsFragment productsFragment = new ProductsFragment();
        Bundle args = new Bundle();
        args.putBoolean("is_selection_mode", true);
        args.putStringArrayList("allowed_categories", topCategories);
        productsFragment.setArguments(args);
        navigateToFragment(productsFragment);
    }
    private void navigateToSingleCategorySelection(String selectionType, String filterCategory) { /* ... unchanged ... */
        this.currentCategorySelection = selectionType;
        ProductsFragment productsFragment = new ProductsFragment();
        Bundle args = new Bundle();
        args.putBoolean("is_selection_mode", true);
        args.putString("filter_type", filterCategory);
        productsFragment.setArguments(args);
        navigateToFragment(productsFragment);
    }
    private void navigateToFragment(Fragment fragment) { /* ... unchanged ... */
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}