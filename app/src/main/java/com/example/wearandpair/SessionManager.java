package com.example.wearandpair;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SessionManager {

    //... (existing variables)
    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;
    private final Context context;

    private static final String PREF_NAME = "WearAndPairLogin";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    public static final String KEY_USER_EMAIL = "userEmail";


    private final String sampleLogin =
            "[" +
                    "{\"username\": \"user1\", \"email\": \"user1@example.com\", \"password\": \"password123\"}," +
                    "{\"username\": \"user2\", \"email\": \"user2@example.com\", \"password\": \"mypassword\"}" +
                    "]";

    public SessionManager(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    /**
     * --- NEW METHOD ---
     * Exposes the raw JSON string of sample users for the demo dialog.
     * @return The sample login data as a JSON string.
     */
    public String getSampleLoginJson() {
        return sampleLogin;
    }

    // ... (rest of the methods in SessionManager remain the same)
    public void createLoginSession(String email) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_USER_EMAIL, email);
        editor.commit();
    }

    public void checkLogin() {
        if (!this.isLoggedIn()) {
            Intent i = new Intent(context, AuthPage.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }

    public String getUserEmail() {
        return prefs.getString(KEY_USER_EMAIL, null);
    }

    public String getUsername() {
        String loggedInEmail = getUserEmail();
        if (loggedInEmail == null) {
            return "Guest";
        }

        try {
            JSONArray users = new JSONArray(sampleLogin);
            for (int i = 0; i < users.length(); i++) {
                JSONObject user = users.getJSONObject(i);
                String email = user.getString("email");
                if (loggedInEmail.equalsIgnoreCase(email)) {
                    return user.getString("username");
                }
            }
        } catch (JSONException e) {
            return "User";
        }
        return "User";
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();
        WishlistManager.resetInstance();
        Intent i = new Intent(context, AuthPage.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(i);
    }
}