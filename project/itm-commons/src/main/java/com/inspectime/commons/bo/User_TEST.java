/*
 * Ujo4GXT - GXT module for the Ujorm
 * Copyright(c) 2011 Pavel Ponec
 * License: GNU/GPL v3 (see detail on http://www.gnu.org/licenses/gpl.html).
 *          If you need a commercial license, please contact support@inspectime.com.
 * Support: support@ujorm.com - for both technical or business information
 */

package com.inspectime.commons.bo;

import org.ujorm.Key;
import org.ujorm.core.KeyFactory;
import org.ujorm.orm.annot.Column;
import org.ujorm.orm.annot.Comment;

/**
 * User of the Company
 * @author Hampl,ponec,gola
 */
@Comment("User of the Company")
final public class User_TEST extends AbstractBo {
    
    private static final String INDEX_NAME = "idx_user";
    public static final String ADMIN_LOGIN = "admin";
    
    /** TEST CODE */
    @Comment("Not deleted. The null value means a logical deleted state")
    @Column(uniqueIndex = INDEX_NAME)
    public static final StringBuilder ADMIN_LOGIN2 = new StringBuilder("TEST_CODE");
    
    /** Key Factory */
    private static final KeyFactory f = KeyFactory.Builder.get(User_TEST.class);
    
    /** Primary Key */
    @Column(pk = true)
    public static final Key<User, Long> id = f.newKey($ID);    

    /** Property initialization */
    static {
        f.lock();
    }
    
    @Override
    public Long getId() {
        return 0L;
    }

}
