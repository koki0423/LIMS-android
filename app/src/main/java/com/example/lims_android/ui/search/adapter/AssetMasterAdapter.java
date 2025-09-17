package com.example.lims_android.ui.search.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lims_android.R;
import com.example.lims_android.data.model.AssetMasterResponse;
import java.util.ArrayList;
import java.util.List;

public class AssetMasterAdapter extends RecyclerView.Adapter<AssetMasterAdapter.MasterViewHolder> {

    private List<AssetMasterResponse> masters = new ArrayList<>();
    private OnItemClickListener listener;

    // --- 1. クリックイベントをFragmentに伝えるためのインターフェース ---
    public interface OnItemClickListener {
        void onItemClick(AssetMasterResponse master);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // --- RecyclerView.Adapterの必須メソッド ---
    @NonNull
    @Override
    public MasterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_asset_master, parent, false);
        return new MasterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MasterViewHolder holder, int position) {
        AssetMasterResponse currentMaster = masters.get(position);
        holder.bind(currentMaster);

        // --- 3. クリックされた項目をリスナーに通知 ---
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(currentMaster);
            }
        });
    }

    @Override
    public int getItemCount() {
        return masters.size();
    }

    // --- データを更新するためのメソッド ---
    public void submitList(List<AssetMasterResponse> newMasters) {
        this.masters = newMasters;
        notifyDataSetChanged();
    }

    // --- 2. 各行のUIを保持するViewHolder ---
    static class MasterViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;
        private final TextView managementNumberTextView;

        public MasterViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.text_view_master_name);
            managementNumberTextView = itemView.findViewById(R.id.text_view_management_number);
        }

        public void bind(AssetMasterResponse master) {
            nameTextView.setText(master.getName());
            managementNumberTextView.setText("管理番号: " + master.getManagementNumber());
        }
    }
}