//
//  AdjustIo.java
//  AdjustIo
//
//  Created by Christian Wellenbrock on 11.10.12.
//  Copyright (c) 2012 adeven. All rights reserved.
//

package com.adeven.adjustio;

import java.util.Map;

import android.app.Application;

public class AdjustIo {
    // Tell AdjustIo that the application did launch. This is required to
    // initialize AdjustIo. Call this in the onCreate method of your launch
    // activity.
    public static void appDidLaunch(Application app) {
        appId = app.getPackageName();
        macAddress = Util.getMacAddress(app);
        userAgent = Util.getUserAgent(app);

        trackSessionStart();
    }
    
    // Track any kind of event. You can assign a callback url to the event which
    // will get called every time the event is reported. You can also provide
    // parameters that will be forwarded to these callbacks.
    public static void trackEvent(String eventId) {
        trackEvent(eventId, null);
    }
    
    public static void trackEvent(String eventId, Map<String,String> parameters) {
        String paramString = Util.getBase64EncodedParameters(parameters);
        
        RequestTask requestTask = new RequestTask("/event");
        requestTask.setSuccessMessage("Tracked event " + eventId + ".");
        requestTask.setFailureMessage("Failed to track event " + eventId + ".");
        requestTask.setUserAgent(userAgent);
        requestTask.execute("id", eventId, "app_id", appId, "mac", macAddress, "params", paramString);
    }
    
    // Tell AdjustIo that the current user generated some revenue. The amount is
    // measured in cents and rounded to on digit after the decimal point. If you 
    // want to differentiate between various types of revenues you can do so by 
    // providing different eventIds. If your revenue events have callbacks, you
    // can also pass in parameters that will be forwarded to your server.    
    public static void trackRevenue(float amountInCents) {
        AdjustIo.trackRevenue(amountInCents, null);
    }
    
    public static void trackRevenue(float amountInCents, String eventId) {
        AdjustIo.trackRevenue(amountInCents, eventId, null);
    }
    
    public static void trackRevenue(float amountInCents, String eventId, Map<String,String> parameters) {
        int amountInMillis = Math.round(10 * amountInCents);
        String amount = Integer.toString(amountInMillis);
        String paramString = Util.getBase64EncodedParameters(parameters);
        
        RequestTask requestTask = new RequestTask("/revenue");
        requestTask.setSuccessMessage("Tracked revenue.");
        requestTask.setFailureMessage("Failed to track revenue.");
        requestTask.setUserAgent(userAgent);
        requestTask.execute("app_id", appId, "mac", macAddress, "amount", amount, "event_id", eventId, "params", paramString);
    }
    
    // This line marks the end of the public interface.
    
    private static String appId;
    private static String macAddress;
    private static String userAgent;
    
    private static void trackSessionStart() {
        RequestTask requestTask = new RequestTask("/startup");
        requestTask.setSuccessMessage("Tracked session start.");
        requestTask.setFailureMessage("Failed to track session.");
        requestTask.setUserAgent(userAgent);
        requestTask.execute("app_id", appId, "mac", macAddress);
    }
}