package vmap.a2016.khkt.myvmap.itemsandspecialties;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import vmap.a2016.khkt.myvmap.AppActivities.specialityIntroduce_activity;
import vmap.a2016.khkt.myvmap.DatabaseHelper.Database;
import vmap.a2016.khkt.myvmap.R;


public class SpecialitesAdapter extends BaseAdapter {
    private List<Specialites> listData;
    private LayoutInflater layoutInflater;
    private Context context;
    private Activity activity;
    public  SpecialitesAdapter(Context aContext, List<Specialites> listData,Activity activity){
        this.context = aContext;
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
        this.activity = activity;
    }

    @Override
    public int getCount(){
        return  listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        SpecialityViewHolder holder;
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.speciality,null);
            holder = new SpecialityViewHolder();
            holder.itemNameView = (Button) convertView.findViewById(R.id.specialityName);
            convertView.setTag(holder);
        } else {
            holder = (SpecialityViewHolder) convertView.getTag();
        }
        final Specialites specialites = this.listData.get(position);
        holder.itemNameView.setText(specialites.specialityName);
        holder.itemNameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase database = Database.initDatabase(activity,"vmapsdb.sqlite");
                Cursor c = database.query("Img_specialities",new String[]{"idspeciality", "linkImg"},"idspeciality = ?", new String[]{specialites.id + ""},null,null,null,null);
                ArrayList<String> temp = new ArrayList<>();
                Intent intent = new Intent(activity, specialityIntroduce_activity.class);
                Bundle bundle = new Bundle();
                temp.clear();
                for (int i = 0; i < c.getCount(); i++){
                    c.moveToPosition(i);
                    temp.add(c.getString(1));
                }
                c.close();
                bundle.putStringArrayList("img", temp);
                bundle.putString("introText", specialites.introduce);
                bundle.putString("title",specialites.specialityName);
                intent.putExtra("getIMG", bundle);
                activity.startActivity(intent);
            }
        });
        return convertView;
    }
    private static class SpecialityViewHolder {
        Button itemNameView;
    }
}
