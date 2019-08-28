package com.holike.crm.popupwindown;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.BitmapDrawable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单详情菜单PopupWindow  物流信息
 */
public class ListMenuPopupWindow extends BasePopupWindow {
    private List<ListMenuBean> beans = new ArrayList<>(6);
    private RecyclerView mListView;
    private ItemSelectListener listener;
    int strId;
    int imgId;

    public ListMenuPopupWindow(Context context, int strId, int imgId, ItemSelectListener listener) {
        super(context);
        this.listener = listener;
        this.strId = strId;
        this.imgId = imgId;
        setBackgroundDrawable(new BitmapDrawable());
        setBackgroundAlpha(0.5f);
        initData();
        initView();
    }


    private void initView() {
        mListView = mContentView.findViewById(R.id.rv_menu_popup);
        mListView.setLayoutManager(new LinearLayoutManager(mContext));
        mListView.setAdapter(new CommonAdapter<ListMenuBean>(mContext, beans) {
            @Override
            protected int bindView(int viewType) {
                return R.layout.item_list_menu;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, ListMenuBean bean, int position) {
                holder.setText(R.id.tv_menu_name, bean.name);
                holder.setImageResource(R.id.iv_menu_icon, bean.id);
                holder.obtainView(R.id.v_menu_line).setVisibility(beans.size() - 1 == position ? View.GONE : View.VISIBLE);
                holder.itemView.setOnClickListener(v -> {
                    dismiss();
                    listener.onSelect(bean.name);
                });
            }

        });


    }

    private void initData() {
        String[] names = mContext.getResources().getStringArray(strId);
        TypedArray ids = mContext.getResources().obtainTypedArray(imgId);

        for (int i = 0; i < names.length; i++) {
            beans.add(new ListMenuBean(names[i], ids.getResourceId(i, 0)));
        }
        ids.recycle();
    }

    @Override
    public void dismiss() {
        setBackgroundAlpha(1f);
        super.dismiss();
    }

    @Override
    int setContentView() {
        return R.layout.popup_list_menu;
    }


    class ListMenuBean {
        String name;
        int id;

        public ListMenuBean(String name, int id) {
            this.name = name;
            this.id = id;
        }
    }

    public interface ItemSelectListener {
        void onSelect(String title);
    }
}
