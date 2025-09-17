package com.example.lims_android.ui.search.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lims_android.R;
import com.example.lims_android.data.model.Asset;
import com.example.lims_android.data.model.AssetResponse;

import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class AssetInstanceAdapter extends RecyclerView.Adapter<AssetInstanceAdapter.InstanceViewHolder> {

    private List<AssetResponse> instances = new ArrayList<>();

    @NonNull
    @Override
    public InstanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_asset_instance, parent, false);
        return new InstanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InstanceViewHolder holder, int position) {
        holder.bind(instances.get(position));
    }

    @Override
    public int getItemCount() {
        return instances.size();
    }

    public void submitList(List<AssetResponse> newInstances) {
        this.instances = newInstances;
        notifyDataSetChanged();
    }

    static class InstanceViewHolder extends RecyclerView.ViewHolder {
        private final TextView serialText, statusText, ownerText, purchasedAtText;
        private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.JAPAN);


        public InstanceViewHolder(@NonNull View itemView) {
            super(itemView);
            serialText = itemView.findViewById(R.id.text_view_serial);
            statusText = itemView.findViewById(R.id.text_view_status);
            ownerText = itemView.findViewById(R.id.text_view_owner);
            purchasedAtText = itemView.findViewById(R.id.text_view_purchased_at); // ✅ 追加
        }

        public void bind(AssetResponse instance) {
            // ✅ 表示する項目を追加
            serialText.setText(instance.getSerial() != null ? instance.getSerial() : "N/A");
            statusText.setText(convertStatusIdToString(instance.getStatusId()));
            ownerText.setText(instance.getLocation());
            if (instance.getPurchasedAt() != null) {
                purchasedAtText.setText(dateFormat.format(instance.getPurchasedAt()));
            } else {
                purchasedAtText.setText("N/A");
            }
        }

        // status_idを文字列に変換するヘルパーメソッド
        private String convertStatusIdToString(int statusId) {
            switch (statusId) {
                case 1: return "正常";
                case 2: return "故障";
                case 3: return "修理中";
                case 4: return "貸出中";
                case 5: return "廃棄済み";
                case 6: return "紛失";
                default: return "不明";
            }
        }
    }

    public static class AssetAdapter extends RecyclerView.Adapter<AssetAdapter.AssetViewHolder> {

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
}