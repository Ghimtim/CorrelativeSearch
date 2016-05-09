package widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jnu.correlativesearch.R;


/**
 * Created by Leo on 2016/3/30.
 */
public class ExpandableLayout extends LinearLayout {

    private Context mContext;
    private LinearLayout mHandleView;
    private LinearLayout mContentView;
    private ImageView mIconExpand;
    int mContentHeight = 0;
    int mTitleHeight = 0;
    private boolean isExpand;
    private Animation animationUp;
    private Animation animationDown;

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mHandleView = (LinearLayout)this.findViewById(R.id.btn_collapse);
        this.mContentView = (LinearLayout)this.findViewById(R.id.expand_part);
        this.mIconExpand = (ImageView)this.findViewById(R.id.expand_img);
        this.mHandleView.setOnClickListener(new ExpandListener());
        this.mContentView.setOnClickListener(new ExpandListener());
        mContentView.setVisibility(View.GONE);
    }

    public ExpandableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(this.mContentHeight == 0){
            this.mContentView.measure(widthMeasureSpec,0);
            this.mContentHeight = this.mContentView.getMeasuredHeight();
        }
        if(this.mTitleHeight == 0){
            this.mHandleView.measure(widthMeasureSpec,0);
            this.mTitleHeight = this.mHandleView.getMeasuredHeight();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private class ExpandListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            clearAnimation();
            if(!isExpand){
                if(animationDown == null){
                    animationDown = new DropDownAnim(mContentView,mContentHeight,true);
                    animationDown.setDuration(200);
                }
                startAnimation(animationDown);
//                mContentView.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.animalpha);
                mIconExpand.setImageResource(R.drawable.img_hide);
                isExpand = true;
            }else {
                isExpand = false;
                if(animationUp == null){
                    animationUp = new DropDownAnim(mContentView,mContentHeight,false);
                    animationUp.setDuration(200);
                }
                startAnimation(animationUp);
                mIconExpand.setImageResource(R.drawable.img_show);
            }

        }
    }
    class DropDownAnim extends Animation{
        private int targetHeight;
        private View view;
        private boolean down;

        /**
         * 构造方法
         *
         * @param targetview
         *            需要被展现的view
         * @param vieweight
         *            目的高
         * @param isdown
         *            true:向下展开，false:收起
         */

        public DropDownAnim(View targetView , int viewHeight , boolean isDown){
            this.view = targetView;
            this.targetHeight = viewHeight;
            this.down = isDown;
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
           int newHeight;
            if(down){
                newHeight = (int)(targetHeight * interpolatedTime);
            }else {
                newHeight = (int)(targetHeight * (1 - interpolatedTime));
            }
            view.getLayoutParams().height = newHeight;
            view.requestLayout();
            if(view.getVisibility() == View.GONE) {
                view.setVisibility(View.VISIBLE);
            }
        }
    }
}
