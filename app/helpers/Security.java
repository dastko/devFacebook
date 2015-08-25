package helpers;

import models.User;
import play.mvc.Http;
import play.mvc.Result;

/**
 * Created by dastko on 8/25/15.
 */
public class Security extends play.mvc.Security.Authenticator {

    @Override
    public String getUsername(Http.Context ctx) {
        String token = getTokenFromHeader(ctx);
        if (token != null) {
            User user = User.find.where().eq("authToken", token).findUnique();
            if (user != null) {
                return user.email;
            }
        }
        return null;
    }

    @Override
    public Result onUnauthorized(Http.Context context) {
        return super.onUnauthorized(context);
    }

    private String getTokenFromHeader(Http.Context ctx) {
        String[] authTokenHeaderValues = ctx.request().headers().get("X-AUTH-TOKEN");
        if ((authTokenHeaderValues != null) && (authTokenHeaderValues.length == 1) && (authTokenHeaderValues[0] != null)) {
            return authTokenHeaderValues[0];
        }
        return null;
    }
}
