package com.events.core.servlets;

import com.events.core.utils.JsonUtil;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;
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
 * Mocked REST Endpoint servlet that validates form fields.It is called by the Subscription servlet for field validations.
 * Ex endpoint to invoke servlet :
 *      http://localhost:4502/content/events/us/en.subscriptionvalidation.html?firstName=check&lastName=check&email=email@email.com
 */
@Component(service = { Servlet.class })
@SlingServletResourceTypes(
        resourceTypes="events/components/page",
        methods=HttpConstants.METHOD_GET,
        selectors = {"subscriptionvalidation"},
        extensions = {"json"})
@ServiceDescription("Subscription validation Servlet")
public class SubscriptionValidationServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 1L;

    private final String emailPattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";

    private final String namePattern = "[A-Za-z]*";

    @Override
    protected void doGet(final SlingHttpServletRequest req,
            final SlingHttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setHeader(CACHE_CONTROL, CACHE_CONTROL_NO_CACHE);
        final String firstName = req.getParameter("firstName");
        final String lastName = req.getParameter("lastName");
        final String email = req.getParameter("email");
        JsonObject responseJson = new JsonObject();
        if (!StringUtils.isEmpty(firstName) && !StringUtils.isEmpty(lastName) &&
        !StringUtils.isEmpty(email)) {
            if (isValidPattern(firstName, namePattern)) {
                if (isValidPattern(lastName, namePattern)) {
                    if (isValidPattern(email, emailPattern)) {
                        resp.setStatus(200);
                        JsonUtil.setJsonProperties(200, "Valid fields.", responseJson);
                    } else {
                        resp.setStatus(200);
                        JsonUtil.setJsonProperties(403, "Please enter valid Email address.", responseJson);
                    }
                } else {
                    resp.setStatus(200);
                    JsonUtil.setJsonProperties(403, "Please enter valid Last name.", responseJson);
                }
            } else {
                resp.setStatus(200);
                JsonUtil.setJsonProperties(403, "Please enter valid First name.", responseJson);
            }
        } else if (StringUtils.isEmpty(firstName) && StringUtils.isEmpty(lastName) && StringUtils.isEmpty(email)){
            resp.setStatus(200);
            JsonUtil.setJsonProperties(403, "Please fill all form fields.", responseJson);
        }
        resp.getWriter().write(responseJson.toString());
    }

    public boolean isValidPattern(String element, String regexPattern) {
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regexPattern);
        java.util.regex.Matcher matcher = pattern.matcher(element);
        return matcher.matches();
    }
}
