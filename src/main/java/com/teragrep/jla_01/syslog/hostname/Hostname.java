package com.teragrep.jla_01.syslog.hostname;

import java.net.InetAddress;
import java.net.UnknownHostException;

public final class Hostname {
    private final String defaultHostname;

    public Hostname(final String defaultHostname) {
        this.defaultHostname = defaultHostname;
    }

    public String hostname() {
        String rv;
        try {
            rv = InetAddress.getLocalHost().getHostName();
        }
        catch (UnknownHostException e) {
            rv = defaultHostname;
            System.err.println("Could not determine hostname, defaulting to <["+defaultHostname+"]>");
        }
        return rv;
    }
}
