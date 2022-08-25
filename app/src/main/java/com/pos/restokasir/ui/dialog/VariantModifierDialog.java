package com.pos.restokasir.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.pos.restokasir.R;
import com.pos.restokasir.adapter.ModifierAdapter;
import com.pos.restokasir.adapter.VariantAdapter;
import com.pos.restokasir.tools.NavigationItem;

import java.util.Objects;

public class VariantModifierDialog extends Dialog {

    public VariantModifierDialog(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.dialog_variant_modifier);
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(0));
        int width = (int)(context.getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(context.getResources().getDisplayMetrics().heightPixels*0.90);
        getWindow().setLayout(width, height);

        Button btnPilih = findViewById(R.id.btn_pilih);
        Button btnTutup = findViewById(R.id.btn_cancel);

        View.OnClickListener diKlik = v -> {
            if (v == btnPilih) {
                // pilih
            } else {
                dismiss();
            }
        };

        btnPilih.setOnClickListener(diKlik);
        btnTutup.setOnClickListener(diKlik);

        ListView lvVar = findViewById(R.id.listVariant);
        VariantAdapter variantAdapter = new VariantAdapter(context, R.layout.row_variant);
        variantAdapter.add(NavigationItem.varitem("Paha Atas", "Rp14.250", "Rp15.000"));
        variantAdapter.add(NavigationItem.varitem("Dada", "Rp15.200", "Rp16.000"));
        lvVar.setAdapter(variantAdapter);

        ListView lvMod = findViewById(R.id.listModifier);
        ModifierAdapter modifierAdapter = new ModifierAdapter(context, R.layout.row_modifier);
        modifierAdapter.add(NavigationItem.moditem("Telur mata sapi", "Rp5.000"));
        modifierAdapter.add(NavigationItem.moditem("Es Jeruk", "Rp10.000"));
        lvMod.setAdapter(modifierAdapter);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
