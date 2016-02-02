package wizit.cm.wizit;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;

/**
 * Created by Petrucco on 20.08.2015.
 */
public class WizitApp extends Application {

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        Parse.initialize(this, "aYgomqPi13KqHpLgYkftNZkfTt6Wn4NjUBDm6VKz", "SL8goBidvl5hFX7YezNbvEMSIAiEgEbkqbSx76p6");
        Parse.enableLocalDatastore(this);

    }
}
