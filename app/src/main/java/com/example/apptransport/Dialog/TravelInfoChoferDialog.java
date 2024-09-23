package com.example.apptransport.Dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.apreciasoft.mobile.asremis.Entity.Currency;
import com.apreciasoft.mobile.asremis.Entity.InfoTravelEntity;
import com.apreciasoft.mobile.asremis.Entity.Passenger;
import com.apreciasoft.mobile.asremis.Entity.resp;
import com.apreciasoft.mobile.asremis.Http.HttpConexion;
import com.apreciasoft.mobile.asremis.R;
import com.apreciasoft.mobile.asremis.Services.ServicesTravel;
import com.apreciasoft.mobile.asremis.Util.Globales;
import com.apreciasoft.mobile.asremis.Util.GlovalVar;
import com.apreciasoft.mobile.asremis.Util.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

/**
 * @author Marlon Viana
 * @email 92marlonViana@gmail.com
 */
@SuppressLint("ValidFragment")
public class TravelInfoChoferDialog extends DialogFragment {
    private View v;
    public static TextView txt_client_info = null;
    public static TextView txt_observationFlight = null;
    public static TextView txt_date_info = null;
    public static TextView txt_destination_info = null;
    public static TextView txt_origin_info = null;
    public static TextView txt_km_info = null;
    public static TextView txt_calling_info = null;
    public static TextView txt_observationFromDriver = null;
    public static TextView txt_amount_info = null;
    public static TextView txt_pasajeros_info;
    public static TextView txt_lote = null;
    public static TextView txt_flete = null;
    public static TextView txt_piso_dialog = null;
    public static TextView txt_issleep_dialog = null;
    public static TextView txt_isretunr_dialog = null;
    public static TextView txt_dpto_dialog = null;
    public static ImageView your_imageView;

    private InfoTravelEntity infoTravelEntity;
    public  GlovalVar gloval;
    public static int PARAM_26_VER_PRECIO_EN_VIAJE = 0;


    public TravelInfoChoferDialog (InfoTravelEntity infoTravelEntity, GlovalVar gloval) {
        this.infoTravelEntity= infoTravelEntity;
        this.gloval= gloval;
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return createSimpleDialog();
    }

    public AlertDialog createSimpleDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        v = inflater.inflate(R.layout.dialog_info_travel_chofer, null);
        builder.setView(v);

        addView();
        setValores();
        setPhoto();
        getPassengerInfo();

        return builder.create();
    }

    private void addView(){

       txt_date_info =  v.findViewById(R.id.txt_date_info);
       txt_client_info =  v.findViewById(R.id.txt_client_info);
       txt_destination_info =  v.findViewById(R.id.txt_destination_info);
       txt_origin_info =  v.findViewById(R.id.txt_origin_info);
       txt_lote =  v.findViewById(R.id.txt_lote);
       txt_flete =  v.findViewById(R.id.txt_flete);
       txt_piso_dialog =  v.findViewById(R.id.txt_piso_dialog);
       txt_dpto_dialog =  v.findViewById(R.id.txt_dpto_dialog);
       txt_issleep_dialog =  v.findViewById(R.id.txt_issleep_dialog);
       txt_isretunr_dialog =  v.findViewById(R.id.txt_isretunr_dialog);
       txt_km_info =  v.findViewById(R.id.txt_km_info);
       txt_amount_info =  v.findViewById(R.id.txt_amount_info);
       txt_calling_info =  v.findViewById(R.id.txt_calling_info);
       txt_observationFromDriver =  v.findViewById(R.id.txt_observationFromDriver);
       txt_observationFlight =  v.findViewById(R.id.txt_observationFlight);
       txt_pasajeros_info =  v.findViewById(R.id.txt_pasajeros_info);
       your_imageView =  v.findViewById(R.id.img_face_client);

        ImageButton buttonCancelar= v.findViewById(R.id.button_cancelar);
        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        txt_calling_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.openDialer(getActivity(), txt_calling_info.getText().toString());
            }
        });

        ImageView imgPhone =  v.findViewById(R.id.imageView8);
        imgPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.openDialer(getActivity(), txt_calling_info.getText().toString());
            }
        });
    }

    private void setPhoto()
    {
        try
        {

            String url = Utils.getUrlImageUser(infoTravelEntity.getIdUserClient());
            Glide.with(your_imageView.getContext())
                    .load(url)
                    .placeholder(R.drawable.ic_update)
                    .apply(RequestOptions.circleCropTransform())
                    .error(R.drawable.ic_user)
                    //.centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(your_imageView);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

    }


    private void setValores(){
        Currency currecy =  Utils.getCurrency(getContext());
        txt_date_info.setText(infoTravelEntity.getMdate());
        txt_client_info.setText(infoTravelEntity.getClient());
        txt_client_info.setText(infoTravelEntity.getClient());

        int param49_ver_Telefono_de_pasajero = Integer.parseInt(gloval.getGv_param().get(Globales.ParametrosDeApp.PARAM_49_VER_TELEFONO_DE_PASAJERO).getValue());// SE PUEDE VER TELEFONO DE PASAJEROS

        if(infoTravelEntity.getPhoneNumber() != null && param49_ver_Telefono_de_pasajero == 1) {
            txt_calling_info.setText(infoTravelEntity.getPhoneNumber());
        }

        if(!infoTravelEntity.getObservationFromDriver().isEmpty()){
            txt_observationFromDriver.setText(infoTravelEntity.getObservationFromDriver());
        }

        if (!infoTravelEntity.getObsertavtionFlight().isEmpty()){
            txt_observationFlight.setText(infoTravelEntity.getObsertavtionFlight());
        }

        if (infoTravelEntity.getPasajero()!=null){
            txt_pasajeros_info.setText(infoTravelEntity.getPasajero());
        }


        if (infoTravelEntity.isTravelMultiOrigin == null || getString(R.string.no).toUpperCase().equals(infoTravelEntity.isTravelMultiOrigin)){
            txt_origin_info.setText(infoTravelEntity.getNameOrigin());
        }else{
            int control = 1;
            String multiOrigen = "";

            if (infoTravelEntity.getOriginMultipleDesc1() != null ){
                multiOrigen += control+") "+infoTravelEntity.getOriginMultipleDesc1()+"\n";
                control++;
            }

            if (infoTravelEntity.getOriginMultipleDesc2() != null){
                multiOrigen += control+") "+infoTravelEntity.getOriginMultipleDesc2()+"\n";
                control++;
            }

            if (infoTravelEntity.getOriginMultipleDesc3() != null){
                multiOrigen += control+") "+infoTravelEntity.getOriginMultipleDesc3()+"\n";
                control++;
            }

            if (infoTravelEntity.getOriginMultipleDesc4() != null){
                multiOrigen += control+") "+infoTravelEntity.getOriginMultipleDesc4()+"\n";
            }

            txt_origin_info.setText(multiOrigen);
        }

        if (infoTravelEntity.isMultiDestination == null || getString(R.string.no).toUpperCase().equals(infoTravelEntity.isMultiDestination )){
            txt_destination_info.setText(infoTravelEntity.getNameDestination());
        }else{
            StringBuilder multiOrigen = new StringBuilder();
            String[] destination = infoTravelEntity.MultiDestination.split("-");

            for (int i = 0;i<destination.length; i++){
                multiOrigen.append(i + 1).append(") ").append(destination[i]).append("\n");
            }

            txt_destination_info.setText(multiOrigen.toString());
        }

        txt_lote.setText(infoTravelEntity.getLot());
        txt_flete.setText(String.valueOf(infoTravelEntity.getIsFleetTravelAssistance()));
        txt_dpto_dialog.setText(infoTravelEntity.getDepartment());
        txt_piso_dialog.setText(infoTravelEntity.getFLOOR());

        if(infoTravelEntity.getIsExitSleepIntravel() == 1){
            txt_issleep_dialog.setText(getString(R.string.si));
        }else {
            txt_issleep_dialog.setText(getString(R.string.no));
        }

        if(infoTravelEntity.getIsTravelFromReturn() == 1){
            txt_isretunr_dialog.setText(getString(R.string.si));
        }else {
            txt_isretunr_dialog.setText(getString(R.string.no));
        }

        txt_km_info.setText(infoTravelEntity.getDistanceLabel());

        PARAM_26_VER_PRECIO_EN_VIAJE =  Integer.parseInt(gloval.getGv_param().get(Globales.ParametrosDeApp.PARAM_26_VER_PRECIO_EN_VIAJE_EN_APP).getValue());// PRECIO DE LISTA
        if(infoTravelEntity.getIdSatatusTravel() == Globales.StatusTravel.VIAJE_FINALIZADO)
        {
            mostrarPrecio(PARAM_26_VER_PRECIO_EN_VIAJE, infoTravelEntity.getTotalAmount(), currecy, infoTravelEntity.isTravelComany==1);
        }else
        {
            mostrarPrecio(PARAM_26_VER_PRECIO_EN_VIAJE, infoTravelEntity.getAmountCalculate(), currecy, infoTravelEntity.isTravelComany==1);
        }
    }

    private Double getParamValue(int pos, Double defaultValue)
    {
        try {
            return Double.parseDouble(gloval.getGv_param().get(pos).getValue());
        }
        catch (Exception ex)
        {
            return defaultValue;
        }
    }

    private void mostrarPrecio(int param26_mostrarPrecio, String amount, Currency currency, boolean isTravelCompany)
    {
        String amountToShow="0";
        if (Utils.isShowPrice(param26_mostrarPrecio, isTravelCompany)) {
            amountToShow=amount;
        }
        txt_amount_info.setText(currency.getSymbol().concat(" ").concat(amountToShow));
    }

    private void obtenerFoto() {

    }

    private void getPassengerInfo()
    {
        try{
            ServicesTravel servicesTravel = HttpConexion.getUri().create(ServicesTravel.class);
            Call<List<Passenger>> call = servicesTravel.getPassengerFromTravel(infoTravelEntity.getIdTravel());

            call.enqueue(new Callback<List<Passenger>>() {
                @Override
                public void onResponse(Call<List<Passenger>> call, Response<List<Passenger>> response) {
                    try{
                        if(response!=null && response.body()!=null)
                        {
                            int param49_ver_Telefono_de_pasajero = Integer.parseInt(gloval.getGv_param().get(Globales.ParametrosDeApp.PARAM_49_VER_TELEFONO_DE_PASAJERO).getValue());// SE PUEDE VER TELEFONO DE PASAJEROS
                            String result="";

                            List<Passenger> listPassenger = response.body();
                            if(listPassenger.size()==1){
                                //Si es Viaje particular, viene la lista con datos
                                if(!TextUtils.isEmpty(listPassenger.get(0).getListPassenger()))
                                {
                                    result = getString(R.string.listado_de_pasajeros).concat("\n\n")
                                            .concat(listPassenger.get(0).getListPassenger().replace("/","\n"));
                                    txt_pasajeros_info.setText(result);
                                }
                                else if(listPassenger.get(0).getpAQuantity()!=null){
                                    //Si es Viaje Empresa y es por cantidad en vez de por lista de nombres
                                    result = getString(R.string.listado_de_pasajeros).concat("\n\n")
                                            .concat(getString(R.string.adultos)).concat(listPassenger.get(0).getpAQuantity().getAdults()==null?"0" : String.valueOf(listPassenger.get(0).getpAQuantity().getAdults())).concat("\n")
                                            .concat(getString(R.string.ninios)).concat(listPassenger.get(0).getpAQuantity().getChildren()==null?"0" :String.valueOf(listPassenger.get(0).getpAQuantity().getChildren())).concat("\n")
                                            .concat(getString(R.string.bebes)).concat(listPassenger.get(0).getpAQuantity().getBabies()==null?"0" :String.valueOf(listPassenger.get(0).getpAQuantity().getBabies())).concat("\n")
                                            .concat(getString(R.string.movilidad_reducida)).concat(listPassenger.get(0).getpAQuantity().getDisable()==null?"0" :String.valueOf(listPassenger.get(0).getpAQuantity().getDisable()));
                                    txt_pasajeros_info.setText(result);
                                }
                                else{
                                    String phone = param49_ver_Telefono_de_pasajero ==1 && listPassenger.get(0).getPhonePasaguer()!=null && !"0".equals(listPassenger.get(0).getPhonePasaguer())  ? " (".concat(listPassenger.get(0).getPhonePasaguer()).concat(")"): "";
                                    txt_pasajeros_info.setText(listPassenger.get(0).getFullNamePanguer().concat(phone));
                                }
                            }
                            else{
                                String phone;
                                result = getString(R.string.listado_de_pasajeros).concat("\n\n");
                                for (int i=1;i<listPassenger.size();i++) {
                                    phone = param49_ver_Telefono_de_pasajero==1 && listPassenger.get(i).getPhonePasaguer()!=null && !"0".equals(listPassenger.get(i).getPhonePasaguer())  ? " (".concat(listPassenger.get(i).getPhonePasaguer()).concat(")"): "";
                                    result = result.concat(listPassenger.get(i).getFullNamePanguer()).concat(phone).concat("\n");
                                }
                                txt_pasajeros_info.setText(result);
                            }
                        }
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<List<Passenger>> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }


}
