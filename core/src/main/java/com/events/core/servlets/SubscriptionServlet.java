package com.events.core.servlets;

import com.events.core.services.RepositoryPersistenceService;
import com.events.core.services.ValidationService;
import com.events.core.utils.JsonUtil;
import com.google.gson.JsonObject;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;

import javax.servlet.Servlet;
import java.io.IOException;

import static org.apache.http.HttpHeaders.CACHE_CONTROL;
import static org.apache.http.client.cache.HeaderConstants.CACHE_CONTROL_NO_CACHE;

/**
 * Servlet that get invoked from Submit button of Event Subscription Component.
 * Uses Subscription validation endpoint to validate form fields.
 * Uses service to create gui nodes at /etc/events/subscriptions based on the form fields submitted.
 */
@Component(service = { Servlet.class })
@SlingServletResourceTypes(
        resourceTypes="events/components/page",
        methods=HttpConstants.METHOD_GET,
        selectors = {"subscription"},
        extensions = "json")
@ServiceDescription("Subscription Servlet")
public class SubscriptionServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 1L;

    @Reference
    private ValidationService validationService;

    @Reference
    private RepositoryPersistenceService repositoryPersistenceService;

    @Override
    protected void doGet(final SlingHttpServletRequest req,
            final SlingHttpServletResponse resp) throws IOException {
        ResourceResolver resourceResolver = req.getResourceResolver();
        resp.setContentType("application/json");
        resp.setHeader(CACHE_CONTROL, CACHE_CONTROL_NO_CACHE);
        final String firstName = req.getParameter("firstName");
        final String lastName = req.getParameter("lastName");
        final String email = req.getParameter("email");
        boolean persistenceStatus = false;
        JsonObject responseJson = validationService.isValidRequest(firstName, lastName, email);
        Resource resource = resourceResolver.getResource(RepositoryPersistenceService.ROOT_PATH+"/"+lastName);
        if ( responseJson.get("status").getAsInt() == 200 && resource == null) {
            persistenceStatus = repositoryPersistenceService.persistDataToGui(firstName, lastName, email);
            if (persistenceStatus) {
                JsonUtil.setJsonProperties(200, "Successfully persisted subscription data", responseJson);
            } else {
                if (repositoryPersistenceService.getSystemResourceResolver() == null) {
                    JsonUtil.setJsonProperties(403, "Please create 'subscriptionWriteService' system user with jcr:all permissions on /etc/events", responseJson);
                } else {
                    JsonUtil.setJsonProperties(403, "Error while persisting subscription data", responseJson);
                }
            }
        } else if (responseJson.get("status").getAsInt() == 200 && resource != null) {
            JsonUtil.setJsonProperties(403, "User already subscribed", responseJson);
        }
        resp.getWriter().write(responseJson.toString());
    }
}
