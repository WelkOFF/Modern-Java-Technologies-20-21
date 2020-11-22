package bg.sofia.uni.fmi.mjt.smartcity.device;

import bg.sofia.uni.fmi.mjt.smartcity.enums.DeviceType;

import java.time.LocalDateTime;

public class SmartCamera extends AbstractSmartDevice {


    public SmartCamera(String name, double powerConsumption, LocalDateTime installationDateTime) {
        super(name, powerConsumption, installationDateTime);

        this.UNIQUE_DEVICE_NUMBER = smartCameraCount;
        smartCameraCount++;

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
        return DeviceType.CAMERA;
    }

    static int smartCameraCount = 0;

    private final int UNIQUE_DEVICE_NUMBER;
    private final String DEVICE_ID;
}
