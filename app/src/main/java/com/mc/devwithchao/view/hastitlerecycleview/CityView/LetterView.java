package com.mc.devwithchao.view.hastitlerecycleview.CityView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class LetterView extends LinearLayout {
    private Context mContext;
    private CharacterClickListener mListener;
    private int height;
    private int totalHeight;
    private ArrayList<TextView> texts;//字母集合
    public LetterView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;

        setOrientation(VERTICAL);
        texts=new ArrayList<>();
        initView();
    }

    private void initView() {
//        addView(buildImageLayout());

        for (char i = 'A'; i <= 'Z'; i++) {
            final String character = i + "";
            TextView tv = buildTextLayout(character);
            addView(tv);
        }
        addView(buildTextLayout("#"));
    }

    private TextView buildTextLayout(final String character) {
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,1);

        TextView tv = new TextView(mContext);
        tv.setLayoutParams(layoutParams);
        tv.setGravity(Gravity.CENTER);
        tv.setClickable(false);
        tv.setText(character);
//        tv.setTextColor("");
        texts.add(tv);
        return tv;
    }

    private ImageView buildImageLayout() {
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1);

        ImageView iv = new ImageView(mContext);
        iv.setLayoutParams(layoutParams);
        return iv;
    }

    public void setCharacterListener(CharacterClickListener listener) {
        mListener = listener;
    }

    public interface CharacterClickListener {
        void clickCharacter(String character);

        void clickArrow(boolean click);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {//不在往下面分发
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                setBackgroundColor(getResources().getColor(R.color.m_line_gray_one));//手指按下时背景变色
                if (mListener!=null){
                    mListener.clickArrow(true);
                }
                //注意这里没有break，因为down时，也要计算落点 回调监听器
            case MotionEvent.ACTION_MOVE:
                float y = event.getY();
                //通过计算判断落点在哪个区域：
                int pressI = (int) ((y - getPaddingTop()));
                //边界处理（在手指move时，有可能已经移出边界，防止越界）
                if (pressI < 0) {
                    pressI = 0;
                } else if (pressI >= totalHeight) {
                    pressI = totalHeight;
                }
                //计算落点position
                int position=(int)(pressI/(double)height+0.4)-1;//position需要-1
                if (position>27){//防止落点超过26个字母
                    position=27;
                }else if (position<0){
                    position=0;
                }
                String text=texts.get(position).getText().toString();
                //回调监听器
                if (null != mListener && pressI > -1 && pressI <=totalHeight) {
                    mListener.clickCharacter(text);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            default:
                setBackgroundResource(android.R.color.transparent);//手指抬起时背景恢复透明
                //回调监听器
                if (mListener!=null){
                    mListener.clickArrow(false);
                }
        }
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int specWidth = MeasureSpec.getSize(widthMeasureSpec);
        int actualWidth = specWidth;// 实际宽度
        int childCount = getChildCount();
        totalHeight = 0;
        for (int index = 0; index < childCount; index++) {
            View child = getChildAt(index);
            int width = child.getMeasuredWidth();
            height = child.getMeasuredHeight();
            totalHeight +=height;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
