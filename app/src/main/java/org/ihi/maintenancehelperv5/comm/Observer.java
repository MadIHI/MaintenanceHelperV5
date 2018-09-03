package org.ihi.maintenancehelperv5.comm;


import com.clj.fastble.data.BleDevice;

public interface Observer {

    void disConnected(BleDevice bleDevice);
}
