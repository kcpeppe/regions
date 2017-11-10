package com.jclarity.region.model;


import java.util.Locale;

public enum RegionType {
    FREE("FREE"),
    EDEN("EDEN"),
    SURV("SURV"),
    OLD("OLD"),
    HUMS("HUMS"),
    HUMC("HUMC");

    private final String label;

    RegionType(String label) {
        this.label = label;
    }

    public String getLabel() { return label; }

    public String toStyleClass() {
        return getLabel().toLowerCase(Locale.ROOT);
    }
}
