package com.smartseals.generic.Modelo;

public class TrabajoModel {
    String code, start_date, end_date,status,_id;

    public TrabajoModel() {
    }

    public TrabajoModel(String code, String start_date, String end_date, String status, String _id) {
        this.code = code;
        this.start_date = start_date;
        this.end_date = end_date;
        this.status = status;
        this._id = _id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
