package vmap.a2016.khkt.myvmap.AppActivities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

import vmap.a2016.khkt.myvmap.DatabaseHelper.Database;
import vmap.a2016.khkt.myvmap.R;
import vmap.a2016.khkt.myvmap.itemsandspecialties.Specialites;
import vmap.a2016.khkt.myvmap.itemsandspecialties.SpecialitesAdapter;

public class specialities_activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.specialities_activity);
        ArrayList<Specialites> speciality = new ArrayList<>();
        Intent intent = getIntent();
        SQLiteDatabase database = Database.initDatabase(specialities_activity.this,"vmapsdb.sqlite");
        ArrayList<Integer> getId = intent.getIntegerArrayListExtra("SPECIALITYID");
        for (int i = 0; i < getId.size();i++){
            Cursor c = database.query("speciality", new String[]{"id","name","introduce"}, "id = ?", new String[]{getId.get(i)+""},null,null,null);
            c.moveToFirst();
            speciality.add(new Specialites(c.getInt(0),c.getString(1),c.getString(2)));
            c.close();
        }

        ListView listView = (ListView) findViewById(R.id.lws);
        listView.setAdapter(new SpecialitesAdapter(this, speciality,specialities_activity.this));
    }
}
