package wizit.cm.wizit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.with.tourbuild.Login;
import com.with.tourbuild.SignUp;

public class WelcomeActivity extends Activity {
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        if (ParseUser.getCurrentUser()!=null){
            intent = new Intent(this, MainActivity.class);
            //            при нажатии кнопки Back не возвращаться на предыдущее окно:
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ParseUser.getCurrentUser()!=null){
            intent = new Intent(this, MainActivity.class);
            //            при нажатии кнопки Back не возвращаться на предыдущее окно:
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    public void signUpWelcome(View view) {
        startActivity(new Intent(getApplicationContext(), SignUp.class));
    }

    public void logInWelcome(View view) {
        startActivity(new Intent(getApplicationContext(), Login.class));
    }

    public void skip(View view) {
        intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
