package com.smartseals.generic.Modelo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Zait Paulo on 16/02/2019.
 */
public class ResponseEndPointBean implements Parcelable {
    private static final String TAG = ResponseEndPointBean.class.getSimpleName();

    @Expose
    private String message;

    @Expose
    private boolean success;

    @Expose
    private String data;

    @Expose
    private int status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public JSONObject getJSONObject() throws Exception {
        return new JSONObject(data);
    }

    public JSONArray getJSONArray() throws Exception {
        return new JSONArray(data);
    }

    public ResponseEndPointBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.message);
        dest.writeByte(this.success ? (byte) 1 : (byte) 0);
        dest.writeString(this.data);
        dest.writeInt(this.status);
    }

    protected ResponseEndPointBean(Parcel in) {
        this.message = in.readString();
        this.success = in.readByte() != 0;
        this.data = in.readString();
        this.status = in.readInt();
    }

    public static final Creator<ResponseEndPointBean> CREATOR = new Creator<ResponseEndPointBean>() {
        @Override
        public ResponseEndPointBean createFromParcel(Parcel source) {
            return new ResponseEndPointBean(source);
        }

        @Override
        public ResponseEndPointBean[] newArray(int size) {
            return new ResponseEndPointBean[size];
        }
    };
}