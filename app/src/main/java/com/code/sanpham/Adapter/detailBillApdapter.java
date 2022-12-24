package com.code.sanpham.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.code.lib.Model.detail_BillModel;
import com.example.sanpham.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class detailBillApdapter extends BaseAdapter {
    private Context context;
    private int layOut;
    private List<detail_BillModel> detailList;

    public detailBillApdapter(Context context, int layOut, List<detail_BillModel> detailList) {
        this.context = context;
        this.layOut = layOut;
        this.detailList = detailList;
    }

    @Override
    public int getCount() {
        return detailList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layOut, null);
            viewHolder.Img = (ImageView) convertView.findViewById(R.id.img_sanpham);
            viewHolder.gia = (TextView) convertView.findViewById(R.id.gia_sanpham);
            viewHolder.Name = (TextView) convertView.findViewById(R.id.ten_sanpham);
            viewHolder.MoTa = (TextView) convertView.findViewById(R.id.mota_sanpham);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        detail_BillModel detail_BillModel = detailList.get(position);
        viewHolder.gia.setText(Integer.toString( (int) detail_BillModel.getProductModel().getPrice_out())+" VNĐ");
        viewHolder.MoTa.setText("Số lượng: " + Float.toString(detail_BillModel.getQuality())+ " kg");
        viewHolder.Name.setText(detail_BillModel.getProductModel().getName());
        Picasso.get().load(detail_BillModel.getProductModel().getImg()).into(viewHolder.Img);
        return convertView;
    }

    private static class ViewHolder {
        TextView Name;
        ImageView Img;
        TextView MoTa;
        TextView gia;
    }

}
