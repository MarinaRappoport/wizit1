package wizit.cm.wizit;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by Petrucco on 03.09.2015.
 */
public class ParseWizit extends Application {

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "aYgomqPi13KqHpLgYkftNZkfTt6Wn4NjUBDm6VKz", "SL8goBidvl5hFX7YezNbvEMSIAiEgEbkqbSx76p6");
    }
}
