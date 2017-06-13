package com.raxdenstudios.commons.util;

import java.util.List;

/**
 * Created by Angel on 17/05/2017.
 */

public class ArrayUtils {

    public static <T> int indexOf(List<T> data, T o) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i) == o) {
                return i;
            }
        }
        return -1;
    }

}
