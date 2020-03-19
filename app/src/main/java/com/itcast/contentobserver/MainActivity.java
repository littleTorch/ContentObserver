package com.itcast.contentobserver;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 添加
     */
    private Button mBt1;
    /**
     * 更新
     */
    private Button mBt2;
    /**
     * 删除
     */
    private Button mBt3;
    /**
     * 查询
     */
    private Button mBt4;
    private ContentResolver resolver;
    private Uri uri;
    private ContentValues values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        createDB();
    }

    private void createDB() {
        PersonDBOpenHelper helper=new PersonDBOpenHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        for (int i = 0; i < 3; i++) {
            ContentValues values=new ContentValues();
            values.put("name","itcast"+i);
            db.insert("info",null,values);
        }
        db.close();
    }

    private void initView() {
        mBt1 = (Button) findViewById(R.id.bt1);
        mBt1.setOnClickListener(this);
        mBt2 = (Button) findViewById(R.id.bt2);
        mBt2.setOnClickListener(this);
        mBt3 = (Button) findViewById(R.id.bt3);
        mBt3.setOnClickListener(this);
        mBt4 = (Button) findViewById(R.id.bt4);
        mBt4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        resolver=getContentResolver();
        uri=Uri.parse("content://com.itcast.contentobserver/info");
        values=new ContentValues();
        switch (v.getId()) {
            default:
                break;
            case R.id.bt1:
                values.put("name","add_itcast"+new Random().nextInt(10));
                Uri newuri = resolver.insert(uri, values);
                Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
                Log.d("MainActivity", "添加");
                break;
            case R.id.bt2:
                values.put("name","update_itcast");
                int update = resolver.update(uri, values, "name=?", new String[]{"itcast1"});
                Toast.makeText(this, "修改成功"+update+"行", Toast.LENGTH_SHORT).show();
                Log.d("MainActivity", "修改成功");
                break;
            case R.id.bt3:
                resolver.delete(uri,"name=?",new String[]{"itcast0"});
                Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
                Log.d("MainActivity", "删除成功");
                break;
            case R.id.bt4:
                List<Map<String,String>> data=new ArrayList<>();
                Cursor cursor = resolver.query(uri, new String[]{"_id", "name"}, null, null, null);
                while (cursor.moveToNext()){
                    Map<String,String> map=new HashMap<>();
                    map.put("_id",cursor.getString(0));
                    map.put("name",cursor.getString(1));
                    data.add(map);
                }
                cursor.close();
                Toast.makeText(this, "查询成功", Toast.LENGTH_SHORT).show();
                Log.d("MainActivity", "查询成功"+data.toString());
                break;
        }
    }
}
