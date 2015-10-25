package info.rolandkrueger.userservice.api.enums;

/**
 * Enum to specify sort-by semantics for the user resource.
 *
 * @author Roland Krüger
 */
public enum UsersSort {
    USERNAME("username"), EMAIL("email"), FULLNAME("fullname");

    private String property;

    /**
     * @param property name of the {@link info.rolandkrueger.userservice.api.model.UserApiData} property to sort after
     */
    UsersSort(String property) {
        this.property = property;
    }

    /**
     * Resolves enum constant to domain type property.
     */
    public String getProperty() {
        return property;
    }
}
