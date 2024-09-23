package com.example.apptransport.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.appbar.AppBarLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.apreciasoft.mobile.asremis.Fragments.FragmentNewClient;
import com.apreciasoft.mobile.asremis.Fragments.FragmentRegisterForm;
import com.apreciasoft.mobile.asremis.Fragments.ListenerFragmentRegister;
import com.apreciasoft.mobile.asremis.R;
import com.apreciasoft.mobile.asremis.Util.DisplayUtil;

/**
 * @author Marlon Viana
 * @email 92marlonViana@gmail.com
 */
public class FragmentManagerRegisterActivity extends AppCompatActivity implements ListenerFragmentRegister {

    private static final int ACTIVITY_CREATE_DRIVER = 1;
    private TextView tituloToolbar;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private String firstName, lastName, email, socialid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_manager);
        setToolbar();
        loadParams();
        goToRegister();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (ACTIVITY_CREATE_DRIVER):
                if(resultCode==RESULT_OK){
                    this.setResult(Activity.RESULT_OK);
                    finish();
                }
                break;
        }
    }

    private void loadParams()
    {
        try{
            Bundle b = getIntent().getExtras();
            if(b != null)
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


    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tituloToolbar= findViewById(R.id.title_toolbar);

        if (toolbar != null) {
            if (getSupportActionBar() != null)
                getSupportActionBar().setDisplayShowTitleEnabled(false);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                if (hasToolbarPadding()) {
                    AppBarLayout appBar = findViewById(R.id.toolbar_login);
                    if (appBar != null)
                        appBar.setPadding(0, DisplayUtil.getStatusBarHeight(this), 0, 0);
                }
            }
        }
    }

    private boolean hasToolbarPadding() {
        return true;
    }

    private void goToRegister() {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        FragmentRegisterForm fragment = new FragmentRegisterForm(this);
        Bundle args = new Bundle();
        args.putString("firstName", firstName);
        args.putString("lastName", lastName);
        args.putString("email", email);
        args.putString("socialid", socialid);
        fragment.setArguments(args);
        fragmentTransaction.add(R.id.fragment_register, fragment,"REGISTER_FRAGMENT_TAG");
        fragmentTransaction.commit();
    }

    public void goToRegisterCliente() {
        tituloToolbar.setText(R.string.registro_pasajero);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        FragmentNewClient fragment = new FragmentNewClient();
        Bundle args = new Bundle();
        args.putString("firstName", firstName);
        args.putString("lastName", lastName);
        args.putString("email", email);
        args.putString("socialid", socialid);
        fragment.setArguments(args);
        fragmentTransaction.add(R.id.fragment_register, fragment,"REGISTERCLIENT_FRAGMENT_TAG");
        fragmentTransaction.commit();
    }


    public void goToRegisterChofer()
    {

        // LAMAMOS A EL SEGUNDO ACTIVITY//
        Intent home = new Intent(FragmentManagerRegisterActivity.this, NewDriverActivity.class);
        Bundle args = new Bundle();
        home.putExtra("firstName", firstName);
        home.putExtra("lastName", lastName);
        home.putExtra("email", email);
        home.putExtra("socialid", socialid);
        //startActivity(home);
        startActivityForResult(home, ACTIVITY_CREATE_DRIVER);


        /*tituloToolbar.setText(R.string.registro_chofer);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        FragmentNewDriver fragment = new FragmentNewDriver();
        fragmentTransaction.add(R.id.fragment_register, fragment,"REGISTERCHOFER_FRAGMENT_TAG");
        fragmentTransaction.commit();*/
    }
}
