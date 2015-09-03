package info.rolandkrueger.userservice.api.util;

/**
 * @author Roland Krüger
 */
public enum UsersSort {
    USERNAME("username"), EMAIL("email"), FULLNAME("fullname");

    private String property;

    UsersSort(String property) {
        this.property = property;
    }

    public String getProperty() {
        return property;
    }
}
