package geekhub.activeshoplistapp.helpers;

/**
 * Created by rage on 06.02.15.
 *
 * On this class will be a constant application
 */
public final class AppConstants {

    public static final String APP_PREFERENCES = "ASlistPreferences";
    public static final String APP_PREFERENCES_USERNAME = "UserName";

    public static final int LOGIN_BUTTON_CONTINUE = 1000;
    public static final int LOGIN_BUTTON = 1001;
    public static final int LOGIN_G_PLUS_BUTTON = 1002;

    public static final int REQUEST_CODE_GOOGLE_PLUS_AUTH = 2000;



    private final static String G_PLUS_SCOPE = "https://www.googleapis.com/auth/plus.me";
    private final static String G_PLUS_USER_INFO_SCOPE = "https://www.googleapis.com/auth/userinfo.profile";
    private final static String G_PLUS_EMAIL_SCOPE = "https://www.googleapis.com/auth/userinfo.email";
    public final static String G_PLUS_SCOPES = "oauth2:" + G_PLUS_SCOPE + " " + G_PLUS_USER_INFO_SCOPE + " " + G_PLUS_EMAIL_SCOPE;
}
