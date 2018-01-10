package com.webkul.mobikul.mobikulstandalonepos.db.converters;

import android.arch.persistence.room.TypeConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aman.gupta on 10/1/18. @Webkul Software Private limited
 */
public class DataConvertor {

    @TypeConverter
    String fromIntegerList(List<Integer> cIds) {
        if (cIds == null) {
            return (null);
        }

        return cIds.toString();
    }


    @TypeConverter
    List<Integer> toIntegerList(String cIdString) {
        if (cIdString == null) {
            return (null);
        }

        List<Integer> cIds = new ArrayList<>();

        return cIds.toString();
    }
}
