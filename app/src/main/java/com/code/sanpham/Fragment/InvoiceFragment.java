package com.code.sanpham.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.code.lib.Interface.IChuyenData;
import com.code.lib.Model.Invoice;
import com.code.lib.Repository.Methods;
import com.code.lib.retrofitClient;
import com.code.sanpham.Adapter.InvoiceAdapter;
import com.code.sanpham.MainActivity;
import com.example.sanpham.R;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InvoiceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InvoiceFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView List_invoice ;
    View view ;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    IChuyenData iChuyenData;

    public InvoiceFragment() {
        // Required empty public constructor
    }


    public static InvoiceFragment newInstance(String param1, String param2) {
        InvoiceFragment fragment = new InvoiceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        iChuyenData = (IChuyenData) getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_invoice, container, false);
        List_invoice = view.findViewById(R.id.List_invoice);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(),2);
        List_invoice.setLayoutManager(gridLayoutManager);
        MainActivity mainActivity = (MainActivity) getActivity();
        Methods methods = retrofitClient.getRetrofit().create(Methods.class);
        Call<Invoice[]> call = methods.getBillByEmail(mainActivity.getTaiKhoan().getFullname(),mainActivity.getTaiKhoan().getEmail());
        call.enqueue(new Callback<Invoice[]>() {
            @Override
            public void onResponse(Call<Invoice[]> call, Response<Invoice[]> response) {
                InvoiceAdapter InvoiceAdapter = new InvoiceAdapter(Arrays.asList(response.body()), (MainActivity) getActivity());
                List_invoice.setAdapter(InvoiceAdapter);
            }

            @Override
            public void onFailure(Call<Invoice[]> call, Throwable t) {

            }
        });

        return view;
    }


}