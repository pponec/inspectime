package com.google.gwt.core.client;

/**
 * http://code.google.com/p/google-web-toolkit/issues/detail?id=7692
 *
 * "GXT should be updated to use shared.GWT on the server-side rather than
 * client.GWT. but old version of GXT possibly can not be updated now. In
 * gwt-2.5RC I see class defined as( in package "com.google.gwt.core.client")
 * public abstract class GWTBridge extends com.google.gwt.core.shared.GWTBridge
 * { } but the class file of this is not included in gwt-servlet.jar I have
 * manually included .class file in gwt-servlet.jar just as a work around."
 */
public abstract class GWTBridge extends com.google.gwt.core.shared.GWTBridge {
}