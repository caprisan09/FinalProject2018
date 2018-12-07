package com.example.hunar_parneet.finalproject2018;

/**
 *
 */
public class RouteDetail {
    /**
     *
      */
    private final int busNumber;
    /**
     *
     */
    private final String routeHeading;

    /**
     *
     * @param busNumber
     * @param routeHeading
     */
    public RouteDetail(int busNumber, String routeHeading){
        this.busNumber = busNumber;
        this.routeHeading = routeHeading;

    }

    /**
     *
     * @return
     */
    public int getBusNumber() {
        return busNumber;
    }

    /**
     *
     * @return
     */
    public String getRouteHeading() {
        return routeHeading;
    }

    /**
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RouteDetail that = (RouteDetail) o;

        if (getBusNumber() != that.getBusNumber()) return false;
        return getRouteHeading().equals(that.getRouteHeading());
    }

    /**
     *
     * @return
     */
    @Override
    public int hashCode() {
        int result = getBusNumber();
        result = 31 * result + getRouteHeading().hashCode();
        return result;
    }
}
