package com.trekkon.patigeni.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.widget.Toast;

import com.trekkon.patigeni.model.GambarModel;
import com.trekkon.patigeni.model.TabelStatus;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "Trekkon";

    // Contacts table name
    private static final String KEY_TABEL_GAMBAR = "gambar";
    private static final String KEY_TABEL_STATUS = "tabel_status";

    // Contacts Table Columns names
    private static final String KEY_IDX = "idx";
    private static final String KEY_ID_TITIK = "id_titik";
    private static final String KEY_FILE_GAMBAR = "filegambar";
    private static final String KEY_INDEKS = "indeks";
    private static final String KEY_LAT = "lat";
    private static final String KEY_LONG = "long";
    private static final String KEY_SOS = "sos";
    private static final String KEY_KETERANGAN = "keterangan";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        String buat_tabel_gambar = "CREATE TABLE " + KEY_TABEL_GAMBAR + " (" +
                KEY_IDX + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ," +
                KEY_ID_TITIK + " TEXT," +
                KEY_FILE_GAMBAR + " TEXT," +
                KEY_INDEKS + " TEXT," +
                KEY_LAT + " REAL," +
                KEY_LONG + " REAL," +
                KEY_SOS + " TEXT," +
                KEY_KETERANGAN + " TEXT" +
                ")";
        db.execSQL(buat_tabel_gambar);

        String buat_tabel_status = "CREATE TABLE " + KEY_TABEL_STATUS + " (" +
                KEY_IDX + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ," +
                KEY_ID_TITIK + " TEXT," +
                KEY_KETERANGAN + " TEXT" +
                ")";
        db.execSQL(buat_tabel_status);

        Log.d("Ini","Tabel berhasil dibuat");
    }


    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + KEY_TABEL_GAMBAR);

        // Create tables again
        onCreate(db);
    }

    public void addGambar(GambarModel gambarModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FILE_GAMBAR, gambarModel.getFilegambar());
        values.put(KEY_ID_TITIK, gambarModel.getIdTitik());
        values.put(KEY_INDEKS, gambarModel.getIndeks());
        values.put(KEY_LAT, gambarModel.getLat());
        values.put(KEY_LONG, gambarModel.getLong());
        values.put(KEY_SOS, gambarModel.getSos());
        values.put(KEY_KETERANGAN, gambarModel.getKeterangan());

        db.insert(KEY_TABEL_GAMBAR, null, values);
        db.close(); // Closing database connection
    }

    public void addTabelStatus(TabelStatus tabelStatus){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_TITIK, tabelStatus.getIdTitik());
        values.put(KEY_KETERANGAN, tabelStatus.getKeterangan());

        db.insert(KEY_TABEL_STATUS, null, values);
        db.close(); // Closing database connection
    }

    public List<GambarModel> getAllgambar() {
        List<GambarModel> gambarModels = new ArrayList<GambarModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + KEY_TABEL_GAMBAR;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {


                GambarModel gambarModel = new GambarModel();
                gambarModel.setIdx(cursor.getInt(0));
                gambarModel.setIdTitik(cursor.getString(1));
                gambarModel.setFilegambar(cursor.getString(2));
                gambarModel.setIndeks(cursor.getString(3));
                gambarModel.setLat(cursor.getDouble(4));
                gambarModel.setLong(cursor.getDouble(5));
                gambarModel.setSos(cursor.getString(6));
                gambarModel.setKeterangan(cursor.getString(7));
                gambarModels.add(gambarModel);
            } while (cursor.moveToNext());
        }

        // return contact list
        return gambarModels;
    }


    public List<TabelStatus> getAllTabelStatus() {
        List<TabelStatus> tabelStatuses = new ArrayList<TabelStatus>();
        String selectQuery = "SELECT  * FROM " + KEY_TABEL_STATUS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                TabelStatus tabelStatus = new TabelStatus();
                tabelStatus.setIdx(cursor.getInt(0));
                tabelStatus.setIdTitik(cursor.getString(1));
                tabelStatus.setKeterangan(cursor.getString(2));
                tabelStatuses.add(tabelStatus);
            } while (cursor.moveToNext());
        }

        // return contact list
        return tabelStatuses;
    }


    public GambarModel getGambar(int id, Context context) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(KEY_TABEL_GAMBAR, new String[] { KEY_IDX,
                        KEY_ID_TITIK, KEY_FILE_GAMBAR, KEY_INDEKS, KEY_LAT, KEY_LONG, KEY_SOS, KEY_KETERANGAN }, KEY_IDX + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null){
            cursor.moveToFirst();
            GambarModel gambarModel = new GambarModel(
                    Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getDouble(4),
                    cursor.getDouble(5),
                    cursor.getString(6),
                    cursor.getString(7)
            );
            return gambarModel;
        }else{
            Toast.makeText(context, "Tidak ada data", Toast.LENGTH_SHORT).show();

            GambarModel gambarModel = new GambarModel();
            gambarModel.setFilegambar("");
            return gambarModel;
        }


        //GambarModel gambarModel = new GambarModel();
        //gambarModel.setIdx(cursor.getInt(0));
        //gambarModel.setIdTitik(cursor.getString(1));
        //gambarModel.setFilegambar(cursor.getString(2));
        //gambarModel.setIndeks(cursor.getString(3));
        //gambarModel.setLat(cursor.getDouble(4));
        //gambarModel.setLong(cursor.getDouble(5));
        //gambarModel.setSos(cursor.getString(6));
        //gambarModel.setKeterangan(cursor.getString(7));




    }

    public Boolean cekDetail(String idNotif, Context context){
        TabelStatus tabelStatus = null;
        Boolean hasil = false;

        List<TabelStatus> tabelStatuses = this.getAllTabelStatus();
        for (int i = 0; i<= tabelStatuses.size()-1;i++){
            tabelStatus = tabelStatuses.get(i);
            if (tabelStatus.getIdTitik().equals(idNotif)){
                hasil = true ;
                break;
            }
        }

        return hasil;
    }

    public String getDetail(String idNotif, Context context){
        TabelStatus tabelStatus = null;
        String hasil="";

        List<TabelStatus> tabelStatuses = this.getAllTabelStatus();
        for (int i = 0; i<= tabelStatuses.size()-1;i++){
            tabelStatus = tabelStatuses.get(i);
            if (tabelStatus.getIdTitik().equals(idNotif)){
                hasil = tabelStatus.getKeterangan() ;
                break;
            }
        }

        return hasil;
    }

    public TabelStatus getTabelStatus(String idNotif, Context context) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(KEY_TABEL_STATUS, new String[] { KEY_IDX,
                        KEY_ID_TITIK, KEY_KETERANGAN }, KEY_ID_TITIK + "=?",
                new String[] { idNotif}, null, null, null, null);
        if (cursor != null){
            cursor.moveToFirst();


            TabelStatus ts = new TabelStatus();
            ts.setIdTitik(cursor.getString(1));
            ts.setKeterangan(cursor.getString(2));
            return ts;

        }else{

            TabelStatus tabelStatus = new TabelStatus();
            tabelStatus.setKeterangan("");
            return tabelStatus;
        }


    }

    public long getGambarCount() {

        SQLiteDatabase database = this.getReadableDatabase();
        long total = DatabaseUtils.longForQuery(database,"select count(*) from gambar", null);

        return total;
    }

    public long getTabelStatusCount(){
        SQLiteDatabase database = this.getReadableDatabase();
        long total = DatabaseUtils.longForQuery(database,"select count(*) from tabel_status", null);

        return total;
    }

    public void DeleteTableGambar(){
        String sql = "DELETE FROM " + KEY_TABEL_GAMBAR;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        sqLiteDatabase.execSQL(sql);
    }

    public void DeleteTabelStatus(){
        String sql = "DELETE FROM " + KEY_TABEL_STATUS;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        sqLiteDatabase.execSQL(sql);
    }


    public void DropTableGambar(){
        String sql = "DROP TABLE IF EXISTS " + KEY_TABEL_GAMBAR;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        sqLiteDatabase.execSQL(sql);

    }

    public void DropTabelStatus(){
        String sql = "DROP TABLE IF EXISTS " + KEY_TABEL_STATUS;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        sqLiteDatabase.execSQL(sql);

    }

    public void deleteGambar(GambarModel gambarModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(KEY_TABEL_GAMBAR, KEY_IDX + " = ?",
                new String[] { String.valueOf(gambarModel.getIdx()) });
        db.close();
    }

    public void deleteTabelStatus(TabelStatus tabelStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(KEY_TABEL_STATUS, KEY_IDX + " = ?",
                new String[] { String.valueOf(tabelStatus.getIdx()) });
        db.close();
    }

    public void deleteGambar(int idx){
        String sql = "DELETE FROM " + KEY_TABEL_GAMBAR + " WHERE " + KEY_IDX + " = " + String.valueOf(idx);
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        sqLiteDatabase.execSQL(sql);
    }

    public void deleteTabelStatus(String idNotif){
        String sql = "DELETE FROM " + KEY_TABEL_STATUS + " WHERE " + KEY_ID_TITIK + " = " + idNotif;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        sqLiteDatabase.execSQL(sql);
    }

    public void UpdateKeterangan(String keterangan){

        String sql = "UPDATE " + KEY_TABEL_GAMBAR + " SET KETERANGAN = " + keterangan;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        sqLiteDatabase.execSQL(sql);
    }

    public void UpdateKeterangan(String keterangan, int idx){

        String sql = "UPDATE " + KEY_TABEL_GAMBAR + " SET " + KEY_KETERANGAN + " = " + keterangan + " WHERE " + KEY_IDX + " = " + String.valueOf(idx);
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        sqLiteDatabase.execSQL(sql);
    }



}