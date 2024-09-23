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
import android.widget.TextView;

import com.apreciasoft.mobile.asremis.R;

@SuppressLint("ValidFragment")
public class GenericDialog  extends DialogFragment {

    private View v;
    private String titulo,contenido;
    private int idTravle, option;
    private ListenerDialogGeneric listenerDialogGeneric;

    public GenericDialog(String titulo,String contenido,int idTravel,int option,ListenerDialogGeneric listenerDialogGeneric) {
        this.titulo= titulo;
        this.contenido= contenido;
        this.idTravle= idTravel;
        this.option= option;
        this.listenerDialogGeneric= listenerDialogGeneric;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return createSimpleDialog();
    }

    public AlertDialog createSimpleDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        v = inflater.inflate(R.layout.dialog_info_generico, null);
        builder.setView(v);

        addView();

        return builder.create();
    }

    private void addView() {
        TextView textViewTitulo= v.findViewById(R.id.textView_titulo_dialogo);
        textViewTitulo.setText(titulo);

        TextView textViewContenido= v.findViewById(R.id.textView_contenido_dialog);
        textViewContenido.setText(contenido);

        Button buttonOk= v.findViewById(R.id.button_ok);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opcionSelect();
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

        if (option==1){
            listenerDialogGeneric.confirmAceptByClient(idTravle);
        }else if (option==2){
            listenerDialogGeneric.confirmCancelByClient(idTravle);
        }

    }

}
