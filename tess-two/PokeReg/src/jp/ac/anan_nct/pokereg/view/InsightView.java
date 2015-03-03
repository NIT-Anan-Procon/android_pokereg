package jp.ac.anan_nct.pokereg.view;


import jp.ac.anan_nct.pokereg.R;
import jp.ac.anan_nct.pokereg.config.Configuration;
import jp.ac.anan_nct.pokereg.entity.Category;
import jp.ac.anan_nct.pokereg.entity.CategorySet;
import jp.ac.anan_nct.pokereg.entity.EntitiesObserver;
import jp.ac.anan_nct.pokereg.entity.Receipt;
import jp.ac.anan_nct.pokereg.entity.ReceiptRow;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class InsightView extends View {
	private Paint p = new Paint();

	/* Constructors */
	public InsightView(Context context) {
		super(context);
		this.init();
	}
	public InsightView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.init();
	}
	public InsightView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.init();
	}

	private void init(){
		p.setColor(0xffffff00);
		p.setStrokeWidth(10.0f);
	}
	
	protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec){
	    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawLine(0, 0, 0, this.getHeight(), p);
		canvas.drawLine(0, this.getHeight(), this.getWidth(), this.getHeight(), p);
		canvas.drawLine(this.getWidth(), this.getHeight(), this.getWidth(), 0, p);
		canvas.drawLine(this.getWidth(), 0, 0, this.getHeight(), p);
	}
}
