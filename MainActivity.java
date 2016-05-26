package com.example.pzhxbz.note;

import android.app.AlertDialog;
import android.support.v4.app.FragmentManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements Edit.FOneBtnClickListener,Welcome.welcomeThing {
    SQLiteDatabase mDb;
    SQLiteDatabaseDao dao;
    public ArrayList<HashMap<String, Object>> listData;
    private Edit edit;
    FragmentManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        dao =new SQLiteDatabaseDao();
        Welcome welcome=new Welcome();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.watching, welcome);
        transaction.commit();
    }
    @Override
    public void onFOneBtnClick(String Title,String Content)
    {
        insert(Title, Content);
        Welcome welcome=new Welcome();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.watching, welcome);
        transaction.commit();
    }
    @Override
    public void listClick(String title,String content){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        edit=new Edit();
        transaction.replace(R.id.watching, edit);
        edit.listClickSet(title, content);
        transaction.commit();
    }
    @Override
    public void addClick(){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        edit=new Edit();
        transaction.replace(R.id.watching,edit);
        transaction.commit();
    }
    @Override
    public ArrayList<HashMap<String, Object>> getData(){
        dao.getAllData("note");
        return listData;
    }
    @Override
    public void deleteData(int pos,int position){
        delete(pos, position);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        Welcome welcome=new Welcome();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.setCustomAnimations(
                R.anim.push_left_in,
                R.anim.push_left_out,
                R.anim.push_left_in,
                R.anim.push_left_out);
        transaction.replace(R.id.watching, welcome);
        transaction.commit();
    }
    @Override
    public void finish() {
        // TODO Auto-generated method stub
        super.finish();
        mDb.close();
    }
    public void insert(String title,String content){
        dao.insert(mDb, "note", title, content);
    }
    public void delete(int pos,int position){
        if (dao.delete(mDb, "note", pos)) {
            listData.remove(position);
        }
    }
    public String time(){
        Date date=new Date();
        DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time=format.format(date);
        return time;
    }
    public class SQLiteDatabaseDao {
        public SQLiteDatabaseDao() {
            mDb = openOrCreateDatabase("users.db",
                    SQLiteDatabase.CREATE_IF_NECESSARY, null);
            createTable(mDb, "note");
            getAllData("note");
        }
        public void createTable(SQLiteDatabase mDb, String table) {
            try {
                mDb.execSQL("create table if not exists "
                        + table
                        + " (id integer primary key autoincrement, "
                        + "title text not null, content text not null,date text not null);");
            } catch (SQLException e) {
                Toast.makeText(getApplicationContext(), "数据表创建失败",
                        Toast.LENGTH_LONG).show();
            }
        }
        public void insert(SQLiteDatabase mDb, String table,String s1,String s2) {
            ContentValues values = new ContentValues();
            values.put("title", s1);
            values.put("content", s2);
            values.put("date", time());
            mDb.insert(table, null, values);
        }
        public void getAllData(String table) {
            Cursor c = mDb.rawQuery("select * from note", null);
            listData = new ArrayList<HashMap<String, Object>>();
            while (c.moveToNext()) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("id", c.getString(0));
                    map.put("title", c.getString(1));
                    map.put("content", c.getString(2));
                    map.put("date", c.getString(3));
                    listData.add(map);
            }
        }
        public boolean delete(SQLiteDatabase mDb, String table, int id) {
            String whereClause = "id=?";
            String[] whereArgs = new String[] { String.valueOf(id) };
            try {
                mDb.delete(table, whereClause, whereArgs);
            } catch (SQLException e) {
                Toast.makeText(getApplicationContext(), "删除数据库失败",
                        Toast.LENGTH_LONG).show();
                return false;
            }
            return true;
        }
    }
}

