package com.example.apptransport.Dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.apreciasoft.mobile.asremis.R;

@SuppressLint("ValidFragment")
public class ExitDialog extends DialogFragment {

    private View v;
    private ListenerDialogExit listenerDialogExit;

    public ExitDialog (ListenerDialogExit listenerDialogExit) {
        this.listenerDialogExit= listenerDialogExit;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return createSimpleDialog();
    }

    public AlertDialog createSimpleDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        v = inflater.inflate(R.layout.dialog_salir, null);
        builder.setView(v);

        addView();

        return builder.create();
    }

    private void addView() {

        Button buttonOk= v.findViewById(R.id.button_ok);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opcionSelect();
                dismiss();
            }
        });

        Button buttonNo= v.findViewById(R.id.button_no);
        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        ImageButton buttonCancelar= v.findViewById(R.id.button_cancelar);
        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }


    private void opcionSelect(){
        listenerDialogExit.salirApp();
    }
}
