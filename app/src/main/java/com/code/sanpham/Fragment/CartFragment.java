package com.code.sanpham.Fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.code.lib.Interface.IChuyenData;
import com.code.lib.Model.bill_model;
import com.code.lib.Model.cartItem;
import com.code.lib.Model.detail_BillModel;
import com.code.lib.Model.userResponse;
import com.code.lib.Repository.Methods;
import com.code.lib.retrofitClient;
import com.code.sanpham.Adapter.CartProductAdapter;
import com.code.sanpham.MainActivity;
import com.example.sanpham.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CartFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View view;
    ImageButton btn_back_product;
    ListView cart_list;
    private  int TongTien;
    TextView TongTien1;
    Button button,btn_update;
    public List<cartItem> cartItems ;
    IChuyenData iChuyenData;

    public CartFragment() {
        // Required empty public constructor
    }



    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cart, container, false);
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.ChipNavigationBar.setItemSelected(R.id.menu_nav_buy,true);
        AnhXa();
        InitList();
        cartItems = mainActivity.cartItems;
        CartProductAdapter adapter =
                new CartProductAdapter(
                        getActivity(),
                        R.layout.cart_product,
                        cartItems
                );
        cart_list.setAdapter(adapter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mainActivity.getTaiKhoan() == null ){
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentContainerView, new NonUserProfileFragment());
                    fragmentTransaction.addToBackStack("Fragment home");
                    fragmentTransaction.commit();
                }
                else if(mainActivity.getTaiKhoan().getAddress()== null || mainActivity.getTaiKhoan().getPhone()== null ){
                    Toast.makeText(getActivity(), "nhập thông tin vào", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Methods methods = retrofitClient.getRetrofit().create(Methods.class);
                    bill_model bill_model = new bill_model();
                    userResponse account = mainActivity.getTaiKhoan();
                    bill_model.setName(account.getFullname());
                    bill_model.setAddress(account.getAddress());
                    bill_model.setPhone(account.getPhone());
                    bill_model.setEmail(account.getEmail());
                    List<detail_BillModel> detail_billModels = new ArrayList<>();
                    for (cartItem cartItem:mainActivity.cartItems) {
                        detail_BillModel detail_billModel = new detail_BillModel(
                                cartItem.getProduct(),
                                cartItem.getQuality());
                        detail_billModels.add(detail_billModel);
                    }
                    bill_model.setDetailList(detail_billModels);
                    Call<String> call = methods.checkout(bill_model);
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            mainActivity.cartItems.clear();
                            CartProductAdapter adapter =
                                    new CartProductAdapter(
                                            getActivity(),
                                            R.layout.cart_product,
                                            mainActivity.cartItems
                                    );
                            cart_list.setAdapter(adapter);
                            ShowMessage();
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(getActivity(), "đặt hàng không thành công", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        return view;
    }


    private void AnhXa() {
        btn_update = view.findViewById(R.id.btn_update);
        btn_back_product= view.findViewById(R.id.btn_back_product);
        TongTien1= view.findViewById(R.id.TongTien);
        cart_list = view.findViewById(R.id.cart_list);
        button = view.findViewById(R.id.checkout);
        btn_back_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });


        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float res = 0 ;
                for (cartItem i: cartItems)
                    res+= i.getProduct().getPrice_out() * i.getQuality();
                TongTien1.setText(String.valueOf(res)+" VNĐ");
            }
        });
    }

    private void InitList() {
        MainActivity mainActivity = (MainActivity) getActivity();
        cartItems = mainActivity.cartItems;
        int res = 0 ;
        for (cartItem i: cartItems)
            res+= i.getProduct().getPrice_out() * i.getQuality();
        TongTien1.setText(String.valueOf(res) +" VNĐ");

    }

    public void ShowMessage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Cám ơn bạn");
        builder.setTitle("Bạn đã đặt hàng thành công");
        builder.setPositiveButton("OK", null);
        builder.setCancelable(true);
        builder.create().show();
    }
}