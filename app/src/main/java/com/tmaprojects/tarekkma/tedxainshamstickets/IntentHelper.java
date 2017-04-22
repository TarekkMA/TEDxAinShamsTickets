package com.tmaprojects.tarekkma.tedxainshamstickets;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by tarekkma on 4/22/17.
 */

public class IntentHelper {
    public static void openLink(Context c, String s){
        if (!s.startsWith("http://") && !s.startsWith("https://"))
            s = "http://" + s;
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(s));
        c.startActivity(browserIntent);
    }
    public static void openGitHubRebo(Context c){
        openLink(c,"https://github.com/TarekkMA/TEDxAinShamsTickets");
    }
    public static void openFacebook(Context c){
        openLink(c,"https://www.facebook.com/TarekkMA1");
    }
    public static void openLinkin(Context c){
        openLink(c,"https://www.linkedin.com/in/tarekkma/");
    }
}
