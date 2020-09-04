package com.taikang.business.common.utils;

import java.util.Arrays;
import java.util.List;

public class ListUtils {

    public static List<String> arrayToList(String ids){
        String[] arrayIds = null;
        if (ids.contains(",")){
            arrayIds = ids.split(",");
        }else{
            arrayIds =new String[]{ids};
        }
        List<String> list = Arrays.asList(arrayIds);
        return  list;
    }
}
