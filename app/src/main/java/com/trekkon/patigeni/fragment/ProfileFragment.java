package com.trekkon.patigeni.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.trekkon.patigeni.R;
import com.trekkon.patigeni.activities.MainActivity;
import com.trekkon.patigeni.controller.BaseResult;
import com.trekkon.patigeni.controller.updateuser.UpdateUserController;
import com.trekkon.patigeni.helper.BaseFunction;
import com.trekkon.patigeni.model.User;
import com.trekkon.patigeni.utils.SessionManagement;

import java.util.HashMap;

/**
 * Created by Iwan on 18-Aug-17.
 */

public class ProfileFragment extends Fragment implements MainActivity.OnDataPassedListener, BaseResult {
    EditText txtName, txtEmail, txtOldPassword, txtConfirmPassword;
    TextView txtCompany;
    String userName, companyName, email, oldPassword;
    UpdateUserController updateUserController;
    Button logoutButton, saveButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);


        txtCompany = (TextView)rootView.findViewById(R.id.txtCompany);
        txtName = (EditText)rootView.findViewById(R.id.txtName);
        txtEmail = (EditText)rootView.findViewById(R.id.txtEmail);
        txtOldPassword = (EditText)rootView.findViewById(R.id.txtOldPassword);
        txtConfirmPassword = (EditText)rootView.findViewById(R.id.txtConfirmPassword);


        SessionManagement sessionManagement = new SessionManagement(getActivity());
        HashMap<String, String> profil = new HashMap<>();
        profil=sessionManagement.getUserDetails();


        //txtCompany.setText(companyName);
        txtName.setText(profil.get("name"));
        txtEmail.setText(profil.get("email"));
        txtOldPassword.setText(profil.get("password"));
        txtConfirmPassword.setText(profil.get("password"));

        txtName.setEnabled(true);
        txtEmail.setEnabled(true);
        txtOldPassword.setEnabled(true);
        txtConfirmPassword.setEnabled(true);



        return rootView;
    }

    @Override
    public void onDataPassed(User user) {
        companyName = user.getCompanyName();
        userName = user.getName();
        email = user.getEmail();
        oldPassword = user.getPassword();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        BaseFunction.PeriksaGPS(getActivity());

        logoutButton=(Button)getActivity().findViewById(R.id.logoutButton);
        saveButton=(Button)getActivity().findViewById(R.id.saveButton);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BaseFunction.Logout(getActivity());
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateUser();
            }
        });
    }

    void UpdateUser(){
        updateUserController = new UpdateUserController(this,getActivity());
        updateUserController.updateUser(BaseFunction.GetUserID(getActivity()),txtName.getText().toString(),txtOldPassword.getText().toString(),txtEmail.getText().toString());
    }
    @Override
    public void showValidationError() {
        Toast.makeText(getActivity(),"Input tidak valid",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess() {
        Toast.makeText(getActivity(),"User telah diupdate",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(String errorMessage) {
        Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
    }
}
