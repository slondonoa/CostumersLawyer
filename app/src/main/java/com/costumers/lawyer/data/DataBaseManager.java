package com.costumers.lawyer.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.costumers.lawyer.entities.Persons;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stiven on 2/29/2016.
 */
public class DataBaseManager {
    public static final String TABLE_NAME_PERSONS="Persons";
    public static final String TABLE_NAME_FILTER="FilterPerson";
    public static String CN_IdPerson = "IdPerson";
    public static String CN_Name = "Name";
    public static String CN_LastName = "LastName";
    public static String CN_Document = "Document";
    public static String CN_address = "address";
    public static String CN_phone = "phone";
    public static String CN_phone2 = "phone2";
    public static String CN_phone3 = "phone3";
    public static String CN_birthdate = "birthdate";
    public static String CN_Sex = "Sex";
    public static String CN_civilstatus = "civilstatus";
    public static String CN_source = "source";
    public static String CN_processed = "processed";
    public static String CN_clientstatus = "clientstatus";
    public static String CN_observations = "observations";
    public static String CN_lastContact = "lastContact";
    public static String CN_ProcessStatus = "ProcessStatus";
    public static String CN_LimitDateProcessStatus = "LimitDateProcessStatus";
    public static String CN_cell1 = "cell1";
    public static String CN_cell2 = "cell2";
    public static String CN_cell3 = "cell3";
    public static String CN_maial1 = "maial1";
    public static String CN_maial2 = "maial2";
    public static String CN_Where= "Filter";
    public static String CN_ID="id";
    public static String CN_START="start";

    public static final String CREATE_TABLE_PERSONS = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_PERSONS + "(" +
            CN_IdPerson + " TEXT, " +
            CN_Name + " TEXT, " +
            CN_LastName + " TEXT, " +
            CN_Document + " TEXT, " +
            CN_address + " TEXT, " +
            CN_phone + " TEXT, " +
            CN_phone2 + " TEXT, " +
            CN_phone3+ " TEXT, " +
            CN_birthdate + " TEXT, " +
            CN_Sex + " TEXT, " +
            CN_civilstatus + " TEXT, " +
            CN_source + " TEXT, " +
            CN_processed + " TEXT, " +
            CN_clientstatus + " TEXT, " +
            CN_observations + " TEXT, " +
            CN_lastContact + " TEXT, " +
            CN_ProcessStatus + " TEXT, " +
            CN_LimitDateProcessStatus + " TEXT, " +
            CN_cell1 + " TEXT, " +
            CN_cell2 + " TEXT, " +
            CN_cell3 + " TEXT, " +
            CN_maial1 + " TEXT, " +
            CN_maial2+ " TEXT, " +
            CN_START + "TEXT);";

    public static final String CREATE_TABLE_FILTER = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_FILTER + "(" +
            CN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CN_Where+ " TEXT);";


    private Connection cn;
    private SQLiteDatabase db;
    public DataBaseManager(Context context) {
        cn=new Connection(context);
        db=cn.getWritableDatabase();
    }

    public void  Close(Context context)
    {
        cn=new Connection(context);
        cn.close();
    }

    public void Open(Context context)
    {
        cn=new Connection(context);
        cn.getWritableDatabase();
    }

    public  void InsertCostumers(List<Persons> lstperson)
    {
        List<Persons> lstper=lstperson;
        long v=0;
        db.delete(TABLE_NAME_PERSONS, null, null);
        for (Persons person:lstperson) {

            v= db.insert(TABLE_NAME_PERSONS, null, ContentValuesPersons(person));
            long g=v;
        }
    }

    public  void InsertCostumer(List<Persons> lstperson)
    {
        List<Persons> lstper=lstperson;
        long v=0;
        for (Persons person:lstperson) {
            v= db.insert(TABLE_NAME_PERSONS, null, ContentValuesPersons(person));
            long g=v;
        }
    }

    public  void UpdatePerson(List<Persons> lstperson)
    {
        List<Persons> lstper=lstperson;
        long v=0;
        for (Persons person:lstperson) {
            db.delete(TABLE_NAME_PERSONS,CN_IdPerson +"="+ person.IdPerson,null);
            v= db.insert(TABLE_NAME_PERSONS, null, ContentValuesPersons(person));
            long g=v;
        }
    }


    public  void  InsertFilter(String filter)
    {
        db.insert(TABLE_NAME_FILTER,null,ContentValuesFilter(filter));

    }

    public void  DeleteFilter()
    {
        db.delete(TABLE_NAME_FILTER, null, null);
    }

    public String getFilter()
    {
        String selectQuery = "SELECT * FROM " + TABLE_NAME_FILTER;
        //Cursor cursor =db.query(TABLE_NAME_FILTER, new String[]{CN_Where}, null, null, null, null, null); //db.rawQuery(selectQuery, null);
        Cursor cursor =db.rawQuery(selectQuery, null);
        String filter="";
// looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                filter = (cursor.getString(cursor.getColumnIndex(CN_Where)));;
            } while (cursor.moveToNext());

            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return filter;
    }

    public List<Persons> getFilterPersons(String filter) {
        List<Persons> PersonsList = new ArrayList<Persons>();
// Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_NAME_PERSONS  +" "+ filter; //+ " LIMIT 5; ";//
        //Cursor cursor =db.query(TABLE_NAME_PERSONS,new String[]{CN_Name,CN_lastContact},null,null,null,null,null); //db.rawQuery(selectQuery, null);
        Cursor cursor =db.rawQuery(selectQuery, null);

// looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String ad=cursor.getString(0);
                Persons persons = new Persons();

                persons.address=(cursor.getString(cursor.getColumnIndex(CN_address)));
                persons.birthdate=(cursor.getString(cursor.getColumnIndex(CN_birthdate)));
                persons.cell1=(cursor.getString(cursor.getColumnIndex(CN_cell1)));
                persons.cell2=(cursor.getString(cursor.getColumnIndex(CN_cell2)));
                persons.cell3=(cursor.getString(cursor.getColumnIndex(CN_cell3)));
                persons.civilstatus=(cursor.getString(cursor.getColumnIndex(CN_civilstatus)));
                persons.clientstatus=(cursor.getString(cursor.getColumnIndex(CN_clientstatus)));
                persons.Document=(cursor.getString(cursor.getColumnIndex(CN_Document)));
                persons.IdPerson=(cursor.getString(cursor.getColumnIndex(CN_IdPerson)));
                persons.lastContact=(cursor.getString(cursor.getColumnIndex(CN_lastContact)));
                persons.LastName=(cursor.getString(cursor.getColumnIndex(CN_LastName)));
                persons.LimitDateProcessStatus=(cursor.getString(cursor.getColumnIndex(CN_LimitDateProcessStatus)));
                persons.maial1=(cursor.getString(cursor.getColumnIndex(CN_maial1)));
                persons.maial2=(cursor.getString(cursor.getColumnIndex(CN_maial2)));

                persons.Name=(cursor.getString(cursor.getColumnIndex(CN_Name)));

                persons.observations=(cursor.getString(cursor.getColumnIndex(CN_observations)));
                persons.phone=(cursor.getString(cursor.getColumnIndex(CN_phone)));
                persons.phone2=(cursor.getString(cursor.getColumnIndex(CN_phone2)));
                persons.phone3=(cursor.getString(cursor.getColumnIndex(CN_phone3)));
                persons.processed=(cursor.getString(cursor.getColumnIndex(CN_processed)));
                persons.ProcessStatus=(cursor.getString(cursor.getColumnIndex(CN_ProcessStatus)));
                persons.Sex=(cursor.getString(cursor.getColumnIndex(CN_Sex)));
                persons.source=(cursor.getString(cursor.getColumnIndex(CN_source)));

// Adding contact to list
                PersonsList.add(persons);
            } while (cursor.moveToNext());

            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
// return contact list
        return PersonsList;
    }


    public List<Persons> getAllPersons() {
        List<Persons> PersonsList = new ArrayList<Persons>();
// Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_NAME_PERSONS;
        //Cursor cursor =db.query(TABLE_NAME_PERSONS,new String[]{CN_IdPerson,CN_birthdate},null,null,null,null,null); //db.rawQuery(selectQuery, null);
        Cursor cursor =db.rawQuery(selectQuery, null);

// looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String ad=cursor.getString(0);
                Persons persons = new Persons();
                persons.address=(cursor.getString(cursor.getColumnIndex(CN_address)));
                persons.birthdate=(cursor.getString(cursor.getColumnIndex(CN_birthdate)));
                persons.cell1=(cursor.getString(cursor.getColumnIndex(CN_cell1)));
                persons.cell2=(cursor.getString(cursor.getColumnIndex(CN_cell2)));
                persons.cell3=(cursor.getString(cursor.getColumnIndex(CN_cell3)));
                persons.civilstatus=(cursor.getString(cursor.getColumnIndex(CN_civilstatus)));
                persons.clientstatus=(cursor.getString(cursor.getColumnIndex(CN_clientstatus)));
                persons.Document=(cursor.getString(cursor.getColumnIndex(CN_Document)));
                persons.IdPerson=(cursor.getString(cursor.getColumnIndex(CN_IdPerson)));
                persons.lastContact=(cursor.getString(cursor.getColumnIndex(CN_lastContact)));
                persons.LastName=(cursor.getString(cursor.getColumnIndex(CN_LastName)));
                persons.LimitDateProcessStatus=(cursor.getString(cursor.getColumnIndex(CN_LimitDateProcessStatus)));
                persons.maial1=(cursor.getString(cursor.getColumnIndex(CN_maial1)));
                persons.maial2=(cursor.getString(cursor.getColumnIndex(CN_maial2)));
                persons.Name=(cursor.getString(cursor.getColumnIndex(CN_Name)));
                persons.observations=(cursor.getString(cursor.getColumnIndex(CN_observations)));
                persons.phone=(cursor.getString(cursor.getColumnIndex(CN_phone)));
                persons.phone2=(cursor.getString(cursor.getColumnIndex(CN_phone2)));
                persons.phone3=(cursor.getString(cursor.getColumnIndex(CN_phone3)));
                persons.processed=(cursor.getString(cursor.getColumnIndex(CN_processed)));
                persons.ProcessStatus=(cursor.getString(cursor.getColumnIndex(CN_ProcessStatus)));
                persons.Sex=(cursor.getString(cursor.getColumnIndex(CN_Sex)));
                persons.source=(cursor.getString(cursor.getColumnIndex(CN_source)));

// Adding contact to list
                PersonsList.add(persons);
            } while (cursor.moveToNext());

            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
// return contact list
        return PersonsList;
    }

    public List<Persons> getValidatePersons() {
        List<Persons> PersonsList = new ArrayList<Persons>();
// Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_NAME_PERSONS +" LIMIT 1; ";;
        //Cursor cursor =db.query(TABLE_NAME_PERSONS,new String[]{CN_IdPerson,CN_birthdate},null,null,null,null,null); //db.rawQuery(selectQuery, null);
        Cursor cursor =db.rawQuery(selectQuery, null);

// looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String ad=cursor.getString(0);
                Persons persons = new Persons();
                persons.address=(cursor.getString(cursor.getColumnIndex(CN_address)));
                persons.birthdate=(cursor.getString(cursor.getColumnIndex(CN_birthdate)));
                persons.cell1=(cursor.getString(cursor.getColumnIndex(CN_cell1)));
                persons.cell2=(cursor.getString(cursor.getColumnIndex(CN_cell2)));
                persons.cell3=(cursor.getString(cursor.getColumnIndex(CN_cell3)));
                persons.civilstatus=(cursor.getString(cursor.getColumnIndex(CN_civilstatus)));
                persons.clientstatus=(cursor.getString(cursor.getColumnIndex(CN_clientstatus)));
                persons.Document=(cursor.getString(cursor.getColumnIndex(CN_Document)));
                persons.IdPerson=(cursor.getString(cursor.getColumnIndex(CN_IdPerson)));
                persons.lastContact=(cursor.getString(cursor.getColumnIndex(CN_lastContact)));
                persons.LastName=(cursor.getString(cursor.getColumnIndex(CN_LastName)));
                persons.LimitDateProcessStatus=(cursor.getString(cursor.getColumnIndex(CN_LimitDateProcessStatus)));
                persons.maial1=(cursor.getString(cursor.getColumnIndex(CN_maial1)));
                persons.maial2=(cursor.getString(cursor.getColumnIndex(CN_maial2)));
                persons.Name=(cursor.getString(cursor.getColumnIndex(CN_Name)));
                persons.observations=(cursor.getString(cursor.getColumnIndex(CN_observations)));
                persons.phone=(cursor.getString(cursor.getColumnIndex(CN_phone)));
                persons.phone2=(cursor.getString(cursor.getColumnIndex(CN_phone2)));
                persons.phone3=(cursor.getString(cursor.getColumnIndex(CN_phone3)));
                persons.processed=(cursor.getString(cursor.getColumnIndex(CN_processed)));
                persons.ProcessStatus=(cursor.getString(cursor.getColumnIndex(CN_ProcessStatus)));
                persons.Sex=(cursor.getString(cursor.getColumnIndex(CN_Sex)));
                persons.source=(cursor.getString(cursor.getColumnIndex(CN_source)));

// Adding contact to list
                PersonsList.add(persons);
            } while (cursor.moveToNext());

            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
// return contact list
        return PersonsList;
    }


    public  ContentValues ContentValuesPersons(Persons persons)
    {
        ContentValues valores=new ContentValues();
        valores.put(CN_IdPerson ,persons.IdPerson);
        valores.put(CN_Name,persons.Name);
        valores.put(CN_LastName,persons.LastName);
        valores.put(CN_Document,persons.Document);
        valores.put(CN_address,persons.address);
        valores.put(CN_phone,persons.phone);
        valores.put(CN_phone2,persons.phone2);
        valores.put(CN_phone3,persons.phone3);
        valores.put(CN_birthdate,persons.birthdate);
        valores.put(CN_Sex,persons.Sex);
        valores.put(CN_civilstatus,persons.civilstatus);
        valores.put(CN_source,persons.source);
        valores.put(CN_processed ,persons.processed);
        valores.put(CN_clientstatus,persons.clientstatus);
        valores.put(CN_observations ,persons.observations);
        valores.put(CN_lastContact,persons.lastContact);
        valores.put(CN_ProcessStatus,persons.ProcessStatus);
        valores.put(CN_LimitDateProcessStatus,persons.LimitDateProcessStatus);
        valores.put(CN_cell1,persons.cell1);
        valores.put(CN_cell2 ,persons.cell2);
        valores.put(CN_cell3,persons.cell3);
        valores.put(CN_maial1,persons.maial1);
        valores.put(CN_maial2,persons.maial2);
        valores.put(CN_START,persons.start);
        return valores;
    }


    public  ContentValues ContentValuesFilter(String filter)
    {
        ContentValues valores=new ContentValues();
        valores.put(CN_Where ,filter);
        return valores;
    }


    public List<Persons> getPerson(String document) {
        List<Persons> PersonsList = new ArrayList<Persons>();
// Select All Query
        //String selectQuery = "SELECT * FROM " + TABLE_NAME_PERSONS;
        String[] args = new String[] {document};
        Cursor cursor =db.query(TABLE_NAME_PERSONS,new String[]{CN_IdPerson,CN_Document}, CN_Document+"=?",args,null,null,null); //db.rawQuery(selectQuery, null);
        //Cursor cursor =db.rawQuery(selectQuery, null);

// looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String ad=cursor.getString(0);
                Persons persons = new Persons();
                persons.Document=(cursor.getString(cursor.getColumnIndex(CN_Document)));
                persons.IdPerson=(cursor.getString(cursor.getColumnIndex(CN_IdPerson)));
// Adding contact to list
                PersonsList.add(persons);
            } while (cursor.moveToNext());

            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
// return contact list
        return PersonsList;
    }


}
