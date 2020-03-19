package com.itcast.contentobserver;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class PersonProvider extends ContentProvider {
    private static UriMatcher mUriMatcher=new UriMatcher(-1);
    private static final int SUCCESS=1;
    private PersonDBOpenHelper helper;
    static {
        mUriMatcher.addURI("com.itcast.contentobserver","info",SUCCESS);
    }
    public PersonProvider(Context context) {}

    public PersonProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int code=mUriMatcher.match(uri);
        if (code==SUCCESS) {
            SQLiteDatabase db = helper.getReadableDatabase();
            int count=db.delete("info",selection,selectionArgs);
            if (count>0){
                getContext().getContentResolver().notifyChange(uri,null);
            }
            db.close();
            return count;
        }else {
            throw new UnsupportedOperationException("路径不正确,拒绝给你数据!!!");
        }
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int code=mUriMatcher.match(uri);
        if (code==SUCCESS){
            SQLiteDatabase db=helper.getReadableDatabase();
            long rowId=db.insert("info",null,values);
            if (rowId>0){
                Uri insertedUri= ContentUris.withAppendedId(uri,rowId);
                getContext().getContentResolver().notifyChange(uri,null);
                return insertedUri;
            }
            db.close();
            return uri;
        }else{
        throw new UnsupportedOperationException("路径不正确,拒绝给你数据!!!");
    }
    }

    @Override
    public boolean onCreate() {
        helper=new PersonDBOpenHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        int code=mUriMatcher.match(uri);
        if (code==SUCCESS){
            SQLiteDatabase db=helper.getReadableDatabase();
            return db.query("info",projection,selection,selectionArgs,null,null,null,sortOrder);
        }else {
            throw new UnsupportedOperationException("路径不正确,拒绝给你数据!!!");
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int code=mUriMatcher.match(uri);
        if (code==SUCCESS) {
            SQLiteDatabase db = helper.getReadableDatabase();
            int count=db.delete("info",selection,selectionArgs);
            if (count>0){
                getContext().getContentResolver().notifyChange(uri,null);
            }
            db.close();
            return count;
        }else {
            throw new UnsupportedOperationException("路径不正确,拒绝给你数据!!!");
        }
    }
}
