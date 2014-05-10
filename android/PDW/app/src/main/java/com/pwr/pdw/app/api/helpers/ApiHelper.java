package com.pwr.pdw.app.api.helpers;

import com.google.gson.Gson;
import com.pwr.pdw.app.api.ApiConstants;
import com.pwr.pdw.app.api.responses.BaseResponse;
import com.pwr.pdw.app.api.responses.GetDoctorResponse;
import com.pwr.pdw.app.api.responses.GetPacientVisitResponse;
import com.pwr.pdw.app.api.responses.LoginResponse;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert on 2014-05-10.
 */

public class ApiHelper {
    public static String Login(String username, String password) throws IOException {
        Gson gson = new Gson();
        final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair(ApiConstants.FIELD_EMAIL, username));
        nameValuePairs.add(new BasicNameValuePair(ApiConstants.FIELD_PASSWORD, password));

        LoginResponse response = gson.fromJson(ConnectionHelper.postRequest(ApiConstants.URL + ApiConstants.METHOD_LOGIN, nameValuePairs), LoginResponse.class);
        if (response.isSuccess())
            return response.getToken();
        else
            return null;
    }

    public static boolean Register(String username, String password, String firstName, String lastName) throws IOException {
        Gson gson = new Gson();
        final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair(ApiConstants.FIELD_EMAIL, username));
        nameValuePairs.add(new BasicNameValuePair(ApiConstants.FIELD_PASSWORD, password));
        nameValuePairs.add(new BasicNameValuePair(ApiConstants.FIELD_FIRSTNAME, firstName));
        nameValuePairs.add(new BasicNameValuePair(ApiConstants.FIELD_LASTNAME, lastName));

        BaseResponse response = gson.fromJson(ConnectionHelper.postRequest(ApiConstants.URL + ApiConstants.METHOD_REGISTER, nameValuePairs), BaseResponse.class);
        return response.isSuccess();
    }

    public static GetPacientVisitResponse getPacientVisit(String token) throws IOException {
        Gson gson = new Gson();
        final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair(ApiConstants.FIELD_TOKEN, token));

        GetPacientVisitResponse response = gson.fromJson(ConnectionHelper.postRequest(ApiConstants.URL + ApiConstants.METHOD_GET_PACIENT_VISIT, nameValuePairs), GetPacientVisitResponse.class);
        return response;
    }

    public static GetDoctorResponse GetDoctor() throws IOException {
        Gson gson = new Gson();

        GetDoctorResponse response = gson.fromJson(ConnectionHelper.postRequest(ApiConstants.URL + ApiConstants.METHDO_GET_DOCTOR, new ArrayList<NameValuePair>()), GetDoctorResponse.class);
        return response;
    }

    public static boolean SetPacientVisit(String token, int doctorId, String startDate, String stopDate) throws IOException {
        Gson gson = new Gson();
        final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair(ApiConstants.FIELD_TOKEN, token));
        nameValuePairs.add(new BasicNameValuePair(ApiConstants.FIELD_DOCTORID, doctorId+""));
        nameValuePairs.add(new BasicNameValuePair(ApiConstants.FIELD_STARTDATE, startDate));
        nameValuePairs.add(new BasicNameValuePair(ApiConstants.FIELD_STOPDATE, stopDate));

        BaseResponse response = gson.fromJson(ConnectionHelper.postRequest(ApiConstants.URL + ApiConstants.METHOD_SET_PACIENT_VISIT, nameValuePairs), BaseResponse.class);
        return response.isSuccess();
    }
}
