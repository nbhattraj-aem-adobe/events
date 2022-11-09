# Events Project

This is a project containing code for events code challenge for Adobe.

As part of the challenge below features have been created:

1. Event-Details page : /content/events/us/en/movies/black-adam.html

2. Event Details component : custom component that extends wcm-core text component, uses sling model that using sling delegate. 

3. Event-Register page: /content/events/us/en/movies/black-adam/subscribe-now.html - Navigate via "Subscribe now" button on Event details page.

4. Servlet that accepts 3 fields firstname , lastname , email and on successful validation creates node at /etc/events/subscriptions with lastname as the name of the node.
    
    **Prerequisite:** **System User Created with name 'subscriptionWriteService' and containing jcr:all permissions on '/etc/events'**
   Servlet Endpoint : http://localhost:4502/content/events/us/en/jcr:content.subscription.json?firstName=name&lastName=lastname&email=check@check.com

5. Rest endpoint servlet and service that validates the firstname, lastname and email fields.

   Servlet Endpoint : http://localhost:4502/content/events/us/en/jcr:content.subscriptionvalidation.json?firstName=name&lastName=lastname&email=check@check.com

6. Junit for SubscriptionServlet class.

**Improvement areas:**
1. Connection between submit on Event-Register page and servlet that validates, persists data.
2. Usage of real REST Endpoint could help reuse ACS-COMMONS REST connector service rather than using custom AEM Servlet as REST Endpoint having to provide username, password in config.
3. Usage of Encryption on username, password fields in config.
