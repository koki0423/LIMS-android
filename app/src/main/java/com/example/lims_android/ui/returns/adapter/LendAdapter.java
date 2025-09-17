package com.example.lims_android.ui.returns.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lims_android.R;
import com.example.lims_android.data.model.LendResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LendAdapter extends RecyclerView.Adapter<LendAdapter.LendViewHolder> {

    private List<LendResponse> lends = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(LendResponse lend);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public LendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_lend, parent, false);
        return new LendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LendViewHolder holder, int position) {
        LendResponse currentLend = lends.get(position);
        holder.bind(currentLend);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(currentLend);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lends.size();
    }

    public void submitList(List<LendResponse> newLends) {
        this.lends = newLends;
        notifyDataSetChanged();
    }

    static class LendViewHolder extends RecyclerView.ViewHolder {
        private final TextView borrowerIdText;
        private final TextView lentAtText;
        private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.JAPAN);

        public LendViewHolder(@NonNull View itemView) {
            super(itemView);
            borrowerIdText = itemView.findViewById(R.id.text_view_borrower_id);
            lentAtText = itemView.findViewById(R.id.text_view_lent_at);
        }

        public void bind(LendResponse lend) {
            borrowerIdText.setText("借受者ID: " + lend.getBorrowerId());
            if (lend.getLentAt() != null) {
                lentAtText.setText("貸出日時: " + dateFormat.format(lend.getLentAt()));
            }
        }
    }
}