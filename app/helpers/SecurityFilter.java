package helpers;

import models.User;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;


/**
 * Created by dastko on 8/25/15.
 */
public class SecurityFilter extends Security.Authenticator {

    @Override
    public String getUsername(Http.Context ctx) {
        String authToken = getTokenFromCookie(ctx);
        if (authToken != null) {
            User user = User.findByAuthToken(authToken);
            if (user != null) {
                return user.getEmail();
            }
        }
        return null;
    }

    @Override
    public Result onUnauthorized(Http.Context context) {
        return forbidden();
    }

    private String getTokenFromCookie(Http.Context ctx) {
        String authValue = ctx.request().cookies().get("authToken").value().toString();
        if (authValue != null) {
            return authValue;
        }
        return null;
    }
}