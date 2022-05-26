/*
 * Tencent is pleased to support the open source community by making QMUI_Android available.
 *
 * Copyright (C) 2017-2018 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the MIT License (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://opensource.org/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.acorn.myframeapp.ui.nestedscroll.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.acorn.basemodule.base.recyclerAdapter.BaseRecyclerAdapter;
import com.acorn.myframeapp.extendfun.MockDataKt;
import com.acorn.myframeapp.ui.recyclerview.adapter.ConventionalRecyclerAdapter;
import com.qmuiteam.qmui.nestedScroll.IQMUIContinuousNestedBottomView;
import com.qmuiteam.qmui.nestedScroll.QMUIContinuousNestedBottomDelegateLayout;
import com.qmuiteam.qmui.nestedScroll.QMUIContinuousNestedBottomRecyclerView;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUIPagerAdapter;
import com.qmuiteam.qmui.widget.QMUIViewPager;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class QDContinuousBottomView extends QMUIContinuousNestedBottomDelegateLayout {

    private MyViewPager mViewPager;
    private QMUIContinuousNestedBottomRecyclerView mCurrentItemView;
    private int mCurrentPosition = -1;
    private OnScrollNotifier mOnScrollNotifier;

    public QDContinuousBottomView(Context context) {
        super(context);
    }

    public QDContinuousBottomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public QDContinuousBottomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @NonNull
    @Override
    protected View onCreateHeaderView() {
        TextView headerView = new TextView(getContext());
        headerView.setTextSize(16);
        headerView.setTextColor(Color.BLACK);
        headerView.setBackgroundColor(Color.LTGRAY);
        headerView.setGravity(Gravity.CENTER);
        headerView.setText("This is normal view with ViewPager below");
        return headerView;
    }

    @Override
    protected int getHeaderHeightLayoutParam() {
        return QMUIDisplayHelper.dp2px(getContext(), 200);
    }

    @Override
    protected int getHeaderStickyHeight() {
        return QMUIDisplayHelper.dp2px(getContext(), 50);
    }


    @NonNull
    @Override
    protected View onCreateContentView() {
        mViewPager = new MyViewPager(getContext());
        mViewPager.setAdapter(new QMUIPagerAdapter() {
            @Override
            protected Object hydrate(ViewGroup container, int position) {
                QMUIContinuousNestedBottomRecyclerView recyclerView = new QMUIContinuousNestedBottomRecyclerView(getContext());

                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()) {
                    @Override
                    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT);
                    }
                });

                ConventionalRecyclerAdapter adapter = new ConventionalRecyclerAdapter(getContext(), MockDataKt.getRandomList(30));
                adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(@NonNull View itemView, int position, int itemViewType) {
                        Toast.makeText(getContext(), "click " + position, Toast.LENGTH_SHORT).show();
                    }
                });

                recyclerView.setAdapter(adapter);
                return recyclerView;
            }

            @Override
            protected void populate(ViewGroup container, Object item, int position) {
                container.addView((View) item);
            }

            @Override
            protected void destroy(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
                return view == o;
            }

            @Override
            public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                super.setPrimaryItem(container, position, object);
                mCurrentItemView = (QMUIContinuousNestedBottomRecyclerView) object;
                mCurrentPosition = position;
                if (mOnScrollNotifier != null) {
                    mCurrentItemView.injectScrollNotifier(mOnScrollNotifier);
                }
            }
        });
        return mViewPager;
    }


    class MyViewPager extends QMUIViewPager implements IQMUIContinuousNestedBottomView {
        static final String KEY_CURRENT_POSITION = "demo_bottom_current_position";

        public MyViewPager(Context context) {
            super(context);
        }

        @Override
        public void consumeScroll(int dyUnconsumed) {
            if (mCurrentItemView != null) {
                mCurrentItemView.consumeScroll(dyUnconsumed);
            }

        }

        @Override
        public void smoothScrollYBy(int dy, int duration) {
            if (mCurrentItemView != null) {
                mCurrentItemView.smoothScrollYBy(dy, duration);
            }
        }

        @Override
        public void stopScroll() {
            if (mCurrentItemView != null) {
                mCurrentItemView.stopScroll();
            }
        }

        @Override
        public int getContentHeight() {
            if (mCurrentItemView != null) {
                return mCurrentItemView.getContentHeight();
            }
            return 0;
        }

        @Override
        public int getCurrentScroll() {
            if (mCurrentItemView != null) {
                return mCurrentItemView.getCurrentScroll();
            }
            return 0;
        }

        @Override
        public int getScrollOffsetRange() {
            if (mCurrentItemView != null) {
                return mCurrentItemView.getScrollOffsetRange();
            }
            return getHeight();
        }

        @Override
        public void injectScrollNotifier(OnScrollNotifier notifier) {
            mOnScrollNotifier = notifier;
            if (mCurrentItemView != null) {
                mCurrentItemView.injectScrollNotifier(notifier);
            }
        }

        @Override
        public void saveScrollInfo(@NonNull Bundle bundle) {
            bundle.putInt(KEY_CURRENT_POSITION, mCurrentPosition);
            if (mCurrentItemView != null) {
                mCurrentItemView.saveScrollInfo(bundle);
            }
        }

        @Override
        public void restoreScrollInfo(@NonNull Bundle bundle) {
            if (mCurrentItemView != null) {
                int currentPos = bundle.getInt(KEY_CURRENT_POSITION, -1);
                if (currentPos == mCurrentPosition) {
                    mCurrentItemView.restoreScrollInfo(bundle);
                }
            }
        }
    }
}