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
import android.widget.TextView;

public class OutfitDetailFragment extends Fragment {

    private static final String ARG_OUTFIT = "outfit_arg";
    private Outfit outfit;

    public OutfitDetailFragment() {
        // Required empty public constructor
    }

    public static OutfitDetailFragment newInstance(Outfit outfit) {
        OutfitDetailFragment fragment = new OutfitDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_OUTFIT, outfit);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            outfit = getArguments().getParcelable(ARG_OUTFIT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_outfit_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (outfit == null || getContext() == null) {
            return;
        }

        TextView outfitName = view.findViewById(R.id.outfit_detail_name);
        TextView outfitDescription = view.findViewById(R.id.outfit_detail_description);
        RecyclerView productsRecyclerView = view.findViewById(R.id.outfit_products_recycler_view);

        outfitName.setText(outfit.getName());
        outfitDescription.setText(outfit.getDescription());

        productsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        OutfitProductsAdapter adapter = new OutfitProductsAdapter(getContext(), outfit.getItems());
        productsRecyclerView.setAdapter(adapter);
    }
}