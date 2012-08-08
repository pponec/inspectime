/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.commons.bo;

import org.apache.log4j.Logger;
import org.ujorm.Key;
import org.ujorm.orm.DbType;
import org.ujorm.orm.annot.Column;
import org.ujorm.orm.annot.Comment;

/**
 * Description content for long texts.
 * @author Pavel Ponec
 */
@Comment("Description content for long texts")
final public class Description extends AbstractBo {

    static final private Logger LOGGER = Logger.getLogger(Description.class.getName());
    public static final int CONTENT_LENGTH = 256;

    /** Primary Key */
    @Column(pk = true)
    public static final Key<Description, Long> id = newKey($ID);
    /** Description content (text) */
    @Column(type=DbType.VARCHAR, length=CONTENT_LENGTH)
    public static final Key<Description, String> content = newKey("");
    /** Description content (text) */
    @Column(mandatory=false)
    public static final Key<Description, Description> more = newKey();

    /** Property initialization */
    static {
        init(Description.class);
    }

    // <editor-fold defaultstate="collapsed" desc="SET/GET">
    @Override
    public Long getId() {
        return id.getValue(this);
    }

    /** Get text longer that CONTENT_LENGTH . */
    public String getFullText() {
        StringBuilder result = new StringBuilder(256);
        Description _more = this;

        while (_more!=null) {
            result.append(_more.get(content));
            _more = _more.get(more);
        }

        return result.toString();
    }

    /** Write text longer that CONTENT_LENGTH . */
    public void setFullText(String text) {
        
        if (text==null) {
            text = "";
        }
        
        boolean tooLong  = text.length()>CONTENT_LENGTH ;
        String _text = tooLong
                ? text.substring(0, CONTENT_LENGTH) 
                : text
                ;
        set(content, _text);

        if (tooLong) {
            Description _more = get(more);
            if (_more==null) {
                _more = new Description();
            }
            set(more, _more);
            _more.setFullText(text.substring(CONTENT_LENGTH));
        }
    }

    // </editor-fold>
}
