package jp.ac.anan_nct.pokereg.view;

import java.util.LinkedList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class IconsBarView extends View {

	public interface OnTouchIconListener{
		public void OnTouchIcon(int index, IconMenu icon);
	}
	public class IconMenu{
		private int id;
		private String caption;
		private Bitmap image;
		private Matrix matrix;
		private boolean enabled;
		public IconMenu(int id, String caption, Bitmap image, boolean enabled){
			this.id = id;
			this.enabled = enabled;
			this.caption = caption;
			this.matrix = new Matrix();
			if(image != null){
				Matrix rm = new Matrix();
				rm.postScale(radius * 1.6f / (float)image.getWidth(),
						radius * 1.6f / (float)image.getHeight());
				this.image = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), rm, true);
			} else {
				this.image = image;
			}
		}
		public int getId(){ return this.id; }
		public String getCaption(){ return this.caption; }
		public Bitmap getImage(){ return this.image; }
		public Matrix getMatrix(){ return this.matrix; }
		public boolean isEnabled(){ return this.enabled; };
	}
	
	/* Constructors */
	public IconsBarView(Context context) {
		super(context);
		this.init();
	}
	public IconsBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.init();
	}
	public IconsBarView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.init();
	}
	private void init(){
		this.setClickable(true);
		this.setPaint();
	}
	
	private OnTouchIconListener listener;
	public void setListener(OnTouchIconListener listener){
		this.listener = listener;
	}
	
	private LinkedList<IconMenu> icons = new LinkedList<IconMenu>();
	public void addIconMenus(int id, String caption, Bitmap bitmap, boolean enabled){
		this.icons.add(new IconMenu(id, caption, bitmap, enabled));
	}
	public void clearIconMenus(){
		this.icons.clear();
	}
	
	private Paint circleBg, emphasizedBg, textFg, imagePaint,
		disabledBg, disabledFg, disabledImg;
	private void setPaint(){
		this.circleBg = new Paint();
		this.emphasizedBg = new Paint();
		this.textFg = new Paint();
		this.imagePaint = new Paint();
		this.disabledBg = new Paint();
		this.disabledFg = new Paint();
		this.disabledImg = new Paint();
		this.circleBg.setColor(0x66bbbbbb);
		this.emphasizedBg.setColor(0x669999ff);
		this.textFg.setColor(0xff000000);
		this.textFg.setTextSize(this.textSize);
		this.imagePaint.setColor(0x66000000);
		this.disabledBg.setColor(0x66eeeeee);
		this.disabledFg.setTextSize(this.textSize);
		this.disabledFg.setColor(0xff666666);
		this.disabledImg.setColor(0x33000000);
	}
	
	private float textSize = 25.0f;
	private int radius = 80;
	private int duration = 30;
	
	@Override
	protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec){
	    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	    setMeasuredDimension(widthMeasureSpec, this.radius * 2);
    }
	
	private int pressedIndex = -1;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
	    float x = event.getX();
	    float y = event.getY();
	    int index = this.getIndexByPosition(x, y);
	    switch(event.getAction()){
	    case MotionEvent.ACTION_DOWN:
		    android.util.Log.i("POS", String.valueOf(index));
	    	this.pressedIndex = index;
	    	this.invalidate();
	    	break;
	    case MotionEvent.ACTION_UP:
		    if(index >= 0 && this.pressedIndex == index && this.listener != null){
		    	this.listener.OnTouchIcon(index, this.icons.get(index));
		    }
	    	this.pressedIndex = -1;
	    	this.invalidate();
	    	break;
	    }
	    return true;
	}
	
	private Rect r = new Rect();
	
	@Override
	protected void onDraw(Canvas canvas) {
		if(this.icons.isEmpty()) return;
		for(int i=0; i<this.icons.size(); i++){
			int x = (this.radius * 2 + this.duration) * i;
			int y = 0;
			canvas.drawCircle(x + this.radius, y + this.radius, this.radius,
					this.icons.get(i).isEnabled() ?
							this.pressedIndex == i ? this.emphasizedBg : this.circleBg
							: this.disabledBg);
			Bitmap image = this.icons.get(i).getImage();
			if(image != null){
				Matrix m = this.icons.get(i).getMatrix();
				canvas.drawBitmap(image, 
						x + this.radius - image.getWidth() / 2,
						y + this.radius - image.getHeight() / 2,
						this.icons.get(i).isEnabled() ?
								this.imagePaint : this.disabledImg);
			}
			String text = this.icons.get(i).getCaption();
			this.textFg.getTextBounds(text, 0, text.length(), r);
			canvas.drawText(text,
					x + this.radius - r.width() / 2,
					(int)(y + (this.radius * 2) - r.height()), this.icons.get(i).isEnabled() ?
							this.textFg : this.disabledFg);
		}
	}

	private int getIndexByPosition(float x, float y){
		int x0,x1, rx=-1;
		for(int i=0; i<this.icons.size(); i++){
			x0 = (this.radius * 2 + this.duration) * i;
			x1 = (this.radius * 2 + this.duration) * i + 2 * this.radius;
			if(x0 < x && x < x1) return this.icons.get(i).isEnabled() ? i : -1;
		}
		return -1;
	}
}
