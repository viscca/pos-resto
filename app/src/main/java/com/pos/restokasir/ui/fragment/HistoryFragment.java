package com.pos.restokasir.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.pos.restokasir.R;
import com.pos.restokasir.adapter.HistoryAdapter;
import com.pos.restokasir.databinding.FragmentHistoryBinding;
import com.pos.restokasir.tools.NavigationItem;
import com.pos.restokasir.ui.activity.DetailHistoryActivity;

public class HistoryFragment extends Fragment {

    private FragmentHistoryBinding binding;

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ListView lv = root.findViewById(R.id.list);
        HistoryAdapter histAdapter = new HistoryAdapter(inflater.getContext(), R.layout.row_history);
        histAdapter.add(NavigationItem.cusitem("22/07/2022 13:15", getResources().getDrawable(R.drawable.ic_money), "Nasi Goreng (Reguler)", "Rp20.000"));
        histAdapter.add(NavigationItem.cusitem("22/07/2022 15:20", getResources().getDrawable(R.drawable.ic_wallet), "Nasi Goreng (Spesial), Teh Manis", "Rp35.000"));
        histAdapter.add(NavigationItem.cusitem("22/07/2022 17:11", getResources().getDrawable(R.drawable.ic_credit_card), "Ayam Goreng (Reguler)", "Rp10.000"));
        lv.setAdapter(histAdapter);
        lv.setOnItemClickListener(DiPilih);

        return root;
    }

    private final AdapterView.OnItemClickListener DiPilih = (adapterView, view, position, id) -> {
        NavigationItem menu = ((HistoryAdapter) adapterView.getAdapter()).getItem(position);
        assert menu != null;
        Intent intent = new Intent(getActivity(), DetailHistoryActivity.class);
        startActivity(intent);
        requireActivity().overridePendingTransition(0, 0);
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}