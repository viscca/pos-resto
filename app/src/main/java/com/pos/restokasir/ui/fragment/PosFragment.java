package com.pos.restokasir.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.pos.restokasir.R;
import com.pos.restokasir.adapter.MenuAdapter;
import com.pos.restokasir.databinding.FragmentPosBinding;
import com.pos.restokasir.tools.NavigationItem;
import com.pos.restokasir.ui.activity.PayActivity;
import com.pos.restokasir.ui.activity.ProductActivity;

import java.util.Objects;

public class PosFragment extends Fragment {

    private FragmentPosBinding binding;
    TableLayout tableList, tableTotal;
    private Button btnaddcustomer, btnpay;
    private TextView btnaddproduk;

    @SuppressLint("UseCompatLoadingForDrawables")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        String Judul = "All Item";
        Toolbar mToolbar = root.findViewById(R.id.toolbar_actionbar);
        TextView textView = root.findViewById(R.id.toolbar_title);
        textView.setText(Judul);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(mToolbar);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowHomeEnabled(true);

        GridView gv = root.findViewById(R.id.gvproduk);
        MenuAdapter trxAdapter;
        trxAdapter = new MenuAdapter(inflater.getContext(), R.layout.list_menu);
        trxAdapter.add(new NavigationItem("Nasi Goreng\nRp10.000", getResources().getDrawable(R.drawable.food)));
        trxAdapter.add(new NavigationItem("Soto\nRp15.000", getResources().getDrawable(R.drawable.food)));
        trxAdapter.add(new NavigationItem("Ayam Goreng\nRp8.000", getResources().getDrawable(R.drawable.food)));
        trxAdapter.add(new NavigationItem("Sate\nRp15.000", getResources().getDrawable(R.drawable.food)));
        trxAdapter.add(new NavigationItem("Pecel Lele\nRp7.000", getResources().getDrawable(R.drawable.food)));
        trxAdapter.add(new NavigationItem("Ayam Geprek\nRp10.000", getResources().getDrawable(R.drawable.food)));
        trxAdapter.add(new NavigationItem("Teh Manis\nRp5.000", getResources().getDrawable(R.drawable.food)));

        gv.setAdapter(trxAdapter);

        final Spinner spinner = binding.spin1;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.metode, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(adapter);

        tableList = binding.tbllist;
        tableTotal = binding.tbltotal;
        InsertRow();
        InsertTotal();

        btnaddcustomer = binding.btnadd;
        btnaddcustomer.setOnClickListener(DiKlik);
        btnpay = binding.btnpay;
        btnpay.setOnClickListener(DiKlik);
        btnaddproduk = root.findViewById(R.id.txtaddproduk);
        btnaddproduk.setOnClickListener(DiKlik);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @SuppressLint("SetTextI18n")
    private void InsertRow(){
        TableRow tr = new TableRow(getActivity());
        View v = getLayoutInflater().inflate(R.layout.row_list, tr, false);
        //want to get childs of row for example TextView, get it like this:
        ((TextView)v.findViewById(R.id.col1)).setText("Item1");
        ((TextView)v.findViewById(R.id.col2)).setText("x2");
        ((TextView)v.findViewById(R.id.col3)).setText("Rp10.000");
        tableList.addView(v);
    }

    @SuppressLint("SetTextI18n")
    private void InsertTotal(){
        TableRow tr = new TableRow(getActivity());
        View v = getLayoutInflater().inflate(R.layout.row_list, tr, false);
        View v2 = getLayoutInflater().inflate(R.layout.row_list, tr, false);
        View v3 = getLayoutInflater().inflate(R.layout.row_list, tr, false);
        View v4 = getLayoutInflater().inflate(R.layout.row_list, tr, false);
        //want to get childs of row for example TextView, get it like this:
        ((TextView)v.findViewById(R.id.col1)).setText("Sub Total");
        ((TextView)v.findViewById(R.id.col1)).setTextAppearance(getActivity(), android.R.style.TextAppearance_Medium);
        ((TextView)v.findViewById(R.id.col1)).setTextColor(getResources().getColor(R.color.darkgrey));
        ((TextView)v.findViewById(R.id.col2)).setText("");
        ((TextView)v.findViewById(R.id.col3)).setText("Rp20.000");
        ((TextView)v.findViewById(R.id.col3)).setTextAppearance(getActivity(), android.R.style.TextAppearance_Medium);
        ((TextView)v.findViewById(R.id.col3)).setTextColor(getResources().getColor(R.color.darkgrey));
        tableTotal.addView(v);
        ((TextView)v2.findViewById(R.id.col1)).setText("Tip (3%)");
        ((TextView)v2.findViewById(R.id.col1)).setTextAppearance(getActivity(), android.R.style.TextAppearance_Small);
        ((TextView)v2.findViewById(R.id.col1)).setTextColor(getResources().getColor(R.color.grey_200));
        ((TextView)v2.findViewById(R.id.col2)).setText("");
        ((TextView)v2.findViewById(R.id.col3)).setText("Rp600");
        ((TextView)v2.findViewById(R.id.col3)).setTextAppearance(getActivity(), android.R.style.TextAppearance_Small);
        ((TextView)v2.findViewById(R.id.col3)).setTextColor(getResources().getColor(R.color.grey_200));
        tableTotal.addView(v2);
        ((TextView)v3.findViewById(R.id.col1)).setText("PPN (10%)");
        ((TextView)v3.findViewById(R.id.col1)).setTextAppearance(getActivity(), android.R.style.TextAppearance_Small);
        ((TextView)v3.findViewById(R.id.col1)).setTextColor(getResources().getColor(R.color.grey_200));
        ((TextView)v3.findViewById(R.id.col2)).setText("");
        ((TextView)v3.findViewById(R.id.col3)).setText("Rp2.000");
        ((TextView)v3.findViewById(R.id.col3)).setTextAppearance(getActivity(), android.R.style.TextAppearance_Small);
        ((TextView)v3.findViewById(R.id.col3)).setTextColor(getResources().getColor(R.color.grey_200));
        tableTotal.addView(v3);
        ((TextView)v4.findViewById(R.id.col1)).setText("Total");
        ((TextView)v4.findViewById(R.id.col1)).setTextAppearance(getActivity(), android.R.style.TextAppearance_Medium);
        ((TextView)v4.findViewById(R.id.col1)).setTextColor(getResources().getColor(R.color.darkgrey));
        ((TextView)v4.findViewById(R.id.col2)).setText("");
        ((TextView)v4.findViewById(R.id.col3)).setText("Rp22.600");
        ((TextView)v4.findViewById(R.id.col3)).setTextAppearance(getActivity(), android.R.style.TextAppearance_Medium);
        ((TextView)v4.findViewById(R.id.col3)).setTextColor(getResources().getColor(R.color.darkgrey));
        tableTotal.addView(v4);
    }

    private final View.OnClickListener DiKlik= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v==btnaddcustomer) {

            } else if (v==btnpay) {
                Intent intent = new Intent(getActivity(), PayActivity.class);
                startActivity(intent);
                requireActivity().overridePendingTransition(0, 0);
            } else if (v==btnaddproduk) {
                Intent intent = new Intent(getActivity(), ProductActivity.class);
                startActivity(intent);
                requireActivity().overridePendingTransition(0, 0);
            }
        }
    };
}