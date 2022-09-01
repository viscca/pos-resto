package com.pos.restokasir.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.pos.restokasir.R;
import com.pos.restokasir.adapter.ModifierAdapter;
import com.pos.restokasir.adapter.VariantAdapter;
import com.pos.restokasir.tools.NavigationItem;
import com.pos.restokasir.ui.activity.LoginActivity;
import com.pos.restokasir.ui.activity.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VariantModifierDialog extends Dialog {
    private final String TAG="Prs_DlgModifier";
    public Button btnPilih;
    public View.OnClickListener OnPilih;
    private JSONArray subMenu, dtVariant;
    public JSONObject Hasil;
    private Integer Jml_subMenu;
    private Spinner spinner;
    private TableLayout tbl_modifier;
    private ArrayList<CheckBox> ObjCB;
    private double HrgItem, HrgModif;
    private EditText eNilai;
    private androidx.appcompat.widget.AppCompatButton btnMinus, btnPlus;

    public VariantModifierDialog(@NonNull JSONObject dt,@NonNull Context context) {
        super(context);
        Jml_subMenu = -1;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.dialog_variant_modifier);
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(0));
        int width = (int)(context.getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(context.getResources().getDisplayMetrics().heightPixels*0.90);
        getWindow().setLayout(width, height);

        spinner = findViewById(R.id.spinVarian);

        btnPilih = findViewById(R.id.btn_pilih);
        btnPilih.setOnClickListener(DiKlik);
        Button btnTutup = findViewById(R.id.btn_cancel);
        btnTutup.setOnClickListener(DiKlik);
        eNilai = findViewById(R.id.eNilai);
        btnMinus = findViewById(R.id.btnMinus);
        btnPlus = findViewById(R.id.btnPlus);
        btnPlus.setOnClickListener(KurangTambah);
        btnMinus.setOnClickListener(KurangTambah);

        tbl_modifier = findViewById(R.id.tbl_modifier);
        TextView Jdl = findViewById(R.id.txt_menu);
        Hasil = new JSONObject();
        try{
            Hasil.put("modifier_id","");
        } catch (JSONException ignored) {}
        btnPlus.callOnClick();
        try{
            Jdl.setText(dt.getString("name"));
            HrgItem = dt.getDouble("sale_price");
            Hasil.put("id", dt.getString("id"));
            subMenu = dt.getJSONArray("menu_modifiers");
            Jml_subMenu = subMenu.length();
            BuatListModifier();
            dtVariant = dt.getJSONArray("menu_variant");
            SetSpinner();
        } catch (JSONException ignored) {}
    }

    private void SetSpinner(){
        if(dtVariant.length()<1) {
            spinner.setVisibility(View.GONE);
            return;
        }
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(getContext(), R.layout.spinner_item);
        try {
            for (int i=0; i < dtVariant.length(); i++) {
                final JSONObject Isi = dtVariant.getJSONObject(i);
                adapter.add(Isi.getString("name"));
            }
        } catch (JSONException ignored) {}
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(
                    AdapterView<?> parent, View view, int position, long id) {
                try {
                    final JSONObject Isi = dtVariant.getJSONObject(position);
                    Hasil.put("id", Isi.getString("id"));
                } catch (JSONException ignored) {}
                Log.d(TAG,"Pilih Spiner");

            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void BuatListModifier(){
        tbl_modifier.removeAllViews();
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        Integer Hrg;
        ObjCB = new ArrayList<CheckBox>();
        HrgModif=0;
        try {
            for (int i=0; i < Jml_subMenu; i++) {
                TableRow tr = new TableRow(getContext());
                View v = getLayoutInflater().inflate(R.layout.row_modifier, tr, false);
                final JSONObject Isi = subMenu.getJSONObject(i);
                CheckBox Obj= v.findViewById(R.id.cb1);
                Obj.setText(Isi.getString("name"));
                Obj.setOnClickListener(KlikCB);
                ObjCB.add(Obj);
                Hrg= Isi.getInt("price");
                ((TextView) v.findViewById(R.id.col1)).setText("Rp"+formatter.format(Hrg));
                tbl_modifier.addView(v);
            }
        } catch (JSONException ignored) {}
    }

    private final View.OnClickListener KurangTambah= v-> {
        Integer I=0;
        try {
            I=Integer.parseInt(eNilai.getText().toString());
        } catch(NumberFormatException nfe) {
            Log.d(TAG,"Could not parse " + nfe);
        }
        if(v==btnPlus) I++;else if(v==btnMinus) I--;
        if(I<=0) I=0;
        try{
            Hasil.put("qty", I);
            eNilai.setText(""+I);
            HitungBayar();
        } catch (JSONException ignored) {}
    };

    private final View.OnClickListener KlikCB= v-> {
        HrgModif=0;
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < ObjCB.size(); i++) {
            CheckBox Obj = ObjCB.get(i);
            if (Obj.isChecked()) try {
                final JSONObject Isi = subMenu.getJSONObject(i);
                HrgModif += Isi.getInt("price");
                list.add(Isi.getString("id"));
            } catch (JSONException ignored) {}
        }
        try{
            Hasil.put("modifier_id", TextUtils.join(",",list));
            HitungBayar();
        } catch (JSONException ignored) {}
    };

    private void HitungBayar(){
        double Byr= HrgItem+HrgModif;
        try{
            Byr*= Hasil.getInt("qty");
        } catch (JSONException ignored) {}
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        ((TextView) findViewById(R.id.txtTotal)).setText("Total Harga Rp"+formatter.format(Byr));
    }

    private final View.OnClickListener DiKlik= v-> {
        if(v==btnPilih) {
            try{
                Hasil.getInt("qty");
                Hasil.getString("id");
                Hasil.put("note", ((EditText)findViewById(R.id.eNote)).getText().toString());
                dismiss();
                OnPilih.onClick(v);
            } catch (JSONException ignored) {
                BuatToast("Error:"+ignored.getMessage());
            }
        }else{
            dismiss();
        }
    };

    private void BuatToast(String Txt){
        Toast.makeText(getContext(), Txt, Toast.LENGTH_LONG ).show();
    }

}
