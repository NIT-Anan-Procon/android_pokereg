package jp.ac.anan_nct.pokereg;

import jp.ac.anan_nct.pokereg.entity.Category;
import jp.ac.anan_nct.pokereg.entity.CategorySet;
import jp.ac.anan_nct.pokereg.entity.Data;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class CategoryActivity extends Activity {
	public class WrappedCategory extends Category{
		public WrappedCategory(Category c){ super(c); }
		@Override
		public String toString(){
			if(Data.getInstance().getCategories().getDefaultCategory().getId() == this.getId()){
				return String.format("%s (デフォルト)", this.getCaption());
			}
			return this.getCaption();
		}
		@Override
		public boolean equals(Object o){
			if(o instanceof WrappedCategory) return ((WrappedCategory)o).getId() == this.getId();
			return false;
		}
		@Override
		public int hashCode(){ return this.hashCode(); }
	}
	
	private ArrayAdapter<WrappedCategory> adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category);

		ListView list = (ListView)findViewById(R.id.categoryListView);
		adapter = new ArrayAdapter<WrappedCategory>(this, android.R.layout.simple_list_item_1);
		adapter.clear();
		for(Category c : Data.getInstance().getCategories()){
			adapter.add(new WrappedCategory(c));
		}
        list.setAdapter(adapter);
        registerForContextMenu(list);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.category, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.addNewCategory) {
			showAddNewCategoryDialog();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	private void showAddNewCategoryDialog(){
        final EditText editView = new EditText(CategoryActivity.this);
        new AlertDialog.Builder(CategoryActivity.this)
            .setIcon(android.R.drawable.ic_dialog_info)
            .setTitle("カテゴリの追加")
            .setView(editView)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	if(!editView.getText().toString().isEmpty()){
                		Category c = new Category(editView.getText().toString());
                		Data.getInstance().getCategories().add(c);
                		adapter.add(new WrappedCategory(c));
                		adapter.notifyDataSetChanged();
                	}
            		getFragmentManager().invalidateOptionsMenu();
                }
            }).setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) { }
            }).show();
	}
	private void showEditCategoryDialog(final Category cat){
        final EditText editView = new EditText(CategoryActivity.this);
        editView.setText(cat.getCaption());
        new AlertDialog.Builder(CategoryActivity.this)
            .setIcon(android.R.drawable.ic_dialog_info)
            .setTitle("名前の変更")
            .setView(editView)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	if(!editView.getText().toString().isEmpty()){
                		cat.setCaption(editView.getText().toString());
                		adapter.notifyDataSetChanged();
                	}
            		getFragmentManager().invalidateOptionsMenu();
                }
            }).setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) { }
            }).show();
	}
	

    //コンテキストメニュー
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo){
      super.onCreateContextMenu(menu, view, menuInfo);
      AdapterContextMenuInfo adapterinfo = (AdapterContextMenuInfo)menuInfo;
      ListView listView = (ListView)view;
      menu.setHeaderTitle((String)listView.getItemAtPosition(adapterinfo.position).toString());
      menu.add(0, 0, 0, "デフォルトに設定");
      menu.add(0, 1, 0, "名前の変更");
      menu.add(0, 2, 0, "削除");
    }
 
    //コンテキストメニュークリック時のリスナ
    public boolean onContextItemSelected(MenuItem item){
      AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
      CategorySet cs = Data.getInstance().getCategories();
	  Category ec = cs.getByIndex(info.position);
      switch(item.getItemId()){
      case 0:
    	  cs.setDefaultCategoryIndex(info.position);
    	  adapter.notifyDataSetChanged();
    	  return true;
      case 1:
    	  showEditCategoryDialog(ec);
    	  adapter.notifyDataSetChanged();
    	  return true;
      case 2:
    	  Data.getInstance().getCategories().remove(ec);
    	  adapter.remove(new WrappedCategory(ec));
    	  adapter.notifyDataSetChanged();
    	  return true;
      }
      return false;
    }
}
