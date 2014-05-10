package com.pwr.pdw.app.ui.acitivites;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pwr.pdw.app.R;
import com.pwr.pdw.app.api.ApiConstants;
import com.pwr.pdw.app.api.helpers.ApiHelper;

import java.io.IOException;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText editLogin = (EditText) findViewById(R.id.loginText);
        final EditText editPassword = (EditText) findViewById(R.id.passwordText);

        Button loginButton = (Button) findViewById(R.id.loginButton);
        Button registerButton = (Button) findViewById(R.id.registerButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LoginTask(editLogin.getText().toString(), editPassword.getText().toString(), LoginActivity.this).execute();
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
        finish();
    }

    private class LoginTask extends AsyncTask<Void, Void, Boolean> {
        private final String userName;
        private final String password;
        private ProgressDialog dialog;
        private final Context context;

        public LoginTask(String userName, String password, Context context) {
            this.userName = userName;
            this.password = password;
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(context);
            dialog.setIndeterminate(true);
            dialog.setMessage("Logowanie");
            dialog.setCancelable(false);
            try {
                dialog.show();
            } catch (Exception ignored) {
            }
        }

        protected void onPostExecute(Boolean result) {
            try {
                dialog.dismiss();
            } catch (Exception ignored) {
            }

            if (!result) {
                Toast.makeText(context, "Nie udane logownaie", Toast.LENGTH_LONG).show();
            }else{
                setResult(RESULT_OK);
                finish();
            }
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                String token = ApiHelper.Login(userName, password);
                if (token == null)
                    return false;
                mPrefs.edit().putString(ApiConstants.TOKEN, token).commit();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
    }
}
