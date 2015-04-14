package jp.eagan.gotaku.widget;

import java.text.DecimalFormat;
import java.util.List;

import jp.eagan.gotaku.QuizCategory;
import jp.eagan.gotaku.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CategoryAdapter extends ArrayAdapter<QuizCategory> {
	
	private LayoutInflater inflater;
	private DecimalFormat formatSize;
	
	/**
	 * 
	 * @param context
	 * @param viewResourceId
	 * @param items
	 */
	public CategoryAdapter(Context context, int viewResourceId, List<QuizCategory> items) {
		super(context, viewResourceId, items);
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.formatSize = new DecimalFormat("###0");
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			// null Ç»ÇÁêVãKçÏê¨
			//view = inflater.inflate(R.layout.category_list_row, parent);
			view = this.inflater.inflate(R.layout.category_list_row, null);
		}
		QuizCategory item = getItem(position);
		
		TextView categoryNameView = (TextView) view.findViewById(R.id.category_name);
		categoryNameView.setText(item.getName());
		TextView categorySizeView = (TextView) view.findViewById(R.id.category_size);
		categorySizeView.setText(formatSize.format(item.getQuizNum()));
		
		return view;
	}
}
