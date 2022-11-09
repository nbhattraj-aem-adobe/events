package com.events.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;


@Model(adaptables = Resource.class,
        resourceType = "events/components/eventsubscription",
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class EventSubscriptionModel {

    @ValueMapValue
    @Default(values = "First name:")
    private String firstNameLabel;

    @ValueMapValue
    @Default(values = "Last name:")
    private String lastNameLabel;

    @ValueMapValue
    @Default(values = "Email:")
    private String emailLabel;

    @ValueMapValue
    private String thankYouPage;

    public String getFirstNameLabel(){
        return firstNameLabel;
    }

    public String getLastNameLabel(){
        return lastNameLabel;
    }

    public String getEmailLabel(){
        return emailLabel;
    }

    public String getThankYouPage() {return thankYouPage;}

}
