package com.pwr.pdw.app.ui.acitivites;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pwr.pdw.app.Constants;
import com.pwr.pdw.app.R;
import com.pwr.pdw.app.api.responses.DoctorResponse;
import com.pwr.pdw.app.api.responses.PacientVisitNoteResponse;
import com.pwr.pdw.app.api.responses.PacientVisitResponse;
import com.pwr.pdw.app.data.enums.DoctorSpecialisationEnum;
import com.pwr.pdw.app.ui.helpers.FormatterHelper;

import java.util.ArrayList;
import java.util.Date;

public class DetailsActivity extends BaseActivity {

    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        if (Constants.ITEM == null) {
            finish();
        } else {
            list = (ListView) findViewById(R.id.details_list);
            ListAdapter adapter = new PacientVisitNoteAdapter(this, (ArrayList<PacientVisitNoteResponse>) Constants.ITEM.getPacientVisitNote());
            list.setAdapter(adapter);
        }
    }

    public class PacientVisitNoteAdapter extends ArrayAdapter<PacientVisitNoteResponse> {

        private final ArrayList<PacientVisitNoteResponse> pacientVisitsNote;
        private LayoutInflater layoutInflater;

        public PacientVisitNoteAdapter(Context context, ArrayList<PacientVisitNoteResponse> pacientVisitsNote) {
            super(context, 0, pacientVisitsNote);
            this.pacientVisitsNote = pacientVisitsNote;
            this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            if (pacientVisitsNote != null)
                return pacientVisitsNote.size();
            else
                return 0;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final PacientVisitNoteResponse item = pacientVisitsNote.get(position);
            convertView = layoutInflater.inflate(R.layout.row_pacient_visit_note, parent, false);
            TextView tvDocotrName = (TextView) convertView.findViewById(R.id.row_doctor_name);
            TextView tvSpec = (TextView) convertView.findViewById(R.id.row_doctor_specialisation);
            TextView tvDate = (TextView) convertView.findViewById(R.id.row_date);
            TextView tvNote = (TextView) convertView.findViewById(R.id.row_note);
            if (item != null) {
                tvDate.setText(item.getStartDate());
                tvNote.setText(item.getNote()==null?"Brak notatki":item.getNote());
                DoctorResponse doctor = item.getDoctor().get(0);
                if (doctor != null) {
                    tvDocotrName.setText(doctor.getFirstName() + " " + doctor.getLastName());
                    tvSpec.setText(DoctorSpecialisationEnum.FromInt(doctor.getSpecialisation()).toString());
                }
            }

            return convertView;
        }
    }
}
