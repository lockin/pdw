package com.pwr.pdw.app.ui.acitivites;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pwr.pdw.app.R;
import com.pwr.pdw.app.api.helpers.ApiHelper;

import java.io.IOException;

public class RegisterActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        final EditText editLogin = (EditText) findViewById(R.id.loginText);
        final EditText editPassword = (EditText) findViewById(R.id.passwordText);
        final EditText editFirstName = (EditText) findViewById(R.id.firstName);
        final EditText editLastName = (EditText) findViewById(R.id.lastName);

        Button loginButton = (Button) findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(editLogin.getText().toString()) || TextUtils.isEmpty(editPassword.getText().toString()) || TextUtils.isEmpty(editFirstName.getText().toString()) || TextUtils.isEmpty(editLastName.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "Należy uzupelnić wszystkie pola", Toast.LENGTH_LONG).show();
                } else {
                    new RegisterTask(editLogin.getText().toString(), editPassword.getText().toString(), editFirstName.getText().toString(), editLastName.getText().toString(), RegisterActivity.this).execute();
                }

            }
        });
    }

    private class RegisterTask extends AsyncTask<Void, Void, Boolean> {
        private final String userName;
        private final String password;
        private final String firstName;
        private final String lastName;
        private ProgressDialog dialog;
        private final Context context;

        public RegisterTask(String userName, String password, String firstName, String lastName, Context context) {
            this.userName = userName;
            this.password = password;
            this.context = context;
            this.firstName = firstName;
            this.lastName = lastName;
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(context);
            dialog.setIndeterminate(true);
            dialog.setMessage("Rejestracja");
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
                Toast.makeText(context, "Nie udana rejestracja", Toast.LENGTH_LONG).show();
            } else {
                finish();
            }
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                return ApiHelper.Register(userName, password, firstName, lastName);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
    }

}
