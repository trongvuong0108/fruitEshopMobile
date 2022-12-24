package com.code.sanpham.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.code.lib.Interface.IChuyenData;
import com.code.lib.Model.cartItem;
import com.code.lib.Model.product;
import com.code.lib.Repository.Methods;
import com.code.lib.retrofitClient;
import com.code.sanpham.Adapter.PhotoAdapter;
import com.code.sanpham.Adapter.ProductAdapter;
import com.code.sanpham.MainActivity;
import com.example.sanpham.R;

import java.util.Arrays;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DetailProductFragment extends Fragment {



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    float count ;
    MainActivity mainActivity;
    View view;
    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private PhotoAdapter photoAdapter;
    private RecyclerView recyclerView;
    product p;

    TextView Name,Price,description;
    ImageButton btn_cart, detail_sl_cong, detail_sl_tru ,btn_back_product;
    EditText detail_sl;
    private Button detail_btnThemSP;
    private Button btnFind;
    private TextView txtFind;
    private ListView list_SP_LienQuan;
    private IChuyenData iChuyenData;



    public static DetailProductFragment newInstance(String param1, String param2) {
        DetailProductFragment fragment = new DetailProductFragment();
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
        Bundle bundle = this.getArguments();
        p = (product) bundle.getSerializable("product");
        count = 0;
        mainActivity = (MainActivity) getActivity();
        view = inflater.inflate(R.layout.fragment_detail, container, false);
        viewPager = view.findViewById(R.id.view_Pager);
        circleIndicator = view.findViewById(R.id.Circle_Indicator);


        anhXa();
        setImgs(p,inflater);
        SetData(p);
        open_cart();
        detail_btnThemSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(p.getQuantity() >= Float.parseFloat(detail_sl.getText().toString()) ){
                    cartItem cartItem = new cartItem();
                    cartItem.setProduct(p);
                    if(count == 0)
                        cartItem.setQuality(Float.parseFloat(detail_sl.getText().toString()));
                    else
                        cartItem.setQuality(count);
                    List<cartItem> cartItems = mainActivity.cartItems;
                    if(cartItems.size() == 0)
                        cartItems.add(cartItem);
                    else {
                        if(alreadyExit(cartItem))
                        {
                            for (int i = 0; i < cartItems.size() ; i++) {
                                cartItem item = cartItems.get(i);
                                if(item.getProduct() == cartItem.getProduct()){
                                    item.setQuality(item.getQuality() + cartItem.getQuality());
                                }
                            }
                        }
                        else
                            cartItems.add(cartItem);
                    }
                    mainActivity.cartItems = cartItems ;
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    CartFragment cartFragment = new CartFragment();
                    fragmentTransaction.replace(R.id.fragmentContainerView, cartFragment);
                    fragmentTransaction.addToBackStack("fragment cart");
                    fragmentTransaction.commit();
                }
                else {
                    ShowMessage();
                }


            }
        });
        return view;

    }

    private void addToCart(){

    }

    public void ShowMessage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Thông báo");
        builder.setTitle("Không đủ số lượng sản phẩm");
        builder.setPositiveButton("OK", null);
        builder.setCancelable(true);
        builder.create().show();
    }

    private boolean alreadyExit(cartItem cartItem){
        MainActivity mainActivity = (MainActivity) getActivity();
        List<cartItem> cartItems = mainActivity.cartItems;
        for (int i = 0; i < cartItems.size() ; i++) {
            cartItem item = cartItems.get(i);
            if(item.getProduct().getName().equals(cartItem.getProduct().getName()))
                return true;
        }
        return false ;
    }

    private void setImgs(product p, LayoutInflater inflater) {
        Methods methods = retrofitClient.getRetrofit().create(Methods.class);
        Call<String[]> call = methods.getImgs(p.getName());
        call.enqueue(new Callback<String[]>() {
            @Override
            public void onResponse(Call<String[]> call, Response<String[]> response) {
                photoAdapter = new PhotoAdapter(inflater.getContext(), Arrays.asList(response.body()));
                viewPager.setAdapter(photoAdapter);
                circleIndicator.setViewPager(viewPager);
                photoAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
            }

            @Override
            public void onFailure(Call<String[]> call, Throwable t) {

            }
        });

    }

    private void xuLy() {
        detail_sl_cong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count ++;
                detail_sl.setText(String.valueOf(count));
            }
        });

        detail_sl_tru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count == 0 )
                {
                    Toast.makeText(getContext(),"Sl > 0",Toast.LENGTH_LONG);
                }
                else{
                    count -- ;
                    detail_sl.setText(String.valueOf(count));
                }
            }
        });


    }

    private void CheckSL() {
        float sl =Float.parseFloat(detail_sl.getText().toString());
        if(sl <0)
        {
        }
    }

    private void open_cart() {
        btn_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentContainerView, new CartFragment());
                    fragmentTransaction.addToBackStack("fragment cart ");
                    fragmentTransaction.commit();
                }
            });
    }

    public void chuyenData(product product) {
        iChuyenData.ChuyenData(product);
    }
    private void loading(){
        Methods methods = retrofitClient.getRetrofit().create(Methods.class);;
        Call<product[]> call = methods.getSameCate(p);
        call.enqueue(new Callback<product[]>() {
            @Override
            public void onResponse(Call<product[]> call, Response<product[]> response) {
                List<product> listProduct = Arrays.asList(response.body());
                ProductAdapter productAdapter =
                        new ProductAdapter(
                                getActivity(),
                                R.layout.san_pham,
                                listProduct
                        );
                list_SP_LienQuan.setAdapter(productAdapter);
                list_SP_LienQuan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        chuyenData(listProduct.get(position));
                    }
                });

            }
            @Override
            public void onFailure(Call<product[]> call, Throwable t) {

            }
        });
    }

    private void anhXa() {
        detail_sl= view.findViewById(R.id.detail_sl);
        detail_sl.setText(String.valueOf(count));
//        recyclerView = view.findViewById(R.id.list_SP_LienQuan);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
//        recyclerView.setLayoutManager(linearLayoutManager);

        btn_cart = view.findViewById(R.id.btn_cart);
        detail_sl_cong = view.findViewById(R.id.detail_sl_cong);
        detail_sl_tru = view.findViewById(R.id.detail_sl_tru);
        btn_back_product = view.findViewById(R.id.btn_back_product);
        detail_btnThemSP = view.findViewById(R.id.detail_btnThemSP);
        list_SP_LienQuan = view.findViewById(R.id.list_SP_LienQuan);

        this.Name = view.findViewById(R.id.detail_ten);
        this.Price = view.findViewById(R.id.detail_gia);
        xuLy();
        loading();
        btn_back_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });


    }

    private void SetData(product product_in) {
        Name.setText(product_in.getName());
        Price.setText(Integer.toString((int) product_in.getPrice_out())+" VNĐ");
    }


}