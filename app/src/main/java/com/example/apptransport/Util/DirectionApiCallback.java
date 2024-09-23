package com.example.apptransport.Util;


import com.akexorcist.googledirection.model.Direction;

public interface DirectionApiCallback {
    void onDirectionSuccess(Direction direction);
    void onDirectionError();
}
