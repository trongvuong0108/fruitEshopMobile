package com.code.sanpham.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.code.lib.Interface.IChuyenData;
import com.code.lib.Model.category;
import com.code.lib.Model.product;
import com.code.lib.Model.userResponse;
import com.code.lib.Repository.Methods;
import com.code.lib.retrofitClient;
import com.code.sanpham.Adapter.CategoryApdapter;
import com.code.sanpham.Adapter.ProductAdapter;
import com.example.sanpham.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProductFragment extends Fragment  {
    public ProductFragment(userResponse userInfo){
        this.userInfo = userInfo;
    }

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    IChuyenData iChuyenData;
    ListView listView;
    List<product> listProduct;
    RecyclerView recyclerView;
    ImageButton imageButton,btn_find;
    userResponse userInfo;
    TextView txt_find;
    Methods methods;

    private String mParam1;
    private String mParam2;

    public ProductFragment() {
    }


    public static ProductFragment newInstance(String param1, String param2) {
        ProductFragment fragment = new ProductFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    View result;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        iChuyenData = (IChuyenData) getActivity();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        result = inflater.inflate(R.layout.fragment_product, container, false);
        methods = retrofitClient.getRetrofit().create(Methods.class);
        anhXa();
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(
                        getActivity(),
                        RecyclerView.HORIZONTAL,
                        false
                );
        recyclerView.setLayoutManager(linearLayoutManager);
        return result;
    }

    private void open_cart() {
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new CartFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainerView,fragment);
                fragmentTransaction.addToBackStack("Fragment home");
                fragmentTransaction.commit();
            }
        });
    }




    public void chuyenData(product product) {
        iChuyenData.ChuyenData(product);
    }

    private void anhXa() {
        listView = result.findViewById(R.id.listview);
        recyclerView = result.findViewById(R.id.list_category);
        imageButton = result.findViewById(R.id.product_btn_cart);
        btn_find = result.findViewById(R.id.button_find);
        txt_find = result.findViewById(R.id.txt_input);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(layoutManager);


        getData();
        find();
        open_cart();
    }

    private void getData(){
        listProduct = new ArrayList<>();
        Call<product[]> call = methods.getProduct();
        call.enqueue(new Callback<product[]>() {
            @Override
            public void onResponse(Call<product[]> call, Response<product[]> response) {
                ProductAdapter productAdapter =
                        new ProductAdapter(
                                getActivity(),
                                R.layout.san_pham,
                                Arrays.asList(response.body())
                        );
                listProduct = Arrays.asList(response.body());
                listView.setAdapter(productAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //call api to get product by id;
                        chuyenData(listProduct.get(position));
                    }
                });
            }

            @Override
            public void onFailure(Call<product[]> call, Throwable t) {

            }
        });

        Call<category[]> callCate = methods.getCate();
        callCate.enqueue(new Callback<category[]>() {
            @Override
            public void onResponse(Call<category[]> call, Response<category[]> response) {
                CategoryApdapter categoryApdapter =
                        new CategoryApdapter(
                                Arrays.asList(response.body()),
                                new CategoryApdapter.OnNoteListener() {
                                    @Override
                                    public void onNoteClick(category category) {
                                        Call<product[]> getByCate = methods.getByCate(category.getName());
                                        getByCate.enqueue(new Callback<product[]>() {
                                            @Override
                                            public void onResponse(Call<product[]> call, Response<product[]> response) {
                                                ProductAdapter productAdapter =
                                                        new ProductAdapter(
                                                                getActivity(),
                                                                R.layout.san_pham,
                                                                Arrays.asList(response.body())
                                                        );
                                                listView.setAdapter(productAdapter);
                                            }

                                            @Override
                                            public void onFailure(Call<product[]> call, Throwable t) {

                                            }
                                        });
                                    }
                                });
                recyclerView.setAdapter(categoryApdapter);
            }

            @Override
            public void onFailure(Call<category[]> call, Throwable t) {

            }
        });

    }

    private void find(){
        listProduct = new ArrayList<>();
        btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<product[]> call = methods.find(txt_find.getText().toString().trim());
                call.enqueue(new Callback<product[]>() {
                    @Override
                    public void onResponse(Call<product[]> call, Response<product[]> response) {
                        if(response.body() != null ){
                            ShowMessage("Tìm thấy", "Kết quả","Ok");
                            ProductAdapter productAdapter =
                                    new ProductAdapter(
                                            getActivity(),
                                            R.layout.san_pham,
                                            Arrays.asList(response.body())
                                    );
                            listProduct = Arrays.asList(response.body());
                            listView.setAdapter(productAdapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    chuyenData(listProduct.get(position));
                                }
                            });
                        }

                        else {
                            ShowMessage("không tìm thấy", "Kết quả","Ok");
                        }

                    }

                    @Override
                    public void onFailure(Call<product[]> call, Throwable t) {

                    }
                });
            }
        });
    }

    public void ShowMessage(String message, String title, String text){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setPositiveButton(text, null);
        builder.setCancelable(true);
        builder.create().show();
    }
}