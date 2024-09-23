package com.example.apptransport.Dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.apreciasoft.mobile.asremis.Entity.BeneficioEntity;
import com.apreciasoft.mobile.asremis.Entity.Currency;
import com.apreciasoft.mobile.asremis.Entity.InfoTravelEntity;
import com.apreciasoft.mobile.asremis.R;
import com.apreciasoft.mobile.asremis.Util.Globales;
import com.apreciasoft.mobile.asremis.Util.GlovalVar;
import com.apreciasoft.mobile.asremis.Util.Utils;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class DistanceChooserDialog  extends DialogFragment {
    private View v;
    private Button buttonFinalizar;
    private double km_presupuestado, km_app;
    private RadioButton radio_app, radio_calculated;
    private ChooseDistanceListener onButtonAceptListener;

    public DistanceChooserDialog(double kilometros_aplicacion, double kilometros_presupuestado) {
        this.km_app =kilometros_aplicacion;
        this.km_presupuestado = kilometros_presupuestado;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return createSimpleDialog();
    }

    public AlertDialog createSimpleDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        v = inflater.inflate(R.layout.dialog_distance_chooser, null);
        builder.setView(v);
        addView();
        setInfo();
        return builder.create();
    }

    public void setOnClickListener(ChooseDistanceListener onClickListener)
    {
        onButtonAceptListener = onClickListener;
    }

    private void addView() {
        radio_app = v.findViewById(R.id.radio_app);
        radio_calculated = v.findViewById(R.id.radio_calculated);
        radio_calculated.setChecked(true);

        buttonFinalizar = v.findViewById(R.id.btn_acept);
        buttonFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonAceptListener.btnAcept(radio_app.isChecked() ? km_app : km_presupuestado, radio_calculated.isChecked() ? 1 : 0);
            }
        });
    }

    @SuppressLint("LongLogTag")
    private void setInfo() {
        try {
            radio_app.setText(getString(R.string.distancia_aplicacion).concat(String.valueOf(Utils.round(km_app, 2))).concat(getString(R.string.km)));
            radio_calculated.setText(getString(R.string.distancia_presupuestada).concat(String.valueOf(Utils.round(km_presupuestado, 2))).concat(getString(R.string.km)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public interface ChooseDistanceListener{
        void btnAcept(double km_selected, int isKmWithError);
    }


}