package com.pwr.pdw.app.data.enums;

/**
 * Created by Robert on 2014-05-10.
 */
public enum DoctorSpecialisationEnum {
    Internista, Kardiolog, Neurolog, Ortopeda;
    public static DoctorSpecialisationEnum FromInt(int value){
        return DoctorSpecialisationEnum.values()[value];
    }

}
