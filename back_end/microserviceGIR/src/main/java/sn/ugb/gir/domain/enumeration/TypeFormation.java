package sn.ugb.gir.domain.enumeration;

/**
 * The TypeFormation enumeration.
 */
public enum TypeFormation {
    PUBLIC("Public"),
    PRIVEE("Privee");

    private final String value;

    TypeFormation(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
