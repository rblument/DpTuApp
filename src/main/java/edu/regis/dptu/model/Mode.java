package edu.regis.dptu.model;

public enum Mode {
    SEE_ONE("See One"),
    DO_ONE("Do One"),
    TEACH_ONE("Teach One");

    /** A GUI displayable pretty print string identifying this mode type */
    private final String title;

    /**
     * Initialize this mode with its title.
     *
     * @param title a GUI displayable pretty print name for this mode type
     */
    Mode(String title) {
        this.title = title;
    }

    /**
     * Return the title for this mode type
     *
     * @return a GUI displayable pretty print string for this mode type
     */
    public String title() {
        return title;
    }
}
