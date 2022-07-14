package com.jclarity.region.model;


import java.util.Locale;

public enum RegionType {
    EDEN("EDEN"),
    FREE("FREE"),
    HUMC("HUMC"),
    HUMS("HUMS"),
    OLD("OLD"),
    OARC("OARC"),
    SURV("SURV");

    private final String label;

    RegionType(String label) {
        this.label = label;
    }

    public String getLabel() { return label; }

    public String toStyleClass() {
        return getLabel().toLowerCase(Locale.ROOT);
    }
}
