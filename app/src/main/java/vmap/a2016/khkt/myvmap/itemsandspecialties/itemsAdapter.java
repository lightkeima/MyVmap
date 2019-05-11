package vmap.a2016.khkt.myvmap.itemsandspecialties;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import vmap.a2016.khkt.myvmap.R;

public class itemsAdapter extends BaseAdapter {
    private List<items> listData;
    private LayoutInflater layoutInflater;
    private Context context;
    public  itemsAdapter(Context aContext, List<items> listData){
        this.context = aContext;
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount(){
        return  listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.items,null);
            holder = new ViewHolder();
            holder.itemNameView = (TextView) convertView.findViewById(R.id.itemNameTxt);
            holder.itemPriceView = (TextView) convertView.findViewById(R.id.priceTxt);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        items item = this.listData.get(position);
        holder.itemNameView.setText(item.getItemName());
        holder.itemPriceView.setText(item.getPrice());
        return convertView;
    }
    static class ViewHolder {
        TextView itemNameView;
        TextView itemPriceView;
    }
}
