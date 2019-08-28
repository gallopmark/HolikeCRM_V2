package com.holike.barcodescanner.base;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.util.List;

/**
 * 相机预览
 * <p>
 * 运行的主线为SurfaceHolder.Callback的三个回调方法
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "CameraPreview";

    private CameraWrapper mCameraWrapper;
    private Handler mAutoFocusHandler;
    private boolean mPreviewing = true;//是否正在预览
    private boolean mShouldAutoFocus = true;//自动对焦的开关
    private boolean mSurfaceCreated = false;//surface是否已创建
    private Camera.PreviewCallback mPreviewCallback;
    private float mAspectTolerance = 0.1f;//允许的实际宽高比和理想宽高比之间的最大差值

    public CameraPreview(Context context, CameraWrapper cameraWrapper, Camera.PreviewCallback previewCallback) {
        super(context);
        init(cameraWrapper, previewCallback);//初始化，主要是注册surface生命周期的回调
    }

    /**
     * 初始化，主要是注册surface生命周期的回调
     */
    public void init(CameraWrapper cameraWrapper, Camera.PreviewCallback previewCallback) {
        setCamera(cameraWrapper, previewCallback);
        getHolder().addCallback(this);//surface生命周期的回调
        getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        mAutoFocusHandler = new Handler();
    }

    public void setCamera(CameraWrapper cameraWrapper, Camera.PreviewCallback previewCallback) {
        mCameraWrapper = cameraWrapper;
        mPreviewCallback = previewCallback;
    }

//--------------------------------------------------------------------------------------------------

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mSurfaceCreated = true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        if (surfaceHolder.getSurface() == null) {
            return;
        }
        stopCameraPreview();
        startCameraPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        mSurfaceCreated = false;
        stopCameraPreview();
    }

//--------------------------------------------------------------------------------------------------

    /**
     * 开始扫码（设置各种相机参数、设置各种回调、并开始预览）
     */
    public void startCameraPreview() {
        if (mCameraWrapper != null) {
            try {
                getHolder().addCallback(this);//surface生命周期的回调
                mPreviewing = true;
                setupCameraParameters();//设置相机预览的尺寸（PreviewSize）
                mCameraWrapper.mCamera.setPreviewDisplay(getHolder());//设置在当前surfaceView中进行相机预览
                mCameraWrapper.mCamera.setDisplayOrientation(getDisplayOrientation());//设置相机预览图像的旋转角度
                mCameraWrapper.mCamera.setOneShotPreviewCallback(mPreviewCallback);//设置一次性的预览回调
                mCameraWrapper.mCamera.startPreview();//开始预览
                //自动对焦
                if (mShouldAutoFocus) {
                    if (mSurfaceCreated) { // check if surface created before using autofocus
                        safeAutoFocus();
                    } else {
                        scheduleAutoFocus(); // wait 1 sec and then do check again
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, e.toString(), e);
            }
        }
    }

    /**
     * 停止扫码(停止相机预览并置空各种回调)
     */
    public void stopCameraPreview() {
        if (mCameraWrapper != null) {
            try {
                mPreviewing = false;
                getHolder().removeCallback(this);
                mCameraWrapper.mCamera.cancelAutoFocus();
                mCameraWrapper.mCamera.setOneShotPreviewCallback(null);
                mCameraWrapper.mCamera.stopPreview();
            } catch (Exception e) {
                Log.e(TAG, e.toString(), e);
            }
        }
    }

    /**
     * 设置自动对焦开关的状态,并立即进行自动对焦/取消自动对焦
     */
    public void setShouldAutoFocus(boolean state) {
        if (mCameraWrapper != null && mPreviewing) {
            if (state == mShouldAutoFocus) {
                return;
            }

            mShouldAutoFocus = state;
            if (mShouldAutoFocus) {
                if (mSurfaceCreated) { // check if surface created before using autofocus
                    safeAutoFocus();
                } else {
                    scheduleAutoFocus(); // wait 1 sec and then do check again
                }
            } else {
                mCameraWrapper.mCamera.cancelAutoFocus();
            }
        }
    }

    /**
     * 尝试自动对焦
     */
    public void safeAutoFocus() {
        try {
            mCameraWrapper.mCamera.autoFocus(autoFocusCB);
        } catch (RuntimeException re) {
            //如果对焦失败，则1s后重试
            scheduleAutoFocus();
        }
    }

    /**
     * 一秒之后尝试自动对焦
     */
    private void scheduleAutoFocus() {
        mAutoFocusHandler.postDelayed(doAutoFocus, 1000);
    }

    /**
     * 尝试自动对焦
     */
    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (mCameraWrapper != null && mPreviewing && mShouldAutoFocus && mSurfaceCreated) {
                safeAutoFocus();
            }
        }
    };

    /**
     * 一秒之后尝试自动对焦
     */
    Camera.AutoFocusCallback autoFocusCB = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            scheduleAutoFocus();
        }
    };

    /**
     * 设置相机预览的尺寸（PreviewSize）
     */
    public void setupCameraParameters() {
        Camera.Size optimalSize = getOptimalPreviewSize();
        Camera.Parameters parameters = mCameraWrapper.mCamera.getParameters();
        parameters.setPreviewSize(optimalSize.width, optimalSize.height);
        mCameraWrapper.mCamera.setParameters(parameters);
    }

    /**
     * 要使相机图像的方向与手机中窗口的方向一致，相机图像需要顺时针旋转的角度
     * <p>
     * 此方法由google官方提供，详见Camera类中setDisplayOrientation的方法说明
     */
    public int getDisplayOrientation() {
        if (mCameraWrapper == null) {
            //If we don't have a camera set there is no orientation so return dummy value
            return 0;
        }

        Camera.CameraInfo info = new Camera.CameraInfo();
        if (mCameraWrapper.mCameraId == -1) {
            Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, info);
        } else {
            Camera.getCameraInfo(mCameraWrapper.mCameraId, info);
        }

        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        int rotation = display.getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        return result;
    }

    /**
     * 找到一个合适的previewSize（根据控件的尺寸）
     */
    private Camera.Size getOptimalPreviewSize() {
        if (mCameraWrapper == null) {
            return null;
        }

        //相机图像默认都是横屏(即宽>高)
        List<Camera.Size> sizes = mCameraWrapper.mCamera.getParameters().getSupportedPreviewSizes();
        if (sizes == null) return null;
        int w, h;
        if (DisplayUtils.getScreenOrientation(getContext()) == Configuration.ORIENTATION_LANDSCAPE) {
            w = getWidth();
            h = getHeight();
        } else {
            w = getHeight();
            h = getWidth();
        }

        double targetRatio = (double) w / h;
        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;
        int targetHeight = h;

        // Try to find an size match aspect ratio and size
        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > mAspectTolerance) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }
}