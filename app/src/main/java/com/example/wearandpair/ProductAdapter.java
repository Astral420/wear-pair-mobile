package com.example.wearandpair;

import android.content.Context;
import android.os.Bundle;
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

public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // Define constants for our two view types
    private static final int VIEW_TYPE_ITEM = 0;
    public static final int VIEW_TYPE_FOOTER = 1;

    private final Context context;
    // The list can now hold Products and a null for the footer
    private final List<Object> itemsList;
    private final boolean isSelectionMode;

    // --- NEW: Listener and variables for pagination ---
    private final PaginationListener paginationListener;
    private final int currentPage;
    private final int totalPages;

    public interface PaginationListener {
        void onNextPageClicked();
        void onPrevPageClicked();
    }

    public ProductAdapter(Context context, List<Object> itemsList, boolean isSelectionMode,
                          int currentPage, int totalPages, PaginationListener listener) {
        this.context = context;
        this.itemsList = itemsList;
        this.isSelectionMode = isSelectionMode;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.paginationListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        // If the item at the given position is null, it's our footer
        if (itemsList.get(position) == null) {
            return VIEW_TYPE_FOOTER;
        }
        return VIEW_TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the correct layout based on the view type
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
            return new ProductViewHolder(view);
        } else { // viewType == VIEW_TYPE_FOOTER
            View view = LayoutInflater.from(context).inflate(R.layout.item_pagination, parent, false);
            return new FooterViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // Bind the data based on the ViewHolder type
        if (holder instanceof ProductViewHolder) {
            Product product = (Product) itemsList.get(position);
            ((ProductViewHolder) holder).bind(product);
        } else if (holder instanceof FooterViewHolder) {
            ((FooterViewHolder) holder).bind();
        }
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    // ViewHolder for Products (no changes needed here)
    class ProductViewHolder extends RecyclerView.ViewHolder { /* ... as before ... */
        ImageView productImage;
        TextView productName, productRetailer;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productRetailer = itemView.findViewById(R.id.product_retailer);
        }

        void bind(final Product product) {
            productName.setText(product.getName());
            productRetailer.setText("From " + product.getRetailerLink());

            if (product.getImageLink() != null) {
                Glide.with(context).load(product.getImageLink()).into(productImage);
            } else {
                Glide.with(context).load(product.getDrawableResId()).into(productImage);
            }

            itemView.setOnClickListener(v -> {
                AppCompatActivity activity = (AppCompatActivity) context;

                if (isSelectionMode) {
                    Bundle result = new Bundle();
                    result.putParcelable("selected_product", product);
                    activity.getSupportFragmentManager().setFragmentResult("product_selection_request", result);
                    activity.getSupportFragmentManager().popBackStack();
                } else {
                    ProductDetailFragment detailFragment = ProductDetailFragment.newInstance(product);
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, detailFragment)
                            .addToBackStack(null)
                            .commit();
                }
            });
        }
    }

    // --- NEW: ViewHolder for our Pagination Footer ---
    class FooterViewHolder extends RecyclerView.ViewHolder {
        Button prevButton, nextButton;

        public FooterViewHolder(@NonNull View itemView) {
            super(itemView);
            prevButton = itemView.findViewById(R.id.prev_button_footer);
            nextButton = itemView.findViewById(R.id.next_button_footer);
        }

        void bind() {
            // Set visibility based on current page and total pages
            prevButton.setVisibility(currentPage > 0 ? View.VISIBLE : View.GONE);
            nextButton.setVisibility(currentPage < totalPages - 1 ? View.VISIBLE : View.GONE);

            // Set click listeners that call back to the fragment
            prevButton.setOnClickListener(v -> paginationListener.onPrevPageClicked());
            nextButton.setOnClickListener(v -> paginationListener.onNextPageClicked());
        }
    }
}