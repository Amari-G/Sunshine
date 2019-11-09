package com.example.sunshine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.hbb20.CountryCodePicker;

import java.lang.reflect.Field;

import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber;

import static android.graphics.Color.TRANSPARENT;


@SuppressWarnings("SameParameterValue")
public class OnboardNumberActivity extends AppCompatActivity implements View.OnClickListener{

    CountryCodePicker ccp;
    TextView countryCodeView;

    SharedPreferences sharedPreferences;
    PhoneNumberUtil phoneUtil;

    private EditText phoneNumber;

    private String countryCodeString;
    private String countryNameCodeString = "US";
    private String currentPhoneNumberString;
    private String currentFullPhoneNumberString;

    @SuppressLint("StaticFieldLeak")
    private static OnboardNumberActivity onboardNumberActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        onboardNumberActivity = this;

        sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        currentPhoneNumberString = sharedPreferences.getString(getString(R.string.sk_phone_number), "");
        currentFullPhoneNumberString = sharedPreferences.getString(getString(R.string.sk_full_phone_number), "");

        setContentView(R.layout.activity_onboard_number);
        setCountryCodeView();
        setDividerView();
        setPhoneNumberView();

        updateHint();

        ccp = findViewById(R.id.countryCodePicker);

        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countryCodeString = "+" + ccp.getSelectedCountryCode();
                countryNameCodeString = ccp.getSelectedCountryNameCode();
                countryCodeView.setText(countryCodeString);
                updateHint();
                Log.d("Country Code", countryCodeString);
            }
        });


        showSoftKeyboard(findViewById(R.id.onboardNumberActivity));

        final EditText editText = findViewById(R.id.phoneNumber);

        //Format Phone Number EditText field
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                editText.removeTextChangedListener(this);

                try {
                    String phoneNumber = s.toString();

                    phoneNumber = phoneNumber.replaceAll("\\(", "");
                    phoneNumber = phoneNumber.replaceAll("\\)", "");
                    phoneNumber = phoneNumber.replaceAll(" ", "");
                    phoneNumber = phoneNumber.replaceAll("-", "");

                    if(phoneNumber.length() > 10){
                        phoneNumber = phoneNumber.substring(0, 10);
                    }

                    if(phoneNumber.length() <= 10) {
                        if (countryNameCodeString == null){
                            countryNameCodeString = "US";
                        }

                        String formattedNumber = PhoneNumberUtils.formatNumber(phoneNumber, countryNameCodeString);

                        if (formattedNumber == null){
                            editText.setText(phoneNumber);
                        } else {
                            editText.setText(formattedNumber);
                        }

                        editText.setSelection(editText.getText().length());

                    } else {
                        editText.setText(phoneNumber);
                        editText.setSelection(editText.getText().length());
                    }

                } catch (NumberFormatException nfe){
                    nfe.printStackTrace();
                }

                editText.addTextChangedListener(this);
            }
        });

        ImageButton backButton = findViewById(R.id.backButton);
        ImageButton nextButton = findViewById(R.id.nextButton);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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

        backButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);

    }

    @Override
    protected void onResume(){
        super.onResume();

        currentPhoneNumberString = sharedPreferences.getString(getString(R.string.sk_phone_number), "");
    }

    private void setDividerView(){
        RelativeLayout container = findViewById(R.id.authNumberContainer);
        View divider = new View(this);
        divider.setId(R.id.phoneDivider);
        setDividerProperties(divider, 0, 0, 0, 0);
        divider.setBackground(getDividerDrawable());
        divider.setBackgroundColor(getColor(R.color.colorSurfaceDark));
        divider.setBackgroundTintMode(PorterDuff.Mode.SRC_OVER);
        container.addView(divider);
    }

    private void setDividerProperties(@NonNull View view, int left, int top, int right, int bottom) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                dpToPx(2),
                ViewGroup.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.END_OF, R.id.countryCodePicker);
        view.setLayoutParams(params);

        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams margin = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            margin.setMargins(left, top, right, bottom);
            view.requestLayout();
        }

    }

    private void setCountryCodeView(){
        RelativeLayout container = findViewById(R.id.authNumberContainer);
        countryCodeView = new TextView(this);

         Typeface font = ResourcesCompat.getFont(this, R.font.montserrat);

        countryCodeView.setId(R.id.countryCode);
        setCountryCodeProperties(countryCodeView, dpToPx(10), 0, dpToPx(5), 0);
        countryCodeView.setText("+1");
        countryCodeView.setTextSize(24);
        countryCodeView.setTextColor(getColor(R.color.colorSurfaceText));
        countryCodeView.setTypeface(font);
        container.addView(countryCodeView);
    }

    private void setCountryCodeProperties(@NonNull View view, int left, int top, int right, int bottom) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.END_OF, R.id.phoneDivider);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        params.addRule(TextView.TEXT_ALIGNMENT_TEXT_START);
        view.setLayoutParams(params);

        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams margin = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            margin.setMargins(left, top, right, bottom);
            view.requestLayout();
        }

    }

    private void setPhoneNumberView(){
        RelativeLayout activity = findViewById(R.id.authNumberContainer);
        phoneNumber = new EditText(this);

        Typeface font = ResourcesCompat.getFont(this, R.font.montserrat);

        Phonenumber.PhoneNumber numberHint = getPhoneUtil().getExampleNumberForType(countryNameCodeString, PhoneNumberUtil.PhoneNumberType.MOBILE);
        String formattedHint = numberHint.getNationalNumber() + "";
        formattedHint = PhoneNumberUtils.formatNumber(formattedHint, countryNameCodeString);

//        int maxLength = 14;
//        InputFilter[] fArray = new InputFilter[1];
//        fArray[0] = new InputFilter.LengthFilter(maxLength);

        phoneNumber.setId(R.id.phoneNumber);
        setPhoneNumberProperties(phoneNumber, 0, 0, dpToPx(10), 0);
        setCursorDrawableColor(phoneNumber, Color.BLACK);
        phoneNumber.setText(currentPhoneNumberString);
        phoneNumber.setTextSize(24);
        phoneNumber.setTextColor(getColor(R.color.colorSurfaceText));
        phoneNumber.setTypeface(font);
        phoneNumber.setBackgroundColor(TRANSPARENT);
        phoneNumber.setHint(formattedHint);
//        phoneNumber.setAutofillHints("phone");
        phoneNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
        phoneNumber.setImeOptions(EditorInfo.IME_ACTION_DONE);
        phoneNumber.setImeActionLabel("Next", KeyEvent.KEYCODE_ENTER);
        phoneNumber.setEms(6);
        phoneNumber.setMaxLines(1);
//        phoneNumber.setFilters(fArray);
        activity.addView(phoneNumber);
    }

    private void setPhoneNumberProperties(@NonNull View view, int left, int top, int right, int bottom) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.END_OF, R.id.countryCode);
        params.addRule(RelativeLayout.ALIGN_END, R.id.authNumberContainer);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        params.addRule(TextView.TEXT_ALIGNMENT_TEXT_START);
        view.setLayoutParams(params);

        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams margin = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            margin.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    private Drawable getDividerDrawable(){
        int[] attrs = { android.R.attr.listDivider };
        TypedArray ta = getApplicationContext().obtainStyledAttributes(attrs);
        Drawable drawable = ta.getDrawable(0);
        ta.recycle();
        return drawable;
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private CharSequence getPhoneNumber(){
        EditText phoneNumber = findViewById(R.id.phoneNumber);

        return phoneNumber.getText();
    }

    private CharSequence getFullPhoneNumber(){
        TextView countryCode = findViewById(R.id.countryCode);
        CharSequence countryCodeChar = countryCode.getText();

        EditText phoneNumber = findViewById(R.id.phoneNumber);
        CharSequence phoneNumberChar = phoneNumber.getText();

        return countryCodeChar + " " + phoneNumberChar;
    }

    private PhoneNumberUtil getPhoneUtil() {
        if (phoneUtil == null) {
            phoneUtil = PhoneNumberUtil.createInstance(this);
        }
        return phoneUtil;
    }

    private void updateHint() {
        String formattedNumber = "";
        Phonenumber.PhoneNumber exampleNumber = getPhoneUtil().getExampleNumberForType(
                countryNameCodeString, PhoneNumberUtil.PhoneNumberType.MOBILE);

        if (exampleNumber != null) {
            formattedNumber = exampleNumber.getNationalNumber() + "";
//                Log.d(TAG, "updateHint: " + formattedNumber);
            formattedNumber = PhoneNumberUtils.formatNumber(formattedNumber, countryNameCodeString);
//            formattedNumber = formattedNumber.substring(getSelectedCountryCodeWithPlus().length()).trim();
//                Log.d(TAG, "updateHint: after format " + formattedNumber + " " + selectionMemoryTag);
//        } else {
//                Log.w(TAG, "updateHint: No example number found for this country (" + getSelectedCountryNameCode() + ") or this type (" + hintExampleNumberType.name() + ").");
        }
        phoneNumber.setHint(formattedNumber);
    }

    @SuppressWarnings("JavaReflectionMemberAccess")
    public static void setCursorDrawableColor(EditText editText, int color) {
        try {
            Field fCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            fCursorDrawableRes.setAccessible(true);
            int mCursorDrawableRes = fCursorDrawableRes.getInt(editText);
            Field fEditor = TextView.class.getDeclaredField("mEditor");
            fEditor.setAccessible(true);
            Object editor = fEditor.get(editText);
            Class<?> clazz = editor.getClass();
            Field fCursorDrawable = clazz.getDeclaredField("mCursorDrawable");
            fCursorDrawable.setAccessible(true);
            Drawable[] drawables = new Drawable[2];
            drawables[0] = editText.getContext().getDrawable(mCursorDrawableRes);
            drawables[1] = editText.getContext().getDrawable(mCursorDrawableRes);

            assert drawables[0] != null;
            assert drawables[1] != null;

            drawables[0].setColorFilter(color, PorterDuff.Mode.SRC_IN);
            drawables[1].setColorFilter(color, PorterDuff.Mode.SRC_IN);
            fCursorDrawable.set(editor, drawables);
        } catch (Throwable ignored) {
        }
    }

    public static OnboardNumberActivity getInstance(){
        return onboardNumberActivity;
    }

    private void advance(){
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        currentPhoneNumberString = getPhoneNumber().toString();
        currentFullPhoneNumberString = getFullPhoneNumber().toString();

        editor.putString(getString(R.string.sk_phone_number), currentPhoneNumberString);
        editor.putString(getString(R.string.sk_full_phone_number), currentFullPhoneNumberString);
        editor.apply();

        Intent intent = new Intent(OnboardNumberActivity.this,
                OnboardAuthActivity.class);
        intent.putExtra(getString(R.string.extra_full_phone_number), currentFullPhoneNumberString);

        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                startActivity(new Intent(OnboardNumberActivity.this,
                        LandingPageActivity.class));
                finish();
                break;
            case R.id.nextButton:
                advance();
                break;
        }
    }

}
