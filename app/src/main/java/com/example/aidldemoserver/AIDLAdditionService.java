package com.example.aidldemoserver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

public class AIDLAdditionService extends Service {
//    private MyImpl impl = new MyImpl();
    public AIDLAdditionService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
//         TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
//        return impl;
        return binder;
    }

//    private class MyImpl extends IMyAidlAdditionInterface.Stub{
//
//        @Override
//        public int calculateAddition(int a, int b) throws RemoteException {
//            return a+b;
//        }
//    }
    private final IMyAidlAdditionInterface.Stub binder = new IMyAidlAdditionInterface.Stub() {
        @Override
        public int calculateAddition(int a, int b) throws RemoteException {
            return a+b;
        }
    };
}