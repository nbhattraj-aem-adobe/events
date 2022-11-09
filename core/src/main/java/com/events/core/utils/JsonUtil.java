package com.events.core.utils;

import com.google.gson.JsonObject;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.propertytypes.ServiceDescription;

import javax.servlet.Servlet;
import java.io.IOException;

import static org.apache.http.HttpHeaders.CACHE_CONTROL;
import static org.apache.http.client.cache.HeaderConstants.CACHE_CONTROL_NO_CACHE;

/**
 * Util class to set Json properties
 */
public class JsonUtil {

    private static final long serialVersionUID = 1L;

    public static void setJsonProperties(int status, String message, JsonObject jsonObj) {
        if (jsonObj != null) {
            jsonObj.addProperty("status", status);
            jsonObj.addProperty("message", message);
        }
    }


}
