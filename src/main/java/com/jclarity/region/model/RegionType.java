package com.jclarity.region.model;


import javafx.css.PseudoClass;

import java.util.Locale;

public enum RegionType {
    FREE("FREE"),
    EDEN("EDEN"),
    SURV("SURV"),
    OLD("OLD"),
    HUMC("HUMC"),
    HUMS("HUMS"),
    OARC("OARC");

    private final String label;
    private final PseudoClass pseudoClass;

    RegionType(String label) {
        this.label = label;
        this.pseudoClass = PseudoClass.getPseudoClass(this.label.toLowerCase(Locale.ROOT));
    }

    public String getLabel() { return label; }

    public PseudoClass getPseudoClass() {
        return pseudoClass;
    }
}
