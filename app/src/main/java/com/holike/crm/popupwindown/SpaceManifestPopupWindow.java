package com.holike.crm.popupwindown;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.bean.SpaceManifestBean;

import java.util.List;

public class SpaceManifestPopupWindow extends BasePopupWindow {
    private OnSelectListener listener;
    private RecyclerView rv;
    private Context ctx;
    private View viewDrop;

    private int selectPosition = 0;

    public SpaceManifestPopupWindow(Context context, List<SpaceManifestBean> beans, View view, OnSelectListener listener) {
        super(context);
        this.ctx = context;
        this.listener = listener;
        this.viewDrop = view;
        init();
        setData(beans);
    }

    private void init() {
        rv = getContentView().findViewById(R.id.rv_popup_space_manifest_current);
        View outSide = getContentView().findViewById(R.id.view_outside);
        rv.setLayoutManager(new GridLayoutManager(ctx, 3));
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setBackgroundDrawable(new ColorDrawable());
        outSide.setOnClickListener(v -> dismiss());
    }

    public void setData(List<SpaceManifestBean> beans) {
        rv.setAdapter(new CommonAdapter<SpaceManifestBean>(ctx, beans) {

            @Override
            protected int bindView(int viewType) {
                return R.layout.item_current_space;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, SpaceManifestBean bean, int position) {
                TextView tv = holder.obtainView(R.id.iv_item_popup_space_name);
                tv.setText(bean.getHouseName());
                if (selectPosition == position) {
                    tv.setBackgroundResource(R.drawable.bg_corners30dp_textcolor5);
                    tv.setTextColor(ContextCompat.getColor(ctx, R.color.color_while));
                } else {
                    tv.setBackgroundResource(R.drawable.bg_corners30dp_textcolor20);
                    tv.setTextColor(ContextCompat.getColor(ctx, R.color.textColor5));
                }
                holder.itemView.setOnClickListener(view -> {
                    selectPosition = position;
                    listener.onPopupSelect(bean);
                    notifyDataSetChanged();
                    dismiss();
                });
            }
        });

    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        if (Build.VERSION.SDK_INT >= 24) {
            Rect visibleFrame = new Rect();
            anchor.getGlobalVisibleRect(visibleFrame);
            int height = anchor.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
            setHeight(height);
            super.showAsDropDown(anchor, xoff, yoff, gravity);
        } else {
            super.showAsDropDown(anchor, xoff, yoff, gravity);
        }
        if (viewDrop != null) {
            viewDrop.setRotationX(180);
        }
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        if (viewDrop != null) {
            viewDrop.setRotationX(180);
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (viewDrop != null) {
            viewDrop.setRotationX(0);
        }
    }

    @Override
    int setContentView() {
        return R.layout.popup_space_manifest;
    }

    public interface OnSelectListener {
        void onPopupSelect(SpaceManifestBean bean);
    }
}
