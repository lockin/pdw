package com.pwr.pdw.app.api.responses;

import java.util.List;

/**
 * Created
 */
public class GetPacientVisitResponse extends BaseResponse {
    private List<PacientVisitResponse> data;

    public List<PacientVisitResponse> getDate() {
        return data;
    }
}
