package com.example.apptransport.Util;

import com.apreciasoft.mobile.asremis.BuildConfig;
import com.apreciasoft.mobile.asremis.Http.HttpConexion;

public class Globales {

    public static final String  URL_PHOTO_IMAGE_USER = HttpConexion.BASE_URL+HttpConexion.base+"/Frond/img_users/{USER_ID}.JPEG";
    //public static final String SERVER_KEY = BuildConfig.GoogleApiKey; // "AIzaSyAiB-kD4fRDqGa8ET4T-hwF3EoFvMf_sag";//BuildConfig;//
    public static final String NAME_FIRMA_IMG="asRemisFirma.jpg";
    public static String CURRENT_TRAVEL_NAME_CHOFER="";
    public static int DISTANCE_FOR_POINT_TO_POINT=700;
    public static final String NEED_LOGIN_NEW_CLIENT_STYLE_V4_3_1_CLIENT="NEED_LOGIN_NEW_CLIENT_STYLE_V4_3_1_CLIENT";
    public static final String NEED_LOGIN_NEW_CLIENT_STYLE_V4_3_2_CLIENT="NEED_LOGIN_NEW_CLIENT_STYLE_V4_3_2_CLIENT";
    public static final String NEED_LOGIN_NEW_CLIENT_STYLE_V4_4_1_CLIENT="NEED_LOGIN_NEW_CLIENT_STYLE_V4_4_1_CLIENT";
    public static int MAX_RETRY_SAVE_CREDIT_CARD_DATA=3;
    public static final String KEY_FOR_INIT_TIME_TRAVEL_BY_HOUR="KEY_FOR_INIT_TIME_TRAVEL_BY_HOUR";
    public static final String KEY_FOR_END_TIME_TRAVEL_BY_HOUR="KEY_FOR_END_TIME_TRAVEL_BY_HOUR";
    public static final int ID_REASON_CANCEL_TRAVEL_NO_HAY_AUTOS=107;

    public static final String IS_DRIVER_IN_SERVICE = "IS_DRIVER_IN_SERVICE";
    public static String ID_LISTA_DE_BENEFICIOS_X_KM_EN_EMPRESA="0";

    public enum AutocompleteType {ADDRESS, SELECT_ADDRESS_FROM_MAP }

    public class StatusTravel {

        public static final int VIAJE_RECHAZADO_POR_AGENCIA=0;
        public static final int VIAJE_ACEPTADO_POR_AGENCIA=1;
        public static final int CHOFER_ASIGNADO=2;

        public static final int VIAJE_ACEPTADO_POR_CHOFER=4;
        public static final int VIAJE_EN_CURSO=5;
        public static final int VIAJE_FINALIZADO=6;
        public static final int VIAJE_RECHAZADO_POR_CHOFER=7;
        public static final int VIAJE_CANCELADO_POR_CLIENTE=8;
    };

    public class StatusTypeTravel{
        public static final int VIAJE=1;
        public static final int RESERVA=2;
    }

    public class StatusToast{
        public static final int INFO=0;
        public static final int SUCCESS=1;
        public static final int FAIL=2;
        public static final int WARNING=3;
    }

    public class ProfileId{
        public static final int CLIENT_COMPANY=5;
        public static final int CLIENT_PARTICULAR=2;
        public static final int DRIVER=3;
    }

    public class PreFinishValues{
        public static final int DEFAULT=0;
        public static final int APP_PREFINALIZED=1;
        public static final int WEB_FINALIZED=2;
    }

    public class NotificationChannelCustom
    {
        public static final String CHOFFER_NUEVO_VIAJE="CHOFFER_NUEVO_VIAJE_2";
        public static final String CHOFFER_NUEVA_RESERVA="CHOFFER_NUEVA_RESERVA_2";
        public static final String CHOFFER_VIAJE_CANCELADO="CHOFFER_VIAJE_CANCELADO_2";
        public static final String CHOFFER_RESERVA_CANCELADO="CHOFFER_RESERVA_CANCELADO_2";
        public static final String CHOFFER_VIAJE_ACEPTADO_POR_AGENCIA="CHOFFER_VIAJE_ACEPTADO_POR_AGENCIA_2";
        public static final String CHOFFER_MENSAJE_CHAT_RECBIDO="CHOFFER_MENSAJE_CHAT_RECBIDO";


        public static final String CLIENTE_VIAJE_ACEPTADO_POR_EMPRESA="CLIENTE_VIAJE_ACEPTADO_POR_EMPRESA_2";
        public static final String CLIENTE_VIAJE_ACEPTADO_POR_CHOFER="CLIENTE_VIAJE_ACEPTADO_POR_CHOFER_2";
        public static final String CLIENTE_CHOFER_CERCA="CLIENTE_CHOFER_CERCA_2";
        public static final String CLIENTE_RESERVA_CONFIRMADA="CLIENTE_RESERVA_CONFIRMADA_2";
        public static final String CLIENTE_VIAJE_CANCELADO_POR_CHOFER="CLIENTE_VIAJE_CANCELADO_POR_CHOFER_2";
        public static final String CLIENTE_CHOFER_ASIGNADO="CLIENTE_CHOFER_ASIGNADO_2";

    }

    public class Param78BajadaDeBandera
    {
        public static final int SOLO_LISTA_BENEFICIOS_X_KM_0=0;
        public static final int SUMA_VALOR_MINIMO_O_RECORRIDO_CHOFER_1=1;
        public static final int RECORRIDO_SUPERA_MINIMO_COBRA_VIAJE_REAL_2=2;

    }

    public class ParametrosDeApp{
        public static final int PARAM_1_PRECIO_LISTA_X_KM=0;
        public static final int PARAM_3_CIERRE_VIAJE_EMPRESA_EN_WEB=2;
        public static final int PARAM_5_PRECIO_LISTA_TIEMPO_DE_ESPERA=4;
        public static final int PARAM_6_PRECIO_LISTA_TIEMPO_DE_VUELTA=5;
        public static final int PARAM_7_PORCENTAJE_AUMENTO_HORA_NOCTURNA=6;
        public static final int PARAM_8_HORA_NOCTURNA_DESDE=7;
        public static final int PARAM_9_HORA_NOCTURNA_HASTA=8;
        public static final int PARAM_16_MONTO_MINIMO_VIAJE=15;
        public static final int PARAM_26_VER_PRECIO_EN_VIAJE_EN_APP =25;

        public static final int PARAM_35_USA_RESERVAS =34;
        public static final int PARAM_36_USA_VIAJES =35;

        public static final int PARAM_49_VER_TELEFONO_DE_PASAJERO=48;

        public static final int PARAM_57_BENEFICIO_X_KM_PARTICULARES=56;

        public static final int PARAM_66_VER_PRECIO_EN_VIAJE_EN_APP=65;
        public static final int PARAM_67_CIERRE_VIAJE_PARTICULAR_Y_EXPRESS_EN_WEB=66;
        public static final int PARAM_68_PAGA_CON_TARJETA=67;
        public static final int PARAM_69_KEY_MP=68;

        public static final int PARAM_74_PRECIO_X_HORA_AYUDANTES=73;
        public static final int PARAM_78_BAJADA_DE_BANDERA =77;
        public static final int PARAM_79_MP_SECRET_KEY =78;

        public static final int PARAM_83_PERMITE_EMPRESA_VIAJE_PUNTO_A_PUNTO =82;
        public static final int PARAM_87_ESPERAR_PANEL_PARA_FINALIZAR =86;
        public static final int PARAM_94_REGISTRAR_CHOFER_CAMPOS_OPCIONALES=93;
        public static final int PARAM_95_MIN_ESPERA_NO_RECARGA_EN_PARTICULARES=94;
        public static final int PARAM_96_PORCENTAJE_ADICIONAL_POR_PAGO_CON_TARJETA=95;
        public static final int PARAM_97_MAIL=96;
        public static final int PARAM_99_REGISTRAR_CHOFER_OBLIGATORIO_SUBIR_FOTOS=98;

        public static final int PARAM_101_ACCEPT_PAYMENT_CARD_IN_CLIENT =100;
        public static final int PARAM_103_PAYMENT_CARD_PROVIDER =102;
        public static final int PARAM_104_GOOGLE_MAPS_API_KEY =103;
        public static final int PARAM_105_MUESTRA_FLETE_EN_APP_PASAJERO =104;

        public static final int PARAM_114_HABILITA_TERMINOS_Y_CONDICIONES = 113;



    }

    public class SaveImageKey{
        public static final String UPLOAD_FOTO_DE_PERFIL_KEY = "_PROFILE";
        public static final String UPLOAD_LICENCIA_DEL_CONDUCTOR_KEY = "_LICENSE";
        public static final String UPLOAD_FOTO_DEL_VEHICULO_KEY = "_CAR";
        public static final String UPLOAD_CHAPA_DEL_VEHICULO_KEY = "_IDV";
        public static final String UPLOAD_ANTECEDENTES_POLICIALES_KEY = "_CRIMREC";
    }

    public class PaymentCardProviders{
        public static final String MERCADO_PAGO="1";
        public static final String PLACE_TO_PAY="2";
        public static final String PAYPAL="3";
    }

    public class ClientPaymentStatus
    {
        public static final int NO_PAGA_CON_TARJETA=0;
        public static final int PAGA_CLIENTE_PENDIENTE=1;
        public static final int PAGA_CLIENTE_REALIZADO=2;
        public static final int PAGA_CLIENTE_ERROR=3;
        public static final int PAGA_CLIENTE_CANCELADO_POR_CLIENTE=4;
        public static final int PAGA_CLIENTE_TIEMPO_EXPIRADO=5;
        public static final int PAGA_CLIENTE_REALIZADO_PERO_ERROR_AL_GUARDAR_DATOS=6;
        public static final int PAGA_CLIENTE_INICIO_EL_PROCESO_DE_PAGO=7;
    }

    public class TypeAddressAutocomplete
    {
        public static final int ADDRESS_FROM=1;
        public static final int ADDRESS_TO=2;
    }

    public class SoundNamesForNotification
    {
        public static final String SONIDO_1_NUEVA_RESERVA="sonido1_nueva_reserva";
        public static final String SONIDO_2_NUEVA_CARRERA="sonido2_nueva_carrera";
        public static final String SONIDO_3_CARRERA_CANCELADA="sonido3_carrera_cancelado";
        public static final String SONIDO_4_RESERVA_CANCELADA="sonido4_reserva_cancelada";
        public static final String SONIDO_5_VIAJE_FINALIZADO_Y_APROBADO_POR_LA_AGENCIA="sonido5_viaje_finalizado_y_aprovado_por_agencia";
        public static final String SONIDO_6_CARRERA_ACEPTADA="sonido6_carrera_aceptada";
        public static final String SONIDO_7_VIAJE_ACEPTADO="sonido7_viaje_aceptado";
        public static final String SONIDO_8_RESERVA_CONFIRMADA="sonido8_reserva_confirmada";
        public static final String SONIDO_9_CHOFER_LLEGANDO="sonido9_chofer_llegando";
        public static final String SONIDO_10_CHOFER_EN_CAMINO="sonido10_chofer_en_camino";
        public static final String SONIDO_11_CARRERA_CANCELADA_POR_CHOFER="sonido11_carrera_cancelado_por_chofer";
        public static final String SONIDO_12_VIAJE_CANCELADO_POR_CHOFER="sonido12_viaje_cancelado_por_chofer";
        public static final String SONIDO_DEFAULT="remis";

    }

    public class ChatMessageType{
        public static final String DRIVER_SENDER="1";
        public static final String WEB_SENDER="2";
    }
}
