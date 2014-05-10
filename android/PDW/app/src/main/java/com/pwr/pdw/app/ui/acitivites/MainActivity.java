package com.pwr.pdw.app.ui.acitivites;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pwr.pdw.app.Constants;
import com.pwr.pdw.app.R;
import com.pwr.pdw.app.api.ApiConstants;
import com.pwr.pdw.app.api.helpers.ApiHelper;
import com.pwr.pdw.app.api.responses.DoctorResponse;
import com.pwr.pdw.app.api.responses.GetPacientVisitResponse;
import com.pwr.pdw.app.api.responses.PacientVisitNoteResponse;
import com.pwr.pdw.app.api.responses.PacientVisitResponse;
import com.pwr.pdw.app.data.enums.DoctorSpecialisationEnum;
import com.pwr.pdw.app.ui.helpers.FormatterHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends BaseActivity {

    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = (ListView) findViewById(R.id.main_list);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPrefs.getString(ApiConstants.TOKEN, "").equals("")) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, Constants.REQUEST_LOGIN);
        } else {
            refreshList();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, Constants.MENU_REFRESH, 0, "Odśwież").setIcon(android.R.drawable.ic_menu_more).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(0, Constants.MENU_ADD, 0, "Dodaj").setIcon(android.R.drawable.ic_menu_add).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(0, Constants.MENU_LOGOUT, 0, "Wyloguj się").setIcon(android.R.drawable.ic_lock_power_off).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case Constants.MENU_ADD: {
                Intent intent = new Intent(MainActivity.this, AddNewPacientVisitActivity.class);
                startActivity(intent);
            }
            return true;
            case Constants.MENU_REFRESH: {
                refreshList();
            }
            return true;
            case Constants.MENU_LOGOUT: {
                mPrefs.edit().putString(ApiConstants.TOKEN, "").commit();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, Constants.REQUEST_LOGIN);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.REQUEST_LOGIN:
                if (resultCode == RESULT_OK) {
                    refreshList();
                } else {
                    finish();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public class PacientVisitAdapter extends ArrayAdapter<PacientVisitResponse> {

        private final ArrayList<PacientVisitResponse> pacientVisits;
        private LayoutInflater layoutInflater;

        public PacientVisitAdapter(Context context, ArrayList<PacientVisitResponse> pacientVisits) {
            super(context, 0, pacientVisits);
            this.pacientVisits = pacientVisits;
            this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            if (pacientVisits != null)
                return pacientVisits.size();
            else
                return 0;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final PacientVisitResponse item = pacientVisits.get(position);
            convertView = layoutInflater.inflate(R.layout.row_pacient_visit, parent, false);
            TextView tvDocotrName = (TextView) convertView.findViewById(R.id.row_doctor_name);
            TextView tvSpec = (TextView) convertView.findViewById(R.id.row_doctor_specialisation);
            TextView tvDate = (TextView) convertView.findViewById(R.id.row_date);

            final PacientVisitNoteResponse pacientVisitNoteResponse = item.getPacientVisitNote().get(item.getPacientVisitNote().size() - 1);
            if (pacientVisitNoteResponse != null) {
                tvDate.setText(pacientVisitNoteResponse.getStartDate());
                if (FormatterHelper.fromISODateString(pacientVisitNoteResponse.getStartDate()).after(new Date())) {
                    tvDate.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                    tvDocotrName.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                    tvSpec.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                } else {
                    tvDate.setTextColor(getResources().getColor(android.R.color.black));
                    tvDocotrName.setTextColor(getResources().getColor(android.R.color.black));
                    tvSpec.setTextColor(getResources().getColor(android.R.color.black));
                }
                DoctorResponse doctor = pacientVisitNoteResponse.getDoctor().get(0);
                if (doctor != null) {
                    tvDocotrName.setText(doctor.getFirstName() + " " + doctor.getLastName());
                    tvSpec.setText(DoctorSpecialisationEnum.FromInt(doctor.getSpecialisation()).toString());
                }
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (FormatterHelper.fromISODateString(pacientVisitNoteResponse.getStartDate()).after(new Date())) {
                        Toast.makeText(MainActivity.this, "Ta wizyta dopiero się odbędzie", Toast.LENGTH_LONG).show();
                    } else {
                        Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                        Constants.ITEM = item;
                        //intent.putExtra(Constants.EXTRA_ITEM,item);
                        startActivity(intent);
                    }
                }
            });
            return convertView;
        }

    }

    private void refreshList() {

        new RefreshListTask(this).execute();
    }

    private class RefreshListTask extends AsyncTask<Void, Void, GetPacientVisitResponse> {

        private ProgressDialog dialog;
        private final Context context;

        public RefreshListTask(Context context) {

            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(context);
            dialog.setIndeterminate(true);
            dialog.setMessage("Aktualizacja danych");
            dialog.setCancelable(false);
            try {
                dialog.show();
            } catch (Exception ignored) {
            }
        }

        protected void onPostExecute(GetPacientVisitResponse result) {
            try {
                dialog.dismiss();
            } catch (Exception ignored) {
            }
            ListAdapter adapter = new PacientVisitAdapter(context, (ArrayList<PacientVisitResponse>) result.getDate());
            list.setAdapter(adapter);
        }

        @Override
        protected GetPacientVisitResponse doInBackground(Void... voids) {
            try {
                return ApiHelper.getPacientVisit(mPrefs.getString(ApiConstants.TOKEN, ""));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
