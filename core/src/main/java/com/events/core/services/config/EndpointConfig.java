package com.events.core.services.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * Validation Service used by Subscription servlet to validate fields.
 * Uses Http Rest call to Subscription validation servlet to get response.
 *
 *
 */
@ObjectClassDefinition(name = "REST Endpoint Configuration", description = "Configure rest endpoint to valide form fields")
public @interface EndpointConfig {

    /**
     * REST Endpoint - OSGI Property
     */
    @AttributeDefinition(name = "REST Endpoint", description = "Endpoint to use for validation")
    String endpoint();

    /**
     * Username - OSGI Property
     */
    @AttributeDefinition(name = "User name", description = "Endpoint Username to use for validation")
    String username();

    /**
     * key - OSGI Property
     */
    @AttributeDefinition(name = "Key", description = "Endpoint key to use for validation")
    String key();
}
