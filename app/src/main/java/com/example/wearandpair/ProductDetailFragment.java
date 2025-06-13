package com.example.wearandpair;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;

public class ProductDetailFragment extends Fragment {

    private static final String ARG_PRODUCT = "product";
    private Product product;

    public ProductDetailFragment() {
        // Required empty public constructor
    }

    // Use this factory method to create a new instance with a product
    public static ProductDetailFragment newInstance(Product product) {
        ProductDetailFragment fragment = new ProductDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PRODUCT, product);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            product = getArguments().getParcelable(ARG_PRODUCT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (product != null) {
            ImageView productImage = view.findViewById(R.id.detail_product_image);
            TextView productName = view.findViewById(R.id.detail_product_name);
            TextView productRetailer = view.findViewById(R.id.detail_product_retailer);

            TextView productDescription = view.findViewById(R.id.detail_product_description);
            MaterialButton buyNowButton = view.findViewById(R.id.detail_buy_now_button);
            MaterialButton wishlistButton = view.findViewById(R.id.add_to_wishlist_button);

            // Populate the views
            productName.setText(product.getName());
            productRetailer.setText("From " + product.getRetailerLink());

            productDescription.setText(product.getProdDescription());

            if (product.getImageLink() != null) {
                Glide.with(this).load(product.getImageLink()).into(productImage);
            } else {
                Glide.with(this).load(product.getDrawableResId()).into(productImage);
            }

            // Set click listeners
            buyNowButton.setOnClickListener(v -> {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(product.getBuyLink()));
                startActivity(browserIntent);
            });

            wishlistButton.setOnClickListener(v -> {
                // Placeholder for wishlist functionality
                WishlistManager.getInstance(getContext()).addProduct(product);
                Toast.makeText(getContext(), product.getName() + " added to wishlist!", Toast.LENGTH_SHORT).show();
            });
        }
    }
}