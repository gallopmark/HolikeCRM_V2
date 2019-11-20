package com.holike.crm.util;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by pony on 2019/11/1.
 * Version v3.0 app报表
 */
public class RecyclerUtils {
    /*联动滚动*/
    public static void setScrollSynchronous(final RecyclerView target, final RecyclerView follow) {
        target.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
                    follow.scrollBy(dx, dy);
                }
            }
        });
        follow.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
                    target.scrollBy(dx, dy);
                }
            }
        });
    }
}
