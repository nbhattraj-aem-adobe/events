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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;

import javax.servlet.Servlet;
import java.io.IOException;
import java.io.PrintWriter;

import static org.apache.http.HttpHeaders.CACHE_CONTROL;
import static org.apache.http.client.cache.HeaderConstants.CACHE_CONTROL_NO_CACHE;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SubscriptionServletTest {

    private static final long serialVersionUID = 1L;

    @Mock
    private ValidationService validationService;

    @Mock
    private RepositoryPersistenceService repositoryPersistenceService;

    @Mock
    private ResourceResolver resourceResolver;

    @Mock
    private Resource resource;

    @Mock
    private SlingHttpServletRequest request;

    @Mock
    private SlingHttpServletResponse response;

    @Mock
    private PrintWriter writer;

    @InjectMocks
    private SubscriptionServlet servlet = new SubscriptionServlet();

    @Test
    void testDoGet() throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("status", 200);
        jsonObject.addProperty("message", "Valid fields");
        when(request.getResourceResolver()).thenReturn(resourceResolver);
        when(request.getParameter("firstName")).thenReturn("firstName");
        when(request.getParameter("lastName")).thenReturn("lastName");
        when(request.getParameter("email")).thenReturn("email@email.com");
        when(validationService.isValidRequest("firstName", "lastName", "email@email.com")).thenReturn(jsonObject);
        when(resourceResolver.getResource("/etc/events/subscriptions/lastName")).thenReturn(null);
        when(repositoryPersistenceService.persistDataToGui("firstName", "lastName", "email@email.com")).thenReturn(true);
        when(response.getWriter()).thenReturn(writer);
        servlet.doGet(request,response);
    }

    @Test
    void testDoGetDuplicateSubscription() throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("status", 200);
        jsonObject.addProperty("message", "Valid fields");
        when(request.getResourceResolver()).thenReturn(resourceResolver);
        when(request.getParameter("firstName")).thenReturn("firstName");
        when(request.getParameter("lastName")).thenReturn("lastName");
        when(request.getParameter("email")).thenReturn("email@email.com");
        when(validationService.isValidRequest("firstName", "lastName", "email@email.com")).thenReturn(jsonObject);
        when(resourceResolver.getResource("/etc/events/subscriptions/lastName")).thenReturn(resource);
        when(response.getWriter()).thenReturn(writer);
        servlet.doGet(request,response);
    }

    @Test
    void testDoGetPersistenceException() throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("status", 200);
        jsonObject.addProperty("message", "Valid fields");
        when(request.getResourceResolver()).thenReturn(resourceResolver);
        when(request.getParameter("firstName")).thenReturn("firstName");
        when(request.getParameter("lastName")).thenReturn("lastName");
        when(request.getParameter("email")).thenReturn("email@email.com");
        when(validationService.isValidRequest("firstName", "lastName", "email@email.com")).thenReturn(jsonObject);
        when(resourceResolver.getResource("/etc/events/subscriptions/lastName")).thenReturn(null);
        when(repositoryPersistenceService.persistDataToGui("firstName", "lastName", "email@email.com")).thenReturn(false);
        when(repositoryPersistenceService.getSystemResourceResolver()).thenReturn(resourceResolver);
        when(response.getWriter()).thenReturn(writer);
        servlet.doGet(request,response);
    }

    @Test
    void testDoGetNoSystemUser() throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("status", 200);
        jsonObject.addProperty("message", "Valid fields");
        when(request.getResourceResolver()).thenReturn(resourceResolver);
        when(request.getParameter("firstName")).thenReturn("firstName");
        when(request.getParameter("lastName")).thenReturn("lastName");
        when(request.getParameter("email")).thenReturn("email@email.com");
        when(validationService.isValidRequest("firstName", "lastName", "email@email.com")).thenReturn(jsonObject);
        when(resourceResolver.getResource("/etc/events/subscriptions/lastName")).thenReturn(null);
        when(repositoryPersistenceService.persistDataToGui("firstName", "lastName", "email@email.com")).thenReturn(false);
        when(repositoryPersistenceService.getSystemResourceResolver()).thenReturn(null);
        when(response.getWriter()).thenReturn(writer);
        servlet.doGet(request,response);
    }

    @Test
    void testDoGetInvalidEmail() throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("status", 403);
        jsonObject.addProperty("message", "Invalid email address");
        when(request.getResourceResolver()).thenReturn(resourceResolver);
        when(request.getParameter("firstName")).thenReturn("firstName");
        when(request.getParameter("lastName")).thenReturn("lastName");
        when(request.getParameter("email")).thenReturn("emailemail.com");
        when(validationService.isValidRequest("firstName", "lastName", "emailemail.com")).thenReturn(jsonObject);
        when(response.getWriter()).thenReturn(writer);
        servlet.doGet(request,response);
    }
}
