package bg.sofia.uni.fmi.mjt.smartcity.hub;

import bg.sofia.uni.fmi.mjt.smartcity.device.AbstractSmartDevice;
import bg.sofia.uni.fmi.mjt.smartcity.device.SmartCamera;
import bg.sofia.uni.fmi.mjt.smartcity.device.SmartDevice;
import bg.sofia.uni.fmi.mjt.smartcity.enums.DeviceType;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class SmartCityHub {

    public SmartCityHub() {

    }

    /**
     * Adds a @device to the SmartCityHub.
     *
     * @throws IllegalArgumentException         in case @device is null.
     * @throws DeviceAlreadyRegisteredException in case the @device is already registered.
     */
    public void register(SmartDevice device) throws DeviceAlreadyRegisteredException {

        if (device == null) {
            throw new IllegalArgumentException();
        } else if (devices.containsKey(device.getId())) {
            throw new DeviceAlreadyRegisteredException();
        } else {
            devices.put(device.getId(), device);
        }

    }

    /**
     * Removes the @device from the SmartCityHub.
     *
     * @throws IllegalArgumentException in case null is passed.
     * @throws DeviceNotFoundException  in case the @device is not found.
     */
    public void unregister(SmartDevice device) throws DeviceNotFoundException {

        if (device == null) {
            throw new IllegalArgumentException();
        } else if (devices.containsKey(device.getId())) {
            devices.remove(device.getId());
        } else {
            throw new DeviceNotFoundException();
        }

    }

    /**
     * Returns a SmartDevice with an ID @id.
     *
     * @throws IllegalArgumentException in case @id is null.
     * @throws DeviceNotFoundException  in case device with ID @id is not found.
     */
    public SmartDevice getDeviceById(String id) throws DeviceNotFoundException {

        if (id == null) {
            throw new IllegalArgumentException();
        } else if (devices.containsKey(id)) {
            return devices.get(id);
        } else {
            throw new DeviceNotFoundException();
        }

    }

    /**
     * Returns the total number of devices with type @type registered in SmartCityHub.
     *
     * @throws IllegalArgumentException in case @type is null.
     */
    public int getDeviceQuantityPerType(DeviceType type) {

        if (type == null) {
            throw new IllegalArgumentException();
        } else {
            int deviceTypeCounter = 0;
            for (SmartDevice smartDevice : devices.values()) {
                if (smartDevice.getType().equals(type)) {
                    deviceTypeCounter++;
                }
            }
            return deviceTypeCounter;
        }

    }

    /**
     * Returns a collection of IDs of the top @n devices which consumed
     * the most power from the time of their installation until now.
     * <p>
     * The total power consumption of a device is calculated by the hours elapsed
     * between the two LocalDateTime-s: the installation time and the current time (now)
     * multiplied by the stated nominal hourly power consumption of the device.
     * <p>
     * If @n exceeds the total number of devices, return all devices available sorted by the given criterion.
     *
     * @throws IllegalArgumentException in case @n is a negative number.
     */
    public Collection<String> getTopNDevicesByPowerConsumption(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        } else {
            List<AbstractSmartDevice> devicesByPowerConsumption = new ArrayList<AbstractSmartDevice>();
            for (SmartDevice device : devices.values()) {
                devicesByPowerConsumption.add((AbstractSmartDevice) device);
            }

            Collections.sort(devicesByPowerConsumption, new Comparator<AbstractSmartDevice>() {
                @Override
                public int compare(AbstractSmartDevice d1, AbstractSmartDevice d2) {
                    return d2.getTotalPowerConsumption().compareTo(d1.getTotalPowerConsumption());
                }
            });

            int newCollectionSize = Integer.min(n, devicesByPowerConsumption.size());
            return devicesByPowerConsumption.subList(0, newCollectionSize)
                    .stream()
                    .map(device -> device.getId())
                    .collect(Collectors.toList());
        }
    }

    /**
     * Returns a collection of the first @n registered devices, i.e the first @n that were added
     * in the SmartCityHub (registration != installation).
     * <p>
     * If @n exceeds the total number of devices, return all devices available sorted by the given criterion.
     *
     * @throws IllegalArgumentException in case @n is a negative number.
     */
    public Collection<SmartDevice> getFirstNDevicesByRegistration(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        } else {

            Iterator<SmartDevice> it = devices.values().iterator();
            List<SmartDevice> resultList = new ArrayList<SmartDevice>(devices.values());
            int newCollectionSize = Integer.min(n, devices.size());
            return resultList.subList(0, newCollectionSize);
        }
    }

    private Map<String, SmartDevice> devices = new LinkedHashMap<String, SmartDevice>();

}