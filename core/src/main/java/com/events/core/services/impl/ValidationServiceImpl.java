package com.events.core.services.impl;

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
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Validation Service used by Subscription servlet to validate fields.
 * Uses Http Rest call to Subscription validation servlet to get response.
 *
 *
 */
@Component(service = ValidationService.class)
@Designate(ocd = EndpointConfig.class)
public class ValidationServiceImpl implements ValidationService{

    /**
     * Logger for Service.
     */
    private static final Logger log = LoggerFactory.getLogger(ValidationServiceImpl.class);

    private String endpoint;

    private String username;

    private String key;

    /**
     * Activate of Validation Service Implementation .
     * @param config containing endpoint to validate form fields
     *
     */
    @Activate
    public void activate(final EndpointConfig config) {
        endpoint = config.endpoint();
        username = config.username();
        key = config.key();
    }

    public JsonObject isValidRequest(String fname, String lname, String email){
        JsonObject validationJson = new JsonObject();
        if (!StringUtils.isEmpty(endpoint)) {
            StringBuffer sBuffer = new StringBuffer();
            sBuffer.append(endpoint+"?");
            sBuffer.append("firstName="+fname);
            sBuffer.append("&");
            sBuffer.append("lastName="+lname);
            sBuffer.append("&");
            sBuffer.append("email="+email);

            HttpHost targetHost = new HttpHost("localhost", 4502, "http");
            HttpGet request = new HttpGet(sBuffer.toString());
            CredentialsProvider provider = new BasicCredentialsProvider();
            UsernamePasswordCredentials credentials
                    = new UsernamePasswordCredentials(username, key);
            provider.setCredentials(AuthScope.ANY, credentials);
            AuthCache authCache = new BasicAuthCache();
            authCache.put(targetHost, new BasicScheme());
            HttpClientContext context = HttpClientContext.create();
            context.setCredentialsProvider(provider);
            context.setAuthCache(authCache);
            try (CloseableHttpClient client = HttpClients.createDefault()) {
                HttpResponse response = client.execute(request, context);
                if (response.getStatusLine().getStatusCode() == 200) {
                    HttpEntity httpEntity = response.getEntity();
                    String responseStr = EntityUtils.toString(httpEntity, UTF_8.name());
                    validationJson = (JsonObject) JsonParser.parseString(responseStr);
                } else {
                    JsonUtil.setJsonProperties(403, "Could not get valid response from rest endpoint.",
                            validationJson);
                }
            } catch (IOException e) {
                log.error("IOException occured while making REST call from ValidationServiceImpl "+e.getMessage());
            }
        } else {
            JsonUtil.setJsonProperties(403, "Please configure rest endpoint for field validation.",
                    validationJson);
        }
        return validationJson;
    }

    private static final String getBasicAuthenticationHeader(String username, String password) {
        String valueToEncode = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
    }
}
