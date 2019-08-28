package com.holike.barcodescanner.zbar;

import android.content.Context;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

import com.holike.barcodescanner.base.BarcodeScannerView;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;


/**
 * zbar扫码视图，继承自基本扫码视图BarcodeScannerView
 * <p>
 * BarcodeScannerView内含CameraPreview（相机预览）和ViewFinderView（扫码框、阴影遮罩等）
 */
public class ZBarScannerView extends BarcodeScannerView {
    private static final String TAG = "ZBarScannerView";
    private ImageScanner mScanner;
    private List<BarcodeFormat> mFormats;
    private ResultHandler mResultHandler;

    public interface ResultHandler {
        void handleResult(Result rawResult);
    }

    /*
     * 加载zbar动态库
     * zbar.jar中的类会用到
     */
    static {
        System.loadLibrary("iconv");
    }

    public ZBarScannerView(Context context) {
        super(context);
        setupScanner();//创建ImageScanner（zbar扫码器）并进行基本设置（如支持的码格式）
    }

    public ZBarScannerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setupScanner();
    }

    /**
     * 创建ImageScanner并进行基本设置（如支持的码格式）
     */
    public void setupScanner() {
        mScanner = new ImageScanner();

        mScanner.setConfig(0, Config.X_DENSITY, 3);
        mScanner.setConfig(0, Config.Y_DENSITY, 3);

        mScanner.setConfig(Symbol.NONE, Config.ENABLE, 0);

        for (BarcodeFormat format : getFormats()) {//设置支持的码格式
            mScanner.setConfig(format.getId(), Config.ENABLE, 1);
        }
    }

    //此方法与Camera.open()在同一线程执行
    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if (mResultHandler == null) return;
        long startTime = SystemClock.currentThreadTimeMillis();

        try {
            Camera.Parameters parameters = camera.getParameters();
            int previewWidth = parameters.getPreviewSize().width;
            int previewHeight = parameters.getPreviewSize().height;

            //根据ViewFinderView和preview的尺寸之比，缩放扫码区域
            Rect rect = getScaledRect(previewWidth, previewHeight);

            /*
             * 方案一：旋转图像数据
             */
            //int rotationCount = getRotationCount();//相机图像需要被顺时针旋转几次（每次90度）
            //if (rotationCount == 1 || rotationCount == 3) {//相机图像需要顺时针旋转90度或270度
            //    //交换宽高
            //    int tmp = previewWidth;
            //    previewWidth = previewHeight;
            //    previewHeight = tmp;
            //}
            ////旋转数据
            //data = rotateData(data, camera);

            /*
             * 方案二：旋转截取区域
             */
            rect = getRotatedRect(previewWidth, previewHeight, rect);

            //从preView的图像中截取扫码区域
            Image barcode = new Image(previewWidth, previewHeight, "Y800");
            barcode.setData(data);
            barcode.setCrop(rect.left, rect.top, rect.width(), rect.height());

            //使用zbar库识别扫码区域
            int result = mScanner.scanImage(barcode);
            if (result != 0) {//识别成功
                SymbolSet syms = mScanner.getResults();
                final Result rawResult = new Result();
                for (Symbol sym : syms) {
                    // In order to retreive QR codes containing null bytes we need to
                    // use getDataBytes() rather than getData() which uses C strings.
                    // Weirdly ZBar transforms all data to UTF-8, even the data returned
                    // by getDataBytes() so we have to decode it as UTF-8.
                    String symData;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                        symData = new String(sym.getDataBytes(), StandardCharsets.UTF_8);
                    } else {
                        symData = sym.getData();
                    }
                    if (!TextUtils.isEmpty(symData)) {
                        rawResult.setContents(symData);
                        rawResult.setBarcodeFormat(BarcodeFormat.getFormatById(sym.getType()));
                        break;//识别成功一个就跳出循环
                    }
                }

                Log.e("logg", String.format("图像处理及识别耗时: %d ms", SystemClock.currentThreadTimeMillis() - startTime));

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        // Stopping the preview can take a little long.
                        // So we want to set result handler to null to discard subsequent calls to
                        // onPreviewFrame.
                        ResultHandler tmpResultHandler = mResultHandler;
                        mResultHandler = null;

                        stopCameraPreview();//停止扫码(停止相机预览并置空各种回调)
                        if (tmpResultHandler != null) {
                            tmpResultHandler.handleResult(rawResult);
                        }
                    }
                });
            } else {//识别失败
                camera.setOneShotPreviewCallback(this);//再获取一帧数据（会再次触发onPreviewFrame方法）
            }
        } catch (RuntimeException e) {
            Log.e(TAG, e.toString(), e);
        }
    }

//--------------------------------------------------------------------------------------------------

    /**
     * 设置支持的码格式
     */
    public void setFormats(List<BarcodeFormat> formats) {
        mFormats = formats;
        setupScanner();
    }

    public Collection<BarcodeFormat> getFormats() {
        if (mFormats == null) {
            return BarcodeFormat.ALL_FORMATS;
        }
        return mFormats;
    }

    public void setResultHandler(ResultHandler resultHandler) {
        mResultHandler = resultHandler;
    }

    /**
     * 开始扫码（设置各种相机参数、设置各种回调、并开始预览）
     */
    public void startCameraPreview(ResultHandler handler) {
        mResultHandler = handler;
        super.startCameraPreview();
    }
}