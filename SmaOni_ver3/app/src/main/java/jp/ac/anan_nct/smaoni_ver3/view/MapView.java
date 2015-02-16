package jp.ac.anan_nct.smaoni_ver3.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import jp.ac.anan_nct.smaoni_ver3.activity.GameActivity;
import jp.ac.anan_nct.smaoni_ver3.activity.OptionActivity;
import jp.ac.anan_nct.smaoni_ver3.activity.R;

/**
 * Created by skriulle on 2015/02/15.
 */
public class MapView extends View{


    final int WC = LinearLayout.LayoutParams.WRAP_CONTENT;

    int width;
    boolean isTouched = false;
    int x = -1, y = -1, x1, y1;
    float rawX, rawY;

    MotionEvent me;
    int num;
    int w;

    public MapView(Context context){
        this(context, null);
    }

    public MapView(Context context, AttributeSet attrs){
        this(context, attrs, R.attr.mapViewStyle);
    }

    public MapView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);

        num = OptionActivity.gameData.gridNum;
    }


    @Override
    public boolean onTouchEvent(MotionEvent e){
        me = e;
        w = getWidth();
        x = (int)(((e.getRawX()-50)/(getWidth()-100))*num);
        y = (int)((e.getRawY()-330)/(getWidth()-100)*num);
        if(e.getRawY()<50)y=-1;
        rawX = e.getRawX();
        rawY = e.getRawY();
        if(e.getAction() == MotionEvent.ACTION_DOWN) isTouched = true;
        else if(e.getAction() == MotionEvent.ACTION_UP) isTouched = false;
        invalidate();
        GameActivity.setXY(x,y);
        return true;
    }

    public void setXY(int x, int y){
        this.x1 = x;
        this.y1 = y;
        invalidate();
    }

    public int getXY(char z){
        return (z == 'x') ? x : (z == 'y') ? y : -1;
    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);

        Paint paint = new Paint();

        width = canvas.getWidth()-100;

        Rect rect = new Rect(55,55,width/num+45,width/num+45);
        canvas.drawColor(Color.parseColor("#cccccc"));
        paint.setColor(Color.parseColor("#ff2222"));

        for(int j = 0; j < num; j++){         //y担当
            for(int i = 0; i < num; i++) {    //x担当
                if(i==x&&j==y) paint.setColor(Color.parseColor("#0000ff"));

                else           paint.setColor(Color.parseColor("#ff2222"));
                canvas.drawRect(rect, paint);
                rect.offset(width/num, 0);
            }
            rect = new Rect(55,55+width/num*(j+1),width/num+45,55+width/num*(j+2)-10);
            //rect = new Rect(55,55+100*(j+1),width/num+45,145+width/num*(j+1));
        }
        paint.setTextSize(100f);
        if(isTouched){
            paint.setColor(Color.GREEN);
            canvas.drawRect(me.getRawX() - 50, me.getRawY() - 350, me.getRawX() + 50, me.getRawY() - 250, paint);
        }



    }

    public void blueDraw(int x, int y){
        this.x = x;
        this.y = y;
        invalidate();
    }

}
