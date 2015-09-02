package info.rolandkrueger.userservice.api;

/**
 * @author Roland Krüger
 */
public enum Projections {

    NO_AUTHORITIES_NO_PW(Values.NO_AUTHORITIES_NO_PW);

    private String name;

    Projections(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static class Values {
        public static final String NO_AUTHORITIES_NO_PW = "no-authorities-no-pw";
    }

}
