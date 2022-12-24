package com.code.sanpham.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.code.lib.Model.Invoice;
import com.code.lib.Model.detail_BillModel;
import com.code.lib.Repository.Methods;
import com.code.lib.retrofitClient;
import com.code.sanpham.Adapter.detailBillApdapter;
import com.example.sanpham.R;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link invoiceDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class invoiceDetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Invoice bill;

    public invoiceDetailFragment() {
        // Required empty public constructor
    }

    public invoiceDetailFragment(Invoice bill) {
        this.bill = bill;
    }

    public static invoiceDetailFragment newInstance(String param1, String param2) {
        invoiceDetailFragment fragment = new invoiceDetailFragment();
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
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_invoice_detail, container, false);
        ListView listView = view.findViewById(R.id.invoice_products_list);
        ImageButton btn_back = view.findViewById(R.id.btn_back_invoice_list);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        //call api get bill product
        Methods methods = retrofitClient.getRetrofit().create(Methods.class);
        Call<detail_BillModel[]> call = methods.getBillById(bill.getId());
        call.enqueue(new Callback<detail_BillModel[]>() {
            @Override
            public void onResponse(Call<detail_BillModel[]> call, Response<detail_BillModel[]> response) {
                detailBillApdapter detailBillApdapter =
                        new detailBillApdapter(
                                getActivity(),
                                R.layout.san_pham,
                                Arrays.asList(response.body())
                        );
                listView.setAdapter(detailBillApdapter);
            }

            @Override
            public void onFailure(Call<detail_BillModel[]> call, Throwable t) {

            }
        });

        TextView txtDate_invoice = view.findViewById(R.id.Date_invoice);
        txtDate_invoice.setText(bill.getCreateAt());

        TextView TongTien_invoice = view.findViewById(R.id.TongTien_invoice);
        TongTien_invoice.setText(Integer.toString((int) bill.getTotal())+" VNĐ");
        return view;
    }



}