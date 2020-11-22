package bg.sofia.uni.fmi.mjt.smartcity.device;

import bg.sofia.uni.fmi.mjt.smartcity.enums.DeviceType;

import java.time.Duration;
import java.time.LocalDateTime;

public abstract class AbstractSmartDevice implements SmartDevice {


    public AbstractSmartDevice(String name, double powerConsumption, LocalDateTime installationDateTime) {
        this.name = name;
        this.powerConsumption = powerConsumption;
        this.installationDateTime = installationDateTime;
    }


    /**
     * Returns the name of the device.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Returns the power consumption of the device.
     * For example, a lamp may consume 1kW/hour.
     */
    @Override
    public double getPowerConsumption() {
        return powerConsumption;
    }

    /**
     * Returns the date and time of device installation.
     * This is a time in the past when the device was 'physically' installed.
     * It is not related to the time when the device is registered in the Hub.
     */
    @Override
    public LocalDateTime getInstallationDateTime() {
        return installationDateTime;
    }


    public Double getTotalPowerConsumption() {
        long hoursPassedSinceInstallation = Duration.between(getInstallationDateTime(), LocalDateTime.now()).toHours();
        return hoursPassedSinceInstallation * getPowerConsumption();
    }

    private String name;
    private double powerConsumption;
    private LocalDateTime installationDateTime;
}
