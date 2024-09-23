package com.example.apptransport.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.apreciasoft.mobile.asremis.R;
import com.mercadopago.android.px.internal.util.TextUtil;

/**
 * Created by usario on 31/7/2017.
 */

@SuppressLint("ValidFragment")
public class FragmentRegisterForm extends Fragment {
    private View view;
    TextView txtNombre;
    private ListenerFragmentRegister listenerFragmentRegister;
    private String firstName, lastName, email, socialid;

    public FragmentRegisterForm(ListenerFragmentRegister listenerFragmentRegister) {
        this.listenerFragmentRegister = listenerFragmentRegister;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_form_register, container, false);
        getParametrs();
        addView();
        return view;
    }

    private void getParametrs()
    {
        Bundle b = getArguments();
        try{
            if(b!=null)
            {
                firstName = b.getString("firstName");
                lastName = b.getString("lastName");
                email = b.getString("email");
                socialid = b.getString("socialid");
            }
            else{
              cleanParams();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            cleanParams();
        }
    }

    private void cleanParams()
    {
        firstName = "";
        lastName = "";
        email = "";
        socialid = "";
    }

    private void addView() {
        Button btn_new_driver = view.findViewById(R.id.button_driver);
        btn_new_driver.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               listenerFragmentRegister.goToRegisterChofer();
            }
        });

        Button btn_new_client = view.findViewById(R.id.button_passager);
        btn_new_client.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               listenerFragmentRegister.goToRegisterCliente();
            }
        });

        txtNombre = view.findViewById(R.id.txtNombre);
        if(!TextUtils.isEmpty(firstName))
        {
            txtNombre.setText(getString(R.string.hola).concat(" ").concat(firstName).concat( " ").concat( lastName));
        }
        else{
            txtNombre.setVisibility(View.GONE);
        }
    }

}
