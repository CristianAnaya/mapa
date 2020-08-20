package com.smartseals.generic.Modelo;

import com.google.gson.annotations.SerializedName;

public class UsuarioBean {
    @SerializedName("username")
   private String username;


    @SerializedName("password")
    private String password;

    @SerializedName("email")
    private String email;

    @SerializedName("name")
    private String name;


    @SerializedName("role")
    private String role;


    @SerializedName("area_id")
    private String area_id;


    @SerializedName("_id")
    private String _id;

    public UsuarioBean() {
    }


    public UsuarioBean(String username, String email, String role, String area_id, String _id) {
        this.username = username;
        this.email = email;
        this.role = role;
        this.area_id = area_id;
        this._id = _id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
