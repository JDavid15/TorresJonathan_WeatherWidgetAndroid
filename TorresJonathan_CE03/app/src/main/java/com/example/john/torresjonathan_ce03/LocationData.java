// Jonathan Torres
// MDF3 - C201708
// LocationData.java

package com.example.john.torresjonathan_ce03;

import java.io.Serializable;
import java.util.Objects;


//Class to save data to the device for the Widget
 class LocationData implements Serializable {

    private final String temp;
    private final String condition;
    private final String dataTime;
    private final String imageUrl;

     LocationData(String _temp, String _condition, String _dateTime, String _imageUrl) {
        temp = _temp;
        condition = _condition;
        dataTime = _dateTime;
        imageUrl = _imageUrl;
    }

     String getTemp() {
        return temp;
    }

     String getCondition() {
        return condition;
    }

     String getDataTime() {
        return dataTime;
    }

     String getImageUrl() {
        return imageUrl;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof LocationData)) {
            return false;
        }

        LocationData other = (LocationData)obj;
        return Objects.equals(temp, other.temp) &&
                Objects.equals(condition, other.condition) &&
                dataTime.equals(other.dataTime);
    }

}
