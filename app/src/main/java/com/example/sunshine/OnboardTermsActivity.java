package com.example.sunshine;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("SameParameterValue")
public class OnboardTermsActivity extends AppCompatActivity {

    private static final String TAG = "OnboardTermsActivity";

    private String extraPhoneNumber;
    private String extraUserName;

//    private Date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        extraUserName = getIntent().getStringExtra(getString(R.string.extra_full_phone_number));
        extraPhoneNumber = getIntent().getStringExtra(getString(R.string.extra_user_name));

        setContentView(R.layout.activity_onboard_terms);

        TextView subtitle = findViewById(R.id.onboardTermsSubtitle);
        subtitle.setMovementMethod(LinkMovementMethod.getInstance());
        stripUnderlines(subtitle);

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OnboardTermsActivity.this,
                        OnboardNameActivity.class));
                finish();
            }
        });

        Button acceptButton = findViewById(R.id.nextButton);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUserToDatabase();

                OnboardNumberActivity.getInstance().finish(); //trying to finish phone number activity and causing error

                startActivity(new Intent(OnboardTermsActivity.this,
                        MainActivity.class));
                finishOnboardActivities();
            }
        });

//        Button vibeCheck = findViewById(R.id.vibeCheckButton);
//        vibeCheck.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(createPackageContext("Vibe Check", 0)));
//            }
//        });

    }

    //Custom URLSpan class that displays linked without an underline
    private class URLSpanNoUnderline extends URLSpan {
        URLSpanNoUnderline(String url) {
            super(url);
        }
        @Override public void updateDrawState(@NonNull TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }
    }

    private void stripUnderlines(TextView textView) {
        Spannable s = new SpannableString(textView.getText());
        URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);
        for (URLSpan span: spans) {
            int start = s.getSpanStart(span);
            int end = s.getSpanEnd(span);
            s.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL());
            s.setSpan(span, start, end, 0);
        }
        textView.setText(s);
    }

    private void addUserToDatabase (){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> user = new HashMap<>();
        user.put("name", extraUserName);
        user.put("phone", extraPhoneNumber);
        user.put("joinedDate", new Timestamp(Calendar.getInstance().getTimeInMillis()));

// Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void finishOnboardActivities(){
        OnboardNumberActivity.getInstance().finish();
        OnboardAuthActivity.getInstance().finish();
        OnboardNameActivity.getInstance().finish();
        finish();
    }
}
