package com.events.core.services;


import org.apache.sling.api.resource.ResourceResolver;

/**
 * Repository Subscription Service used by Subscription servlet to persist fields to ROOT_PATH.
 */

public interface RepositoryPersistenceService {

    public static final String ROOT_PATH = "/etc/events/subscriptions";

    boolean persistDataToGui(String fname, String lname, String email);

    ResourceResolver getSystemResourceResolver();
}
