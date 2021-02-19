
package com.cash.hunterchartdemo;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


/**
 * Base class of all Activities of the Demo Application.
 *
 * @author Philipp Jahoda
 */
public abstract class DemoBase extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    protected Typeface tfRegular;
    protected Typeface tfLight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tfRegular = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        tfLight = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");
    }

    protected float getRandom(float range, float start) {
        return (float) (Math.random() * range) + start;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
    }

}
