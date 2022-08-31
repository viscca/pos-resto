package com.pos.restokasir.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.pos.restokasir.R;
import com.pos.restokasir.databinding.FragmentSettingBinding;
import com.pos.restokasir.db_sqlite.C_DB_Setting;
import com.pos.restokasir.ui.activity.DetailHistoryActivity;

public class SettingFragment extends Fragment {
    private static final String[] MENU = {"Printer"};
    private FragmentSettingBinding binding;

    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = com.pos.restokasir.databinding.FragmentSettingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        C_DB_Setting DB_Setting = new C_DB_Setting(requireActivity());
        binding.txtEmail.setText(DB_Setting.mSettings.getString("idUser",""));
        ListView lv = root.findViewById(R.id.list);
        lv.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.row_setting, R.id.txtMenu, MENU));
        lv.setOnItemClickListener(DiPilih);

        return root;
    }

    private final AdapterView.OnItemClickListener DiPilih = (adapterView, view, position, id) -> {
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
