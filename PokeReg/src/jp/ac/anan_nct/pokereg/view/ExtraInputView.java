package jp.ac.anan_nct.pokereg.view;

import jp.ac.anan_nct.pokereg.R;
import jp.ac.anan_nct.pokereg.entity.EntitiesObserver;
import jp.ac.anan_nct.pokereg.entity.Receipt;
import jp.ac.anan_nct.pokereg.entity.ReceiptRow;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ExtraInputView extends LinearLayout 
		implements EntitiesObserver   {

	public interface Callback{
		void afterDeleteItem(ReceiptRow item);
	}
	private Callback callback;
	public void setCallback(Callback callback) {
		this.callback = callback;
	}
	
	private Receipt receipt;
	
	public Receipt getReceipt() {
		return receipt;
	}
	public void setReceipt(Receipt receipt) {
		this.receipt = receipt;
		this.receipt.addOvserver(this);
		this.refresh();
	}
	/* Constructors */
	public ExtraInputView(Context context) {
		super(context);
		init(context);
	}
	public ExtraInputView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	public ExtraInputView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}
	
	private View layout;
	private ListView list;
	private ArrayAdapter<ReceiptRow> adapter;
	private void init(Context context){
		this.setClickable(true);
		TextView input = new TextView(this.getContext());
		this.addView(input);
	}
	public View getContextTarget(){
		return this.list;
	}
	
	public void refresh(){
		this.adapter.clear();
		for(ReceiptRow r : this.receipt){
			this.adapter.add(r);
		}
		this.adapter.notifyDataSetChanged();
		this.list.smoothScrollToPosition(0);
		this.invalidate();
	}
	@Override
	public void updatedEntities() {
		this.refresh();
		
	}
}
