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
    MyAdapter listItemAdapter;
    private Edit edit;
    private Welcome welcome;
    FragmentManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        dao =new SQLiteDatabaseDao();
        manager=getSupportFragmentManager();
        welcome=(Welcome)manager.findFragmentById(R.id.mainList);
    }
    @Override
    public void onFOneBtnClick(String Title,String Content)
    {
        insert(Title, Content);
        if (welcome == null)
        {

        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.mainList,welcome);
        transaction.commit();
    }
    @Override
    public void addClick(){
        addNote();
    }
    @Override
    public ArrayList<HashMap<String, Object>> getData(){
        return listData;
    }
    @Override
    public void deleteData(int pos,int position){
        delete(pos,position);
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
            listItemAdapter.notifyDataSetChanged();
        }
    }
    public  void addNote(){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        edit=new Edit();
        transaction.replace(R.id.mainList,edit);
        transaction.commit();
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
    class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        public MyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }
        @Override
        public Object getItem(int position) {
            return null;
        }
        @Override
        public int getCount() {
            return listData.size();
        }
        @Override
        public long getItemId(int position) {
            return 0;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
                View myView = mInflater.inflate(R.layout.item, null);
                holder = new ViewHolder();
                dao.getAllData("note");
                holder.title = (TextView) myView.findViewById(R.id.title);
                holder.title.setText(listData.get(position).get("title").toString());
                holder.text = (TextView) myView.findViewById(R.id.content);
                holder.text.setText(listData.get(position).get("content").toString());
                holder.bt = (ImageButton) myView.findViewById(R.id.delete);
                holder.date = (TextView) myView.findViewById(R.id.date);
                holder.date.setText(listData.get(position).get("date").toString());
            final int arg = position;
            holder.bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder info=new AlertDialog.Builder(MainActivity.this);
                    //info.setTitle("  ");
                    info.setMessage("是否删除");
                    info.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(
                                DialogInterface dialoginterface, int i) {
                            //int mListPos = info.position;
                            HashMap<String, Object> map = listData
                                    .get(position);
                            int id = Integer.valueOf((map.get("id")
                                    .toString()));
                            if (dao.delete(mDb, "note", id)) {
                                listData.remove(position);
                                listItemAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                    info.setPositiveButton("取消", null);
                    info.show();
                }
            });
            return myView;
        }
        public final class ViewHolder {
            public TextView title;
            public TextView text;
            public ImageButton bt;
            public TextView date;
        }
    }
}

