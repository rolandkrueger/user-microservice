package info.rolandkrueger.userservice.api.enums;

/**
 * @author Roland Krüger
 */
public enum AuthoritiesSort {

    AUTHORITY("authority"), DESCRIPTION("description");

    private String property;

    AuthoritiesSort(String property) {
        this.property = property;
    }

    public String getProperty() {
        return property;
    }
}
