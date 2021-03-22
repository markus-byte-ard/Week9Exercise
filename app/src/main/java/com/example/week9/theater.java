package com.example.week9;

public class theater {
    String id;
    String name;

    public theater(String xmlId, String xmlName) {
        id = xmlId;
        name = xmlName;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
