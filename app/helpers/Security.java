package helpers;

import controllers.UserCtrl;
import models.User;
import play.mvc.Http;
import play.mvc.Result;

/**
 * Created by dastko on 8/25/15.
 */
public class Security extends play.mvc.Security.Authenticator {

    @Override
    public String getUsername(Http.Context ctx) {
        User user = null;
        String[] authTokenHeaderValues = ctx.request().headers().get(UserCtrl.AUTH_TOKEN_HEADER);
        if ((authTokenHeaderValues != null) && (authTokenHeaderValues.length == 1) && (authTokenHeaderValues[0] != null)) {
            user = User.findByAuthToken(authTokenHeaderValues[0]);
            if (user != null) {
                ctx.args.put("user", user);
                return user.getEmail();
            }
        }
        return null;
    }
    @Override
    public Result onUnauthorized(Http.Context context) {
        return forbidden();
    }
}
