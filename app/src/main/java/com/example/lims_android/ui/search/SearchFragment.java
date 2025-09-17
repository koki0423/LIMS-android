package com.example.lims_android.ui.search;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lims_android.R;
import com.google.android.material.textfield.TextInputEditText;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SearchFragment extends Fragment {

    private SearchViewModel viewModel;
    private TextInputEditText searchEditText;
    private Spinner categorySpinner;
    private RecyclerView resultsRecyclerView;
    private TextView noResultsTextView;
    private AssetAdapter assetAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        return inflater.inflate(R.layout.fragment_search, container, false); // あなたのレイアウトファイル名に合わせる
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // UIの初期化
        searchEditText = view.findViewById(R.id.edit_text_search_item);
        categorySpinner = view.findViewById(R.id.spinner_item_category);
        resultsRecyclerView = view.findViewById(R.id.recycler_view_search_results);
        noResultsTextView = view.findViewById(R.id.text_view_no_results);

        setupRecyclerView();
        setupSpinner();
        setupSearchListener();
        observeViewModel();

        // 初期検索を実行
        performSearch();
    }

    private void setupRecyclerView() {
        assetAdapter = new AssetAdapter();
        resultsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        resultsRecyclerView.setAdapter(assetAdapter);
    }

    private void setupSpinner() {
        // スピナーに表示するカテゴリリストを作成
        String[] categories = {"すべて", "経理部", "営業部", "会議室"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        // スピナーの項目が選択された時に検索を実行
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                performSearch();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupSearchListener() {
        // キーボードの検索ボタンが押された時に検索を実行
        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH) {
                performSearch();
                return true;
            }
            return false;
        });
    }

    private void observeViewModel() {
        viewModel.searchResults.observe(getViewLifecycleOwner(), assets -> {
            assetAdapter.submitList(assets);
            // 検索結果の有無に応じて表示を切り替え
            if (assets.isEmpty()) {
                resultsRecyclerView.setVisibility(View.GONE);
                noResultsTextView.setVisibility(View.VISIBLE);
            } else {
                resultsRecyclerView.setVisibility(View.VISIBLE);
                noResultsTextView.setVisibility(View.GONE);
            }
        });
    }

    private void performSearch() {
        String query = searchEditText.getText().toString();
        String category = categorySpinner.getSelectedItem().toString();
        viewModel.search(query, category);
    }
}