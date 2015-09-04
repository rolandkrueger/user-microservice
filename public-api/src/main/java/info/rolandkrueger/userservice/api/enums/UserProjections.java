package info.rolandkrueger.userservice.api.enums;

/**
 * @author Roland Krüger
 */
public enum UserProjections {

    FULL_DATA(Values.USER_FULL_DATA),
    EXCERPT(Values.USER_EXCERPT_DATA);

    private String name;

    UserProjections(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static class Values {
        public static final String USER_FULL_DATA = "full-data";
        public static final String USER_EXCERPT_DATA = "excerpt";
    }

}
