package com.pwr.pdw.app.api.responses;

import java.util.List;

/**
 * Created
 */
public class GetDoctorResponse extends BaseResponse{
    private List<DoctorResponse> data;

    public List<DoctorResponse> getData() {
        return data;
    }
}
