package com.example.wearandpair;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class ProfileWishlistAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_PRODUCT = 0;
    private static final int TYPE_OUTFIT = 1;

    private final Context context;
    private final List<Object> items;

    public ProfileWishlistAdapter(Context context, List<Object> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) instanceof Product ? TYPE_PRODUCT : TYPE_OUTFIT;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_PRODUCT) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_profile_product, parent, false);
            return new ProductViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_profile_outfit, parent, false);
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

    class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName;

        ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
        }

        void bind(final Product product) {
            productName.setText(product.getName());
            Glide.with(context).load(product.getDrawableResId()).into(productImage);
        }
    }

    class OutfitViewHolder extends RecyclerView.ViewHolder {
        TextView outfitName;

        OutfitViewHolder(@NonNull View itemView) {
            super(itemView);
            outfitName = itemView.findViewById(R.id.outfit_name);
        }

        void bind(final Outfit outfit) {
            outfitName.setText(outfit.getName());
        }
    }
}