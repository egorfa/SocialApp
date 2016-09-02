package com.example.devefalkov.socialapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.vk.sdk.VKSdk;

import java.util.Locale;


public class SocialFragment extends Fragment {
    //VK
     private String[] scopes = {};

    //FB
    public CallbackManager callbackManager;
    private ProfileTracker mProfileTracker;
    private String _fbUserId;
    private String _vkUserId;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public SocialFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SocialFragment newInstance(String param1, String param2) {
        SocialFragment fragment = new SocialFragment();
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

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().logOut();

        ShowToastIfValidate();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btn = (Button) view.findViewById(R.id.btn_vk);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VKSdk.login(getActivity(), scopes);
            }
        });

        LoginButton loginButton = (LoginButton) view.findViewById(R.id.btn_fb);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("SOCIALAPP : ", "FACEBOOK SUCCESS LOGIN");
                if(Profile.getCurrentProfile() == null) {
                    mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                            // profile2 is the new profile
                            Log.v("facebook - profile", profile2.getFirstName());
                            _fbUserId = profile2.getId();
                            ShowToastIfValidate();
                            mProfileTracker.stopTracking();
                        }
                    };
                    // no need to call startTracking() on mProfileTracker
                    // because it is called by its constructor, internally.
                }
                else {
                    Profile profile = Profile.getCurrentProfile();
                    _fbUserId = profile.getId();
                    Log.v("facebook - profile", profile.getFirstName());
                }
                ShowToastIfValidate();
            }
            @Override
            public void onCancel() {
                Log.d("SOCIALAPP : ", "FACEBOOK CANCELED LOGIN");
                if(Profile.getCurrentProfile() != null) {
                    Profile profile = Profile.getCurrentProfile();
                    _fbUserId = profile.getId();
                }
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("SOCIALAPP : ", "FACEBOOK FAILED LOGIN");
            }
        });

    }

    private void ShowToastIfValidate() {
        if (_vkUserId != null && !_vkUserId.isEmpty()
                && _fbUserId != null && !_fbUserId.isEmpty())
            Snackbar.make(getView(), String.format(Locale.ENGLISH, "FB_ID = %s \n VK_ID = %s",  _fbUserId, _vkUserId), Snackbar.LENGTH_INDEFINITE)
                    .show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void set_vkUserId(String id) {
        _vkUserId = id;
        ShowToastIfValidate();
    }

}
