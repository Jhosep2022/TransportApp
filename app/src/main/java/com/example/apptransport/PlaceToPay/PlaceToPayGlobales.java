package com.example.apptransport.PlaceToPay;

public class PlaceToPayGlobales {

    public final static String CURRENCY_ECUADOR="USD";
    public final static int MINUTES_FOR_EXPIRATION_REQUEST=20;
    public final static long ONE_MINUTE_IN_MILLIS=60000;
    public final static String URL_WEBSITE = "http://www.placetopay.com";
    public final static String REDIRECT_URL_OK="http://localhost/redirectOK";
    public final static String REDIRECT_URL_CANCEL="http://localhost/redirectCancel";
    public final static String URL_TEST = "https://test.placetopay.ec/redirection/";
    public final static String URL_PRODUCTION = "https://test.placetopay.ec/redirection/";
    public final static String PARAM_URL_PAYMENT="PARAM_URL_PAYMENT";

    public final static String ECUADOR_DOCUMENT_TYPE="CI";
    public final static String ECUADOR_DOCUMENT_TYPE_PASSPORT="PPN";

    public final static String LOCAL_PREFERENCES_SAVE_REQUEST_ID_TEMP="LOCAL_PREFERENCES_SAVE_REQUEST_ID_TEMP";

    public class StatusPlaceToPay
    {
        public final static String OK="OK";
        public final static String FAILED="FAILED";
        public final static String APPROVED="APPROVED";
        public final static String APPROVED_PARTIAL="APPROVED_PARTIAL";
        public final static String PARTIAL_EXPIRED="PARTIAL_EXPIRED";
        public final static String REJECTED="REJECTED";
        public final static String PENDING="PENDING";
        public final static String PENDING_VALIDATION="PENDING_VALIDATION";
        public final static String REFUNDED="REFUNDED";

    }

}
