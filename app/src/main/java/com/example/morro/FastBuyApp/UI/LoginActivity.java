package com.example.morro.FastBuyApp.UI;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.example.morro.FastBuyApp.R;
import com.example.morro.FastBuyApp.UI.Admin.AdminMainActivity;
import com.example.morro.FastBuyApp.UI.Buyer.BuyerMainActivity;

/**
 * Provides a login interface for user or admin login.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText usernameET;
    private EditText passwordET;

    /**
     * Gets called upon activity creation, instantiate the view
     * @param savedInstanceState system bundle for resuming activity after app sleep
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameET = (EditText) findViewById(R.id.user_name);
        passwordET  = (EditText) findViewById(R.id.pass_word);
        findViewById(R.id.login_button).setOnClickListener(this);
    }

    /**
     * Called when the user taps the login button
     * @return true if login successful, false otherwise
     */
    private boolean login() {
        Intent intent = null;

        String user = usernameET.getText().toString();
        String psw = passwordET.getText().toString();

        switch (user.toLowerCase()) {
            case "admin":
                if (psw.equals("admin")) {
                    intent = new Intent(this, AdminMainActivity.class);
                } else {
                    userAlert("Error","Wrong password for admin");
                    return false;
                }
                break;
            case "user":
                if (psw.equals("user")) {
                    intent = new Intent(this, BuyerMainActivity.class);
                } else {
                    userAlert("Error","Wrong password for user!");
                    return false;
                }
                break;
            default:
                userAlert("Error","Wrong user name or password");
                return false;
        }
        startActivity(intent);
        return true;
    }

    /**
     * onClick listener for the login button (capable of handling any other button as well)
     * @param view containing the id of the button for identification
     */
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.login_button){
            if(!login())
                System.out.println("Login error");
        }
    }

    /**
     * Alerts the user with a custom message
     * @param message to be delivered to the user
     */
    private void userAlert(String title, String message){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(title);
        dialogBuilder.setMessage(message);

        dialogBuilder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                System.out.println("Operation abort");
            }
        });

        AlertDialog b = dialogBuilder.create();
        b.show();
    }

}
