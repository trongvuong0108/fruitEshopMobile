package com.code.sanpham.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.code.lib.Model.category;
import com.example.sanpham.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryApdapter extends RecyclerView.Adapter<CategoryApdapter.CategoryViewHolder> {

    List<category> list;

    private CategoryApdapter.OnNoteListener mListener;

    public CategoryApdapter(List<category> list, CategoryApdapter.OnNoteListener mListener) {
        this.list = list;

        this.mListener = mListener;
    }

    public void setList(List<category> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryApdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category,parent,false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryApdapter.CategoryViewHolder holder, int position) {
        category category = list.get(position);
        if(category == null)
            return;
        else
        {
            System.out.println(category.getImg());
            Picasso.get().load(category.getImg()).into(holder.imgHangSX);
            holder.Name.setText(category.getName());
        }
        holder.item_cate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onNoteClick(category);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list != null)
            return list.size();
        return 0;
    }

    public  class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView imgHangSX;
        TextView Name;
        LinearLayout item_cate;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.name);
                    imgHangSX = itemView.findViewById(R.id.img);
            item_cate = itemView.findViewById(R.id.item_cate);
        }
    }

    public interface OnNoteListener{
        void onNoteClick(category position);
    }
}
