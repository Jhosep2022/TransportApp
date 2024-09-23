package com.example.apptransport.Dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.apreciasoft.mobile.asremis.Entity.ClientEmail;
import com.apreciasoft.mobile.asremis.Entity.ClientRecoveryPass;
import com.apreciasoft.mobile.asremis.Entity.resp;
import com.apreciasoft.mobile.asremis.Http.HttpConexion;
import com.apreciasoft.mobile.asremis.R;
import com.apreciasoft.mobile.asremis.Services.ServicesLoguin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("ValidFragment")
public class RecoveryPassDialog extends DialogFragment {
    private ListenerDialogRecoveryPass listenerDialogRecoveryPass;
    private View v;
    private EditText editTextEmail;
    private Button buttonRecovery;

    public RecoveryPassDialog(ListenerDialogRecoveryPass listenerDialogRecoveryPass){
        this.listenerDialogRecoveryPass= listenerDialogRecoveryPass;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return createSimpleDialog();
    }

    public AlertDialog createSimpleDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        v = inflater.inflate(R.layout.dialog_recovery_pass, null);
        builder.setView(v);

        addView();

        return builder.create();
    }

    private void addView(){

        editTextEmail= v.findViewById(R.id.edit_email);

        buttonRecovery= v.findViewById(R.id.button_recovery);
        buttonRecovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarEmail();
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


    private void validarEmail(){
        InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()){
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }


        String email= editTextEmail.getText().toString().trim();

        if (email.isEmpty()){
            editTextEmail.setError(getString(R.string.completar_campo));
            editTextEmail.requestFocus();
        }else if (validationEmail(email)){
            postRecovery(email);
        }else {
            editTextEmail.setError(getString(R.string.email_invalido));
            editTextEmail.requestFocus();
        }

    }

    private Boolean validationEmail(String _email){

        Pattern pattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

        Matcher mather = pattern.matcher(_email);

        if (mather.find() == true) {
            return  true;

        } else {
            return  false;
        }

    }


    private void postRecovery(String email){
        buttonRecovery.setEnabled(false);
        buttonRecovery.setText(getString(R.string.validando));

        ClientEmail clientEmail= new ClientEmail();
        clientEmail.setMailClient(email);
        ClientRecoveryPass clientRecoveryPass= new ClientRecoveryPass();
        clientRecoveryPass.setClient(clientEmail);

        ServicesLoguin apiService = HttpConexion.getUri().create(ServicesLoguin.class);
        apiService.postRecoveryPass(clientRecoveryPass).enqueue(new Callback<resp>() {
            @Override
            public void onResponse(Call<resp> call, Response<resp> response) {
                buttonRecovery.setEnabled(true);
                buttonRecovery.setText(getString(R.string.recuperar));
                try {
                    listenerDialogRecoveryPass.respuestaDialogRecovery(response.body().response);
                }catch (Exception e){
                    Log.e("Exception PASS",e.toString());

                    listenerDialogRecoveryPass.respuestaDialogRecovery(getString(R.string.fallo_general));
                }

            }

            @Override
            public void onFailure(Call<resp> call, Throwable t) {
                Log.e("FAILURE RECOVERU PASS",t.toString());
                buttonRecovery.setEnabled(true);
                buttonRecovery.setText(getString(R.string.recuperar));

                listenerDialogRecoveryPass.respuestaDialogRecovery(getString(R.string.fallo_general));
            }
        });
    }

}
