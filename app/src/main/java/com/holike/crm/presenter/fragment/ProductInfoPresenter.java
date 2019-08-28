package com.holike.crm.presenter.fragment;

import android.content.Context;
import android.os.Handler;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gallopmark.recycler.adapterhelper.CommonAdapter;
import com.holike.crm.R;
import com.holike.crm.base.BasePresenter;
import com.holike.crm.bean.ProductInfoBean;
import com.holike.crm.customView.WrapContentLinearLayoutManager;
import com.holike.crm.customView.listener.TouchPressListener;
import com.holike.crm.model.fragment.ProductInfoModel;
import com.holike.crm.util.DensityUtil;
import com.holike.crm.util.LogCat;
import com.holike.crm.view.fragment.ProductInfoView;

import java.util.ArrayList;
import java.util.List;

/**
 * 产品信息
 */
public class ProductInfoPresenter extends BasePresenter<ProductInfoView, ProductInfoModel> {

    private int selectPosition = 0;

    public void getData(String orderId) {
        model.getData(orderId, new ProductInfoModel.ProductInfoListener() {
            @Override
            public void success(List<ProductInfoBean> beans) {
                if (getView() != null)
                    getView().onSuccess(beans);
            }

            @Override
            public void fail(String errorMsg) {
                if (getView() != null)
                    getView().onFail(errorMsg);
            }
        });
    }

    /*
    1代表衣柜的柜体、背板、装饰件 2代表衣柜的移门 3代表衣柜的吸塑 4代表衣柜的五金
    5代表木门的门套板、平口门和线条 6代表木门的五金 7代表橱柜的柜体、装饰件、台面
    8代表橱柜的吸塑 9代表橱柜的五金
     */

    public List<ProductInfoTitleBean> getType(ProductInfoBean data) {
        String[] titles = new String[]{};
        int[] sizes = new int[]{};
        List<ProductInfoTitleBean> beans = new ArrayList<>(6);
        switch (data.type) {
            case "1"://1代表衣柜的柜体、背板、装饰件.
                titles = new String[]{"花色", "材质", "纹路", "长度", "宽度", "厚度", "数量", "开料长", "开料宽", "封边信息"};
                sizes = new int[]{90, 64, 40, 48, 48, 48, 48, 56, 56, 90};
                break;
            case "2"://2代表衣柜的移门.
                titles = new String[]{"花色", "材质", "纹路", "长度", "宽度", "厚度", "数量", "柜分组名", "备注"};
                sizes = new int[]{90, 64, 40, 48, 48, 48, 48, 90, 90};
                break;
            case "3"://3代表衣柜的吸塑.
                titles = new String[]{"花色", "材质", "纹路", "贴面工艺", "长度", "宽度", "厚度", "数量", "柜分组名", "备注"};
                sizes = new int[]{90, 64, 40, 90, 48, 48, 48, 48, 90, 90};
                break;
            case "4":// 4代表衣柜的五金.
                titles = new String[]{"五金规格", "长度", "宽度", "厚度", "数量", "备注"};
                sizes = new int[]{90, 48, 48, 48, 48, 90};
                break;
            case "5"://5代表木门的门套板、平口门和线条.
                titles = new String[]{"长度", "宽度", "厚度", "花色", "材质", "纹路", "数量", "柜分组名", "备注"};
                sizes = new int[]{48, 48, 48, 90, 64, 40, 48, 90, 90};
                break;
            case "6"://6代表木门的五金.
                titles = new String[]{"五金规格", "长度", "宽度", "厚度", "数量", "柜分组名", "备注"};
                sizes = new int[]{90, 48, 48, 48, 48, 90, 90};
                break;
            case "7"://7代表橱柜的柜体、装饰件、台面
                titles = new String[]{"花色", "材质", "纹路", "长度", "宽度", "厚度", "数量", "备注"};
                sizes = new int[]{90, 64, 40, 48, 48, 48, 48, 90};
                break;
            case "8"://8代表橱柜的吸塑
                titles = new String[]{"花色", "贴面工艺", "纹路", "长度", "宽度", "厚度", "数量", "备注", "柜分组名"};
                sizes = new int[]{90, 64, 40, 48, 48, 48, 48, 90};
                break;
            case "9"://9代表橱柜的五金
                titles = new String[]{"五金规格", "长度", "宽度", "厚度", "数量", "备注"};
                sizes = new int[]{90, 48, 48, 48, 48, 90};
                break;
        }
        return addData(beans, titles, sizes);


    }


    private List<ProductInfoTitleBean> addData(List<ProductInfoTitleBean> beans, String[] titles, int[] sizes) {
        for (int i = 0; i < titles.length; i++) {
            beans.add(new ProductInfoTitleBean(titles[i], sizes[i]));
        }

        return beans;
    }

    class ProductInfoTitleBean {
        String name;
        int layoutWith;

        public ProductInfoTitleBean(String name, int layoutWith) {
            this.name = name;
            this.layoutWith = layoutWith;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getLayoutWith() {
            return layoutWith;
        }

        public void setLayoutWith(int layoutWith) {
            this.layoutWith = layoutWith;
        }
    }


    public void setTabAdapter(final Context ctx, final RecyclerView rv, final List<ProductInfoBean> datas) {

        rv.setLayoutManager(new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false));
        rv.setAdapter(new CommonAdapter<ProductInfoBean>(ctx, datas) {

            @Override
            protected int bindView(int viewType) {
                return R.layout.item_receipt_delivery_title_bar;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, ProductInfoBean bean, int position) {
                holder.itemView.setSelected(selectPosition == position);
                TextView tv = holder.obtainView(R.id.item_tv_tab_bar);
                tv.setText(TextUtils.isEmpty(bean.getName()) ? "" : bean.getName());
                tv.setBackgroundResource(holder.itemView.isSelected() ? R.drawable.bg_tab_bar_blue : 0);
                tv.setTextColor(holder.itemView.isSelected() ? ContextCompat.getColor(ctx, R.color.color_while) : ContextCompat.getColor(ctx, R.color.textColor5));
//
                holder.itemView.setOnTouchListener(new TouchPressListener(ctx, R.color.color_while, R.color.textColor5, R.color.light_blue, new View[]{tv}, v -> {
                    if (getView() != null)
                        getView().onTagClickStart(datas.get(position).getDataList().size() > 11);
                    notifyDataSetChanged();
                    if (!holder.itemView.isSelected()) {
                        selectPosition = position;
                        rv.scrollToPosition(position);
                        new Handler().post(() -> {
                            if (getView() != null)
                                getView().onTagSelect(position, datas);
                        });
                    }

                }));
            }
        });
    }

    public void setContentNameAdapter(final Context ctx, final RecyclerView rv, final ProductInfoBean data) {

        rv.setLayoutManager(new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false));
        rv.setAdapter(new CommonAdapter<ProductInfoTitleBean>(ctx, getType(data)) {

            @Override
            protected int bindView(int viewType) {
                return R.layout.item_product_content_name;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, ProductInfoTitleBean bean, int position) {
                holder.setText(R.id.tv_product_info_content_name, bean.getName());
                FrameLayout frameLayout = holder.obtainView(R.id.fl_product_info_content_name);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) frameLayout.getLayoutParams();
                params.width = DensityUtil.dp2px(bean.getLayoutWith());
                frameLayout.setLayoutParams(params);
            }
        });
    }


    public void setSideAdapter(final Context ctx, final RecyclerView rv, final List<ProductInfoBean> datas, int position) {
        final ProductInfoBean infoBean = datas.get(position);
        rv.setLayoutManager(new WrapContentLinearLayoutManager(ctx));
        rv.setAdapter(new CommonAdapter<ProductInfoBean.DataListBean>(ctx, datas.get(position).getDataList()) {

            @Override
            protected int bindView(int viewType) {
                return  R.layout.item_quotation_info_side;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, ProductInfoBean.DataListBean bean, int position) {
                holder.setText(R.id.item_tv_side, TextUtils.isEmpty(bean.getMaterialName()) ? "-" : bean.getMaterialName());
                holder.setVisibility(R.id.item_tv_side_number, false);
                holder.setBackgroundColor(R.id.item_tv_side, ctx.getResources().getColor(position % 2 == 0 ? R.color.light_text5 : R.color.color_while));
                setLayoutHeight(holder.itemView, bean, infoBean);
            }
        });
    }


    public void setContentAdapter(final Context ctx, final RecyclerView rv, final List<ProductInfoBean> datas, int position) {
        final ProductInfoBean infoBean = datas.get(position);
        rv.setLayoutManager(new WrapContentLinearLayoutManager(ctx));
        rv.setAdapter(new CommonAdapter<ProductInfoBean.DataListBean>(ctx, datas.get(position).getDataList()) {

            @Override
            protected int bindView(int viewType) {
                return  R.layout.item_product_info_content;
            }

            @Override
            public void onBindHolder(RecyclerHolder holder, ProductInfoBean.DataListBean bean, int position) {
                holder.setText(R.id.tv_product_info_design_and_color, TextUtils.isEmpty(bean.getColorName()) ? "-" : bean.getColorName());
                holder.setText(R.id.tv_product_info_hardware_specifications, TextUtils.isEmpty(bean.getUnit()) ? "-" : bean.getUnit());
                holder.setText(R.id.tv_product_info_material, TextUtils.isEmpty(bean.getMatName()) ? "-" : bean.getMatName());
                holder.setText(R.id.tv_product_overlaying_process, TextUtils.isEmpty(bean.getFace()) ? "-" : bean.getFace());
                holder.setText(R.id.tv_product_info_grain, TextUtils.isEmpty(bean.getGrainDirection()) ? "-" : bean.getGrainDirection());
                holder.setText(R.id.tv_product_info_length, TextUtils.isEmpty(bean.getLength()) ? "-" : bean.getLength());
                holder.setText(R.id.tv_product_info_width, TextUtils.isEmpty(bean.getDepth()) ? "-" : bean.getDepth());
                holder.setText(R.id.tv_product_info_thickness, TextUtils.isEmpty(bean.getHeight()) ? "-" : bean.getHeight());
                holder.setText(R.id.tv_product_info_count, TextUtils.isEmpty(bean.getQty()) ? "-" : bean.getQty());
                holder.setText(R.id.tv_product_info_remark, TextUtils.isEmpty(bean.getRemark()) ? "-" : bean.getRemark());
                holder.setText(R.id.tv_product_tank_group_name, TextUtils.isEmpty(bean.getTankGroupName()) ? "-" : bean.getTankGroupName());
                holder.setText(R.id.tv_product_cutting_length, TextUtils.isEmpty(bean.getCuttingLong()) ? "-" : bean.getCuttingLong());
                holder.setText(R.id.tv_product_cutting_width, TextUtils.isEmpty(bean.getCuttingWidth()) ? "-" : bean.getCuttingWidth());
                holder.setText(R.id.tv_product_side_info, TextUtils.isEmpty(bean.getEdgeBanding()) ? "-" : bean.getEdgeBanding());
                setLayoutHeight(holder.itemView, bean, infoBean);
                switch (infoBean.type) {
                    case "1":
                        holder.setVisibility(R.id.fl_product_cutting_length, true);
                        holder.setVisibility(R.id.fl_product_cutting_width, true);
                        holder.setVisibility(R.id.fl_product_side_info, true);

                        holder.setVisibility(R.id.fl_product_info_hardware_specifications, false);
                        holder.setVisibility(R.id.fl_product_overlaying_process, false);
                        holder.setVisibility(R.id.fl_product_info_remark, false);
                        holder.setVisibility(R.id.fl_product_tank_group_name, false);
                        break;
                    case "2":
                        holder.setVisibility(R.id.fl_product_info_hardware_specifications, false);
                        holder.setVisibility(R.id.fl_product_overlaying_process, false);
                        break;
                    case "3":
                        holder.setVisibility(R.id.fl_product_info_hardware_specifications, false);
                        break;
                    case "4":
                        holder.setVisibility(R.id.fl_product_info_design_and_color, false);
                        holder.setVisibility(R.id.fl_product_info_material, false);
                        holder.setVisibility(R.id.fl_product_overlaying_process, false);
                        holder.setVisibility(R.id.fl_product_info_grain, false);
                        holder.setVisibility(R.id.fl_product_tank_group_name, false);
                        break;
                    case "5":
                        holder.setVisibility(R.id.fl_product_info_hardware_specifications, false);
                        holder.setVisibility(R.id.fl_product_overlaying_process, false);
                        break;
                    case "6":
                        holder.setVisibility(R.id.fl_product_info_design_and_color, false);
                        holder.setVisibility(R.id.fl_product_info_material, false);
                        holder.setVisibility(R.id.fl_product_overlaying_process, false);
                        holder.setVisibility(R.id.fl_product_info_grain, false);
                        break;
                    case "7":
                        holder.setVisibility(R.id.fl_product_info_hardware_specifications, false);
                        holder.setVisibility(R.id.fl_product_overlaying_process, false);
                        holder.setVisibility(R.id.fl_product_tank_group_name, false);
                        break;
                    case "8":
                        holder.setVisibility(R.id.fl_product_info_hardware_specifications, false);
                        holder.setVisibility(R.id.fl_product_info_material, false);
                        break;
                    case "9":
                        holder.setVisibility(R.id.fl_product_info_design_and_color, false);
                        holder.setVisibility(R.id.fl_product_info_material, false);
                        holder.setVisibility(R.id.fl_product_overlaying_process, false);
                        holder.setVisibility(R.id.fl_product_info_grain, false);
                        holder.setVisibility(R.id.fl_product_tank_group_name, false);
                        break;

                }
                holder.setBackgroundColor(R.id.ll_product_info_content_parent, ctx.getResources().getColor(position % 2 == 0 ? R.color.light_text5 : R.color.color_while));
            }
        });
    }

    private void setLayoutHeight(View view, ProductInfoBean.DataListBean bean, ProductInfoBean infoBean) {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
        if (!infoBean.type.equals("1")) {
            LogCat.d("length:" + bean.getMaterialName().getBytes().length);
            if (bean.getMaterialName().getBytes().length > 12 ||
                    bean.getRemark().getBytes().length > 20) {
                params.height = DensityUtil.dp2px(66);
            } else {
                params.height = DensityUtil.dp2px(44);
            }
        } else if (infoBean.type.equals("4") || infoBean.type.equals("6") || infoBean.type.equals("8") || infoBean.type.equals("9")) {
            if (bean.getMaterialName().getBytes().length > 30 ||
                    bean.getRemark().getBytes().length > 20) {
                params.height = DensityUtil.dp2px(66);
            } else {
                params.height = DensityUtil.dp2px(44);
            }

        } else {
            LogCat.d("length:" + bean.getMaterialName().getBytes().length);
            if (bean.getMaterialName().getBytes().length > 12) {
                params.height = DensityUtil.dp2px(66);
            } else {
                params.height = DensityUtil.dp2px(44);
            }
        }

        view.setLayoutParams(params);
    }

}
