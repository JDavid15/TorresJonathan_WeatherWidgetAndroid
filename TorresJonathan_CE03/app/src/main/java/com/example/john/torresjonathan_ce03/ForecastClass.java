// Jonathan Torres
// MDF3 - C201708
// ForecastClass.java

package com.example.john.torresjonathan_ce03;

import java.io.Serializable;

//Class to store and pass data to the UI for the Forecast
class ForecastClass implements Serializable {

    private final String weekday;
    private final String high;
    private final String low;
    private final byte[] iconBytes;

    ForecastClass(String _weekdays, String _high, String _low, byte[] _iconBytes){

        weekday = _weekdays;
        high = _high;
        low = _low;
        iconBytes = _iconBytes;
    }

     String getWeekday() {
        return weekday;
    }

     String getHigh() {
        return high;
    }

     String getLow() {
        return low;
    }

     byte[] getIconBytes() {
        return iconBytes;
    }
}
