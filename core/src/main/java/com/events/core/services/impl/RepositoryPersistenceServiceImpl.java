package com.events.core.services.impl;

import com.events.core.services.RepositoryPersistenceService;
import com.events.core.services.ValidationService;
import com.events.core.services.config.EndpointConfig;
import com.events.core.utils.JsonUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.sling.api.resource.*;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Repository Subscription Service used by Subscription servlet to persist fields to ROOT_PATH.
 */
@Component(service = RepositoryPersistenceService.class)
public class RepositoryPersistenceServiceImpl implements RepositoryPersistenceService{

    /**
     * Logger for Service.
     */
    private static final Logger log = LoggerFactory.getLogger(RepositoryPersistenceServiceImpl.class);

    private static final String SERVICE_USER = "subscriptionWriteService";

    @Reference
    ResourceResolverFactory resolverFactory;

    ResourceResolver resourceResolver;

    @Override
    public boolean persistDataToGui(String fname, String lname, String email) {
        resourceResolver = getResourceResolver();
        if (resourceResolver != null) {
            Resource rootResource = resourceResolver.getResource(ROOT_PATH);
            log.info("rootResource :: "+rootResource);
            if (rootResource != null) {
                Map<String, Object> properties = new HashMap<>();
                properties.put("firstName", fname);
                properties.put("lastName",lname);
                properties.put("email",email);
                try {
                    resourceResolver.create(rootResource, lname, properties);
                    resourceResolver.commit();
                    return true;
                } catch (PersistenceException e) {
                    log.error("PersistenceException occurred, while creating lname subscription::{0}", e.getMessage());
                } finally {
                    if (resourceResolver.isLive()) {
                        resourceResolver.close();
                    }
                }

            }
        }
        return false;
    }

    @Override
    public ResourceResolver getSystemResourceResolver() {
        return resourceResolver;
    }

    private ResourceResolver getResourceResolver() {
        Map<String, Object> param = new HashMap<>();
        param.put(ResourceResolverFactory.SUBSERVICE, SERVICE_USER);
        try {
            return resolverFactory != null ? resolverFactory.getServiceResourceResolver(param) : null;
        } catch (LoginException e) {
            log.error("Login Exception occurred, while accessing system user::{0}", e.getMessage());
        }
        return null;
    }
}
