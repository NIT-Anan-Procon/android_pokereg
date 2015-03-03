package jp.ac.anan_nct.pokereg.view;

import java.util.LinkedList;

import jp.ac.anan_nct.pokereg.Key;
import jp.ac.anan_nct.pokereg.KeySetPattern;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class Flickable10Key extends View {

	public interface Flickable10KeyEventListener {
		void onTouch(Key key);
		void onFlick(int[] sequence);
	}
	
	private Flickable10KeyEventListener listener;
	private KeySetPattern keySetPattern;
	private Key[] keys = new Key[12];
	private int buttonRadius = 60;
	private int buttonDuration = 30;
	private LinkedList<Integer> flicks;
	private int emphasizeKeyIndex = -1;
	private boolean acceptInput = true;
	private boolean keepPath = false;
	
	public void setKeepPath(boolean keepPath) {
		this.keepPath = keepPath;
	}
	/* Constructors */
	public Flickable10Key(Context context) {
		super(context);
		this.init();
	}
	public Flickable10Key(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.init();
	}
	public Flickable10Key(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.init();
	}
	private void init(){
		this.setPaint();
		this.setKeySetPattern(KeySetPattern.BottomUp);
		this.flicks = new LinkedList<Integer>();
	}
	
	public void setWidth(int width){
		this.buttonDuration = 30;
		this.buttonRadius = width / 4 - this.buttonDuration * 2;
	}
	
	protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec){
	    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	    setMeasuredDimension(
	    		this.buttonRadius*3*2 + this.buttonDuration * 2,
	    		this.buttonRadius*4*2 + this.buttonDuration * 3);
    }
	
	private int touchingKeyIndex = -1;
	private int flickingKeyIndex = -1;
	private float[] fingerPosition = null;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(!this.acceptInput) return false;
	    float x = event.getX();
	    float y = event.getY();
    	int index = this.getKeyIndexByPosition(x,y);
	    switch(event.getAction()){
	    case MotionEvent.ACTION_DOWN:
    		this.emphasizeKeyIndex = index;
    		this.touchingKeyIndex = index;
    		this.flickingKeyIndex = index;
    		this.flicks.clear();
    		if(index >= 0){
    			this.flicks.add(index);
    		}
	    	this.invalidate(); 
	    	break;
	    case MotionEvent.ACTION_UP:
	    	if(this.listener != null){
		    	if(this.touchingKeyIndex >= 0){
		    		Key key = this.keys[this.touchingKeyIndex];
		    		this.listener.onTouch(key);
		    	}
		    	if(this.flicks.size() > 1){
		    		int[] fs = new int[this.flicks.size()];
		    		int i=0;
		    		for(int idx : this.flicks){
		    			fs[i++] = idx;
		    		}
		    		this.listener.onFlick(fs);
		    	}
	    	}
    		this.emphasizeKeyIndex = -1;
    		this.touchingKeyIndex = -1;
    		this.flickingKeyIndex = -1;
    		this.fingerPosition = null;
    		if(!this.keepPath) this.flicks.clear();
	    	this.invalidate(); 
	    	break;
	    case MotionEvent.ACTION_MOVE:
	    	if(this.touchingKeyIndex != index) this.touchingKeyIndex = -1;
	    	if(this.flickingKeyIndex != index && index != -1){
	    		this.flicks.add(index);
	    		this.flickingKeyIndex = index;
	    	}
	    	this.fingerPosition = new float[]{x,y};
	    	this.invalidate(); 
	    	break;
	    }
	    
	    return true;
	}

	private Paint buttonp = new Paint();
	private Paint empbuttonp = new Paint();
	private Paint pathp = new Paint();
	private Paint textp = new Paint();
	private void setPaint(){
		buttonp.setColor(0x66ffffff);
		empbuttonp.setColor(0x669999ff);
		pathp.setColor(0x990000ff);
		pathp.setStrokeWidth(20f);
		textp.setColor(0xff000000);
		textp.setTextSize(this.buttonRadius);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		float[] p = null;
		float[] p0 = null;
		for(int index : this.flicks){
			float[] pos = this.getPositionByKeyIndex(index);
			if(p!=null){
				canvas.drawLine(p[0], p[1], pos[0], pos[1], pathp);
			}
			p0 = p;
			p = pos;
		}
		if(p != null && this.fingerPosition != null){
			canvas.drawLine(p[0], p[1], this.fingerPosition[0], this.fingerPosition[1], pathp);
			p0 = p;
			p = this.fingerPosition;
		}
		if(p != null && p0 != null && !this.acceptInput){
			float[] q = new float[]{ p0[0] - p[0], p0[1] - p[1] };
				
			double r = Math.sqrt(q[0]*q[0] + q[1]*q[1]);
			double th = Math.acos( q[0] / r );
			double dth1 = th - Math.PI / 6 + Math.PI;
			double dth2 = th + Math.PI / 6 + Math.PI;
			int dr = 60;
			if(p0[1]<p[1]){
				canvas.drawLine(p[0], p[1],
						p[0] - (float)(dr * Math.cos(dth1)),
						p[1] + (float)(dr * Math.sin(dth1)),
						pathp);			
				canvas.drawLine(p[0], p[1],
						p[0] - (float)(dr * Math.cos(dth2)),
						p[1] + (float)(dr * Math.sin(dth2)),
						pathp);
			}else{
				canvas.drawLine(p[0], p[1],
						p[0] - (float)(dr * Math.cos(dth1)),
						p[1] - (float)(dr * Math.sin(dth1)),
						pathp);			
				canvas.drawLine(p[0], p[1],
						p[0] - (float)(dr * Math.cos(dth2)),
						p[1] - (float)(dr * Math.sin(dth2)),
						pathp);
			}
		}
		
		for(int i=0; i<4; i++){
			for(int j=0; j<3; j++){
				canvas.drawCircle(
						this.buttonRadius*(2*j+1) + this.buttonDuration*j, 
						this.buttonRadius*(2*i+1) + this.buttonDuration*i,
						this.buttonRadius, this.emphasizeKeyIndex == (i*3+j) ? empbuttonp : buttonp);
				String name = this.keys[i*3+j].toString();
				canvas.drawText(name,
						this.buttonRadius*(2*j+1) + this.buttonDuration*j - (this.buttonRadius * 1 / 5 * name.length()),
						this.buttonRadius*(2*i+1) + this.buttonDuration*i + (this.buttonRadius * 1 / 3),
						textp);
			}
		}
		
	}
	
	/* Helpers */
	private float[] getPositionByKeyIndex(int index){
		int x = index % 3;
		int y = index / 3;
		return new float[]{
				this.buttonRadius*(2*x+1) + this.buttonDuration*x,
				this.buttonRadius*(2*y+1) + this.buttonDuration*y
		};
	}
	private int getKeyIndexByPosition(float x, float y){
		int x0,x1,y0,y1, rx=-1, ry=-1;
		for(int i=0; i<3; i++){
			x0 = this.buttonRadius*(2*i) + this.buttonDuration*i;
			x1 = this.buttonRadius*(2*i+2) + this.buttonDuration*i;
			if(x0 < x && x < x1){ rx = i; break; }
		}
		for(int i=0; i<4; i++){
			y0 = this.buttonRadius*(2*i) + this.buttonDuration*i;
			y1 = this.buttonRadius*(2*i+2) + this.buttonDuration*i;
			if(y0 < y && y < y1){ ry = i; break; }
		}
		return (rx < 0 || ry < 0) ? -1 : (ry * 3 + rx);
	}
	
	
	/* Setters */
	public void setEventListner(Flickable10KeyEventListener listener){
		this.listener = listener;
	}	
	public void setKeySetPattern(KeySetPattern pat){
		this.keySetPattern = pat;
		switch(pat){
		case TopDown:
			this.keys[0] = Key.Number1;
			this.keys[1] = Key.Number2;
			this.keys[2] = Key.Number3;
			this.keys[3] = Key.Number4;
			this.keys[4] = Key.Number5;
			this.keys[5] = Key.Number6;
			this.keys[6] = Key.Number7;
			this.keys[7] = Key.Number8;
			this.keys[8] = Key.Number9;
			this.keys[9] = Key.Number0;
			this.keys[10] = Key.Enter;
			this.keys[11] = Key.Clear;
			break;
		case BottomUp:
			this.keys[0] = Key.Number7;
			this.keys[1] = Key.Number8;
			this.keys[2] = Key.Number9;
			this.keys[3] = Key.Number4;
			this.keys[4] = Key.Number5;
			this.keys[5] = Key.Number6;
			this.keys[6] = Key.Number1;
			this.keys[7] = Key.Number2;
			this.keys[8] = Key.Number3;
			this.keys[9] = Key.Number0;
			this.keys[10] = Key.Enter;
			this.keys[11] = Key.Clear;
			break;
		}
		this.invalidate();
	}

	public boolean isAcceptInput() {
		return acceptInput;
	}
	public void setAcceptInput(boolean acceptInput) {
		this.acceptInput = acceptInput;
	}

	public void setFlicks(Key[] keys) {
		this.flicks.clear();
		for(Key key : keys){
			int index = this.keySetPattern.getIndexFromKey(key);
			if(index >= 0){
				this.flicks.add(index);
			}
		}
	}

}
