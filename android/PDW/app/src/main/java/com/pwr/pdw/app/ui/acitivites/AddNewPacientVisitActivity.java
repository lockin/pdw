package com.pwr.pdw.app.ui.acitivites;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.pwr.pdw.app.R;
import com.pwr.pdw.app.api.ApiConstants;
import com.pwr.pdw.app.api.helpers.ApiHelper;
import com.pwr.pdw.app.api.responses.DoctorResponse;
import com.pwr.pdw.app.api.responses.GetDoctorResponse;
import com.pwr.pdw.app.data.enums.DoctorSpecialisationEnum;
import com.pwr.pdw.app.ui.helpers.FormatterHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddNewPacientVisitActivity extends BaseActivity {

    private Spinner doctorsSpinner;
    private TimePicker timePicker;
    private DatePicker datePicker;
    private GetDoctorResponse doctorsList;
    private DoctorResponse selectedDoctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_pacient_visit);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        doctorsSpinner = (Spinner) findViewById(R.id.add_newpacient_doctors);
        timePicker = (TimePicker) findViewById(R.id.time_picker);
        timePicker.setIs24HourView(true);
        datePicker = (DatePicker) findViewById(R.id.date_picker);
        new GetDoctorTask(this).execute();
        Button setVisit = (Button) findViewById(R.id.set_visit);
        setVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.MINUTE, timePicker.getCurrentMinute());
                cal.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                cal.set(Calendar.MONTH, datePicker.getMonth());
                cal.set(Calendar.YEAR, datePicker.getYear());
                cal.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                if (cal.after(Calendar.getInstance()))
                    new SetPacientVisitTask(AddNewPacientVisitActivity.this, cal).execute();
                else
                    Toast.makeText(AddNewPacientVisitActivity.this, "Wizyta musi być w przyszłości", Toast.LENGTH_LONG).show();
            }
        });
        doctorsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                selectedDoctor = doctorsList.getData().get(pos);
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private class SetPacientVisitTask extends AsyncTask<Void, Void, Boolean> {
        private ProgressDialog dialog;
        private final Context context;
        private String startDate;
        private String stopDate;

        public SetPacientVisitTask(Context context, Calendar cal) {
            this.context = context;


            this.startDate = FormatterHelper.getFormattedDate(cal);
            cal.add(Calendar.HOUR_OF_DAY, 1);
            this.stopDate = FormatterHelper.getFormattedDate(cal);

        }


        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(context);
            dialog.setIndeterminate(true);
            dialog.setMessage("Pobieranie lekarzy");
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
                Toast.makeText(context, "Nie udało się zarejestrować wizyty", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "Wizyta zarejestrowana", Toast.LENGTH_LONG).show();
                finish();
            }
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                Log.i("PWD","startDate:"+startDate+" stopDate:"+stopDate+" selectedDoctor.getId():"+selectedDoctor.getId());
                return ApiHelper.SetPacientVisit(mPrefs.getString(ApiConstants.TOKEN, ""), selectedDoctor.getId(), startDate, stopDate);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class GetDoctorTask extends AsyncTask<Void, Void, GetDoctorResponse> {
        private ProgressDialog dialog;
        private final Context context;

        public GetDoctorTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(context);
            dialog.setIndeterminate(true);
            dialog.setMessage("Pobieranie lekarzy");
            dialog.setCancelable(false);
            try {
                dialog.show();
            } catch (Exception ignored) {
            }
        }

        protected void onPostExecute(GetDoctorResponse result) {
            try {
                dialog.dismiss();
            } catch (Exception ignored) {
            }
            doctorsList = result;
            if (result == null) {
                finish();
                Toast.makeText(context, "Nie udało się pobrać lekarzy", Toast.LENGTH_LONG).show();
            } else {
                List<String> list = new ArrayList<String>();
                for (DoctorResponse item : result.getData()) {
                    list.add(item.getFirstName() + " " + item.getLastName() + " (" + DoctorSpecialisationEnum.FromInt(item.getSpecialisation()) + ")");
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, list);

                doctorsSpinner.setAdapter(adapter);
            }
        }

        @Override
        protected GetDoctorResponse doInBackground(Void... voids) {
            try {
                return ApiHelper.GetDoctor();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
