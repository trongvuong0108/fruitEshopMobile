package com.code.sanpham.Fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.code.lib.Interface.IChuyenData;
import com.code.lib.Model.googleAccount;
import com.code.lib.Model.jwt;
import com.code.lib.Model.loginRequest;
import com.code.lib.Model.userResponse;

import com.code.lib.Repository.Methods;
import com.code.lib.retrofitClient;
import com.code.sanpham.MainActivity;
import com.example.sanpham.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LogInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LogInFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button btn_dn ,btn_dnGG;
    private IChuyenData iChuyenData;
    private View view ;
    private EditText dn_UserName;
    private EditText dn_Password;
    public loginRequest loginAccount;
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN =001;
    public LogInFragment() {

    }
    public static LogInFragment newInstance(String param1, String param2) {
        LogInFragment fragment = new LogInFragment();
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
        view =  inflater.inflate(R.layout.fragment_dangnhap, container, false);
        Methods methods = retrofitClient.getRetrofit().create(Methods.class);
        anhXa();
        btn_dn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                loginRequest temp = new loginRequest();
                temp.setUsername(dn_UserName.getText().toString());
                temp.setPassword(dn_Password.getText().toString());
                Call<jwt> call = methods.login(temp);
                call.enqueue(new Callback<jwt>() {
                    @Override
                    public void onResponse(Call<jwt> call, Response<jwt> response) {
                        if(response.body() != null){
                            Call<userResponse> getUser = methods.getUser(response.body().getJwtToken());
                            bundle.putString("jwt",response.body().getJwtToken());
                            intent.putExtras(bundle);
                            startActivity(intent);
                            getActivity().finish();
                            getUser.enqueue(new Callback<userResponse>() {
                                @Override
                                public void onResponse(Call<userResponse> call, Response<userResponse> response) {
                                    startActivity(intent);
                                    bundle.putSerializable("data",response.body());
                                    bundle.putBoolean("isGoogle",false);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    getActivity().finish();
                                }
                                @Override
                                public void onFailure(Call<userResponse> call, Throwable t) {
                                }
                            });
                        }
                        else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setMessage("Tài khoản không hợp lệ");
                            builder.setTitle("Cảnh báo");
                            builder.setPositiveButton("OK", null);
                            builder.setCancelable(true);
                            builder.create().show();
                        }
                    }
                    @Override
                    public void onFailure(Call<jwt> call, Throwable t) {
                        Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(),gso);
        btn_dnGG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.btn_dn2:
                        signIn();
                        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
                        if (acct != null) {
                            Call<googleAccount> call = methods.googleGet(acct.getEmail());
                            call.enqueue(new Callback<googleAccount>() {
                                @Override
                                public void onResponse(Call<googleAccount> call, Response<googleAccount> response) {
                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    userResponse userResponse = new userResponse();
                                    userResponse.setFullname(acct.getDisplayName());
                                    userResponse.setEmail(response.body().getEmail());
                                    userResponse.setAddress(response.body().getAddress());
                                    userResponse.setPhone(response.body().getPhone());
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("data",userResponse);
                                    bundle.putBoolean("isGoogle",true);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    getActivity().finish();
                                }

                                @Override
                                public void onFailure(Call<googleAccount> call, Throwable t) {

                                }
                            });
                        }
                        break;
                    // ...
                }
            }
        });

        return view;
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void anhXa(){
        btn_dn=view.findViewById(R.id.btn_dn);
        btn_dnGG = view.findViewById(R.id.btn_dn2);
        iChuyenData = (IChuyenData) getActivity();
        dn_UserName = view.findViewById(R.id.dn_UserName);
        dn_Password = view.findViewById(R.id.dn_Password);
    }


}