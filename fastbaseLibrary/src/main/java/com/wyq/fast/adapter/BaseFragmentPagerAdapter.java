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
package com.wyq.fast.adapter;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * Author:WangYongQi
 * Base FragmentPagerAdapter
 */

public class BaseFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> list;

    public BaseFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        list = new ArrayList<>();
    }

    public void setList(List<Fragment> fragmentList) {
        list = fragmentList;
    }

    public void addList(List<Fragment> fragmentList) {
        if (null == list) {
            list = new ArrayList<>();
        }
        if (!list.contains(fragmentList)) {
            list.addAll(fragmentList);
        }
    }

    public void addFragment(Fragment fragment) {
        if (null != list) {
            list.add(fragment);
        }
    }

    public void addFragment(Fragment fragment, int position) {
        if (null != list) {
            list.add(position, fragment);
        }
    }

    public void removeFragment(Fragment fragment) {
        if (null != list) {
            list.remove(fragment);
        }
    }

    public void removeFragment(int position) {
        if (null != list && list.size() > position) {
            list.remove(position);
        }
    }

    public void clearList() {
        if (null != list) {
            list.clear();
        }
    }

    @Override
    public Fragment getItem(int position) {
        if (null != list && list.size() > position) {
            return list.get(position);
        }
        return null;
    }

    @Override
    public int getCount() {
        if (null != list) {
            return list.size();
        }
        return 0;
    }

}
