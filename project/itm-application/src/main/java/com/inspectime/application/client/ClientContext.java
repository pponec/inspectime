/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.inspectime.application.client;

/**
 * ClientContext - singleton.
 * @author Ponec
 */
public class ClientContext {

    private static ClientContext context = new ClientContext();

    /** Event table refresh request */
    private boolean refreshEventTableRequest;
    /** Hot keys refresh request  */
    private boolean refreshHotKeysRequest;
    /** Lock table refresh  */
    private boolean refreshLockRequest;

    public static ClientContext getInstance() {
        return context;
    }

    /** Reset original flag */
    public boolean isRefreshEventTableRequest() {
        if (refreshEventTableRequest) {
            refreshEventTableRequest = false;
            return true;
        } else {
            return false;
        }
    }

    public void setRefreshEventTableRequest() {
        this.refreshEventTableRequest = true;
    }

    /** Reset original flag */
    public boolean isRefreshHotKeysRequest() {
        if (refreshHotKeysRequest) {
            refreshHotKeysRequest = false;
            return true;
        } else {
            return false;
        }
    }

    public void setRefreshHotKeysRequest() {
        this.refreshHotKeysRequest = true;
    }


    /** Reset original flag */
    public boolean isRefreshLockRequest() {
        if (refreshLockRequest) {
            refreshLockRequest = false;
            return true;
        } else {
            return false;
        }
    }

    public void setRefreshLockRequest() {
        this.refreshLockRequest = true;
    }

}
