package com.example.wearandpair;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_PRODUCT = 0;
    private static final int TYPE_OUTFIT = 1;

    private final Context context;
    private final List<Object> items;
    private final OnItemRemovedListener listener;

    public interface OnItemRemovedListener {
        void onProductRemoved(Product product);

        void onOutfitRemoved(Outfit outfit);
    }

    public WishlistAdapter(Context context, List<Object> items, OnItemRemovedListener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof Product) {
            return TYPE_PRODUCT;
        } else {
            return TYPE_OUTFIT;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_PRODUCT) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_wishlist_product, parent, false);
            return new ProductViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_wishlist_outfit, parent, false);
            return new OutfitViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_PRODUCT) {
            ((ProductViewHolder) holder).bind((Product) items.get(position));
        } else {
            ((OutfitViewHolder) holder).bind((Outfit) items.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // ViewHolder for Products
    class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productRetailer;
        Button buyNowButton, removeButton;

        ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productRetailer = itemView.findViewById(R.id.product_retailer);
            buyNowButton = itemView.findViewById(R.id.buy_now_button);
            removeButton = itemView.findViewById(R.id.remove_button);
        }

        void bind(final Product product) {
            productName.setText(product.getName());
            productRetailer.setText("From " + product.getRetailerLink());

            if (product.getImageLink() != null) {
                Glide.with(context).load(product.getImageLink()).into(productImage);
            } else {
                Glide.with(context).load(product.getDrawableResId()).into(productImage);
            }

            buyNowButton.setOnClickListener(v -> {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(product.getBuyLink()));
                context.startActivity(browserIntent);
            });

            removeButton.setOnClickListener(v -> listener.onProductRemoved(product));
        }
    }

    // ViewHolder for Outfits
    class OutfitViewHolder extends RecyclerView.ViewHolder {
        TextView outfitName, outfitDescription;
        Button removeButton;
        // Add references to the preview ImageViews
        ImageView preview1, preview2, preview3, preview4;
        ImageView[] previews; // An array for easier handling

        OutfitViewHolder(@NonNull View itemView) {
            super(itemView);
            outfitName = itemView.findViewById(R.id.outfit_name);
            outfitDescription = itemView.findViewById(R.id.outfit_description);
            removeButton = itemView.findViewById(R.id.remove_button);
            // Initialize the preview ImageViews
            preview1 = itemView.findViewById(R.id.preview_image_1);
            preview2 = itemView.findViewById(R.id.preview_image_2);
            preview3 = itemView.findViewById(R.id.preview_image_3);
            preview4 = itemView.findViewById(R.id.preview_image_4);
            previews = new ImageView[]{preview1, preview2, preview3, preview4};
        }

        void bind(final Outfit outfit) {
            outfitName.setText(outfit.getName());
            outfitDescription.setText(outfit.getDescription());

            // Populate the preview images
            List<Product> items = outfit.getItems();
            for (int i = 0; i < previews.length; i++) {
                if (i < items.size()) {
                    previews[i].setVisibility(View.VISIBLE);
                    Product product = items.get(i);
                    if (product.getImageLink() != null) {
                        Glide.with(context).load(product.getImageLink()).into(previews[i]);
                    } else {
                        Glide.with(context).load(product.getDrawableResId()).into(previews[i]);
                    }
                } else {
                    // Hide unused preview slots
                    previews[i].setVisibility(View.GONE);
                }
            }

            removeButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onOutfitRemoved(outfit);
                }
            });

            // --- UPDATE: Set OnClickListener to navigate to the new detail fragment ---
            itemView.setOnClickListener(v -> {
                AppCompatActivity activity = (AppCompatActivity) context;
                OutfitDetailFragment detailFragment = OutfitDetailFragment.newInstance(outfit);

                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, detailFragment)
                        .addToBackStack(null) // Allows user to press back to return to the wishlist
                        .commit();
            });
        }
    }
}