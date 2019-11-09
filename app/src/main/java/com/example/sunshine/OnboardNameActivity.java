package com.example.sunshine;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.DisplayMetrics;
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

import static android.graphics.Color.TRANSPARENT;

@SuppressWarnings("ALL")
public class OnboardNameActivity extends AppCompatActivity implements CountryCodePicker.DialogEventsListener {

    private static OnboardNameActivity onboardNameActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onboardNameActivity = this;

        String phoneNumber = getIntent().getStringExtra(getString(R.string.extra_full_phone_number));

        setContentView(R.layout.activity_onboard_name);

        setIconView();
        setNameView();

        showSoftKeyboard(findViewById(R.id.onboardNameActivity));

        EditText editText = (EditText) findViewById(R.id.fullName);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Intent intent = new Intent(OnboardNameActivity.this,
                            OnboardTermsActivity.class);
                    intent.putExtras(getIntent());
                    intent.putExtra(getString(R.string.extra_user_name), getUserName());

                    startActivity(intent);
                    handled = true;
                }
                return handled;
            }
        });

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OnboardNameActivity.this,
                        OnboardAuthActivity.class));
                finish();
            }
        });

        ImageButton nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OnboardNameActivity.this,
                        OnboardTermsActivity.class);
                intent.putExtras(getIntent());
                intent.putExtra(getString(R.string.extra_user_name), getUserName());

                startActivity(intent);
            }
        });
    }

    private void setIconView() {
        RelativeLayout container = findViewById(R.id.authNameContainer);
        ImageButton icon = new ImageButton(this);
        icon.setId(R.id.nameIcon);
        icon.setBackgroundColor(Color.TRANSPARENT);

        Drawable nameIcon = getDrawable(R.drawable.ic_baseline_person_30px);
        nameIcon.setColorFilter(getColor(R.color.colorSurfaceDark), PorterDuff.Mode.SRC_IN);

        icon.setImageDrawable(nameIcon);

        setIconProperties(icon, dpToPx(10), 0, dpToPx(10), 0);

        container.addView(icon);
    }

    private void setIconProperties(@NonNull View view, int left, int top, int right, int bottom) {

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_START, R.id.authNameContainer);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        view.setLayoutParams(params);

        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams margin = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            margin.setMargins(left, top, right, bottom);

            view.requestLayout();
        }
    }

    private void setNameView(){
        RelativeLayout activity = findViewById(R.id.authNameContainer);
        EditText fullName = new EditText(this);

        Typeface font = ResourcesCompat.getFont(this, R.font.montserrat);

        int maxLength = 255;
        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(maxLength);

        fullName.setId(R.id.fullName);
        setNameProperties(fullName, dpToPx(10), 0, dpToPx(5), 0);
        setCursorDrawableColor(fullName, getColor(R.color.colorSurfaceText));
        fullName.setTextSize(16);
        fullName.setTextColor(getColor(R.color.colorSurfaceText));
        fullName.setTypeface(font);
        fullName.setBackgroundColor(TRANSPARENT);
        fullName.setHint(R.string.hint_name);
//        fullName.setAutofillHints(View.AUTOFILL_HINT_NAME);
        fullName.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        fullName.setImeOptions(EditorInfo.IME_ACTION_DONE);
        fullName.setImeActionLabel("Next", KeyEvent.KEYCODE_ENTER);
        fullName.setEms(6);
        fullName.setMaxLines(1);
        fullName.setFilters(fArray);
        activity.addView(fullName);
    }

    private void setNameProperties(@NonNull View view, int left, int top, int right, int bottom) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.END_OF, R.id.nameIcon);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        params.addRule(TextView.TEXT_ALIGNMENT_TEXT_START);
        view.setLayoutParams(params);

        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams margin = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            margin.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
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

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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

    @Override
    public void onCcpDialogOpen(Dialog dialog) {
        hideSoftKeyboard(this);
    }

    @Override
    public void onCcpDialogDismiss(DialogInterface dialogInterface) {

    }

    @Override
    public void onCcpDialogCancel(DialogInterface dialogInterface) {

    }

    public String getUserName(){
        EditText nameField = findViewById(R.id.fullName);
        return nameField.getText().toString();
    }

    public static OnboardNameActivity getInstance() {
        return onboardNameActivity;
    }
}
