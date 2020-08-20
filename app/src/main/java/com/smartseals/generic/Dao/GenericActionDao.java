package com.smartseals.generic.Dao;

import android.database.Cursor;

import java.util.ArrayList;

/**
 * Created by Zait Paulo on 25/01/2019.
 */
public interface GenericActionDao<T> {

    boolean existe(String tabla, String column, String dato);

    boolean existe(String tabla, String column, long dato);

    T toEntity(Cursor cursor);

    ArrayList<T> toListOfEntities(Cursor cursor);
}