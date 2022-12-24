package com.code.sanpham.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.code.lib.Model.cartItem;
import com.example.sanpham.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartProductAdapter extends BaseAdapter {

    private Context context;
    private int layOut;
    private List<cartItem> cartItems;

    public CartProductAdapter(Context context, int layOut, List<cartItem> cartItems) {
        this.context = context;
        this.layOut = layOut;
        this.cartItems = cartItems;
    }

    @Override
    public int getCount() {
        return cartItems.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder {
        TextView Name;
        ImageView Img;
        TextView gia;
        ImageButton Cart_Product_btnTru;
        ImageButton Cart_Product_btnCong;
        ImageButton Cart_Product_remove;
        EditText SL;
    }
    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        cartItem cartItem = cartItems.get(position);
        float count = cartItem.getQuality();
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layOut, null);
            viewHolder.Img = (ImageView) convertView.findViewById(R.id.Cart_Product_IMG);
            viewHolder.gia = (TextView) convertView.findViewById(R.id.Cart_Product_price);
            viewHolder.Name = (TextView) convertView.findViewById(R.id.Cart_Product_Name);
            viewHolder.SL = (EditText) convertView.findViewById(R.id.Cart_Product_SL);
            viewHolder.SL.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    cartItem.setQuality(Float.parseFloat(viewHolder.SL.getText().toString()));
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    cartItem.setQuality(Float.parseFloat(viewHolder.SL.getText().toString()));
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    cartItem.setQuality(Float.parseFloat(viewHolder.SL.getText().toString()));
                }
            });
            viewHolder.Cart_Product_btnTru = convertView.findViewById(R.id.Cart_Product_btnTru);
            viewHolder.Cart_Product_btnCong = convertView.findViewById(R.id.Cart_Product_btnCong);
            viewHolder.Cart_Product_remove = convertView.findViewById(R.id.Cart_Product_remove);
            convertView.setTag(viewHolder);
            Picasso.get().load(cartItem.getProduct().getImg()).into(viewHolder.Img);
            viewHolder.gia.setText(Integer.toString((int) cartItem.getProduct().getPrice_out())+" VNĐ");
            viewHolder.Name.setText(cartItem.getProduct().getName());
            viewHolder.SL.setText(Float.toString(cartItem.getQuality()));

            viewHolder.Cart_Product_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cartItems.remove(cartItem);
                    notifyDataSetChanged();
                }
            });

            viewHolder.Cart_Product_btnCong.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    float count = cartItem.getQuality();
                    count++;
                    viewHolder.SL.setText(String.valueOf(count));
                    cartItem.setQuality(Float.parseFloat(viewHolder.SL.getText().toString()));
                }
            });

            viewHolder.Cart_Product_btnTru.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    float count = cartItem.getQuality();
                    if(count > 0 ) {
                        count-- ;
                        viewHolder.SL.setText(String.valueOf(count));
                        cartItem.setQuality(Float.parseFloat(viewHolder.SL.getText().toString()));
                    }
                }
            });

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }
}