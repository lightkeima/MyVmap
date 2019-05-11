package vmap.a2016.khkt.myvmap.DatabaseHelper;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import vmap.a2016.khkt.myvmap.AppActivities.Text_Activity;
import vmap.a2016.khkt.myvmap.AppActivities.img_activity;
import vmap.a2016.khkt.myvmap.AppActivities.specialities_activity;
import vmap.a2016.khkt.myvmap.AppActivities.video_activity;
import vmap.a2016.khkt.myvmap.Modules.DirectionFinder;
import vmap.a2016.khkt.myvmap.Modules.DirectionFinderListener;


public class DatabaseHelper {
    private static final String DATABASENAME = "vmapsdb.sqlite";
    public static ArrayList<Marker> GetMarker(GoogleMap mMap, Activity activity, String iconPath){
        ArrayList<Marker> MarkerList = new ArrayList<>();
        String temp;
        if (iconPath.equals("Icons/Mylocation_Icon-2.png")){
            temp = "Icons/marker-2.png" ;
        } else {temp = "Icons/marker-1.png";}
    SQLiteDatabase database = Database.initDatabase(activity, DATABASENAME);
        Cursor c = database.query("Location",new String[]{"id", "title", "x", "y", "snippet", "Zoom_lvl"}
                ,null,null,null,null,null);
        for (int i = 0; i < c.getCount(); i++) {
            c.moveToPosition(i);
             String Location_title = c.getString(1);
            double x = c.getDouble(2);
            double y = c.getDouble(3);
            String Location_snippet = c.getString(4);
            int Zoom_lvl = c.getInt(5);
            MarkerList.add(mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(x, y))
                    .title(Location_title).snippet(Location_snippet)
                    .icon(BitmapDescriptorFactory.fromAsset(temp))));
                    MarkerList.get(i).setTag(1);
            switch (Zoom_lvl) {
                case 1: {
                    break;
                }
                case 2: {
                    MarkerList.get(i).setVisible(false);
                    break;
                }
                case 3: {
                    MarkerList.get(i).setVisible(false);
                    break;
                }
            }
        }
        c.close();
        return MarkerList;
    }
    public static void onLocationButtonClick(Activity activity, String thisMarker){
        SQLiteDatabase database = Database.initDatabase(activity, DATABASENAME);
        Cursor c = database.query("Location",new String[]{"title", "L_info"},"title = ?"
                ,new String[]{thisMarker},null,null,null);
        c.moveToFirst();
        Intent intent = new Intent(activity, Text_Activity.class);
        Bundle bundle = new Bundle();
        String temp = c.getString(1);
        bundle.putString("INFOMATION", temp);
        bundle.putString("TITLE", "Thông tin địa điểm");
        intent.putExtra("getINFOMATION", bundle);
        c.close();
        activity.startActivity(intent);
    }
    public static void onHistoryButtonClick(Activity activity, String thisMarker){
        SQLiteDatabase database = Database.initDatabase(activity, DATABASENAME);
        Cursor c = database.query("Location",new String[]{"title", "H_info"},"title = ?"
                ,new String[]{thisMarker},null,null,null);
        c.moveToFirst();
        Intent intent = new Intent(activity, Text_Activity.class);
        Bundle bundle = new Bundle();
        String temp = c.getString(1);
        bundle.putString("INFOMATION", temp);
        bundle.putString("TITLE", "Thông tin lịch sử");
        intent.putExtra("getINFOMATION", bundle);
        c.close();
        activity.startActivity(intent);
    }
    public static  void onImageButtonClick(Activity activity, String thisMarker){
        SQLiteDatabase database = Database.initDatabase(activity, DATABASENAME);
        Cursor c = database.query("Location",new String[]{"id","title"},"title = ?"
                        ,new String[]{thisMarker},null,null,null,null);
        c.moveToFirst();
        int id = c.getInt(0);
        c.close();
        c = database.query("Img",null,"id_location = ?",new String[]{String.valueOf(id)},null,null,null);
        Intent intent = new Intent(activity, img_activity.class);
        Bundle bundle = new Bundle();
        ArrayList<String> tempList = new ArrayList<>();
        tempList.clear();
        String temp;
        for(int i = 0; i < c.getCount();i++){
            c.moveToPosition(i);
            temp = c.getString(2);
            tempList.add(temp);
        }
        c.close();
        bundle.putStringArrayList("img", tempList);
        intent.putExtra("getIMG", bundle);
        activity.startActivity(intent);
    }
    public static  void onDirectButtonClick(Activity activity,String thisMarker,LatLng myLocation,Marker ImHere,GoogleMap mMap){
        if (myLocation != null) {
            SQLiteDatabase database = Database.initDatabase(activity, DATABASENAME);
            Cursor c = database.query("Location",new String[]{"title","x","y"},"title = ?"
                    ,new String[]{thisMarker},null,null,null);
            c.moveToFirst();
            String origin = ImHere.getPosition().latitude + "," + ImHere.getPosition().longitude;
            String destination = c.getDouble(1) + "," + c.getDouble(2);
            c.close();
            mMap.getUiSettings().setScrollGesturesEnabled(true);
            mMap.getUiSettings().setZoomGesturesEnabled(true);
            try {
                new DirectionFinder((DirectionFinderListener) activity, origin, destination).execute();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }
    public static void onVideoButtonClick(Activity activity, String thisMarker){
        SQLiteDatabase database = Database.initDatabase(activity, DATABASENAME);
        Cursor c = database.query("Location",new String[]{"title", "video_url"},"title = ?"
                ,new String[]{thisMarker}, null,null,null);
        c.moveToFirst();
        Intent intent = new Intent(activity, video_activity.class);
        Bundle bundle = new Bundle();
        String temp = c.getString(1);
        bundle.putString("VIDEO", "https://www.youtube.com/embed/" + temp);
        intent.putExtra("getVIDEO", bundle);
        c.close();
        activity.startActivity(intent);
    }
    public static void onSpecialityButtonClick(Activity activity, String thisMarker){
        SQLiteDatabase database = Database.initDatabase(activity,DATABASENAME);
        ArrayList<Integer> getId = new ArrayList<>();
        Cursor c = database.query("Location", new String[]{"id", "title"},"title = ?", new String[]{thisMarker},null,null,null);
        c.moveToFirst(); int id = c.getInt(0);
        c = database.query("l_specialities", new String[]{"id_speciality","id_location"},"id_location = ?", new String[]{String.valueOf(id)}, null,null,null);
        for(int i = 0; i < c.getCount(); i++){
            c.moveToPosition(i);
            getId.add(c.getInt(0));
        }
        c.close();
        Intent intent = new Intent(activity, specialities_activity.class);
        intent.putIntegerArrayListExtra("SPECIALITYID",getId);
        activity.startActivity(intent);

    }
    public static int setVisibleMarker(Activity activity,float currentZoom, int myLevel, ArrayList<Marker> MarkerList) {
        SQLiteDatabase database = Database.initDatabase(activity, DATABASENAME);
        if (currentZoom <= 5 && myLevel != 1) {
            Cursor c = database.query("Location", new String[]{"id", "Zoom_lvl"}, "Zoom_lvl = ?"
                    , new String[]{"2"}, null, null, null);

            for (int i = 0; i < c.getCount(); i++) {
                c.moveToPosition(i);
                int temp = c.getInt(0) - 1;
                MarkerList.get(temp).setVisible(false);
            }
            c.close();
            return 1;

        }
        if (currentZoom > 5 && currentZoom < 15 && myLevel != 2) {
            Cursor c = database.query("Location", new String[]{"id", "Zoom_lvl"}, "Zoom_lvl = ?"
                    , new String[]{"3"}, null, null, null, null);
            for (int i = 0; i < c.getCount(); i++) {
                c.moveToPosition(i);
                int temp = c.getInt(0) - 1;
                MarkerList.get(temp).setVisible(false);
            }
            c.close();
            c = database.query("Location", new String[]{"id", "Zoom_lvl"}, "Zoom_lvl = ?"
                    , new String[]{"2"}, null, null, null, null);
            for (int i = 0; i < c.getCount(); i++) {
                c.moveToPosition(i);
                int temp = c.getInt(0) - 1;
                MarkerList.get(temp).setVisible(true);
            }
            c.close();
            return 2;
        }
        if (currentZoom >= 15 && myLevel != 3) {
            Cursor c = database.query("Location",new String[]{"id", "Zoom_lvl"},"Zoom_lvl = ?"
                    ,new String[]{"3"},null,null,null,null);
            for(int i = 0; i < c.getCount(); i++){
                c.moveToPosition(i);
                int temp = c.getInt(0) - 1;
                MarkerList.get(temp).setVisible(true);
            }
            c.close();
            return 3;
        } else  return myLevel;
    }
    public static ArrayList<Marker> loadPlaces(Activity activity,final String placeType, ArrayList<Polyline> mpolyline
            , GoogleMap mMap, String icon, int pType){
        ArrayList<Marker> places;
        places = new ArrayList<>();
        double x1,x2,y1,y2;
        List<LatLng> points = new ArrayList<>();
        SQLiteDatabase database = Database.initDatabase(activity, DATABASENAME);
        for(Polyline p:mpolyline){
            for (int i = 0;i < p.getPoints().size();i++){
                 points.add(p.getPoints().get(i));
            }
        }
        for(int i = 1; i <  points.size(); i++){
            if(points.get(i - 1).latitude < points.get(i).latitude){
                x1 = points.get(i - 1).latitude - 0.0003;
                x2 = points.get(i).latitude + 0.0003;
            } else {
                x2 = points.get(i -1 ).latitude + 0.0003;
                x1 = points.get(i).latitude - 0.0003;
            }
            if(points.get(i - 1).latitude < points.get(i).latitude){
                y1 = points.get(i - 1).longitude - 0.0003;
                y2 = points.get(i).longitude +  0.0003;
            } else{
                y1 = points.get(i).longitude - 0.0003;
                y2 = points.get(i - 1 ).longitude + 0.0003;
            }
           Cursor c = database.query(placeType,new String[]{"title", "x", "y", "snippet"} ,"x >= ? AND x <= ? AND y >= ? AND y <= ?",
                    new String[]{ String.valueOf(x1),String.valueOf(x2), String.valueOf(y1),String.valueOf(y2)},null,null,null,null);
            for(int j = 0; j < c.getCount();j++){
                c.moveToPosition(j);
                String title = c.getString(0);
                double x = c.getDouble(1);
                double y = c.getDouble(2);
                String snippet = c.getString(3);
                places.add(mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(x,y))
                        .title(title)
                        .snippet(snippet)
                        .icon(BitmapDescriptorFactory.fromAsset(icon))
                ));
            }
            c.close();
        }
        for(Marker m:places){m.setTag(pType);}
        return places;
    }

}
