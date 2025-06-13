package com.example.wearandpair;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

// Implement the new listener interface from the adapter
public class ProductsFragment extends Fragment implements ProductAdapter.PaginationListener {

    // ... (Most variables remain the same)
    // --- REMOVED: We no longer need direct references to the old buttons ---
    // private Button nextButton, prevButton;

    // ... (onCreate and onCreateView find views but DON'T find the old buttons)

    // --- This method is now REMOVED as the adapter handles it ---
    // private void setupPagination() { ... }

    private void updateProductList() {
        int totalItems = filteredProducts.size();
        int totalPages = (int) Math.ceil((double) totalItems / ITEMS_PER_PAGE);

        if (totalItems == 0) {
            // Handle case with no results
            productsRecyclerView.setAdapter(new ProductAdapter(getContext(), new ArrayList<>(), isSelectionMode, 0, 0, this));
            return;
        }

        int start = currentPage * ITEMS_PER_PAGE;
        int end = Math.min(start + ITEMS_PER_PAGE, totalItems);

        // The list now holds Objects, not just Products
        List<Object> pagedItems = new ArrayList<>(filteredProducts.subList(start, end));

        // --- NEW: Add the footer item if needed ---
        // Add a null to the list to represent the footer if there's more than one page
        if (totalPages > 1) {
            pagedItems.add(null);
        }

        // --- NEW: Pass pagination info to the adapter ---
        productAdapter = new ProductAdapter(getContext(), pagedItems, isSelectionMode, currentPage, totalPages, this);
        productsRecyclerView.setAdapter(productAdapter);
    }

    // --- NEW: Implement the listener methods from the adapter ---
    @Override
    public void onNextPageClicked() {
        currentPage++;
        updateProductList();
    }

    @Override
    public void onPrevPageClicked() {
        currentPage--;
        updateProductList();
    }

    // --- The rest of your methods remain the same ---
    private static final String LAST_CATEGORY_KEY = "LAST_CATEGORY_KEY";
    private RecyclerView productsRecyclerView;
    private ProductAdapter productAdapter;
    private List<Product> allProducts = new ArrayList<>();
    private List<Product> filteredProducts = new ArrayList<>();

    private ChipGroup categoryChipGroup;
    private View chipScrollContainer;
    private TextView productsTitle;

    private static final int ITEMS_PER_PAGE = 10;
    private int currentPage = 0;
    private String currentCategory = "All";
    private boolean isSelectionMode = false;
    private ArrayList<String> allowedCategories;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadProducts(); // Load the full list of products

        if (getArguments() != null) {
            isSelectionMode = getArguments().getBoolean("is_selection_mode", false);
            if (getArguments().containsKey("allowed_categories")) {
                allowedCategories = getArguments().getStringArrayList("allowed_categories");
                currentCategory = "All";
            } else {
                currentCategory = getArguments().getString("filter_type", "All");
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products, container, false);

        productsRecyclerView = view.findViewById(R.id.products_recycler_view);
        categoryChipGroup = view.findViewById(R.id.category_chip_group);
        chipScrollContainer = view.findViewById(R.id.chip_scroll_container);
        productsTitle = view.findViewById(R.id.products_title);
        // The old buttons are no longer in this layout
        // nextButton = view.findViewById(R.id.next_button);
        // prevButton = view.findViewById(R.id.prev_button);

        setupRecyclerView();
        // setupPagination(); // No longer needed

        // Apply the initial filter based on arguments
        if (isSelectionMode) {
            categoryChipGroup.setVisibility(View.VISIBLE);
            chipScrollContainer.setVisibility(View.VISIBLE);
            if (allowedCategories != null && !allowedCategories.isEmpty()) {
                productsTitle.setText("Select a Top");
                setupCategories(allowedCategories);
                filterByAllowedCategories(allowedCategories);
            } else {
                String displayName = getCategoryDisplayName(currentCategory);
                productsTitle.setText("Select a " + displayName);
                setupCategories(new ArrayList<>(Collections.singletonList(currentCategory)));
                filterProductsByCategory(currentCategory);
            }
        } else {
            setupCategories(null);
            updateViewForCategory(currentCategory);
        }

        return view;
    }

    private void loadProducts() {
        allProducts.clear();
        allProducts.add(new Product(1, "Denim Jacket", R.drawable.denimjacket, "https://ph.shein.com/ph/Men's-Casual-Washed-Funny-Plain-Dark-Denim-Jacket,-Spring-&-Fall,-For-Party-p-38400397.html?ref=m&rep=dir&ret=mph&ref=www&rep=dir&ret", "SHEIN", "Hoodies & Jackets", false, true, "Classic denim jacket with button closure and multiple pockets. Perfect for casual everyday wear."));
        allProducts.add(new Product(2, "Knitted Sweater", R.drawable.knittedsweater, "https://ph.shein.com/eur/Manfinity-Hypemode-Men-1Pc-Cartoon-Pattern-Colorblock-Drop-Shoulder-Baggy-Sweater-Crew-Neck-Long-Sleeve-Colorful-Dinosaur-Pattern-Going-Out-Fashion-Sweater-For-Friends-p-23864215.html?ad_type=DPA&currency=EUR&goods_id=42631793&isFromSwitchColor=1&lang=en&main_attr=27_78&mallCode=1&onelink=0%2Fgooglefeed_eur&pf=google&requestId=olw-4nkh3bk1beos&scene=1&skucode=I52h5drktxal&test=5051&ref=www&rep=dir&ret=ph", "SHEIN", "Shirts", false, false, "Soft and cozy knitted sweater perfect for colder weather. Features a comfortable fit and stylish design."));
        allProducts.add(new Product(3, "Slim-Fit Jeans", R.drawable.slimfitjeans, "https://ph.shein.com/Women-s-Slim-Fit-Denim-Pants-With-Pockets-Suitable-For-Daily-Outings-p-34406940.html?ref=www&rep=dir&ret=ph", "SHEIN", "Lowers", false, true, "Modern slim-fit jeans with stretchy fabric for maximum comfort. Stylish and versatile for any occasion."));
        allProducts.add(new Product(4, "White T-Shirt", R.drawable.whitetee, "https://www.uniqlo.com/ph/en/products/E442194-000", "UNIQLO", "Shirts", false, true, " Classic solid round neck t-shirt made with premium cotton. Basic essential for every wardrobe."));
        allProducts.add(new Product(5, "Sleeveless Sport Swimsuit", R.drawable.sleevelesssportsswimsuit, "https://ph.shein.com/DAZY-Contrast-Binding-Halter-Bikini-Swimsuit-p-17444181-cat-1866.html?lang=asia&ref=www&rep=dir&ret=ph", "SHEIN", "Swimwear", true, true, "Athletic swimsuit designed for performance and comfort. Features quick-drying and chlorine-resistant fabric."));
        allProducts.add(new Product(6, "Leather Boots", R.drawable.leatherboots, "https://www.uggoutlet.com.au/products/chunky-boots-lace-up-women-stephanie?variant=43633288282327", "UGG OUTLET", "Shoes", false, true, "Premium leather boots with durable soles. Perfect for both style and functionality in any weather."));
        allProducts.add(new Product(7, "Adjustable Shoulder Bag", R.drawable.adjustableshoulderbag, "https://ph.shein.com/ph/1pc-Adjustable-Strap-Flap-Closure-Plain-Color-Shoulder-Bag,-Casual-Retro-Business-Commuting-Waterproof-PU-Mini-Messenger-Crossbody-Bag-Christmas-Men-Bag-Vacation-Dad-Gifts-Retro-Bag-Vintage-Messenger-Bag-Tote-Bag-Side-Bags-Gifts-Valentine-Day-Black-Bag-Camping-Sling-Bag-Stickers-Winter-Back-To-School-Valentines-Gifts-Valentine-Gifts-Vintage-Gift-Bag-Pack-Cross-Body-Bag-Spring-Vintage-Bags-School-Supplies-Mini-Bag-Small-Bag-Purse-College-Bags-New-Life-Items-For-Students-Men-Essentials-Fashion-Men's-Bags-Graduation-p-37807827.html?ref=m&rep=dir&ret=mph&ref=www&rep=dir&ret=ph", "SHEIN", "Accessories", false, true, "Versatile shoulder bag with adjustable straps and multiple compartments for everyday essentials."));
        allProducts.add(new Product(8, "Men's Fashionable T-Shirt", R.drawable.mensfashionabletshirt, "https://snobasia.com/collections/t-shirts/products/yukariyo-t-shirt", "SNOB ASIA", "Shirts", false, true, "Trendy men's t-shirt with modern design. Made with high-quality fabric for comfort and durability."));
        allProducts.add(new Product(9, "Layered Chain Necklace", R.drawable.layeredchainnecklace, "https://ph.shein.com/Men-s-Necklace-And-Unique-Design-Hip-Hop-Style-Chain-Jewelry-Sweater-Chain-Halloween-p-34255889.html?src_identifier=fc%3DNew%20In%60sc%3DJewelry%20%26%20Accessories%60tc%3DShop%20by%20Category%60oc%3DMen%20Fashion%20Accessories%60ps%3Dtab01navbar09menu01dir12%60jc%3Dreal_2027&src_module=topcat&src_tab_page_id=page_goods_detail1747587001983&mallCode=1&pageListType=4&imgRatio=1-1&pageListType=4", "SHEIN", "Accessories", false, true, "Elegant layered chain necklace with multiple strands. Perfect for adding a sophisticated touch to any outfit."));
        allProducts.add(new Product(10, "Men Star & Lightning Decor Ring Set", R.drawable.menstarlightningdecorringset, "https://ph.shein.com/5pcs-Men-Star-Lightning-Decor-Ring-p-12015672.html?src_identifier=fc%3DAll%60sc%3DJewelry%20%26%20Accessories%60tc%3DShop%20by%20Category%60oc%3DWomen%27s%20Rings%20%26%20Bracelets%60ps%3Dtab00navbar09menu01dir09%60jc%3DitemPicking_001312477&src_module=topcat&src_tab_page_id=page_real_class1747587236885&mallCode=1&pageListType=4&imgRatio=3-4&pageListType=4", "SHEIN", "Accessories", false, true, "Stylish ring set featuring star and lightning designs. Made with quality materials for everyday wear."));
        allProducts.add(new Product(11, "YOUNGLA The Boys Hoodie", R.drawable.younglatheboyshoodie, "https://www.youngla.com/products/5076?hcUrl=%2Fen-US", "YOUNGLA", "Hoodies & Jackets", false, true, "Comfortable and trendy hoodie with modern design. Perfect for casual outings and everyday wear."));
        allProducts.add(new Product(12, "Off-shoulder Graphic Tee", R.drawable.offshouldergraphictee, "https://ph.shein.com/us/goods-p-45942661.html?onelink=0/googlefeed_us&requestId=olw-4gobnxhczgv4&goods_id=45942661&lang=es&currency=USD&skucode=I53vecrg2b24&scene=1&test=5051&ad_type=DPA&pf=google&ref=www&rep=dir&ret=ph", "SHEIN", "Shirts", false, true, "Stylish off-shoulder t-shirt with graphic print. Comfortable fabric and trendy design for casual wear."));
        allProducts.add(new Product(13, "Sleeveless Dress", R.drawable.sleevelessdress, "https://ph.shein.com/Women-Sleeveless-High-Neck-Skinny-Sexy-Long-Dress-Bodycon-Sexy-Party-Dress-Evening-p-67030400.html?src_identifier=fc%3DAll%60sc%3DNew%20In%60tc%3DNew%20in%20Women%27s%20Clothing%60oc%3DDresses%60ps%3Dtab00navbar01menu01dir02%60jc%3DitemPicking_00205078&src_module=topcat&src_tab_page_id=page_home1747589250089&mallCode=1&pageListType=4&imgRatio=3-4&pageListType=4", "SHEIN", "Dresses", true, true, "Elegant sleeveless dress perfect for summer. Features a flattering silhouette and comfortable fabric."));
        allProducts.add(new Product(14, "New Balances 530", R.drawable.nb530, "https://www.newbalance.com/pd/530/MR530-32265-PMG-NA.html", "NEW BALANCE", "Shoes", false, true, "Iconic New Balance 530 sneakers combining style and comfort. Perfect for everyday wear and athletic activities."));
        allProducts.add(new Product(15, "Manfinity Loose Fit Blazer", R.drawable.manfinityloosefitblazer, "https://ph.shein.com/Manfinity-RebelGame-Plus-Size-Men-s-Loose-Fit-Black-Woven-Long-Sleeve-Blazer-Jacket-p-56151083.html?src_identifier=st%3D2%60sc%3Dblazers%60sr%3D0%60ps%3D1&src_module=search&src_tab_page_id=page_goods_detail1747589271156&mallCode=1&pageListType=4&imgRatio=3-4&pageListType=4", "SHEIN", "Blazers", false, false, "Stylish loose-fit blazer for modern men. Versatile piece that can be dressed up or down for various occasions."));
        allProducts.add(new Product(16, "Black Floral Dress", R.drawable.blackdress, "https://petalandpup.com/products/lewis-mini-dress-black-floral?variant=45060878860465", "Petal & Pup", "Dresses", true, true, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fashionable and functional."));
    }
    private void setupCategories(List<String> categories) {
        String[] categoriesToDisplay;
        if (categories != null) {
            categoriesToDisplay = categories.toArray(new String[0]);
        } else {
            categoriesToDisplay = new String[]{"All", "Shirts", "Lowers", "Dresses", "Swimwear", "Shoes", "Blazers", "Hoodies & Jackets", "Accessories"};
        }

        categoryChipGroup.removeAllViews();
        for (String category : categoriesToDisplay) {
            Chip chip = new Chip(getContext());
            chip.setText(category);
            chip.setCheckable(true);
            if (categoriesToDisplay.length == 1) {
                chip.setChecked(true);
                chip.setClickable(false);
            }
            categoryChipGroup.addView(chip);
        }

        if (categoriesToDisplay.length > 1) {
            categoryChipGroup.setOnCheckedChangeListener((group, checkedId) -> {
                Chip chip = group.findViewById(checkedId);
                if (chip != null && chip.isChecked()) {
                    updateViewForCategory(chip.getText().toString());
                } else if (checkedId == View.NO_ID && allowedCategories != null) {
                    productsTitle.setText("Select a Top");
                    filterByAllowedCategories(allowedCategories);
                }
            });
            selectChip(currentCategory);
        }
    }

    private void updateViewForCategory(String category) {
        this.currentCategory = category;
        if ("summer".equalsIgnoreCase(category)) {
            filterBySummerCollection();
        } else if ("popular".equalsIgnoreCase(category)) {
            filterByPopularItems();
        } else {
            filterProductsByCategory(category);
            productsTitle.setText(category);
            selectChip(category);
        }
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(LAST_CATEGORY_KEY, currentCategory);
    }
    private String getCategoryDisplayName(String category) {
        switch (category.toLowerCase()) {
            case "lowers":
                return "Bottom";
            case "shoes":
                return "Shoes";
            case "accessories":
                return "Accessory";
            default:
                return category;
        }
    }
    private void selectChip(String categoryName) {
        for (int i = 0; i < categoryChipGroup.getChildCount(); i++) {
            Chip chip = (Chip) categoryChipGroup.getChildAt(i);
            if (chip.getText().toString().equalsIgnoreCase(categoryName)) {
                chip.setChecked(true);
                break;
            }
        }
    }
    private void filterByAllowedCategories(List<String> categories) {
        filteredProducts = allProducts.stream()
                .filter(p -> categories.contains(p.getCategory()))
                .collect(Collectors.toList());
        currentPage = 0;
        updateProductList();
    }
    private void filterProductsByCategory(String category) {
        if ("All".equalsIgnoreCase(category) && allowedCategories != null) {
            filterByAllowedCategories(allowedCategories);
            return;
        }

        if ("All".equalsIgnoreCase(category)) {
            filteredProducts = new ArrayList<>(allProducts);
        } else {
            filteredProducts = allProducts.stream()
                    .filter(p -> p.getCategory().equalsIgnoreCase(category))
                    .collect(Collectors.toList());
        }
        currentPage = 0;
        updateProductList();
    }
    private void filterBySummerCollection() {
        filteredProducts = allProducts.stream()
                .filter(Product::isSummerCollection)
                .collect(Collectors.toList());
        productsTitle.setText("Summer Collection");
        currentPage = 0;
        updateProductList();
        categoryChipGroup.clearCheck();
    }
    private void filterByPopularItems() {
        filteredProducts = allProducts.stream()
                .filter(Product::isPopular)
                .collect(Collectors.toList());
        productsTitle.setText("Popular Items");
        currentPage = 0;
        updateProductList();
        categoryChipGroup.clearCheck();
    }
    private void setupRecyclerView() {
        // --- THIS NEEDS TO BE UPDATED FOR THE FOOTER ---
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), calculateSpanCount());
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                // If the item is a footer, it should take up all columns (full width)
                if (productAdapter.getItemViewType(position) == ProductAdapter.VIEW_TYPE_FOOTER) {
                    return layoutManager.getSpanCount();
                }
                // Otherwise, it's a normal product item and takes up 1 column
                return 1;
            }
        });
        productsRecyclerView.setLayoutManager(layoutManager);
    }
    private int calculateSpanCount() {
        if (getContext() == null) return 2;
        float displayWidth = getResources().getDisplayMetrics().widthPixels;
        float itemWidthDp = 160;
        float itemWidthPx = itemWidthDp * getResources().getDisplayMetrics().density;
        int spanCount = (int) (displayWidth / itemWidthPx);
        return Math.max(2, spanCount);
    }
}