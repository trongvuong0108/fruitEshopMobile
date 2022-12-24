package com.code.sanpham.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.code.lib.Repository.Methods;
import com.code.lib.retrofitClient;
import com.example.sanpham.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignInFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    TextView username, name, phone , email,address;
    EditText password;
    private View result;
    public SignInFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DangKiFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignInFragment newInstance(String param1, String param2) {
        SignInFragment fragment = new SignInFragment();
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
        result = inflater.inflate(R.layout.fragment_dangki, container, false);;
        anhxa();
        Button btn = result.findViewById(R.id.dangki);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                solve();
            }
        });
        // Inflate the layout for this fragment
        return result ;
    }

    public void anhxa(){
        username = result.findViewById(R.id.username);
        password = result.findViewById(R.id.password);
        name = result.findViewById(R.id.fullname);
        phone = result.findViewById(R.id.phone);
        address = result.findViewById(R.id.address);
        email = result.findViewById(R.id.email);
    }

    public void solve() {
        Methods methods = retrofitClient.getRetrofit().create(Methods.class);
        System.out.println(password.getText().toString().trim());
        Call<String> call = methods.signup(
                username.getText().toString(),
                password.getText().toString(),
                name.getText().toString(),
                email.getText().toString(),
                address.getText().toString(),
                phone.getText().toString()
                );
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                    Fragment fragment = new ConfimFragment(username.getText().toString());
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentContainerView, fragment);
                    fragmentTransaction.addToBackStack("Fragment home");
                    fragmentTransaction.commit();
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }
}