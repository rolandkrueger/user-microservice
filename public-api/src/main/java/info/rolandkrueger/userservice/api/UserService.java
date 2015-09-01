package info.rolandkrueger.userservice.api;

import com.google.common.base.Preconditions;
import info.rolandkrueger.userservice.api._internal.AbstractRestClient;
import info.rolandkrueger.userservice.api._internal.RestApiConstants;
import info.rolandkrueger.userservice.api.exceptions.UnexpectedAPIFormatException;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkDiscoverer;
import org.springframework.hateoas.hal.HalLinkDiscoverer;

import java.util.List;

/**
 * @author Roland Krüger
 */
public class UserService extends AbstractRestClient {

    private Link authoritiesLink;
    private Link usersLink;

    UserService() {
    }

    public void init(String targetURI) {
        Preconditions.checkNotNull(targetURI);

        String rootURIData = restTemplate.getForObject(targetURI, String.class);
        LinkDiscoverer linkDiscoverer = new HalLinkDiscoverer();
        authoritiesLink = readAuthoritiesLink(targetURI, rootURIData, linkDiscoverer);
        usersLink = readUsersLink(targetURI, rootURIData, linkDiscoverer);
    }

    private Link readUsersLink(String targetURI, String rootURIData, LinkDiscoverer linkDiscoverer) {
        List<Link> usersLinks = linkDiscoverer.findLinksWithRel(RestApiConstants.USERS_RESOURCE, rootURIData);
        if (usersLinks.size() != 1) {
            throw new UnexpectedAPIFormatException("Unexpected link structure at API root: found {0} links for " +
                    "resource \"{1}\" at {2}", usersLinks.size(), RestApiConstants
                    .USERS_RESOURCE, targetURI);
        }

        return usersLinks.get(0);
    }

    private Link readAuthoritiesLink(String targetURI, String rootURIData, LinkDiscoverer linkDiscoverer) {
        List<Link> authoritiesLinks = linkDiscoverer.findLinksWithRel(RestApiConstants.AUTHORITIES_RESOURCE, rootURIData);
        if (authoritiesLinks.size() != 1) {
            throw new UnexpectedAPIFormatException("Unexpected link structure at API root: found {0} links for " +
                    "resource \"{1}\" at {2}", authoritiesLinks.size(), RestApiConstants
                    .AUTHORITIES_RESOURCE, targetURI);
        }
        return authoritiesLinks.get(0);
    }
}
