package com.code.sanpham.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.code.sanpham.Adapter.PhotoAdapter;
import com.code.sanpham.MainActivity;
import com.example.sanpham.R;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private PhotoAdapter photoAdapter;
    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view= inflater.inflate(R.layout.fragment_home_1, container, false);
        viewPager = view.findViewById(R.id.viewPager_home);
        circleIndicator = view.findViewById(R.id.CircleIndicator_home);
        List<String> photo = new ArrayList<>();
        photo.add("https://res.cloudinary.com/dzjmvy2ty/image/upload/v1652334021/fruit1_xjkjmj.jpg");
        photo.add("https://res.cloudinary.com/dzjmvy2ty/image/upload/v1652334022/fruit3_e01cie.jpg");
        photo.add("https://res.cloudinary.com/dzjmvy2ty/image/upload/v1652334021/fruit2_owmc7x.jpg");
        photo.add("https://res.cloudinary.com/dzjmvy2ty/image/upload/v1652334022/fruit4_sluqvf.jpg");
        photoAdapter = new PhotoAdapter(inflater.getContext(), photo);
        viewPager.setAdapter(photoAdapter);
        circleIndicator.setViewPager(viewPager);
        photoAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
        Button btn_dkNgay = view.findViewById(R.id.btn_dkNgay);
        btn_dkNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new LogInNavFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainerView, fragment);
                fragmentTransaction.addToBackStack("Fragment home");
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.ChipNavigationBar.setItemSelected(R.id.menu_nav_user,true);
                fragmentTransaction.commit();
            }
        });
        Button btn_chuyen_sanpham = view.findViewById(R.id.btn_chuyen_sanpham);
        btn_chuyen_sanpham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new ProductFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainerView, fragment);
                fragmentTransaction.addToBackStack("Fragment home");
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.ChipNavigationBar.setItemSelected(R.id.menu_nav_buy,true);
                fragmentTransaction.commit();
            }
        });

        ImageButton btn_cart = view.findViewById(R.id.product_btn_cart);
        btn_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new CartFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainerView, fragment);
                fragmentTransaction.addToBackStack("Fragment home");
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.ChipNavigationBar.setItemSelected(R.id.menu_nav_buy,true);
                fragmentTransaction.commit();
            }
        });

//        TextView txt_xinchao = view.findViewById(R.id.txt_xinchao);
//        MainActivity mainActivity = new MainActivity();
//        if(mainActivity.getTaiKhoan() != null) txt_xinchao.setText("Xin chào, "+mainActivity.getTaiKhoan().getUsername());
//        else
//            txt_xinchao.setText("Xin chào");
        return view;
    }

}