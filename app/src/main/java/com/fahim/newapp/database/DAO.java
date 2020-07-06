package com.fahim.newapp.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.fahim.newapp.holder.BookHolder;
import com.fahim.newapp.holder.StandardHolder;
import com.fahim.newapp.holder.SubjectHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class DAO implements DataBaseInterface {

    private static final String TAG = DAO.class.getSimpleName();

    private DBHelpers dbHelper;

    private static DAO instance = null;


    private String

            SELECT_STANDARD_DATA,
            SELECT_SUBJECT_DATA,
            SELECT_BOOK_DATA,

    DELETE_STANDARD, DELETE_SUBJECT, DELETE_BOOKS,

    SELECT_SCAN_DATA,
            SELECT_STOCK_DATA,
            SELECT_TOTAL_STOCK_DATA,
            SELECT_TOTAL_STOCK_DATA_ROOM,
            SELECT_STOCK_MAP,
            SELECT_TEMP_STOCK_DATA,
            SELECT_TOTAL_STOCK_MAP,
            SELECT_FOUND_DATA_BY_LIMIT_OFFSET,
            SELECT_FOUND_DATA,
            SELECT_MISSING_DATA_BY_LIMIT_OFFSET,
            SELECT_EXTRA_DATA_BY_LIMIT_OFFSET,
            SELECT_MISSMATCH_DATA_BY_LIMIT_OFFSET,
            SELECT_ASSETTRANSFER_LIST_BY_LIMIT_OFFSET,
            SELECT_ASSETTRANSFER_LIST,
            SELECT_CUSTODIANTRANSFER_LIST,
            SELECT_FOUND_TOTAL,
            SELECT_MISSING_TOTAL,


    SELECT_FOUND_DATA_BY_CATEGORY_BY_LIMIT_OFFSET,
            SELECT_MISSING_DATA_BY_CATEGORY_BY_LIMIT_OFFSET,

    SELECT_MISSING_DATA,

    SELECT_LOCATION_DATA,

    SELECT_BUILDING_DATA,

    SELECT_FLOOR_DATA, SELECT_ASSETTRANSFERSTOCK_DATA,

    SELECT_PROCESS_STOCK_DATA,
            SELECT_STOCKTRANSFER_DATA,
            DELETE_FROM_ASSETTRANSFER_LIST,
            SELECT_MASTERSTOCK_BY_TYPE,
            SELECT_MASTERSTOCKLIST_BY_TYPE,
            SELECT_FOUND_CATEGORY,
            SELECT_MISSING_CATEGORY,
            SELECT_MISMATCH_CATEGORY,
            SELECT_CUSTODIANTRANSFER_DATA,
            SELECT_TAG_ASSET, SELECT_CUSTODIAN_DATA,
            SELECT_MOVED_DATA;


    public DAO() {


        SELECT_STANDARD_DATA = new StringBuffer(" select *").append(" from ")
                .append(TABLE_STANDARD_HOLDER).toString();
        SELECT_SUBJECT_DATA = new StringBuffer(" select *").append(" from ")
                .append(TABLE_SUBJECT_HOLDER).toString();
        SELECT_BOOK_DATA = new StringBuffer(" select *").append(" from ")
                .append(TABLE_BOOKS_HOLDER).toString();

        DELETE_STANDARD = new StringBuffer(" delete").append(" from ")
                .append(TABLE_STANDARD_HOLDER).toString();
        DELETE_SUBJECT = new StringBuffer(" delete").append(" from ")
                .append(TABLE_SUBJECT_HOLDER).toString();
        DELETE_BOOKS = new StringBuffer(" delete").append(" from ")
                .append(TABLE_BOOKS_HOLDER).toString();

        dbHelper = DBHelpers.getInstance();

    }

    public static DAO getInstance() {

        if (instance == null) {

            instance = new DAO();
        }

        return instance;

    }


    public void insertStandardList(List<StandardHolder> standardHolderArrayList) {

        Log.e(TAG, "StandardHolder: " + standardHolderArrayList.size());
        synchronized (DBHelpers.lock) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            try {
                db.beginTransaction();


                for (StandardHolder standardHolder : standardHolderArrayList) {
                    ContentValues values = new ContentValues();

                    values.put(STANDARD_ID, standardHolder.getId());
                    values.put(STANDARD_NAME, standardHolder.getStandardName());
                    // Inserting Row
                    db.insert(TABLE_STANDARD_HOLDER, null, values);

                    //System.out.println("Stopped Inserting");


                }

                db.setTransactionSuccessful();
            } catch (Exception e) {
                //System.out.println("Exception while inserting into  table" + e);
            } finally {
                db.endTransaction();
                db.close();
            }
        }

    }

    public void insertSubjectList(List<SubjectHolder> list) {

        Log.e(TAG, "StandardHolder: " + list.size());
        synchronized (DBHelpers.lock) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            try {
                db.beginTransaction();


                for (SubjectHolder holder : list) {
                    ContentValues values = new ContentValues();

                    values.put(SUBJECT_ID, holder.getId());
                    values.put(SUBJECT_STANDARD_ID, holder.getStandard_id());
                    values.put(SUBJECT_NAME, holder.getSubject_name());
                    // Inserting Row
                    db.insert(TABLE_SUBJECT_HOLDER, null, values);

                    //System.out.println("Stopped Inserting");


                }

                db.setTransactionSuccessful();
            } catch (Exception e) {
                //System.out.println("Exception while inserting into  table" + e);
            } finally {
                db.endTransaction();
                db.close();
            }
        }

    }

    public void insertBookList(List<BookHolder> list) {

        Log.e(TAG, "StandardHolder: " + list.size());
        synchronized (DBHelpers.lock) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            try {
                db.beginTransaction();


                for (BookHolder holder : list) {
                    ContentValues values = new ContentValues();
                    values.put(BOOKS_ID, holder.getId());
                    values.put(BOOKS_STANDARD_ID, holder.getStandardid());
                    values.put(Books_SUBJECT_ID, holder.getSubjectid());
                    values.put(BOOKS_NAME, holder.getBookname());
                    values.put(BOOKS_LINK, holder.getBooklink());
                    values.put(BOOKS_VIEW_COUNT, holder.getViewCount());


                    // Inserting Row
                    db.insert(TABLE_BOOKS_HOLDER, null, values);

                    //System.out.println("Stopped Inserting");


                }

                db.setTransactionSuccessful();
            } catch (Exception e) {
                System.out.println("Exception while inserting into  table" + e);
            } finally {
                db.endTransaction();
                db.close();
            }
        }

    }

/*

    public HashMap<String, Integer> getStockQtyMapList() {
        synchronized (AMSDBHelpers.lock) {

            //System.out.println("in get");
            HashMap<String, Integer> namelist = new HashMap<String, Integer>();

            String name;


            // SQLiteDatabase db = dbHelper.getReadableDatabase();
            //  Cursor cursor = db.ruery(new StringBuffer(SELECT_CATEGORY_QTY).toString(), null);
            namelist.clear();
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor c = db.rawQuery(new StringBuffer(SELECT_STOCK_MAP).toString(), null);
            if (c.moveToFirst()) {
                do {
                    //assigning values


                    //Do something Here with values
                    namelist.put(c.getString(1), c.getInt(2));
                } while (c.moveToNext());
            }
            c.close();
            db.close();


            return namelist;
        }
    }


    public ArrayList<ImageHolding> getImageList() {
        synchronized (AMSDBHelpers.lock) {

            //System.out.println("in get");
            ArrayList<ImageHolding> namelist = new ArrayList<ImageHolding>();

            String name;


            // SQLiteDatabase db = dbHelper.getReadableDatabase();
            //  Cursor cursor = db.ruery(new StringBuffer(SELECT_CATEGORY_QTY).toString(), null);
            namelist.clear();
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            ImageHolding imageHolding;
            Cursor c = db.rawQuery(new StringBuffer("select * from ").append(TABLE_IMAGELIST).toString(), null);
            if (c.moveToFirst()) {
                do {
                    //assigning values


                    //Do something Here with values
                    imageHolding = new ImageHolding();
                    //imageHolding.setAssetimg(c.getString(1));
                    imageHolding.setAssetId(c.getString(1));
                    //imageHolding.setAssetCode(c.getString(1));
                    imageHolding.setTagimg(c.getBlob(3));
                    imageHolding.setAssetimg(c.getBlob(4));
                    namelist.add(imageHolding);
                } while (c.moveToNext());
            }
            c.close();
            db.close();


            return namelist;
        }
    }

    public void deleteImagelistitem(String assetid) {
        synchronized (AMSDBHelpers.lock) {

            //System.out.println("in get");


            // SQLiteDatabase db = dbHelper.getReadableDatabase();
            //  Cursor cursor = db.ruery(new StringBuffer(SELECT_CATEGORY_QTY).toString(), null);

            SQLiteDatabase db = dbHelper.getReadableDatabase();


            System.out.println("assetID=" + assetid);
            db.delete(TABLE_IMAGELIST, STOCK_ASSETID + "=" + "'" + assetid + "'", null);


            db.close();


        }
    }


    public int getColumnsListsCount() {
        synchronized (AMSDBHelpers.lock) {

            //System.out.println("in get");
            ArrayList<ColumnIdHolder> namelist = new ArrayList<ColumnIdHolder>();

            int idcount = 0;


            // SQLiteDatabase db = dbHelper.getReadableDatabase();
            //  Cursor cursor = db.ruery(new StringBuffer(SELECT_CATEGORY_QTY).toString(), null);

            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor c = db.rawQuery(new StringBuilder("select count(").append(COLUMN_ID).append(") from ").append(TABLE_COLUMN_HOLDER).toString(), null);
            if (c.moveToFirst()) {
                do {
                    //assigning values
                    idcount = c.getInt(0);
                } while (c.moveToNext());
            }
            c.close();
            db.close();


            return idcount;
        }
    }


    public void deleteAllIdentifyTable() {

        synchronized (AMSDBHelpers.lock) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            try {
                db.beginTransaction();

                db.delete(TABLE_ASSET_IDENTIFICATION, null, null);

                db.setTransactionSuccessful();

            } catch (Exception e) {
                // TODO: handle exception
                System.out.println("Exception" + e);
            } finally {
                db.endTransaction();
                db.close();
            }
        }
    }


    public int getCountOfIdentifyAssetList(String TranId) {
        synchronized (AMSDBHelpers.lock) {

            //System.out.println("in get");


            int name = 0;


            // SQLiteDatabase db = dbHelper.getReadableDatabase();
            //  Cursor cursor = db.ruery(new StringBuffer(SELECT_CATEGORY_QTY).toString(), null);

            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor c = db.rawQuery(new StringBuffer(" select count(*)").append(" from ")
                    .append(TABLE_ASSET_IDENTIFICATION)
                    .append(" where ").append(TRANID)
                    .append(" = ").append("'" + TranId + "'")
                    .toString().toString(), null);
            if (c.moveToFirst()) {
                do {
                    //assigning values


                    name = c.getInt(0);
                    //Do something Here with values

                } while (c.moveToNext());
            }
            c.close();
            db.close();


            return name;
        }
    }


    public void updateAssetIdentifyISAdded(int assetid) {
//        TABLE_MISSING_HOLDER
        synchronized (AMSDBHelpers.lock) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            try {
                db.beginTransaction();


                ContentValues args = new ContentValues();

                args.put(ISADDED, 1);
                db.update(TABLE_ASSET_IDENTIFICATION, args, IDENTIFY_ASSETID + "=" + assetid, null);

                db.setTransactionSuccessful();

            } catch (Exception e) {
                // TODO: handle exception
                //System.out.println("Exception" + e);
            } finally {
                db.endTransaction();
                db.close();
            }
        }
    }
*/

    public ArrayList<StandardHolder> getAllStandard() {
        {
            synchronized (DBHelpers.lock) {


                ArrayList<StandardHolder> list = new ArrayList<>();

                StandardHolder mHolder;

                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Cursor c = db.rawQuery(new StringBuffer(SELECT_STANDARD_DATA).toString(), null);
                list.clear();
                if (c.moveToFirst()) {
                    do {
                        //assigning values
                        mHolder = new StandardHolder();

                        mHolder.setId(c.getInt(c.getColumnIndex(STANDARD_ID)));
                        mHolder.setStandardName(c.getString(c.getColumnIndex(STANDARD_NAME)));


                        //Do something Here with values
                        list.add(mHolder);
                    } while (c.moveToNext());
                }
                c.close();
                db.close();


                return list;
            }
        }
    }

    public ArrayList<SubjectHolder> getAllSubjectWithId(int std) {
        {
            synchronized (DBHelpers.lock) {


                ArrayList<SubjectHolder> list = new ArrayList<>();

                SubjectHolder mHolder;

                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Cursor c = db.rawQuery(new StringBuffer(SELECT_SUBJECT_DATA).append(" where ")
                        .append(SUBJECT_STANDARD_ID).append(" = ").append(std).toString(), null);
                list.clear();
                if (c.moveToFirst()) {
                    do {
                        //assigning values
                        mHolder = new SubjectHolder();

                        mHolder.setId(c.getInt(c.getColumnIndex(STANDARD_ID)));
                        mHolder.setStandard_id(c.getInt(c.getColumnIndex(SUBJECT_STANDARD_ID)));
                        mHolder.setSubject_name(c.getString(c.getColumnIndex(SUBJECT_NAME)));


                        //Do something Here with values
                        list.add(mHolder);
                    } while (c.moveToNext());
                }
                c.close();
                db.close();


                return list;
            }
        }
    }

    public ArrayList<SubjectHolder> getAllSubject() {
        {
            synchronized (DBHelpers.lock) {


                ArrayList<SubjectHolder> list = new ArrayList<>();

                SubjectHolder mHolder;

                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Cursor c = db.rawQuery(new StringBuffer(SELECT_SUBJECT_DATA).toString(), null);
                list.clear();
                if (c.moveToFirst()) {
                    do {
                        //assigning values
                        mHolder = new SubjectHolder();

                        mHolder.setId(c.getInt(c.getColumnIndex(STANDARD_ID)));
                        mHolder.setStandard_id(c.getInt(c.getColumnIndex(SUBJECT_STANDARD_ID)));
                        mHolder.setSubject_name(c.getString(c.getColumnIndex(SUBJECT_NAME)));


                        //Do something Here with values
                        list.add(mHolder);
                    } while (c.moveToNext());
                }
                c.close();
                db.close();


                return list;
            }
        }
    }


    public void deleteAllStandardRows() {

        synchronized (DBHelpers.lock) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            try {
                db.beginTransaction();

                db.delete(TABLE_STANDARD_HOLDER, null, null);

                db.setTransactionSuccessful();

            } catch (Exception e) {

                //System.out.println("Exception" + e);
            } finally {
                db.endTransaction();
                db.close();
            }
        }
    }

    public void deleteAllSubjectRows() {

        synchronized (DBHelpers.lock) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            try {
                db.beginTransaction();

                db.delete(TABLE_SUBJECT_HOLDER, null, null);

                db.setTransactionSuccessful();

            } catch (Exception e) {

                //System.out.println("Exception" + e);
            } finally {
                db.endTransaction();
                db.close();
            }
        }
    }

    public void deleteSubjectIDRows(int id) {

        synchronized (DBHelpers.lock) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            try {
                db.beginTransaction();

                db.delete(TABLE_SUBJECT_HOLDER, SUBJECT_ID + "=?", new String[]{String.valueOf(id)});

                db.setTransactionSuccessful();

            } catch (Exception e) {

                //System.out.println("Exception" + e);
            } finally {
                db.endTransaction();
                db.close();
            }
        }
    }

    public void deleteBookIDRows(int id) {

        synchronized (DBHelpers.lock) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            try {
                db.beginTransaction();

                db.delete(TABLE_BOOKS_HOLDER, BOOKS_ID + "=?", new String[]{String.valueOf(id)});

                db.setTransactionSuccessful();

            } catch (Exception e) {

                //System.out.println("Exception" + e);
            } finally {
                db.endTransaction();
                db.close();
            }
        }
    }

    public void deleteAllBooksRows() {

        synchronized (DBHelpers.lock) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            try {
                db.beginTransaction();

                db.delete(TABLE_BOOKS_HOLDER, null, null);

                db.setTransactionSuccessful();

            } catch (Exception e) {

                //System.out.println("Exception" + e);
            } finally {
                db.endTransaction();
                db.close();
            }
        }
    }

    public ArrayList<BookHolder> getAllBooks(int selectedStandardId, int selectedSubjectId) {
        synchronized (DBHelpers.lock) {


            ArrayList<BookHolder> list = new ArrayList<>();

            BookHolder mHolder;

            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor c = db.rawQuery(new StringBuffer(SELECT_BOOK_DATA).append(" where ")
                    .append(BOOKS_STANDARD_ID).append(" = ").append(selectedStandardId)
                    .append(" AND ")
                    .append(Books_SUBJECT_ID).append(" = ").append(selectedSubjectId).toString(), null);
            list.clear();
            if (c.moveToFirst()) {
                do {
                    //assigning values
                    mHolder = new BookHolder();

                    mHolder.setId(c.getInt(c.getColumnIndex(BOOKS_ID)));
                    mHolder.setStandardid(c.getInt(c.getColumnIndex(BOOKS_STANDARD_ID)));
                    mHolder.setSubjectid(c.getInt(c.getColumnIndex(Books_SUBJECT_ID)));
                    mHolder.setViewCount(c.getInt(c.getColumnIndex(BOOKS_VIEW_COUNT)));
                    mHolder.setBookname(c.getString(c.getColumnIndex(BOOKS_NAME)));
                    mHolder.setBooklink(c.getString(c.getColumnIndex(BOOKS_LINK)));


                    //Do something Here with values
                    list.add(mHolder);
                } while (c.moveToNext());
            }
            c.close();
            db.close();


            return list;
        }


    }
}