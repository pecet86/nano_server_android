package pl.pecet.nano_android_server;

import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.io.IOException;

public abstract class ServerService extends BaseService {

    public static final String MODE_START_SERVER = "START_SERVER";
    public static final String MODE_STOP_SERVER = "STOP_SERVER";

    private NanoAndroidServer server;

    @Override
    public void onCreate() {
        super.onCreate();

        server = createServer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopServer();
        server = null;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new ServerIBinder(this);
    }

    @Override
    protected int onStartCommand(String mode, @Nullable Intent intent, int flags, int startId) {
        if (mode == null) {
            stopSelf2();
            return START_NOT_STICKY;
        }

        switch (mode) {
            case MODE_START_SERVER: {
                startServer();
                return _onStartCommand(intent, flags, startId);
            }
            case MODE_STOP_SERVER: {
                stopServer();
                return _onStartCommand(intent, flags, startId);
            }
        }

        return START_NOT_STICKY;
    }

    @NonNull
    protected abstract NanoAndroidServer createServer();

    private void startServer() {
        log("startServer", "init");
        try {
            server.start();
        } catch (IOException ex) {
            log("startServer", ex.getMessage(), ex);
        }
    }

    private void stopServer() {
        log("stopServer", "init");
        if (server != null && server.wasStarted()) {
            server.stop();
        }
    }

    public static class ServerIBinder extends Binder {
        private ServerService service;

        public ServerIBinder(ServerService service) {
            this.service = service;
        }

        @RequiresApi(api = Build.VERSION_CODES.Q)
        public ServerIBinder(@Nullable String descriptor, ServerService service) {
            super(descriptor);
            this.service = service;
        }

        public void start() {
            service.startServer();
        }

        public void stop() {
            service.stopServer();
        }
    }
}
