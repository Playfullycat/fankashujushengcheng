package com.example.fankashujushengcheng;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by yxl on 17/11/29.
 */

public class PersonAdapter extends BaseAdapter {
    private Context mContext;
    private List<Person> mList;

    public PersonAdapter(Context mContext, List<Person> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater _layoutInflater = LayoutInflater.from(mContext);
        view = _layoutInflater.inflate(R.layout.item, null);
        if (view != null) {
            TextView _textView1 = view.findViewById(R.id.textViewName);
            TextView _textView2 = view.findViewById(R.id.textViewNumber);
            _textView1.setText(mList.get(i).getPersonName());
            _textView2.setText(mList.get(i).getPersonWorkerId());
        }
        return view;
    }
}
