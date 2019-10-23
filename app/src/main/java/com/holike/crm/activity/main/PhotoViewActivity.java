package com.holike.crm.activity.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;
import com.holike.crm.R;
import com.holike.crm.base.BaseActivity;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.customView.PhotoViewPager;
import com.holike.crm.util.Constants;
import com.holike.crm.util.GlideUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 图片查看器
 */
public class PhotoViewActivity extends BaseActivity {

    @BindView(R.id.view_pager_photo)
    PhotoViewPager mViewPager;
    @BindView(R.id.tv_image_count)
    TextView mTvImageCount;
    private static List<String> mImages;

    @Override
    protected BasePresenter attachPresenter() {
        return null;
    }

    @Override
    protected int setContentViewId() {
        return R.layout.activity_photo_view;
    }

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
//        setStatusBarColor(R.color.color_black);
        Intent intent = getIntent();
        int currentPosition = intent.getIntExtra(Constants.PHOTO_VIEW_POSITION, 0);
        final List<String> images = new ArrayList<>();
        if (mImages != null && !mImages.isEmpty()) {
            images.addAll(mImages);
            release();
        }
        final int imageSize = images.size();
        mViewPager.setAdapter(new ImageViewerAdapter(images));
        mViewPager.setCurrentItem(currentPosition, false);
        setCurrentPosition(currentPosition, imageSize);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                setCurrentPosition(position, imageSize);
            }
        });
        findViewById(R.id.fl_container).setOnClickListener(view -> finish());
    }

    private void setCurrentPosition(int position, int size) {
        String text = (position + 1) + "/" + size;
        mTvImageCount.setText(text);
    }

    /**
     * 查看图片
     */
    public static void openImage(Context context, int currentPosition, @NonNull List<String> images) {
        Intent intent = new Intent(context, PhotoViewActivity.class);
        intent.putExtra(Constants.PHOTO_VIEW_POSITION, currentPosition);
        mImages = new ArrayList<>(images);
        context.startActivity(intent);
    }

    /**
     * 查看图片
     */
    public static void openImage(Activity activity, int currentPosition, @NonNull List<String> images) {
        Intent intent = new Intent(activity, PhotoViewActivity.class);
        intent.putExtra(Constants.PHOTO_VIEW_POSITION, currentPosition);
        mImages = new ArrayList<>(images);
        activity.startActivity(intent);
    }

    public static void openImage(Fragment fragment, int currentPosition, @NonNull List<String> images) {
        if (fragment.getActivity() == null) return;
        Intent intent = new Intent(fragment.getContext(), PhotoViewActivity.class);
        intent.putExtra(Constants.PHOTO_VIEW_POSITION, currentPosition);
        mImages = new ArrayList<>(images);
        fragment.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        release();
        super.onDestroy();
    }

    private void release() {
        if (mImages != null) {
            mImages.clear();
            mImages = null;
        }
    }

    class ImageViewerAdapter extends PagerAdapter {
        private List<String> mImages;

        ImageViewerAdapter(List<String> images) {
            this.mImages = images;
        }

        @Override
        public int getCount() {
            return mImages.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            String url = mImages.get(position);
            PhotoView photoView = new PhotoView(PhotoViewActivity.this);
            GlideUtils.imagePreview(PhotoViewActivity.this,url,photoView);
            container.addView(photoView);
            photoView.setOnClickListener(v -> finish());
            return photoView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }
    }
}
