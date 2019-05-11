package vmap.a2016.khkt.myvmap.AppActivities;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import vmap.a2016.khkt.myvmap.DatabaseHelper.Database;
import vmap.a2016.khkt.myvmap.R;

public class hotel_activity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hotel_activity);
        Intent intent = getIntent();
        SQLiteDatabase database = Database.initDatabase(hotel_activity.this,"vmapsdb.sqlite");
        String placeTitle = intent.getStringExtra("TITLE");
        Cursor c = database.query("Hotel",new String[]{"title", "introduce", "price"}, "title =?",new String[]{placeTitle},null,null,null,null);
        c.moveToFirst();
        TextView intro = (TextView) findViewById(R.id.hotel_introduceTxt);
        intro.setText(c.getString(1));
        TextView price = (TextView) findViewById(R.id.hotelpriceTxt);
        price.setText(c.getString(1));
        c.close();
    }
}
