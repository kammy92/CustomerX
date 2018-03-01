package com.resultier.customerx.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.resultier.customerx.R;
import com.resultier.customerx.utils.AppConfigTags;
import com.resultier.customerx.utils.AppConfigURL;
import com.resultier.customerx.utils.Constants;
import com.resultier.customerx.utils.NetworkConnection;
import com.resultier.customerx.utils.SetTypeFace;
import com.resultier.customerx.utils.UserDetailsPref;
import com.resultier.customerx.utils.Utils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    
    protected PowerManager.WakeLock mWakeLock;
    UserDetailsPref userDetailPref;
    ProgressDialog progressDialog;
    
    CoordinatorLayout clMain;
    TextView tvTitle;
    TextView tvQuestion;
    
    ImageView ivSettings;
    LinearLayout llQuestion;
    ProgressBar progressBar;
    
    ImageView iv1;
    ImageView iv2;
    ImageView iv3;
    ImageView iv4;
    ImageView iv5;
    
    int group_id = 0;
    int group_type = 0;
    int survey_id = 0;
    
    int temp_app_group_type = 0;
    
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
        initView ();
        initData ();
        isLogin ();
        initListener ();
        initApplication ();
    }
    
    private void initView () {
        clMain = (CoordinatorLayout) findViewById (R.id.clMain);
        tvTitle = (TextView) findViewById (R.id.tvTitle);
        tvQuestion = (TextView) findViewById (R.id.tvQuestion);
        ivSettings = (ImageView) findViewById (R.id.ivSettings);
        llQuestion = (LinearLayout) findViewById (R.id.llQuestion);
        progressBar = (ProgressBar) findViewById (R.id.progressBar);
        iv1 = (ImageView) findViewById (R.id.iv1);
        iv2 = (ImageView) findViewById (R.id.iv2);
        iv3 = (ImageView) findViewById (R.id.iv3);
        iv4 = (ImageView) findViewById (R.id.iv4);
        iv5 = (ImageView) findViewById (R.id.iv5);
    }
    
    private void initData () {
        Utils.setTypefaceToAllViews (this, tvTitle);
        userDetailPref = UserDetailsPref.getInstance ();
    
        tvTitle.setText (userDetailPref.getStringPref (this, UserDetailsPref.USER_NAME));
        
        progressDialog = new ProgressDialog (this);
        
             /* This code together with the one in onDestroy()
         * will make the screen be always on until this Activity gets destroyed. */
        final PowerManager pm = (PowerManager) getSystemService (Context.POWER_SERVICE);
        this.mWakeLock = pm.newWakeLock (PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        this.mWakeLock.acquire ();
    }
    
    private void isLogin () {
        if (userDetailPref.getStringPref (MainActivity.this, UserDetailsPref.USER_LOGIN_KEY).length () == 0) {
            Intent myIntent = new Intent (this, LoginActivity.class);
            startActivity (myIntent);
        }
        if (userDetailPref.getStringPref (MainActivity.this, UserDetailsPref.USER_LOGIN_KEY).length () == 0)
            finish ();
    }
    
    private void initListener () {
        iv1.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                submitResponse (1, survey_id, userDetailPref.getIntPref (MainActivity.this, UserDetailsPref.PARENT_ID), group_id, group_type);
            }
        });
        iv2.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                submitResponse (2, survey_id, userDetailPref.getIntPref (MainActivity.this, UserDetailsPref.PARENT_ID), group_id, group_type);
            }
        });
        iv3.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                submitResponse (3, survey_id, userDetailPref.getIntPref (MainActivity.this, UserDetailsPref.PARENT_ID), group_id, group_type);
            }
        });
        iv4.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                submitResponse (4, survey_id, userDetailPref.getIntPref (MainActivity.this, UserDetailsPref.PARENT_ID), group_id, group_type);
            }
        });
        iv5.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                submitResponse (5, survey_id, userDetailPref.getIntPref (MainActivity.this, UserDetailsPref.PARENT_ID), group_id, group_type);
            }
        });
        
        ivSettings.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                MaterialDialog dialog = new MaterialDialog.Builder (MainActivity.this)
                        .content (getResources ().getString (R.string.dialog_text_enter_password))
                        .contentColor (getResources ().getColor (R.color.primary_text2))
                        .inputType (InputType.TYPE_TEXT_VARIATION_PASSWORD)
                        .typeface (SetTypeFace.getTypeface (MainActivity.this), SetTypeFace.getTypeface (MainActivity.this))
                        .input ("", "", new MaterialDialog.InputCallback () {
                            @Override
                            public void onInput (MaterialDialog dialog, CharSequence input) {
                                if (input.toString ().equalsIgnoreCase (userDetailPref.getStringPref (MainActivity.this, UserDetailsPref.USER_PASSWORD))) {
                                    final MaterialDialog dialog2 = new MaterialDialog.Builder (MainActivity.this)
                                            .title ("Settings")
                                            .customView (R.layout.dialog, true)
                                            .positiveText ("OK")
                                            .neutralText ("LOGOUT")
                                            .negativeText ("CANCEL")
                                            .onPositive (new MaterialDialog.SingleButtonCallback () {
                                                @Override
                                                public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                    userDetailPref.putIntPref (MainActivity.this, UserDetailsPref.APP_GROUP_TYPE, temp_app_group_type);
                                                    llQuestion.setVisibility (View.GONE);
                                                    progressBar.setVisibility (View.VISIBLE);
                                                    getAppQuestion ();
                                                }
                                            })
                                            .onNeutral (new MaterialDialog.SingleButtonCallback () {
                                                @Override
                                                public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                    userDetailPref.putStringPref (MainActivity.this, UserDetailsPref.USER_NAME, "");
                                                    userDetailPref.putStringPref (MainActivity.this, UserDetailsPref.USER_EMAIL, "");
                                                    userDetailPref.putStringPref (MainActivity.this, UserDetailsPref.USER_CONTACT, "");
                                                    userDetailPref.putIntPref (MainActivity.this, UserDetailsPref.APP_GROUP_TYPE, 0);
                                                    userDetailPref.putIntPref (MainActivity.this, UserDetailsPref.PARENT_ID, 0);
                                                    userDetailPref.putStringPref (MainActivity.this, UserDetailsPref.USER_PASSWORD, "");
                                                    userDetailPref.putStringPref (MainActivity.this, UserDetailsPref.USER_LOGIN_KEY, "");
                    
                                                    Intent intent = new Intent (MainActivity.this, LoginActivity.class);
                                                    intent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity (intent);
                                                    overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
                    
                                                }
                                            })
                                            .build ();
                                    final TextView tvDiscoveryMode = (TextView) dialog2.getCustomView ().findViewById (R.id.tvDiscoveryMode);
                                    final TextView tvBaselineMode = (TextView) dialog2.getCustomView ().findViewById (R.id.tvBaselineMode);
    
                                    //0=> discovery, 1=> base
                                    switch (userDetailPref.getIntPref (MainActivity.this, UserDetailsPref.APP_GROUP_TYPE)) {
                                        case 0:
                                            temp_app_group_type = 0;
                                            tvDiscoveryMode.setBackgroundResource (R.drawable.button_filled);
                                            tvBaselineMode.setBackgroundResource (R.drawable.button_hollow);
                                            tvDiscoveryMode.setTextColor (getResources ().getColor (R.color.text_color_white));
                                            tvBaselineMode.setTextColor (getResources ().getColor (R.color.primary_text2));
                                            break;
                                        case 1:
                                            temp_app_group_type = 1;
                                            tvDiscoveryMode.setBackgroundResource (R.drawable.button_hollow);
                                            tvBaselineMode.setBackgroundResource (R.drawable.button_filled);
                                            tvDiscoveryMode.setTextColor (getResources ().getColor (R.color.primary_text2));
                                            tvBaselineMode.setTextColor (getResources ().getColor (R.color.text_color_white));
                                            break;
                                    }
    
                                    tvDiscoveryMode.setOnClickListener (new View.OnClickListener () {
                                        @Override
                                        public void onClick (View view) {
                                            temp_app_group_type = 0;
                                            tvDiscoveryMode.setBackgroundResource (R.drawable.button_filled);
                                            tvBaselineMode.setBackgroundResource (R.drawable.button_hollow);
                                            tvDiscoveryMode.setTextColor (getResources ().getColor (R.color.text_color_white));
                                            tvBaselineMode.setTextColor (getResources ().getColor (R.color.primary_text2));
                                        }
                                    });
    
                                    tvBaselineMode.setOnClickListener (new View.OnClickListener () {
                                        @Override
                                        public void onClick (View view) {
                                            temp_app_group_type = 1;
                                            tvDiscoveryMode.setBackgroundResource (R.drawable.button_hollow);
                                            tvBaselineMode.setBackgroundResource (R.drawable.button_filled);
                                            tvDiscoveryMode.setTextColor (getResources ().getColor (R.color.primary_text2));
                                            tvBaselineMode.setTextColor (getResources ().getColor (R.color.text_color_white));
                                        }
                                    });
                                    Utils.setTypefaceToAllViews (MainActivity.this, tvDiscoveryMode);
                                    dialog2.show ();
                                } else {
                                    Utils.showSnackBar (MainActivity.this, clMain, "Incorrect Password", Snackbar.LENGTH_SHORT, null, null);
                                }
                                dialog.dismiss ();
                            }
                        }).build ();

//        if (config.smallestScreenWidthDp >= 600 && config.smallestScreenWidthDp <= 720) {
//            dialog.getInputEditText ().setTextSize (getResources ().getDimension (R.dimen.text_size_medium));
//            dialog.getActionButton (DialogAction.POSITIVE).setTextSize (getResources ().getDimension (R.dimen.text_size_medium));
//            dialog.getContentView ().setTextSize (getResources ().getDimension (R.dimen.text_size_medium));
//        } else {
                // fall-back code goes here
//        }
                
                
                dialog.show ();
            }
        });
    }
    
    private void initApplication () {
        if (NetworkConnection.isNetworkAvailable (this)) {
//            Utils.showProgressDialog (progressDialog, getResources ().getString (R.string.progress_dialog_text_initializing), false);
            Utils.showLog (Log.INFO, AppConfigTags.URL, AppConfigURL.URL_INIT, true);
            StringRequest strRequest = new StringRequest (Request.Method.POST, AppConfigURL.URL_INIT,
                    new Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject (response);
                                    boolean error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    if (! error) {
                                        userDetailPref.putIntPref (MainActivity.this, UserDetailsPref.APP_GROUP_TYPE, jsonObj.getInt (AppConfigTags.APP_GROUP_TYPE));
                                        getAppQuestion ();
                                    }
                                    progressDialog.dismiss ();
                                } catch (Exception e) {
                                    progressDialog.dismiss ();
                                    e.printStackTrace ();
                                }
                            } else {
                                progressDialog.dismiss ();
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                        }
                    },
                    new Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            progressDialog.dismiss ();
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<> ();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    params.put (AppConfigTags.HEADER_USER_LOGIN_KEY, userDetailPref.getStringPref (MainActivity.this, UserDetailsPref.USER_LOGIN_KEY));
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            strRequest.setRetryPolicy (new DefaultRetryPolicy (DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Utils.sendRequest (strRequest, 30);
        } else {
            progressDialog.dismiss ();
//            initApplication ();
        }
    }
    
    private void getAppQuestion () {
        if (NetworkConnection.isNetworkAvailable (this)) {
//            Utils.showProgressDialog (progressDialog, getResources ().getString (R.string.progress_dialog_text_initializing), false);
            Utils.showLog (Log.INFO, AppConfigTags.URL, AppConfigURL.URL_GET_QUESTION, true);
            StringRequest strRequest = new StringRequest (Request.Method.POST, AppConfigURL.URL_GET_QUESTION,
                    new Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject (response);
                                    boolean error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    if (! error) {
                                        llQuestion.setVisibility (View.VISIBLE);
    
                                        tvQuestion.setText (jsonObj.getString (AppConfigTags.QUESTION));
                                        group_id = jsonObj.getInt (AppConfigTags.GROUP_ID);
                                        group_type = jsonObj.getInt (AppConfigTags.GROUP_TYPE);
                                        survey_id = jsonObj.getInt (AppConfigTags.SURVEY_ID);
    
                                        userDetailPref.putIntPref (MainActivity.this, UserDetailsPref.APP_GROUP_TYPE, jsonObj.getInt (AppConfigTags.APP_GROUP_TYPE));
    
                                        progressBar.setVisibility (View.GONE);
                                        llQuestion.setVisibility (View.VISIBLE);
                                        ivSettings.setVisibility (View.VISIBLE);
                                    }
                                    progressDialog.dismiss ();
                                } catch (Exception e) {
                                    progressDialog.dismiss ();
                                    e.printStackTrace ();
                                }
                            } else {
                                progressDialog.dismiss ();
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                        }
                    },
                    new Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            progressDialog.dismiss ();
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String> ();
                    params.put (AppConfigTags.APP_GROUP_TYPE, String.valueOf (userDetailPref.getIntPref (MainActivity.this, UserDetailsPref.APP_GROUP_TYPE)));
                    Utils.showLog (Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }
    
                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<> ();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    params.put (AppConfigTags.HEADER_USER_LOGIN_KEY, userDetailPref.getStringPref (MainActivity.this, UserDetailsPref.USER_LOGIN_KEY));
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            strRequest.setRetryPolicy (new DefaultRetryPolicy (DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Utils.sendRequest (strRequest, 30);
        } else {
            progressDialog.dismiss ();
        }
    }
    
    private void submitResponse (final int response, final int survey_id, final int parent_id, final int group_id, final int group_type) {
        if (NetworkConnection.isNetworkAvailable (this)) {
//            Utils.showProgressDialog (progressDialog, getResources ().getString (R.string.progress_dialog_text_submitting_responses), false);
            llQuestion.setVisibility (View.GONE);
            progressBar.setVisibility (View.VISIBLE);
            Utils.showLog (Log.INFO, AppConfigTags.URL, AppConfigURL.URL_SUBMIT_RESPONSE, true);
            StringRequest strRequest = new StringRequest (Request.Method.POST, AppConfigURL.URL_SUBMIT_RESPONSE,
                    new Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject (response);
                                    boolean error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    if (! error) {
                                        getAppQuestion ();
    
    
                                        final Dialog dialog = new Dialog (MainActivity.this);
                                        dialog.requestWindowFeature (Window.FEATURE_NO_TITLE);
                                        dialog.setCancelable (true);
                                        dialog.getWindow ().setBackgroundDrawable (new ColorDrawable (android.graphics.Color.TRANSPARENT));
                                        dialog.setContentView (R.layout.dialog_thank_you);
                                        dialog.show ();
                                        
                                        Utils.setTypefaceToAllViews (MainActivity.this, dialog.findViewById (R.id.tv1));
    
                                        new Handler ().postDelayed (new Runnable () {
                                            @Override
                                            public void run () {
                                                dialog.dismiss ();
                                            }
                                        }, 15000);
                                        
                                    }
//                                    progressDialog.dismiss ();
                                } catch (Exception e) {
//                                    progressDialog.dismiss ();
                                    e.printStackTrace ();
                                }
                            } else {
//                                progressDialog.dismiss ();
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                        }
                    },
                    new Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
//                            progressDialog.dismiss ();
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String> ();
                    params.put (AppConfigTags.SURVEY_ID, String.valueOf (survey_id));
                    params.put (AppConfigTags.PARENT_ID, String.valueOf (parent_id));
                    params.put (AppConfigTags.GROUP_ID, String.valueOf (group_id));
                    params.put (AppConfigTags.RESPONSE, String.valueOf (response));
                    params.put (AppConfigTags.GROUP_TYPE, String.valueOf (group_type));
                    Utils.showLog (Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }
                
                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<> ();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    params.put (AppConfigTags.HEADER_USER_LOGIN_KEY, userDetailPref.getStringPref (MainActivity.this, UserDetailsPref.USER_LOGIN_KEY));
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            strRequest.setRetryPolicy (new DefaultRetryPolicy (DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Utils.sendRequest (strRequest, 30);
        } else {
//            progressDialog.dismiss ();
//            initApplication ();
        }
    }
    
}
