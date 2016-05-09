package widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Leo on 2016/3/25.
 */
public class ClearEditText extends EditText implements View.OnFocusChangeListener{

    private Drawable left,right;


    public Drawable getLeftImg() {
        return left;
    }


    public Drawable getRightImg() {
        return right;
    }

    public void setLeft(Drawable left) {
        this.left = left;
    }

    public void setRight(Drawable right) {
        this.right = right;
    }

    public void setIsFocus(boolean isFocus) {
        this.isFocus = isFocus;
    }

    public boolean isFocus() {

        return isFocus;
    }

    private boolean isFocus = false;

    private int Xup;


    public ClearEditText(Context context) {
        this(context, null);
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
         initWidgets();
    }

    public void initWidgets(){
        left = getCompoundDrawables()[0];
        right = getCompoundDrawables()[2];
        setCompoundDrawables(left,null,null,null);
        setOnFocusChangeListener(this);

    }





    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_UP:
                Xup = (int)event.getX();
                //当点击位置到控件最右的距离小于getCompoundPaddingRight()时，隐藏删除图标
                if((getWidth() - Xup) <= getCompoundPaddingRight()){
                    setText("");
                }
        }
        return super.onTouchEvent(event);
    }





    @Override
    public void onFocusChange(View v, boolean hasFocus) {
           this.isFocus = hasFocus;
    }


}


