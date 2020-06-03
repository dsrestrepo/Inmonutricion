package com.example.inmonutricion;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class RegistradorFCM extends Service {
    public RegistradorFCM() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
