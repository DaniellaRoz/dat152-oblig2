package no.hvl.dat152.rest.ws.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component("userSecurity")
public class UserSecurity {

    public boolean isCurrentUser(Long id) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof JwtAuthenticationToken jwtAuth)) {
            return false;
        }

        Object details = jwtAuth.getDetails();

        if (!(details instanceof UserDetailsImpl userDetails)) {
            return false;
        }

        return id.equals(userDetails.getUserid());
    }
}