//package com.holike.barcodescanner.sample;
//
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.holike.qrcodelib.R;
//import com.holike.barcodescanner.zbar.Result;
//import com.holike.barcodescanner.zbar.ZBarScannerView;
//
//
///**
// * 最简单的使用示例
// */
//public class SimpleScannerActivity extends AppCompatActivity implements ZBarScannerView.ResultHandler {
//    private ZBarScannerView mScannerView;
//
//    @Override
//    public void onCreate(Bundle state) {
//        super.onCreate(state);
//        setContentView(R.layout.activity_simple_scanner);
//
//        mScannerView = (ZBarScannerView) findViewById(R.id.zbar);
////        ViewGroup container = (ViewGroup) findViewById(R.id.container);
////        container.addView(mScannerView);
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//
//        mScannerView.setResultHandler(this);
//        mScannerView.startCamera();//打开系统相机，并进行基本的初始化
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//
//        mScannerView.stopCamera();//释放相机资源等各种资源
//    }
//
//    @Override
//    public void handleResult(Result rawResult) {
//        Toast.makeText(this, "Contents = " + rawResult.getContents() + ", Format = " + rawResult.getBarcodeFormat().getName(), Toast.LENGTH_SHORT).show();
//        Log.e("logg", "Contents = " + rawResult.getContents() + ", Format = " + rawResult.getBarcodeFormat().getName());
//        // 2秒后重新开始扫码
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mScannerView.startCameraPreview(SimpleScannerActivity.this);//开始扫码（设置各种相机参数、设置各种回调、并开始预览）
//            }
//        }, 2000);
//    }
//}