package org.wb.hust.dongguess.customui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

import org.wb.hust.dongguess.dongguess.R;
import org.wb.hust.dongguess.modal.IWordButtonClickListener;
import org.wb.hust.dongguess.modal.WordButton;
import org.wb.hust.dongguess.util.Util;

import java.util.ArrayList;

/**
 * Created by Administrator on 2014/12/23.
 */
public class CustomGridView extends GridView {
    public final static int COUNTS_WORDS = 24;
    private ArrayList<WordButton> mButtonList = new ArrayList<WordButton>();
    private CustomGridAdapter mAdapter;
    private Context mContext;
    private Animation mScaleAnim;
    private IWordButtonClickListener mWordButtonClickListener;

    public CustomGridView(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        mContext = context;
        mAdapter = new CustomGridAdapter();
        this.setAdapter(mAdapter);
    }

    public void updateData(ArrayList<WordButton> list){
        mButtonList = list;
        this.setAdapter(mAdapter);
    }

    public void registerWordButtonClickEvents(IWordButtonClickListener IWordButtonClickListener){
        this.mWordButtonClickListener = IWordButtonClickListener;
    }

    class CustomGridAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mButtonList.size();
        }

        @Override
        public Object getItem(int position) {
            return mButtonList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final WordButton holder;
            if(convertView == null){
                convertView = Util.getView(mContext, R.layout.custom_ui_gridview_item);
                mScaleAnim = AnimationUtils.loadAnimation(mContext,R.anim.scale);
                holder = mButtonList.get(position);
                holder.mIndex = position;
                if(holder.mViewButton == null) {
                    holder.mViewButton = (Button) convertView.findViewById(R.id.item_btn);
                    holder.mViewButton.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mWordButtonClickListener.onWordButtonClick(holder);
                        }
                    });

                }
                mScaleAnim.setStartOffset(position * 70l);
                convertView.setTag(holder);
            }else{
                holder= (WordButton) convertView.getTag();
            }
            holder.mViewButton.setText(holder.mText);
            holder.mViewButton.startAnimation(mScaleAnim);
            return convertView;
        }
    }
}
