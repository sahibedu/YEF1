package tech.rtsproduction.yef;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RequestFormActivity extends AppCompatActivity {

    Button bloodType, submitBtn;
    EditText dontorName, releation, mobile, reason;
    RadioGroup typeGroup;
    String userLocation;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("RECORDS");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_form);
        bloodType = findViewById(R.id.bloodSelected);
        Bundle parentBundle = getIntent().getExtras();
        final String getBlood = parentBundle.getString("bloodType");
        bloodType.setText(getBlood);

        dontorName = findViewById(R.id.patientNameEdit);
        releation = findViewById(R.id.releationEdit);
        mobile = findViewById(R.id.mobileEdit);
        reason = findViewById(R.id.reasonEdit);
        submitBtn = findViewById(R.id.submitBtn);
        typeGroup = findViewById(R.id.radioGroup);


        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                userLocation = place.getName().toString();
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("PlaceSDK", "An error occurred: " + status);
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!dontorName.getText().toString().isEmpty() && !releation.getText().toString().isEmpty() && !mobile.getText().toString().isEmpty() && !reason.getText().toString().isEmpty()) {
                    if (typeGroup.getCheckedRadioButtonId() != -1) {
                        RadioButton checkedBtn = findViewById(typeGroup.getCheckedRadioButtonId());
                        String checkedType = checkedBtn.getText().toString();
                        pushData(checkedType, getBlood);
                    } else {
                        Toast.makeText(RequestFormActivity.this, "Please Select Blood Type Required", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RequestFormActivity.this, "Please Enter All Fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void pushData(String typeRequired, String bloodGroup) {
        final String date = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(new Date());
        DonatorClass user = new DonatorClass(dontorName.getText().toString(), releation.getText().toString(), mobile.getText().toString(), userLocation, reason.getText().toString(), date, bloodGroup, typeRequired);
        myRef.push().setValue(user);
        finish();

    }
}
