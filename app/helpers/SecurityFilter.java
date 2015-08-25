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
        User user = null;
        String authToken = getTokenFromHeader(ctx);
        if (authToken != null) {
            user = User.findByAuthToken(authToken);
            if (user != null) {
                ctx.args.put("username", user.email);
                return user.getEmail();
            }
        }
        return null;
    }

    @Override
    public Result onUnauthorized(Http.Context context) {
        return super.onUnauthorized(context);
    }

    private String getTokenFromHeader(Http.Context ctx) {
        String authValue = ctx.request().cookies().get("authToken").value().toString();
        if (authValue != null) {
            return authValue;
        }
        return null;
    }
}