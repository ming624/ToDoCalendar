package com.andante624.todocalendar.DataManage.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kmkyoung on 2014. 9. 28..
 */
public class DBHelper extends SQLiteOpenHelper{
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String category_table = "create table Category_Table( " +
                " Category_ID integer primary key autoincrement NOT NULL, " +
                " Category_Title text NOT NULL);";
        db.execSQL(category_table);

        String todo_table = "create table ToDo_Table( " +
                " ToDo_ID integer primary key autoincrement NOT NULL, " +
                " ToDo_Title text NOT NULL, " +
                " ToDo_Created_date text NOT NULL, " +
                " ToDo_Deadline_date text NOT NULL, " +
                " ToDo_Completed_date text NOT NULL, " +
                " Category_ID integer NOT NULL default(0), " +
                " ToDo_Importance float NOT NULL," +
                " FOREIGN KEY(Category_ID) REFERENCES Category_Table(Category_ID));";
        db.execSQL(todo_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql_droptable= "DROP TABLE IF EXISTS " + "Category_Table;";
        db.execSQL(sql_droptable);
        sql_droptable = "DROP TABLE IF EXISTS " + "ToDo_Table;";
        db.execSQL(sql_droptable);
    }

}
