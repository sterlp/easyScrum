package org.easy.validation.html;

import org.jsoup.safety.Whitelist;

public enum HtmlValidation {
    /**
     * @see Whitelist#none() 
     */
    None(Whitelist.none()),
    /**
     * @see Whitelist#basic() 
     */
    Basic(Whitelist.basic()),
    /**
     * @see Whitelist#basicWithImages() 
     */
    BasicWithImages(Whitelist.basicWithImages()),
    /**
     * @see Whitelist#relaxed() 
     * Including the font tag
     */
    Relaxed(Whitelist.relaxed())
    ;
    
    private final Whitelist whitelist;
    private HtmlValidation(Whitelist whitelist) {
        this.whitelist = whitelist;
    }

    public Whitelist getWhitelist() {
        return whitelist;
    }
}
