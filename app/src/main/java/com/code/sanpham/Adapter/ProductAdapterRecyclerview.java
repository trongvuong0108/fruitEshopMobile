package com.code.sanpham.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.code.lib.Model.category;
import com.code.lib.Model.product;
import com.example.sanpham.R;

import java.util.List;

public class ProductAdapterRecyclerview extends RecyclerView.Adapter<ProductAdapterRecyclerview.HangDTViewHolder>{

    Context context;
    int layout;
    private List<product> mListener;

    public ProductAdapterRecyclerview( Context context, int layout, List<product> mListener) {
        this.context = context;
        this.layout = layout;
        this.mListener = mListener;
    }

    public void setList(List<product> mListener) {
        this.mListener = mListener;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HangDTViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.san_pham,parent,false);
        return new HangDTViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull HangDTViewHolder holder, int position) {
        product product = mListener.get(position);
        if(product == null)
            return;
        else
        {
            //holder.imgHangSX.setImageResource(hangDT.getBrandImg());
            //Picasso.get().load(product.getImg()).into(holder.Img);
            holder.Name.setText(product.getName());
        }

        holder.itemView.setOnClickListener(view -> {
            mListener.get(position);
        });
    }

    @Override
    public int getItemCount() {
        if(mListener!= null)
            return mListener.size();
        return 0;
    }

    public   class  HangDTViewHolder extends RecyclerView.ViewHolder {
        TextView Name;
        ImageView Img;
        TextView MoTa;
        TextView gia;

        public HangDTViewHolder(@NonNull View itemView) {
            super(itemView);
            Img = (ImageView) itemView.findViewById(R.id.img_sanpham);
            gia = (TextView) itemView.findViewById(R.id.gia_sanpham);
            Name = (TextView) itemView.findViewById(R.id.ten_sanpham);
            MoTa = (TextView) itemView.findViewById(R.id.mota_sanpham);
        }
    }

    public interface OnNoteListener{
        void onNoteClick(category position);
    }
}
