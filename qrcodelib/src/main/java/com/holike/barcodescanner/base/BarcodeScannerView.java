package com.holike.barcodescanner.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 基本扫码视图，包含CameraPreview（相机预览）和ViewFinderView（扫码框、阴影遮罩等）
 */
public abstract class BarcodeScannerView extends FrameLayout implements Camera.PreviewCallback {
    private static final String TAG = "BarcodeScannerView";

    private CameraWrapper mCameraWrapper;
    private CameraPreview mPreview;
    private IViewFinder mViewFinderView;
    private Rect scaledRect, rotatedRect;
    private CameraHandlerThread mCameraHandlerThread;
    private Boolean mFlashState;

    public BarcodeScannerView(Context context) {
        super(context);
        init();
    }

    public BarcodeScannerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    private void init() {
        mViewFinderView = new ViewFinderView(getContext());
    }

    /**
     * 打开系统相机，并进行基本的初始化
     */
    public void startCamera() {
        if (mCameraHandlerThread == null) {
            mCameraHandlerThread = new CameraHandlerThread(this);
        }
        mCameraHandlerThread.startCamera(CameraUtils.getDefaultCameraId());
    }

    /**
     * 基本初始化
     */
    public void setupCameraPreview(CameraWrapper cameraWrapper) {
        mCameraWrapper = cameraWrapper;
        if (mCameraWrapper != null) {
            setupLayout(mCameraWrapper);//创建CameraPreview对象，将CameraPreview和ViewFinderView添加到容器中

            if (mFlashState != null) {
                setFlash(mFlashState);//开启/关闭闪光灯
            }
            setShouldAutoFocus(true);//设置自动对焦开关的状态,并立即进行自动对焦/取消自动对焦
        }
    }

    /**
     * 创建CameraPreview对象，将CameraPreview和ViewFinderView添加到容器中
     */
    public final void setupLayout(CameraWrapper cameraWrapper) {
        removeAllViews();

        mPreview = new CameraPreview(getContext(), cameraWrapper, this);
        addView(mPreview);

        if (mViewFinderView instanceof View) {
            addView((View) mViewFinderView);
        } else {
            throw new IllegalArgumentException("IViewFinder object should be instance of android.view.View");
        }
    }

    /**
     * 释放相机资源等各种资源
     */
    public void stopCamera() {
        if (mCameraWrapper != null) {
            mPreview.stopCameraPreview();//停止相机预览并置空各种回调
            mPreview.setCamera(null, null);
            mCameraWrapper.mCamera.release();//释放资源
            mCameraWrapper = null;
        }
        //停止线程
        if (mCameraHandlerThread != null) {
            mCameraHandlerThread.quit();
            mCameraHandlerThread = null;
        }
    }

    /**
     * 开始扫码（设置各种相机参数、设置各种回调、并开始预览）
     */
    public void startCameraPreview() {
        if (mPreview != null) {
            mPreview.startCameraPreview();//开始扫码（设置各种相机参数、设置各种回调、并开始预览）
            mViewFinderView.setAnimatorStart();//开始动画
        }
    }

    /**
     * 停止扫码(停止相机预览并置空各种回调)
     */
    public void stopCameraPreview() {
        if (mPreview != null) {
            mPreview.stopCameraPreview();//停止扫码(停止相机预览并置空各种回调)
            mViewFinderView.setAnimatorEnd();//停止动画
        }
    }

    /**
     * 开启/关闭闪光灯
     */
    public void setFlash(boolean flag) {
        mFlashState = flag;
        if (mCameraWrapper != null && CameraUtils.isFlashSupported(mCameraWrapper.mCamera)) {

            Camera.Parameters parameters = mCameraWrapper.mCamera.getParameters();
            if (flag) {
                if (parameters.getFlashMode().equals(Camera.Parameters.FLASH_MODE_TORCH)) {
                    return;
                }
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            } else {
                if (parameters.getFlashMode().equals(Camera.Parameters.FLASH_MODE_OFF)) {
                    return;
                }
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            }
            mCameraWrapper.mCamera.setParameters(parameters);
        }
    }



    /**
     * 切换闪光灯的点亮状态
     */
    public void toggleFlash() {
        if (mCameraWrapper != null && CameraUtils.isFlashSupported(mCameraWrapper.mCamera)) {
            Camera.Parameters parameters = mCameraWrapper.mCamera.getParameters();
            if (parameters.getFlashMode().equals(Camera.Parameters.FLASH_MODE_TORCH)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            } else {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            }
            mCameraWrapper.mCamera.setParameters(parameters);
        }
    }

    /**
     * 闪光灯是否被点亮
     */
    public boolean isFlashOn() {
        if (mCameraWrapper != null && CameraUtils.isFlashSupported(mCameraWrapper.mCamera)) {
            Camera.Parameters parameters = mCameraWrapper.mCamera.getParameters();
            if (parameters.getFlashMode().equals(Camera.Parameters.FLASH_MODE_TORCH)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * 设置自动对焦开关的状态,并立即进行自动对焦/取消自动对焦
     */
    public void setShouldAutoFocus(boolean state) {
        if (mPreview != null) {
            mPreview.setShouldAutoFocus(state);
        }
    }

    /**
     * 根据ViewFinderView和preview的尺寸之比，缩放扫码区域
     */
    public Rect getScaledRect(int previewWidth, int previewHeight) {
        if (scaledRect == null) {
            Rect framingRect = mViewFinderView.getFramingRect();//获得扫码框区域
            int viewFinderViewWidth = mViewFinderView.getWidth();
            int viewFinderViewHeight = mViewFinderView.getHeight();

            int width, height;
            if (DisplayUtils.getScreenOrientation(getContext()) == Configuration.ORIENTATION_PORTRAIT//竖屏使用
                    && previewHeight < previewWidth) {
                width = previewHeight;
                height = previewWidth;
            } else if (DisplayUtils.getScreenOrientation(getContext()) == Configuration.ORIENTATION_LANDSCAPE//横屏使用
                    && previewHeight > previewWidth) {
                width = previewHeight;
                height = previewWidth;
            } else {
                width = previewWidth;
                height = previewHeight;
            }

            scaledRect = new Rect(framingRect);
            scaledRect.left = scaledRect.left * width / viewFinderViewWidth;
            scaledRect.right = scaledRect.right * width / viewFinderViewWidth;
            scaledRect.top = scaledRect.top * height / viewFinderViewHeight;
            scaledRect.bottom = scaledRect.bottom * height / viewFinderViewHeight;
        }

        return scaledRect;
    }

    public Rect getRotatedRect(int previewWidth, int previewHeight, Rect rect) {
        if (rotatedRect == null) {
            int rotationCount = getRotationCount();
            rotatedRect = new Rect(rect);

            if (rotationCount == 1) {//若相机图像需要顺时针旋转90度，则将扫码框逆时针旋转90度
                rotatedRect.left = rect.top;
                rotatedRect.top = previewHeight - rect.right;
                rotatedRect.right = rect.bottom;
                rotatedRect.bottom = previewHeight - rect.left;
            } else if (rotationCount == 2) {//若相机图像需要顺时针旋转180度,则将扫码框逆时针旋转180度
                rotatedRect.left = previewWidth - rect.right;
                rotatedRect.top = previewHeight - rect.bottom;
                rotatedRect.right = previewWidth - rect.left;
                rotatedRect.bottom = previewHeight - rect.top;
            } else if (rotationCount == 3) {//若相机图像需要顺时针旋转270度，则将扫码框逆时针旋转270度
                rotatedRect.left = previewWidth - rect.bottom;
                rotatedRect.top = rect.left;
                rotatedRect.right = previewWidth - rect.top;
                rotatedRect.bottom = rect.right;
            }
        }

        return rotatedRect;
    }

    /**
     * 旋转data
     */
    public byte[] rotateData(byte[] data, Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
        int width = parameters.getPreviewSize().width;
        int height = parameters.getPreviewSize().height;

        int rotationCount = getRotationCount();
        for (int i = 0; i < rotationCount; i++) {
            byte[] rotatedData = new byte[data.length];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++)
                    rotatedData[x * height + height - y - 1] = data[x + y * width];
            }
            data = rotatedData;
            int tmp = width;
            width = height;
            height = tmp;
        }

        return data;
    }

    /**
     * 获取（旋转角度/90）
     */
    public int getRotationCount() {
        int displayOrientation = mPreview.getDisplayOrientation();
        return displayOrientation / 90;
    }
}