package com.example.lims_android.ui.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lims_android.R;
import com.example.lims_android.data.model.Asset;
import java.util.ArrayList;
import java.util.List;

public class AssetAdapter extends RecyclerView.Adapter<AssetAdapter.AssetViewHolder> {

    private List<Asset> assets = new ArrayList<>();

    @NonNull
    @Override
    public AssetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_searched_item, parent, false);
        return new AssetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AssetViewHolder holder, int position) {
        holder.bind(assets.get(position));
    }

    @Override
    public int getItemCount() {
        return assets.size();
    }

    public void submitList(List<Asset> newAssets) {
        this.assets = newAssets;
        notifyDataSetChanged(); // データを更新して再描画
    }

    static class AssetViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;
        private final TextView barcodeTextView;
        private final TextView descriptionTextView;

        public AssetViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.text_view_asset_name);
            barcodeTextView = itemView.findViewById(R.id.text_view_asset_barcode);
            descriptionTextView = itemView.findViewById(R.id.text_view_asset_description);
        }

        public void bind(Asset asset) {
            nameTextView.setText(asset.getName());
            barcodeTextView.setText("管理番号: " + asset.getBarcode());
            descriptionTextView.setText("部署: " + asset.getDescription());
        }
    }
}