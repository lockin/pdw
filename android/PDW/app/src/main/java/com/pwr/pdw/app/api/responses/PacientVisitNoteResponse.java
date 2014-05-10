package com.pwr.pdw.app.api.responses;

import java.util.List;

/**
 * Created
 */
public class PacientVisitNoteResponse {
    private int id;
    private String startDate;
    private String note;
    private String stopDate;
    private List<DoctorResponse> Doctor;

    public String getStartDate() {
        return startDate;
    }

    public String getNote() {
        return note;
    }

    public String getStopDate() {
        return stopDate;
    }

    public List<DoctorResponse> getDoctor() {
        return Doctor;
    }

    public int getId() {
        return id;
    }
}
