package com.example.apptransport.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TermAndConditionResult {
    @Expose
    @SerializedName("idDocument")
    public String idDocument;

    @Expose
    @SerializedName("documentTitle")
    public String documentTitle;

    @Expose
    @SerializedName("documentText")
    public String documentText;
}
