package vmap.a2016.khkt.myvmap.AppActivities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import vmap.a2016.khkt.myvmap.DatabaseHelper.Database;
import vmap.a2016.khkt.myvmap.R;
import vmap.a2016.khkt.myvmap.itemsandspecialties.items;
import vmap.a2016.khkt.myvmap.itemsandspecialties.itemsAdapter;

public class restaurant_and_coffee_activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_and_coffee_activity);
        Intent intent = getIntent();
        SQLiteDatabase database = Database.initDatabase(restaurant_and_coffee_activity.this,"vmapsdb.sqlite");
        String placeTitle = intent.getStringExtra("TITLE");
        String placeType = intent.getStringExtra("PLACETYPE");
        int ptype = intent.getIntExtra("TYPE",0);
        List<items> itemList = new ArrayList<>();
        TextView introduceTW = (TextView) findViewById(R.id.IntroduceTxt);
        Cursor c = database.query(placeType,new String[]{"id","title","introduce"},"title = ?",new String[]{placeTitle},null,null,null,null);
        c.moveToFirst();
        introduceTW.setText(c.getString(2));
        int id = c.getInt(0);
        c = database.query("items",null,"id = ? AND placeType = ?",new String[]{id+"",ptype +"" },null,null,null,null);
        for (int i = 0;i < c.getCount();i++){
            c.moveToPosition(i);
            itemList.add(new items(c.getString(2),c.getString(3)));
        }
        c.close();
        ListView listView = (ListView) findViewById(R.id.r_menuList);
        listView.setAdapter(new itemsAdapter(this, itemList));
    }
}
