package info.rolandkrueger.userservice.api.enums;

/**
 * @author Roland Krüger
 */
public enum UserProjections {

    NO_AUTHORITIES_NO_PW(Values.NO_AUTHORITIES_NO_PW);

    private String name;

    UserProjections(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static class Values {
        public static final String NO_AUTHORITIES_NO_PW = "no-authorities-no-pw";
    }

}
