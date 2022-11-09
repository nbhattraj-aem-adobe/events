package com.events.core.services;

import com.google.gson.JsonObject;

/**
 * Validation Service used by Subscription servlet to validate fields.
 * Uses Http Rest call to Subscription validation servlet to get response.
 *
 *
 */

public interface ValidationService  {

    JsonObject isValidRequest(String fname, String lname, String email);
}
