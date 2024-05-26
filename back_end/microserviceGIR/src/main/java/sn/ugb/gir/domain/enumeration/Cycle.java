package sn.ugb.gir.domain.enumeration;

/**
 * The Cycle enumeration.
 */
public enum Cycle {
    LICENCE("Licence"),
    MASTER("Master"),
    DOCTOTAT("Doctorat");

    private final String value;

    Cycle(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
