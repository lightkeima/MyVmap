package vmap.a2016.khkt.myvmap.AppActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import vmap.a2016.khkt.myvmap.R;

public class direct_activity extends AppCompatActivity {
    private String _etOrigin;
    private String _etDestination;
    int origin_PLACE_AUTOCOMPLETE_REQUEST_CODE = 2;
    int destination_PLACE_AUTOCOMPLETE_REQUEST_CODE = 3;
    EditText etOrigin;
    EditText etDestination;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.direct_activity);
        intent = new Intent();
        etOrigin = (EditText) findViewById(R.id.editText);
        etDestination = (EditText) findViewById(R.id.editText2);

        etOrigin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                        Intent intent =
                                new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                        .build(direct_activity.this);
                        startActivityForResult(intent, origin_PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                        // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                        // TODO: Handle the error.
                }
            }
        });
        etDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .build(direct_activity.this);
                    startActivityForResult(intent, destination_PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });
        Button btn = (Button)findViewById(R.id.timduongbutton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_etOrigin == null || _etDestination == null ){
                    Toast.makeText(direct_activity.this, "Vui lòng nhập đầy đủ điểm xuất phát và điểm đến!", Toast.LENGTH_SHORT).show();
                } else {
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == origin_PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                _etOrigin = place.getLatLng().latitude + "," + place.getLatLng().longitude + "";
                etOrigin.setText(place.getName().toString());
                intent.putExtra("Origin",_etOrigin);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.       
                // Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
        if (requestCode == destination_PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                _etDestination = place.getLatLng().latitude + "," + place.getLatLng().longitude + "";
                etDestination.setText(place.getName().toString());
                intent.putExtra("Destination",_etDestination);

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.       
                // Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }
}
