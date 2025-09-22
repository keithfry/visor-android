package com.frybynite.magicvisor;

import android.annotation.SuppressLint;
import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.ParcelUuid;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT8;


// This is to surpress warnings about permissions for the Bluetooth service
@SuppressLint("MissingPermission")
public class BluetoothService extends Service {

    protected ScanSettings settings;
    protected ArrayList<ScanFilter> filters;
    private BluetoothLeScanner bleScanner;

    // FeatherWand
    private String WAND_DEVICE_NAME = "FeatherWand";
    private String WAND_SERVICE_UUID_STR = "00001899"; // Starts with. Full id is 00001899-0000-1000-8000-00805F9B34FB
    private String WAND_ACTION_UID = "00007777";
    private BluetoothDevice wandDevice;
    private BluetoothGatt wandGatt;
    private BluetoothGattCharacteristic wandCharacteristic;
    private int wandVal = AS_DOWN;

    // FloraDress
    private String DRESS_DEVICE_NAME = "FloraDress";
    private String DRESS_SERVICE_UUID_STR = "00001888"; // Starts with. Full id is 00001899-0000-1000-8000-00805F9B34FB
    private String DRESS_COLOR_UID = "00001777";
    private BluetoothDevice dressDevice;
    private BluetoothGatt dressGatt;
    private BluetoothGattCharacteristic dressCharacteristic;

    private MDApplication app;

    // If we are stopping the service.
    private boolean stopping = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        app = (MDApplication) getApplication();
        settings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .build();

        // we only want the devices that we care about.
        filters = new ArrayList<ScanFilter>();
        filters.add(new ScanFilter.Builder().setDeviceName(WAND_DEVICE_NAME).build());
        filters.add(new ScanFilter.Builder().setDeviceName(DRESS_DEVICE_NAME).build());


        bleScanner = app.bluetoothAdapter.getBluetoothLeScanner();

    }

    private BroadcastReceiver wandReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction() != null) {
                String action = intent.getAction();
                switch (action) {
//                    case WandActions.ACTION_MOVE_DOWN:
//                        sendColorChangeToDress(1);
//                        break;
//                    case WandActions.ACTION_MOVE_FLAT:
//                        sendColorChangeToDress(2);
//                        break;
//                    case WandActions.ACTION_MOVE_ROTATE:
//                        sendColorChangeToDress(3);
//                        break;
//                    case WandActions.ACTION_MOVE_THROW:
//                        sendColorChangeToDress(4);
//                        break;
//                    case WandActions.ACTION_SHIMMER:
//                        sendColorChangeToDress(0);
//                        break;

                    case WandActions.ACTION_HEARTS:
                        sendAction(4);
                        break;
                    case WandActions.ACTION_RAINBOW:
                        sendAction(3);
                        break;
                    case WandActions.ACTION_SCROLL_TEXT:
                        sendTextActionToDress(2);
                        break;
                    case WandActions.ACTION_POP_TEXT:
                        sendTextActionToDress(5);
                        break;
                    case WandActions.ACTION_CHANGE_COLOR:
                        sendColorAction();
                        break;
                    case WandActions.ACTION_SET_BRIGHTNESS:
                        sendBrightnessAction();
                        break;
                    case WandActions.ACTION_PULSE:
                        sendAction(12);
                        break;
                    case WandActions.ACTION_SET_PULSE_SPEED:
                        sendPulseRateAction();
                        break;
                    case WandActions.ACTION_SET_SCROLL_RATE:
                        sendScrollRateAction();
                        break;

                        /*
                    case WandActions.ACTION_TWINKLE:
                        sendColorChangeToDress(1);
                        break;
                    case WandActions.ACTION_LIGHTNING:
                        sendColorChangeToDress(2);
                        break;
                    case WandActions.ACTION_WHITE_SLIDE:
                        sendColorChangeToDress(4);
                        break;
                    case WandActions.ACTION_TWINKLE_LIGHT:
                        sendColorChangeToDress(5);
                        break;
                    case WandActions.ACTION_RAINBOW_NO_PULSE:
                        sendColorChangeToDress(6);
                        break;
                    case WandActions.ACTION_MAIZE_AND_BLUE:
                        sendColorChangeToDress(7);
                        break;
                    case WandActions.ACTION_PULSE_ALL:
                        sendColorChangeToDress(8);
                        break;
                    case WandActions.ACTION_PULSE_RIGHT:
                        sendColorChangeToDress(9);
                        break;
                    case WandActions.ACTION_PULSE_LEFT:
                        sendColorChangeToDress(10);
                        break;
                    case WandActions.ACTION_MOSTLY_WHITE:
                        //sendColorChangeToDress(11);
                        byte [] data = {
                                11, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18//, 19//, 20, 21, 22, 23, 24, 25
                        };
                        sendDataToDress(data);
                        break;
                         */
                    case WandActions.ACTION_DARK:
                        sendAction(20);
                        break;

                }
            }
        }
    };


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        disconnectDevices();
        scanLeDevice(true);
        WandActions.registerReceiver(getApplicationContext(), wandReceiver);

        return Service.START_STICKY;
    }

    public void broadcast(String action) {
        LocalBroadcastManager mgr = LocalBroadcastManager.getInstance(getApplicationContext());
        mgr.sendBroadcast(new Intent(action));
    }

    public void broadcast(String action, String... strBundle) {
        LocalBroadcastManager mgr = LocalBroadcastManager.getInstance(getApplicationContext());
        Intent intent = new Intent(action);
        for (int i = 0; i < strBundle.length; i++) {
            if ((i + 1) < strBundle.length) {
                intent.putExtra(strBundle[i], strBundle[i + 1]);
            }
        }
        mgr.sendBroadcast(intent);
    }

    @SuppressLint("MissingPermission")
    protected void disconnectDevices() {
        if (wandCharacteristic != null) {
            wandCharacteristic = null;
        }
        if (wandGatt != null) {
            wandGatt.close();
            wandGatt = null;
        }
        if (wandDevice != null) {
            wandDevice = null;
        }
        broadcast(WandActions.ACTION_DISCONNECT);

        if (dressCharacteristic != null) {
            dressCharacteristic = null;
        }
        if (dressGatt != null) {
            dressGatt.close();
            dressGatt = null;
        }
        if (dressDevice != null) {
            dressDevice = null;
        }
        broadcast(DressActions.ACTION_DISCONNECT);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop scanning and make sure now we are stopping scanning.
        scanLeDevice(false, true);
        disconnectDevices();

        WandActions.unregisterReceiver(getApplicationContext(), wandReceiver);

        broadcast(DressActions.ACTION_DISCONNECT);
    }

    private boolean isScanning = false;
    private Handler handler = new Handler();

    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;

    private void scanLeDevice(final boolean enable) {
        scanLeDevice(enable, false);
    }

    private void scanLeDevice(final boolean enable, boolean isStopping) {
        // If we're already stopping, don't stop "more"
        if (stopping) return;
        // Make sure that if we try to come by and start everything again we know that we are stopping.
        // The line above will gate the scan call.

        stopping = isStopping;
        if (enable && !isScanning) {
            // Stops scanning after a predefined scan period.
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.i("bleScan", "STOP scanning");
                    isScanning = false;
                    bleScanner.stopScan(mScanCallback);
                }
            }, SCAN_PERIOD);

            Log.i("bleScan", "START scanning");
            isScanning = true;
            // TODO: reenable this to only find the FloraDress
            bleScanner.startScan(filters, settings, mScanCallback);
            broadcast(DressActions.ACTION_SCANNING);
        } else if (isScanning){
            Log.i("bleScan", "STOP scanning");
            isScanning = false;
            // When not scanning we we're not disconnected, we're just not scanning
            //broadcast(DressActions.ACTION_DISCONNECT);
            bleScanner.stopScan(mScanCallback);
        }
    }

    private MyScanCallback mScanCallback = new MyScanCallback();

    public class MyScanCallback extends ScanCallback {

        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);

            Log.i("result", result.toString());
            try {
                Log.d("device name", result.getDevice().getName());
                for (ParcelUuid puuid : result.getScanRecord().getServiceUuids()) {
                    String pu = puuid.getUuid().toString().toUpperCase();
                    Log.d("puuid", pu);
                    if (pu.startsWith(WAND_SERVICE_UUID_STR) && wandDevice == null) {
                        wandDevice = result.getDevice();
                        connectToWand();
                        break;
                    }
                    else if (pu.startsWith(DRESS_SERVICE_UUID_STR) && dressDevice == null) {
                        dressDevice = result.getDevice();
                        msgLog("DressScan", "Found dress service");
                        connectToDress();
                        break;
                    }
                }
            }
            catch (Exception e) { }
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            for (ScanResult sr : results) {
                Log.i("ScanResult - Results", sr.toString());
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            Log.e("Scan Failed", "Error Code: " + errorCode);
        }
    }

    private void connectToDress() {
        msgLog("", "connectToDress()");
        dressGatt = dressDevice.connectGatt(this, true, dressGattCallback);
    }

    ;

    public void connectToWand() {
        wandGatt = wandDevice.connectGatt(this, false, wandGattCallback);
    }

    private final BluetoothGattCallback wandGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            Log.i("onConnectionStateChange", "Status: " + status);
            switch (newState) {
                case BluetoothProfile.STATE_CONNECTED:
                    Log.i("wandGattCallback", "STATE_CONNECTED");
                    gatt.discoverServices();
                    break;
                case BluetoothProfile.STATE_DISCONNECTED:
                    Log.i("wandGattCallback", "STATE_DISCONNECTED");
                    wandDevice = null;
                    wandGatt = null;
                    wandCharacteristic = null;
                    broadcast(WandActions.ACTION_DISCONNECT);
                    // We want to scan again, but if we are stopping the service we'll just end.
                    scanLeDevice(true);
                    break;
                default:
                    Log.e("gattCallback", "STATE_OTHER");
            }

        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            List<BluetoothGattService> services = gatt.getServices();
            msgLog("WandGattCallback", "onServicesDiscovered()");
            Log.i("onServicesDiscovered", services.toString());
            if (!services.isEmpty()) {
                for (BluetoothGattService s : services) {
                    String ms = s.getUuid().toString().toUpperCase();
                    if (ms.startsWith(WAND_SERVICE_UUID_STR)) {
                        Log.i("onServicesDiscovered", "FOUND SERVICE");
                        for (BluetoothGattCharacteristic charx : s.getCharacteristics()) {
                            String cs = charx.getUuid().toString();
                            if (cs.startsWith(WAND_ACTION_UID)) {
                                Log.i("onServicesDiscovered", "FOUND CHARACTERISTIC");
                                wandCharacteristic = charx;
                                wandGatt.setCharacteristicNotification(wandCharacteristic, true);
                                List<BluetoothGattDescriptor> descs = wandCharacteristic.getDescriptors();
                                if (descs != null) {
                                    for (BluetoothGattDescriptor d : descs) {
                                        d.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                                        wandGatt.writeDescriptor(d);
                                    }
                                }
                                broadcast(WandActions.ACTION_CONNECTED);
                                break;
                            }
                        }

                        // TODO: notify someone here?
                    }
                }
            }
            else {
                msgLog("WandGattCallback", "Service list empty!");
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic
                                                 characteristic, int status) {

        }


        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            // TODO: notify that the characteristic has changed.
            if (characteristic.equals(wandCharacteristic)) {
                handleWandChanged(characteristic);
            }
        }

    };

    private final BluetoothGattCallback dressGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            Log.i("onConnectionStateChange", "Status: " + status);
            switch (newState) {
                case BluetoothProfile.STATE_CONNECTED:
                    msgLog("dressGattCallback", "STATE_CONNECTED");
                    broadcast(DressActions.ACTION_SCANNING);
                    gatt.discoverServices();
                    break;
                case BluetoothProfile.STATE_DISCONNECTED:
                    msgLog("dressGattCallback", "STATE_DISCONNECTED");
                    dressDevice = null;
                    dressGatt = null;
                    dressCharacteristic = null;
                    broadcast(DressActions.ACTION_DISCONNECT);
                    // We want to scan again, but if we are stopping the service we'll just end.
                    scanLeDevice(true);
                    break;
                default:
                    Log.e("gattCallback", "STATE_OTHER");
            }

        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            List<BluetoothGattService> services = gatt.getServices();
            msgLog("DressGATT", "Services Discovered: #" + services.size());
            if (!services.isEmpty()) {
                for (BluetoothGattService s : services) {
                    String ms = s.getUuid().toString().toUpperCase();
                    msgLog("DressGATT", ms);
                    if (ms.startsWith(DRESS_SERVICE_UUID_STR)) {
                        msgLog("DressGATT", "FOUND DRESS SERVICE");
                        for (BluetoothGattCharacteristic charx : s.getCharacteristics()) {
                            String cs = charx.getUuid().toString();
                            msgLog("DressGATT", "Characteristic " + cs);
                            if (cs.startsWith(DRESS_COLOR_UID)) {
                                msgLog("DressGATT", "FOUND DRESS CHARACTERISTIC");
                                dressCharacteristic = charx;
                                dressGatt.setCharacteristicNotification(dressCharacteristic, true);
                                List<BluetoothGattDescriptor> descs = dressCharacteristic.getDescriptors();
                                if (descs != null) {
                                    for (BluetoothGattDescriptor d : descs) {
                                        d.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                                        dressGatt.writeDescriptor(d);
                                    }
                                }
                                broadcast(DressActions.ACTION_CONNECTED);

                                break;
                            }
                        }

                        // TODO: notify someone here?
                    }
                }
            }

        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic
                                                 characteristic, int status) {

        }


        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            // TODO: notify that the characteristic has changed.
            if (characteristic.equals(dressCharacteristic)) {
                handleDressChanged(characteristic);
            }
        }

    };

    private void msgLog(String tag, String s) {
        if (tag != null) Log.i(tag, s);
        String msg = "" + (tag != null?(tag + " :: "):"") + s;
        broadcast(DressActions.ACTION_MESSAGE, "message", msg);
    }

    private void handleDressChanged(BluetoothGattCharacteristic characteristic) {
    }

    static final int AS_DOWN = 0;
    static final int AS_FLAT = 1;
    static final int AS_ROTATE = 2;
    static final int AS_THROW = 3;

    private void handleWandChanged(BluetoothGattCharacteristic characteristic) {
        Integer val = characteristic.getIntValue(FORMAT_UINT8, 0);
        if (val != null && val != wandVal) {
            switch (val) {
                case AS_DOWN:
                    broadcast(WandActions.ACTION_MOVE_DOWN);
                    break;
                case AS_FLAT:
                    broadcast(WandActions.ACTION_MOVE_FLAT);
                    break;
                case AS_ROTATE:
                    broadcast(WandActions.ACTION_MOVE_ROTATE);
                    break;
                case AS_THROW:
                    broadcast(WandActions.ACTION_MOVE_THROW);
                    sendAction(0);
                    break;
            }
            wandVal = val;
        }
    }

    public void sendAction(int actionCode) {
        Log.d("BluetoothService", "Sending actionCode");
        if (dressCharacteristic != null) {
            byte [] data = { (byte)actionCode };
//            if (!dressCharacteristic.setValue(actionCode, FORMAT_UINT8, 0)) {
            if (!dressCharacteristic.setValue(data)) {
                Log.d("BluetoothService", "Unable to set characteristic value");
            };
            if (!dressGatt.writeCharacteristic(dressCharacteristic)) {
                Log.d("BluetoothService", "Unable to SEND characteristic value");
            }
        }
    }

    public void sendDataToVisor(byte[] data) {
        Log.d("BluetoothService", "Sending dress data");
        if (dressCharacteristic != null) {

            if (!dressCharacteristic.setValue(data)) {
                Log.d("BluetoothService", "Unable to set characteristic data ");
            };
            if (!dressGatt.writeCharacteristic(dressCharacteristic)) {
                Log.d("BluetoothService", "Unable to SEND characteristic data ");
            }
        }
    }

    public void sendTextActionToDress(int action) {
        // Convert text to ascii.
        byte [] ascii = app.textToDisplay.getBytes(StandardCharsets.US_ASCII);
        // first encode the utf-16 string as a ByteBuffer
        byte [] data = new byte[ascii.length+1];
        data[0] = (byte)action;
        System.arraycopy(ascii,
                0,
                data,
                1,
                ascii.length);
        sendDataToVisor(data);
    }
    public void sendColorAction() {
        int action = 10;
        // Get the color and send it as R, G, B
        byte data [] = {
                (byte)action,
                (byte)(app.color >> 16 & 0xFF),
                (byte)(app.color >> 8 & 0xFF),
                (byte)(app.color >> 0  & 0xFF)
        };
        sendDataToVisor(data);
    }

    public void sendBrightnessAction() {
        int action = 11;
        byte data [] = {
                (byte)action,
                (byte)(app.brightness & 0xFF)
        };
        sendDataToVisor(data);
    }

    public void sendPulseRateAction() {
        int action = 13;
        byte data [] = {
                (byte)action,
                (byte)(app.pulseSpeedRate & 0xFF)
        };
        sendDataToVisor(data);
    }
    public void sendScrollRateAction() {
        int action = 14;
        byte data [] = {
                (byte)action,
                (byte)(app.scrollRate & 0xFF)
        };
        sendDataToVisor(data);
    }

}
