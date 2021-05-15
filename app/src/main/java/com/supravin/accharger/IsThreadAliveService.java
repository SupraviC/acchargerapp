package com.supravin.accharger;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class IsThreadAliveService extends Service {
    public IsThreadAliveService()
    {

    }

    @Override
    public IBinder onBind(Intent intent)
    {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
