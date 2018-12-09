package com.company;

import java.util.HashMap;
import java.util.Map;

public class MapManager implements onTextChangedListener {

    public static Map<String, Circle> hashMap = new HashMap<>();
    public String whereToGo;
    public int lastReadByte;

    @Override
    public void afterTextChanged() {

    }
}
