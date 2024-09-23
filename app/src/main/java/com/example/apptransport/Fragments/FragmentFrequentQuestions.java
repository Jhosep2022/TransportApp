package com.example.apptransport.Fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.apreciasoft.mobile.asremis.Entity.paramEntity;
import com.apreciasoft.mobile.asremis.PlaceToPay.PlaceToPayGlobales;
import com.apreciasoft.mobile.asremis.R;
import com.apreciasoft.mobile.asremis.Util.Globales;
import com.apreciasoft.mobile.asremis.Util.Utils;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentFrequentQuestions extends Fragment {


    public FragmentFrequentQuestions() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_frequent_questions, container, false);
        configureControls(view);
        return view;
    }

    private void configureControls(View view) {
        TextView txtQuestion1 = view.findViewById(R.id.txt_question_1_text);
        TextView txtQuestion2 = view.findViewById(R.id.txt_question_2_text);
        TextView txtQuestion3 = view.findViewById(R.id.txt_question_3_text);
        TextView txtQuestion4 = view.findViewById(R.id.txt_question_4_text);
        TextView txtQuestion5 = view.findViewById(R.id.txt_question_5_text);
        TextView txtQuestion6 = view.findViewById(R.id.txt_question_6_text);
        TextView txtQuestion7 = view.findViewById(R.id.txt_question_7_text);
        TextView txtQuestion8 = view.findViewById(R.id.txt_question_8_text);
        TextView txtQuestion9 = view.findViewById(R.id.txt_question_9_text);
        TextView txtQuestion10 = view.findViewById(R.id.txt_question_10_text);

        txtQuestion1.setText(replaceWithCommerceValues(txtQuestion1.getText().toString()));
        txtQuestion2.setText(replaceWithCommerceValues(txtQuestion2.getText().toString()));
        txtQuestion3.setText(replaceWithCommerceValues(txtQuestion3.getText().toString()));
        txtQuestion4.setText(replaceWithCommerceValues(txtQuestion4.getText().toString()));
        txtQuestion5.setText(replaceWithCommerceValues(txtQuestion5.getText().toString()));
        txtQuestion6.setText(replaceWithCommerceValues(txtQuestion6.getText().toString()));
        txtQuestion7.setText(replaceWithCommerceValues(txtQuestion7.getText().toString()));
        txtQuestion8.setText(replaceWithCommerceValues(txtQuestion8.getText().toString()));
        txtQuestion9.setText(replaceWithCommerceValues(txtQuestion9.getText().toString()));
        txtQuestion10.setText(replaceWithCommerceValues(txtQuestion10.getText().toString()));

        txtQuestion10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String url = getString(R.string.place_to_pay_question_description_10);
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                    Toast.makeText(getContext(), R.string.redirect_terminos_y_condiciones,Toast.LENGTH_SHORT).show();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        });
    }

    private String replaceWithCommerceValues(String value)
    {
        List<paramEntity> listParams = Utils.getParamsFromLocalPreferences(getContext());

        String result = value.replace("[NOMBRE_COMERCIO]", getString(R.string.app_name));
        result = result.replace("[MAIL_COMERCIO]", listParams.get(Globales.ParametrosDeApp.PARAM_97_MAIL).getValue());
        return result;
    }

}
