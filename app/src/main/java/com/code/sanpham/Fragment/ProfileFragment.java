package com.code.sanpham.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.code.lib.Model.userResponse;
import com.code.lib.Repository.Methods;
import com.code.lib.retrofitClient;
import com.code.sanpham.MainActivity;
import com.example.sanpham.R;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import gun0912.tedbottompicker.TedBottomPicker;
import gun0912.tedbottompicker.TedBottomSheetDialogFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    View view;
    CircleImageView circleImageView;

    private userResponse user;

    public ProfileFragment() {
    }

    public ProfileFragment(userResponse userResponse) {
        this.user = userResponse;
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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

    public class views{
        ImageView imageView;
        TextView ten;
        TextView sdt;
        TextView username;
        TextView address;
        Button btn_chonHinh;
        Button btn_save;
        Button btn_dangxuat;
        Button btn_deal;
    }

    public void mapping(views views){
        views.imageView = view.findViewById(R.id.profile_imageView);
        views.ten  = view.findViewById(R.id.profile_ten);
        views.username = view.findViewById(R.id.profile_Username);
        views.sdt  = view.findViewById(R.id.profile_sdt);
        views.address  = view.findViewById(R.id.profile_Address);
        views.btn_chonHinh = view.findViewById(R.id.btn_chonHinh);
        views.btn_save = view.findViewById(R.id.btn_save);
        views.btn_dangxuat = view.findViewById(R.id.btn_dangxuat);
        views.btn_deal= view.findViewById(R.id.btn_deal);
    }

    public void saveInfo(views views){
        views.btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateGoogleProfile(views);
            }
        });
    }

    public void UpdateGoogleProfile(@NonNull views views) {
        Methods methods = retrofitClient.getRetrofit().create(Methods.class);
        Call<?> call = methods.googleUpdate(user.getEmail(),views.sdt.getText().toString(),views.address.getText().toString());
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                ShowMessage();
                user.setAddress(views.address.getText().toString());
                user.setPhone(views.sdt.getText().toString());
            }

            @Override
            public void onFailure(Call call, Throwable t) {
            }
        });
    }

    public void SignOut(views views){
        views.btn_dangxuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) getActivity();
                activity.setTaiKhoan(null);
                activity.token = null;
                signOutTransform();
            }
        });
    }

    public void signOutTransform(){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment fragment = new LogInNavFragment();
        fragmentTransaction.replace(R.id.fragmentContainerView,fragment);
        fragmentTransaction.addToBackStack("Fragment home");
        fragmentTransaction.commit();
    }

    public void setData(views views){
        views.ten.setText(user.getFullname());
        views.sdt.setText(user.getPhone());
        views.address.setText(user.getAddress());
        views.username.setText(user.getEmail());
    }

    public void chooseImg(views views){
        views.btn_chonHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                XinQuyen();
            }
            public void Chonhinh() {
                // Show va load hinh trong thu vien
                TedBottomPicker.with(getActivity())
                        .show(new TedBottomSheetDialogFragment.OnImageSelectedListener() {
                            @Override
                            public void onImageSelected(Uri uri) {
                                Bitmap bitmap = null;
                                try {
                                    bitmap = MediaStore.Images.Media.getBitmap(view.getContext().getContentResolver(), uri);
                                    views.imageView.setImageBitmap(bitmap);

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            }
            private void XinQuyen() {
                // xin quyen thu vien anh
                PermissionListener permissionlistener = new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        Chonhinh();
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        Toast.makeText(view.getContext(), "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                    }
                };
                TedPermission.create()
                        .setPermissionListener(permissionlistener)
                        .setDeniedMessage("Bạn phải cho phép ứng dụng sử dụng các quyền")
                        .setPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                        .check();
            }
        });
    }

    public void ShowMessage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Hello");
        builder.setTitle("Bạn đã cập nhật thành công");
        builder.setPositiveButton("OK", null);
        builder.setCancelable(true);
        builder.create().show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.profile_fragment, container, false);
        views views = new views();
        mapping(views);
        SignOut(views);
        chooseImg(views);
        setData(views);
        saveInfo(views);
        ShowBill(views);
        return view;
    }

    public void ShowBill(views views){
        views.btn_deal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment fragment = new NonUserProfileFragment();
                fragmentTransaction.replace(R.id.fragmentContainerView,new InvoiceFragment());
                fragmentTransaction.addToBackStack("Fragment home");
                fragmentTransaction.commit();
            }
        });
    }

}