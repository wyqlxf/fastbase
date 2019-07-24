/**
 * MIT License
 * <p>
 * Copyright (c) 2019 wangyognqi
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.wyq.fast.demo.calculator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wyq.fast.core.asynctask.FastAsyncTask;
import com.wyq.fast.demo.BaseAppCompatActivity;
import com.wyq.fast.demo.R;
import com.wyq.fast.interfaces.asynctask.OnDoInBackground;
import com.wyq.fast.interfaces.asynctask.OnPostExecute;
import com.wyq.fast.model.MCalculator;
import com.wyq.fast.model.MCalculatorDetail;
import com.wyq.fast.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Author: WangYongQi
 * Mortgage Calculator detail
 */

public class MoreDetailActivity extends BaseAppCompatActivity {

    // 更多详情适配器
    private MoreDetailAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_detail);
        findViewById(R.id.ivHeadBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mAdapter = new MoreDetailAdapter();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        // 设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        // 设置Item增加、移除动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        // 如果可以確定每個Item的高度是固定的，設置該選項可以提高性能
        recyclerView.setHasFixedSize(true);
        // 设置adapter
        recyclerView.setAdapter(mAdapter);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            MCalculator entity = (MCalculator) bundle.getSerializable("calculator");
            loadData(entity);
        }
    }

    /**
     * 开启异步任务加载数据
     *
     * @param entity
     */
    private void loadData(final MCalculator entity) {
        new FastAsyncTask.Builder<String, String, List<MCalculatorDetail>>().setDoInBackground(new OnDoInBackground<String, List<MCalculatorDetail>>() {
            @Override
            public List<MCalculatorDetail> doInBackground(String... strings) {
                if (entity != null) {
                    // 根据实体对象获取详情集合
                    return entity.getCalculatorDetails();
                } else {
                    return null;
                }
            }
        }).setPostExecute(new OnPostExecute<List<MCalculatorDetail>>() {
            @Override
            public void onPostExecute(List<MCalculatorDetail> mCalculatorDetails) {
                if (mCalculatorDetails != null) {
                    if (mAdapter != null) {
                        mAdapter.updateData(mCalculatorDetails);
                        mAdapter.notifyDataSetChanged();
                    }
                } else {
                    ToastUtil.showShort("获取详情数据失败");
                }
            }
        }).start();
    }

    /**
     * 更多详情适配器
     */
    private class MoreDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<MCalculatorDetail> mList;
        private LayoutInflater mInflater;

        private MoreDetailAdapter() {
            this.mList = new ArrayList<>();
            this.mInflater = LayoutInflater.from(getContext());
        }

        public void updateData(List<MCalculatorDetail> list) {
            if (list != null) {
                this.mList = list;
            }
        }

        @Override
        public int getItemCount() {
            return mList != null ? mList.size() : 0;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.item_more_detail, parent, false);
            return new ListHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof ListHolder) {
                if (mList != null && mList.size() > position) {
                    MCalculatorDetail moreDetail = mList.get(position);
                    ListHolder listHolder = (ListHolder) holder;
                    listHolder.tvItem1.setText(moreDetail.getNumber() + "");
                    listHolder.tvItem2.setText(moreDetail.getMoney() + "");
                    listHolder.tvItem3.setText(moreDetail.getPrincipal() + "");
                    listHolder.tvItem4.setText(moreDetail.getInterest() + "");
                    listHolder.tvItem5.setText(moreDetail.getRemainingMoney() + "");
                    if (position % 2 == 0) {
                        listHolder.llItem.setBackgroundColor(getContext().getResources().getColor(R.color.colorWhite));
                    } else {
                        listHolder.llItem.setBackgroundColor(getContext().getResources().getColor(R.color.colorItemLayout));
                    }
                }
            }
        }

        private class ListHolder extends RecyclerView.ViewHolder {
            private LinearLayout llItem;
            private TextView tvItem1, tvItem2, tvItem3, tvItem4, tvItem5;

            private ListHolder(View view) {
                super(view);
                llItem = view.findViewById(R.id.llItem);
                tvItem1 = view.findViewById(R.id.tvItem1);
                tvItem2 = view.findViewById(R.id.tvItem2);
                tvItem3 = view.findViewById(R.id.tvItem3);
                tvItem4 = view.findViewById(R.id.tvItem4);
                tvItem5 = view.findViewById(R.id.tvItem5);
            }
        }

    }

}
