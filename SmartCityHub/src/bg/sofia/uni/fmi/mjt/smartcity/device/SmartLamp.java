package bg.sofia.uni.fmi.mjt.smartcity.device;

import bg.sofia.uni.fmi.mjt.smartcity.enums.DeviceType;

import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;

public class SmartLamp extends AbstractSmartDevice {


    public SmartLamp(String name, double powerConsumption, LocalDateTime installationDateTime) {
        super(name, powerConsumption, installationDateTime);

        this.UNIQUE_DEVICE_NUMBER = smartLampCount;
        smartLampCount++;

        this.DEVICE_ID = getType().getShortName() + "-" + getName() + "-" + this.UNIQUE_DEVICE_NUMBER;

    }

    /**
     * Returns the ID of the device.
     */
    @Override
    public String getId() {
        return DEVICE_ID;
    }

    /**
     * Returns the type of the device.
     */
    @Override
    public DeviceType getType() {
        return DeviceType.LAMP;
    }

    static int smartLampCount = 0;

    private final int UNIQUE_DEVICE_NUMBER;
    private final String DEVICE_ID;
}
