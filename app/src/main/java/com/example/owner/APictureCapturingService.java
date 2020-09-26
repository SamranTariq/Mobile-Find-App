package com.example.owner;

import android.app.Activity;
import android.content.Context;
import android.hardware.camera2.CameraManager;
import android.os.AsyncTask;
import android.os.Build;
import android.util.SparseIntArray;
import android.view.Surface;


/**
 * Abstract Picture Taking Service.
 *
 * @author hzitoun (zitoun.hamed@gmail.com)
 */
public abstract class APictureCapturingService {

    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    private final Activity activity;
    final Context context;
    CameraManager manager;

    /***
     * @param activity the activity used to get display manager and the application context
     */
    APictureCapturingService(final Activity activity) {
        this.activity = activity;
        this.context = activity.getApplicationContext();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        }
    }

    /***
     * @return  orientation
     */
    int getOrientation() {
        final int rotation = this.activity.getWindowManager().getDefaultDisplay().getRotation();
        return ORIENTATIONS.get(rotation);
    }


    /**
     * starts pictures capturing process.
     *
     * @param listener picture capturing listener
     */
    public abstract void startCapturing(final PictureCapturingListener listener);
}
