package com.vaadin.starter.bakery.app;

import java.net.InetAddress;

public final class HostInfo {

    public static String getHostname() {
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            return localhost.getHostName();
        } catch (Exception e) {
            return "<Unknown>:" + e.getMessage();
        }
    }

    public static String getIpAddress() {
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            return localhost.getHostAddress();
        } catch (Exception e) {
            return "<Unknown>:" + e.getMessage();
        }
    }
}
