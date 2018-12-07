package com.example.hunar_parneet.finalproject2018;

/**
 *
 */
public class BusDetail {
    /**
     *
     */
    private final int busNumber;
    /**
     *
     */
    private final double gpsSpeed;
    /**
     *
     */
    private final double latitude;
    /**
     *
     */
    private final double longitude;
    /**
     *
     */
    private final String tripDestination;
    /**
     *
     */
    private final String tripStartTime;
    /**
     *
     */
    private final String adjustedScheduledTime;

    /**
     * @param busNumber
     * @param gpsSpeed
     * @param latitude
     * @param longitude
     * @param tripDestination
     * @param tripStartTime
     * @param adjustedScheduledTime
     */
    public BusDetail(int busNumber, double gpsSpeed, double latitude, double longitude, String tripDestination,
                     String tripStartTime, String adjustedScheduledTime) {
        this.gpsSpeed = gpsSpeed;
        this.busNumber = busNumber;
        this.latitude = latitude;
        this.longitude = longitude;
        this.tripDestination = tripDestination;
        this.tripStartTime = tripStartTime;
        this.adjustedScheduledTime = adjustedScheduledTime;
    }

    /**
     * @return
     */
    public double getGpsSpeed() {
        return gpsSpeed;
    }

    /**
     * @return
     */
    public int getBusNumber() {
        return busNumber;
    }

    /**
     * @return
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * @return
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * @return
     */
    public String getTripDestination() {
        return tripDestination;
    }

    /**
     * @return
     */
    public String getTripStartTime() {
        return tripStartTime;
    }

    /**
     * @return
     */
    public String getAdjustedScheduledTime() {
        return adjustedScheduledTime;
    }

    /**
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BusDetail busDetail = (BusDetail) o;

        if (Double.compare(busDetail.getGpsSpeed(), getGpsSpeed()) != 0) return false;
        if (Double.compare(busDetail.getLatitude(), getLatitude()) != 0) return false;
        if (Double.compare(busDetail.getLongitude(), getLongitude()) != 0) return false;
        if (!getTripDestination().equals(busDetail.getTripDestination())) return false;
        if (!getTripStartTime().equals(busDetail.getTripStartTime())) return false;
        return getAdjustedScheduledTime().equals(busDetail.getAdjustedScheduledTime());
    }

    /**
     * @return
     */
    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(getGpsSpeed());
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getLatitude());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getLongitude());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + getTripDestination().hashCode();
        result = 31 * result + getTripStartTime().hashCode();
        result = 31 * result + getAdjustedScheduledTime().hashCode();
        return result;
    }
}
