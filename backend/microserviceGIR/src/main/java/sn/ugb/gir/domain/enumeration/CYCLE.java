package sn.ugb.gir.domain.enumeration;

/**
 * The CYCLE enumeration.
 */
public enum CYCLE {
    LICENCE("Licence"),
    MASTER("Master"),
    DOCTOTAT("Doctorat");

    private final String value;

    CYCLE(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
