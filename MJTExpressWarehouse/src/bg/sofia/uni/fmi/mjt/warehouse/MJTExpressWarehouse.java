package bg.sofia.uni.fmi.mjt.warehouse;

import bg.sofia.uni.fmi.mjt.warehouse.exceptions.CapacityExceededException;
import bg.sofia.uni.fmi.mjt.warehouse.exceptions.ParcelNotFoundException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;
import java.util.AbstractMap;


public class MJTExpressWarehouse<L, P> implements DeliveryServiceWarehouse<L, P> {

    /**
     * Creates a new instance of MJTExpressWarehouse with the given characteristics
     *
     * @param capacity        the total number of parcels that the warehouse can store
     * @param retentionPeriod the maximum number of days for which a parcel can stay in the warehouse,
     *                        counted from the day the parcel
     *                        was submitted. After that time passes, the parcel can be removed from the warehouse
     */
    public MJTExpressWarehouse(int capacity, int retentionPeriod) {
        this.capacity = capacity;
        this.retentionPeriod = retentionPeriod;
    }

    /**
     * Adds the provided parcels with the given label as a new item in the warehouse.
     *
     * @param label          the unique identifier of the parcel.
     * @param parcel         the parcel that should be stored. If the storage is full, the parcel can still be stored,
     *                       if there is at least one item that can be evicted
     * @param submissionDate the date when the parcel was submitted into the warehouse
     * @throws CapacityExceededException if there is no capacity left in the warehouse
     * @throws IllegalArgumentException  if the provided date is a date in the future, or any of the parameters is null
     */
    @Override
    public void submitParcel(L label, P parcel, LocalDateTime submissionDate) throws CapacityExceededException {
        if (label == null || parcel == null || submissionDate == null || submissionDate.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException();
        } else if (isFreeCapacity()) {
            parcels.put(label, new AbstractMap.SimpleEntry<>(submissionDate, parcel));

        } else {
            throw new CapacityExceededException();

        }
    }

    /**
     * @param label the label of the wanted parcel
     * @return the parcel with label equal to the provided one. Returns null if no parcel is found
     * @throws IllegalArgumentException when the given label is null
     */
    @Override
    public P getParcel(L label) {
        if (label == null) {
            throw new IllegalArgumentException();
        } else if (parcels.containsKey(label)) {
            return parcels.get(label).getValue();
        } else {
            return null;
        }
    }

    /**
     * Removes the parcel with the given label from the warehouse and returns it
     *
     * @param label the label of the parcel for delivery
     * @return the Parcel with label equal to the given one
     * @throws ParcelNotFoundException  when a parcel with the given label does not exist in the warehouse
     * @throws IllegalArgumentException then the given label is null
     */
    @Override
    public P deliverParcel(L label) throws ParcelNotFoundException {
        if (label == null) {
            throw new IllegalArgumentException();
        } else if (!parcels.containsKey(label)) {
            throw new ParcelNotFoundException();
        } else {
            P parcel = getParcel(label);
            parcels.remove(label);
            return parcel;
        }
    }

    /**
     * @return the free space in the warehouse as a decimal fraction.
     * It should be a number between 0 and 1 rounded to two decimal places
     */
    @Override
    public double getWarehouseSpaceLeft() {

        double quotient = (double) (capacity - parcels.size()) / (double) (capacity);
        return (double) (Math.round(quotient * 100)) / 100;
    }

    /**
     * @return a Map of all items in the warehouse. if there are no items, the returned map is empty
     */
    @Override
    public Map<L, P> getWarehouseItems() {
        Map<L, P> allParcels = new HashMap<>();
        for (Map.Entry<L, Map.Entry<LocalDateTime, P>> entry : parcels.entrySet()) {
            allParcels.put(entry.getKey(), entry.getValue().getValue());
        }
        return allParcels;
    }

    /**
     * Removes all items submitted before the given date from the warehouse, and returns them
     *
     * @param before the date that is used for filtering the items
     * @return the items that will be delivered. If there are no items submitted before the given date,
     * the returned Map is empty. If the given date is in the future, all items in the warehouse are returned
     * @throws IllegalArgumentException when the given date is null
     */
    @Override
    public Map<L, P> deliverParcelsSubmittedBefore(LocalDateTime before) {
        if (before == null) {
            throw new IllegalArgumentException();
        } else {

            Map<L, P> beforeParcels = new HashMap<>();

            for (Map.Entry<L, Map.Entry<LocalDateTime, P>> entry : parcels.entrySet()) {
                L label = entry.getKey();
                LocalDateTime parcelDate = entry.getValue().getKey();
                P parcel = entry.getValue().getValue();

                if (parcelDate.isBefore(before)) {
                    beforeParcels.put(label, parcel);
                }
            }
            for (Map.Entry<L, P> parcel : beforeParcels.entrySet()) {
                parcels.remove(parcel.getKey());
            }

            return beforeParcels;
        }
    }

    /**
     * Removes all items submitted after the given date from the warehouse, and returns them
     *
     * @param after the date that is used for filtering the items
     * @return the items that will be delivered. If there are no items submitted after the given date,
     * the returned Map is empty. An empty Map is returned if the given date is in the future
     * @throws IllegalArgumentException when the given date is null
     */
    @Override
    public Map<L, P> deliverParcelsSubmittedAfter(LocalDateTime after) {
        if (after == null) {
            throw new IllegalArgumentException();
        } else {

            Map<L, P> afterParcels = new HashMap<>();

            for (Map.Entry<L, Map.Entry<LocalDateTime, P>> entry : parcels.entrySet()) {
                L label = entry.getKey();
                LocalDateTime parcelDate = entry.getValue().getKey();
                P parcel = entry.getValue().getValue();

                if (parcelDate.isAfter(after)) {
                    afterParcels.put(label, parcel);
                }
            }
            for (Map.Entry<L, P> parcel : afterParcels.entrySet()) {
                parcels.remove(parcel.getKey());
            }

            return afterParcels;
        }
    }

    private boolean retentionPeriodExpired(LocalDateTime submissionDate) {
        return (Duration.between(submissionDate, LocalDateTime.now()).toDays() > retentionPeriod);
    }

    private void removeExpiredParcels() {

        for (Map.Entry<L, Map.Entry<LocalDateTime, P>> entry : parcels.entrySet()) {
            L label = entry.getKey();
            LocalDateTime parcelDate = entry.getValue().getKey();

            if (retentionPeriodExpired(parcelDate)) {
                parcels.remove(label);

            }
        }
    }

    private boolean isFreeCapacity() {
        if (parcels.size() < capacity) {
            return true;
        } else {
            removeExpiredParcels();
            return parcels.size() < capacity;
        }
    }

    private int capacity;
    private int retentionPeriod;
    private Map<L, Map.Entry<LocalDateTime, P>> parcels = new HashMap<>();

}
