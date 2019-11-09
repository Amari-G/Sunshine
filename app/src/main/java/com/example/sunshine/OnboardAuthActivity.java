package com.example.sunshine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static android.text.InputType.TYPE_CLASS_NUMBER;

public class OnboardAuthActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "OnboardAuthActivity";

    @SuppressLint("StaticFieldLeak")
    private static OnboardAuthActivity onboardAuthActivity;

    private TextView verificationField;

    private EditText et1;
    private EditText et2;
    private EditText et3;
    private EditText et4;
    private EditText et5;
    private EditText et6;

    private FirebaseAuth auth;

    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_onboard_auth);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        auth = FirebaseAuth.getInstance();

        setSubtitleView();
        verificationField = findViewById(R.id.onboardAuthSubtitle);

        ImageButton backButton = findViewById(R.id.backButton);
        ImageButton nextButton = findViewById(R.id.nextButton);

        et1 = findViewById(R.id.phoneAuthField1);
        et2 = findViewById(R.id.phoneAuthField2);
        et3 = findViewById(R.id.phoneAuthField3);
        et4 = findViewById(R.id.phoneAuthField4);
        et5 = findViewById(R.id.phoneAuthField5);
        et6 = findViewById(R.id.phoneAuthField6);


        et1.setInputType(TYPE_CLASS_NUMBER);
        et2.setInputType(TYPE_CLASS_NUMBER);
        et3.setInputType(TYPE_CLASS_NUMBER);
        et4.setInputType(TYPE_CLASS_NUMBER);
        et5.setInputType(TYPE_CLASS_NUMBER);
        et6.setInputType(TYPE_CLASS_NUMBER);


        showSoftKeyboard(findViewById(R.id.onboardAuthActivity));

        et1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    advance();
                    handled = true;
                }
                return handled;
            }
        });
        et2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    advance();
                    handled = true;
                }
                return handled;
            }
        });
        et3.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    advance();
                    handled = true;
                }
                return handled;
            }
        });
        et4.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    advance();
                    handled = true;
                }
                return handled;
            }
        });
        et5.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    advance();
                    handled = true;
                }
                return handled;
            }
        });
        et6.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    advance();
                    handled = true;
                }
                return handled;
            }
        });

        et1.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                if(et1.getText().toString().length() == 1)
                {
                    et2.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            public void afterTextChanged(Editable s) {
            }

        });
        et2.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                if(et2.getText().toString().length() == 1)
                {
                    et3.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }

        });
        et3.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                if(et3.getText().toString().length() == 1)
                {
                    et4.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            public void afterTextChanged(Editable s) {
            }

        });
        et4.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                if(et4.getText().toString().length() == 1)
                {
                    et5.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            public void afterTextChanged(Editable s) {
            }

        });
        et5.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                if(et5.getText().toString().length() == 1)
                {
                    et6.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            public void afterTextChanged(Editable s) {
            }

        });

        et2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_DEL)) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        if (et2.getText().toString().equals("")) {
                            et1.setText("");
                        } else {
                            et2.setText("");
                        }
                    }else if (event.getAction() == KeyEvent.ACTION_UP){
                        et1.requestFocus();
                    }
                    return true;
                } else
                    return false;
            }
        });

        et3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_DEL)) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        if (et3.getText().toString().equals("")) {
                            et2.setText("");
                        } else {
                            et3.setText("");
                        }
                    }else if (event.getAction() == KeyEvent.ACTION_UP){
                        et2.requestFocus();
                    }
                    return true;
                } else
                    return false;
            }
        });

        et4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_DEL)) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        if (et4.getText().toString().equals("")) {
                            et3.setText("");
                        } else {
                            et4.setText("");
                        }
                    }else if (event.getAction() == KeyEvent.ACTION_UP){
                        et3.requestFocus();
                    }
                    return true;
                } else
                    return false;
            }
        });

        et5.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_DEL)) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        if (et5.getText().toString().equals("")) {
                            et4.setText("");
                        } else {
                            et5.setText("");
                        }
                    }else if (event.getAction() == KeyEvent.ACTION_UP){
                        et4.requestFocus();
                    }
                    return true;
                } else
                    return false;
            }
        });

        et6.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_DEL)) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        if (et6.getText().toString().equals("")) {
                            et5.setText("");
                        } else {
                            et6.setText("");
                        }
                    }else if (event.getAction() == KeyEvent.ACTION_UP){
                        et5.requestFocus();
                    }
                    return true;
                } else
                    return false;
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);
                signInWithPhoneAuthCredential(credential);
            }


            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                }

                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                // ...
            }
        };

        backButton.setOnClickListener(this);

        nextButton.setOnClickListener(this);
    }

    protected void onStart() {
        super.onStart();

        onboardAuthActivity = this;

        phoneNumber = getIntent().getStringExtra(getString(
                R.string.extra_full_phone_number)).
                replaceAll("\\s+","");
        startPhoneNumberVerification(phoneNumber);

        Button resendButton = findViewById(R.id.resendButton);
        resendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "Resend button click registered");
                resendVerificationCode(phoneNumber, mResendToken);
            }
        });

    }

    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }

    // [START resend_verification]
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }
    // [END resend_verification]

    // [START sign_in_with_phone]
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            //Send phone number to the next activity
                            Intent intent = new Intent(OnboardAuthActivity.this,
                                    OnboardNameActivity.class);
                            intent.putExtras(getIntent());
                            startActivity(intent);

                            FirebaseUser user = Objects.requireNonNull(task.getResult()).getUser();
                            Log.v(TAG, "FirebaseUser Uid: " + user.getUid());
                            Log.v(TAG, "FirebaseUser Name: " + user.getDisplayName());
                            Log.v(TAG, "FirebaseUser Email: " + user.getEmail());
                            Log.v(TAG, "FirebaseUser Phone: " + user.getPhoneNumber());

                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                // [START_EXCLUDE silent]
                                verificationField.setError("Invalid code");
                                // [END_EXCLUDE]
                            }
                            // [START_EXCLUDE silent]
                            // Update UI
//                            updateUI(STATE_SIGNIN_FAILED);
                            // [END_EXCLUDE]
                        }
                    }
                });
    }

    private void setSubtitleView(){
        LinearLayout container = findViewById(R.id.onboardTitleBox);
        TextView subtitle = new TextView(this);

        String subtitleText = getString(R.string.onboard_auth_subtitle) + " " +
                getIntent().getStringExtra(getString(R.string.extra_full_phone_number));

        Typeface font = ResourcesCompat.getFont(this, R.font.montserrat);

        subtitle.setId(R.id.onboardAuthSubtitle);
        setSubtitleProperties(subtitle);
        subtitle.setText(subtitleText);
        subtitle.setTextSize(16);
        subtitle.setTextColor(getColor(R.color.colorSurfaceText));
        subtitle.setTypeface(font);
        container.addView(subtitle);
    }

    private void setSubtitleProperties(@NonNull View view) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.onboardTitleBox);
        params.addRule(RelativeLayout.ALIGN_PARENT_START, R.id.onboardTitleBox);
        params.addRule(TextView.TEXT_ALIGNMENT_TEXT_START);
        view.setLayoutParams(params);
    }

    private void advance(){
        String code = getCode();

        if (code == null || code.length() < 6){
            Log.v(TAG, "User has not provided a 6 digit verification code");
            nullCodePrompt();
        } else{
            verifyPhoneNumberWithCode(mVerificationId, code);
        }

    }

    private void nullCodePrompt() {
        Toast toast = Toast.makeText(this, R.string.onboard_auth_null_code, Toast.LENGTH_LONG);


        toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0,0);
        toast.show();
    }

    private void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public String getCode(){
        String code = et1.getText().toString();

        code = code.concat(et2.getText().toString());
        code = code.concat(et3.getText().toString());
        code = code.concat(et4.getText().toString());
        code = code.concat(et5.getText().toString());
        code = code.concat(et6.getText().toString());


        return code;
    }

    public static OnboardAuthActivity getInstance(){
        return onboardAuthActivity;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                Log.v(TAG, "\"Back\" button click registered");
                startActivity(new Intent(OnboardAuthActivity.this,
                        OnboardNumberActivity.class));
                finish();
                break;
            case R.id.nextButton:
                advance();
                break;
        }
    }
}
