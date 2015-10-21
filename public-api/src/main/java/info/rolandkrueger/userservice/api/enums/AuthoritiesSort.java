package info.rolandkrueger.userservice.api.enums;

/**
 * Enum to specify sort-by semantics for the authority resource.
 *
 * @author Roland Krüger
 */
public enum AuthoritiesSort {

    AUTHORITY("authority"), DESCRIPTION("description");

    private String property;

    AuthoritiesSort(String property) {
        this.property = property;
    }

    /**
     * Return the property name by which to sort.
     */
    public String getProperty() {
        return property;
    }
}
