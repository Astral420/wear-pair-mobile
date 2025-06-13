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
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class OutfitProductsAdapter extends RecyclerView.Adapter<OutfitProductsAdapter.ViewHolder> {

    private final Context context;
    private final List<Product> productList;

    public OutfitProductsAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_outfit_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(productList.get(position));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productRetailer;
        Button buyNowButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productRetailer = itemView.findViewById(R.id.product_retailer);
            buyNowButton = itemView.findViewById(R.id.buy_now_button);
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
        }
    }
}