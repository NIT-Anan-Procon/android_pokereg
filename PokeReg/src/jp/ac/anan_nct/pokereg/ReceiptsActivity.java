package jp.ac.anan_nct.pokereg;

import jp.ac.anan_nct.pokereg.entity.Data;
import jp.ac.anan_nct.pokereg.entity.Receipt;
import jp.ac.anan_nct.pokereg.entity.ReceiptSet;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class ReceiptsActivity extends Activity {

	public class WrappedReceipt {
		private Receipt receipt;
		public Receipt getReceipt(){
			return this.receipt;
		}
		public WrappedReceipt(Receipt r){ this.receipt = r; }
		@Override
		public String toString(){
			Time t = this.receipt.getTime();
			return String.format("%-2d日%-2d時%-2d分       ¥%8d",
					t.monthDay, t.hour, t.minute,
					this.receipt.getTotal() );
		}
		@Override
		public boolean equals(Object o){
			if(o instanceof WrappedReceipt) return ((WrappedReceipt)o).receipt.getId() == this.receipt.getId();
			return false;
		}
		@Override
		public int hashCode(){ return this.receipt.hashCode(); }
	}
	
	private ArrayAdapter<WrappedReceipt> adapter;
	
	private TextView totalView;
	private int year;
	private int month;
	private Spinner yearSpinner;
	private Spinner monthSpinner;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipts);

        Time now = new Time("Asia/Tokyo"); now.setToNow();
        this.year = now.year;
        this.month = now.month + 1;
        yearSpinner = new Spinner(this);
        monthSpinner = new Spinner(this);
        ArrayAdapter<Integer> yearAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_list_item_1);
        ArrayAdapter<Integer> mounthAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_list_item_1);
        for(int i=2014; i<2114; i++) yearAdapter.add(i);
        for(int i=1; i<=12; i++) mounthAdapter.add(i);
        yearSpinner.setAdapter(yearAdapter);
        monthSpinner.setAdapter(mounthAdapter);
        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                    int position, long id) {
                Spinner spinner = (Spinner)parent;
                year = (Integer)spinner.getSelectedItem();
                updateList();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) { }
        });
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                    int position, long id) {
                Spinner spinner = (Spinner)parent;
                month = (Integer)spinner.getSelectedItem();
                updateList();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) { }
        });
        LinearLayout spinners = (LinearLayout)findViewById(R.id.spinners);
        TextView t1 = new TextView(this);
        t1.setText("年");
        TextView t2 = new TextView(this);
        t2.setText("月");
        spinners.addView(yearSpinner);
        spinners.addView(t1);
        spinners.addView(monthSpinner);
        spinners.addView(t2);
        this.totalView = (TextView)findViewById(R.id.totalView);

        yearSpinner.setSelection(this.year - 2014);
        monthSpinner.setSelection(this.month - 1);
        

		ListView list = (ListView)findViewById(R.id.receipts);
		this.adapter = new ArrayAdapter<WrappedReceipt>(this, android.R.layout.simple_list_item_1);
        list.setAdapter(this.adapter);
        registerForContextMenu(list);
        list.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int location,
					long arg3) {
				Data.getInstance().setDetailReceipt(adapter.getItem(location).getReceipt());
	        	Intent detail = new Intent(ReceiptsActivity.this, ReceiptDetailActivity.class);
	        	startActivity(detail);
			}});
        this.updateList();
    }
    
    public void updateList(){
    	long total = 0;
    	this.adapter.clear();
		for(Receipt r : Data.getInstance().getReceiptSet()){
			if(r.getTime().year == this.year && r.getTime().month + 1 == this.month){
				this.adapter.add(new WrappedReceipt(r));
				total += r.getTotal();
			}
		}
    	this.adapter.notifyDataSetChanged();
    	totalView.setText(String.format("合計 ¥%d", total));
    }
    
    @Override
    protected void onResume(){
    	super.onResume();
  	  	this.updateList();
    }
    
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo){
      super.onCreateContextMenu(menu, view, menuInfo);
      AdapterContextMenuInfo adapterinfo = (AdapterContextMenuInfo)menuInfo;
      ListView listView = (ListView)view;
      menu.setHeaderTitle((String)listView.getItemAtPosition(adapterinfo.position).toString());
      menu.add(0, 0, 0, "削除");
    }
 
    public boolean onContextItemSelected(MenuItem item){
      AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
      ReceiptSet cs = Data.getInstance().getReceiptSet();
      Receipt ec = cs.getByIndex(info.position);
      switch(item.getItemId()){
      case 0:
    	  Data.getInstance().getReceiptSet().remove(ec);
    	  adapter.remove(new WrappedReceipt(ec));
    	  adapter.notifyDataSetChanged();
    	  this.invalidateOptionsMenu();
    	  return true;
      }
      return false;
    }
}
