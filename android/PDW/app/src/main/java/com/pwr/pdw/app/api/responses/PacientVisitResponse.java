package com.pwr.pdw.app.api.responses;

import java.util.List;

/**
 * Created
 */
public class PacientVisitResponse {
    private int id;
    private String createDate;
    private List<PacientVisitNoteResponse> pacientVisitNote;

    public String getCreateDate() {
        return createDate;
    }

    public List<PacientVisitNoteResponse> getPacientVisitNote() {
        return pacientVisitNote;
    }

    public int getId() {
        return id;
    }
}
