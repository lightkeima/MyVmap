package vmap.a2016.khkt.myvmap.AppActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import vmap.a2016.khkt.myvmap.R;

public class Text_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_layout_activity);
        Intent callerIntent = getIntent();
        Bundle packageFromCaller = callerIntent.getBundleExtra("getINFOMATION");
        setTitle(packageFromCaller.getString("TITLE"));
        String myText = packageFromCaller.getString("INFOMATION");
        TextView textView = (TextView) findViewById(R.id.information_textView);
        textView.setText(Html.fromHtml(myText));
    }
}
