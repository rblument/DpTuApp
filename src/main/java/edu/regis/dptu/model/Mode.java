package edu.regis.dptu.model;

import edu.regis.dptu.util.ResourceMgr;

@SuppressWarnings("Logging")

public enum Mode {
    SEE_ONE("mode.seeOne"),
    DO_ONE("mode.doOne"),
    TEACH_ONE("mode.teachOne");

    /** Message key for this mode's display title. */
    private final String msgKey;

    /**
     * Initialize this mode with its message key.
     *
     * @param msgKey the Msgs.properties key for this mode's title
     */
    Mode(String msgKey) {
        this.msgKey = msgKey;
    }

    /**
     * Return the title for this mode type, localized via ResourceMgr.
     *
     * @return a GUI displayable pretty print string for this mode type
     */
    public String title() {
        return ResourceMgr.instance().string(msgKey);
    }

    /**
     * Return the message key associated with this mode.
     *
     * @return the Msgs.properties key used for this mode
     */
    public String getMsgKey() {
        return msgKey;
    }
}
