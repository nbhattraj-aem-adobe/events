package com.events.core.models;

import com.adobe.cq.wcm.core.components.models.Text;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.*;
import org.apache.sling.models.annotations.via.ResourceSuperType;
import java.util.List;

@Model(adaptables = {Resource.class, SlingHttpServletRequest.class},
        adapters = Text.class,
        resourceType = "events/components/eventdetails",
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class EventDetailsModel implements Text{

    @Self
    @Via(type = ResourceSuperType.class)
    private Text text;

    @ChildResource(name = "details")
    private List<DetailsModel> detailsList;

    public String getText(){
        return text.getText();
    }

    public List<DetailsModel> getDetailsList() {
        return detailsList;
    }



}
