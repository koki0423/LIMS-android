package com.example.lims_android.ui.search;

import android.os.Bundle;
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
import com.example.lims_android.ui.search.adapter.AssetInstanceAdapter;
import com.example.lims_android.ui.search.adapter.AssetMasterAdapter;
import com.example.lims_android.ui.search.detail.AssetDetailDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SearchFragment extends Fragment {

    private SearchViewModel viewModel;
    private AssetMasterAdapter assetMasterAdapter;
    private TextInputEditText searchEditText;
    private Spinner categorySpinner;
    private RecyclerView resultsRecyclerView;
    private TextView noResultsTextView;
    private AssetInstanceAdapter.AssetAdapter assetAdapter;

    private void setupRecyclerView() {
        assetMasterAdapter = new AssetMasterAdapter();
        resultsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        resultsRecyclerView.setAdapter(assetMasterAdapter);

        // アダプタにクリックリスナーを設定
        assetMasterAdapter.setOnItemClickListener(master -> {
            // 項目がタップされたら、マスターIDを渡して詳細ダイアログを表示
            AssetDetailDialogFragment dialog = AssetDetailDialogFragment.newInstance(master.getAssetMasterId());
            dialog.show(getParentFragmentManager(), "AssetDetailDialog");
        });
    }

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

    private void setupSpinner() {
        // 1. Categoryオブジェクトのリストを作成
        List<Category> categoryList = new ArrayList<>();
        categoryList.add(new Category(0, "すべて", "ALL"));
        categoryList.add(new Category(1, "個人", "IND"));
        categoryList.add(new Category(2, "事務", "OFS"));
        categoryList.add(new Category(3, "ファシリティ", "FAC"));
        categoryList.add(new Category(4, "組込みシステム", "EMB"));
        categoryList.add(new Category(5, "高度情報演習", "ADV"));

        // 2. StringではなくCategoryを扱うArrayAdapterを作成
        ArrayAdapter<Category> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categoryList);
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
                // キーボードを隠すおまけ
                // InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                // imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                return true;
            }
            return false;
        });
    }

    private void observeViewModel() {
        // viewModel.searchResults は List<AssetMasterResponse> を保持するLiveDataになります
        viewModel.searchResults.observe(getViewLifecycleOwner(), masters -> { // 1. 変数名を masters に変更（分かりやすさのため）

            assetMasterAdapter.submitList(masters); // 2. assetMasterAdapter の submitList を呼び出す

            // 検索結果の有無に応じて表示を切り替えるロジックは同じ
            if (masters.isEmpty()) {
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
        Category selectedCategory = (Category) categorySpinner.getSelectedItem();
        int genreId = selectedCategory.getId();
        viewModel.search(query, genreId);
    }

}