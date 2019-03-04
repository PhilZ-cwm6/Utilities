package com.sentaroh.android.Utilities.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.sentaroh.android.Utilities.NotifyEvent;
import com.sentaroh.android.Utilities.ThemeColorList;
import com.sentaroh.android.Utilities.ThemeUtil;

public class MessageDialogAppFragment extends DialogFragment {
    private final static boolean DEBUG_ENABLE=false;
    private final static String APPLICATION_TAG="MessageDialogFragment";

    private Dialog mDialog=null;
    private MessageDialogAppFragment mFragment=null;
    private boolean terminateRequired=true;

    private String mDialogTitleType="", mDialogTitle="",mDialogMsgText="";
    private boolean mDialogTypeNegative=false;

    private NotifyEvent mNotifyEvent=null;

    private ThemeColorList mThemeColorList;

    public static MessageDialogAppFragment newInstance(
            boolean negative, String type, String title, String msgtext) {
        if (DEBUG_ENABLE) Log.v(APPLICATION_TAG,"newInstance sub="+title+", msg="+msgtext);
        MessageDialogAppFragment frag = new MessageDialogAppFragment();
        Bundle bundle = new Bundle();
        if (title!=null) bundle.putString("title", title);
        else bundle.putString("title", "");

        if (msgtext!=null) bundle.putString("msgtext", msgtext);
        else bundle.putString("msgtext", "");

        bundle.putString("type", type);
        bundle.putBoolean("negative", negative);
        frag.setArguments(bundle);
        return frag;
    }
    public void setNotifyEvent(NotifyEvent ntfy) {mNotifyEvent=ntfy;}

    public MessageDialogAppFragment() {
        if (DEBUG_ENABLE) Log.v(APPLICATION_TAG,"Constructor(Default) terminateRequired="+terminateRequired);
    };

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (DEBUG_ENABLE) Log.v(APPLICATION_TAG,"onSaveInstanceState terminateRequired="+terminateRequired);
        if(outState.isEmpty()){
            outState.putBoolean("WORKAROUND_FOR_BUG_19917_KEY", true);
        }
    };

    @Override
    final public void onConfigurationChanged(final Configuration newConfig) {
        // Ignore orientation change to keep activity from restarting
        super.onConfigurationChanged(newConfig);
        if (DEBUG_ENABLE) Log.v(APPLICATION_TAG,"onConfigurationChanged terminateRequired="+terminateRequired);

        reInitViewWidget();
//	    CommonDialog.setDlgBoxSizeCompact(mDialog);
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (DEBUG_ENABLE) Log.v(APPLICATION_TAG,"onCreateView terminateRequired="+terminateRequired);
        View view=super.onCreateView(inflater, container, savedInstanceState);
        CommonDialog.setDlgBoxSizeCompact(mDialog);
        return view;
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (DEBUG_ENABLE) Log.v(APPLICATION_TAG,"onCreate terminateRequired="+terminateRequired);
        if (!terminateRequired) {
            Bundle bd=getArguments();
            setRetainInstance(true);
            mDialogTitleType=bd.getString("type");
            mDialogTitle=bd.getString("title");
            mDialogMsgText=bd.getString("msgtext");
            mDialogTypeNegative=bd.getBoolean("negative");
        }
        mFragment=this;
    }

    @Override
    final public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (DEBUG_ENABLE) Log.v(APPLICATION_TAG,"onActivityCreated terminateRequired="+terminateRequired);
    };
    @Override
    final public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (DEBUG_ENABLE) Log.v(APPLICATION_TAG,"onAttach terminateRequired="+terminateRequired);
    };
    @Override
    final public void onDetach() {
        super.onDetach();
        if (DEBUG_ENABLE) Log.v(APPLICATION_TAG,"onDetach terminateRequired="+terminateRequired);
    };
    @Override
    final public void onStart() {
//		CommonDialog.setDlgBoxSizeCompact(mDialog);
        super.onStart();
        if (DEBUG_ENABLE) Log.v(APPLICATION_TAG,"onStart terminateRequired="+terminateRequired);
        if (terminateRequired) mDialog.cancel();
    };

    @Override
    final public void onStop() {
        super.onStop();
        if (DEBUG_ENABLE) Log.v(APPLICATION_TAG,"onStop terminateRequired="+terminateRequired);
    };

    @Override
    public void onDestroyView() {
        if (DEBUG_ENABLE) Log.v(APPLICATION_TAG,"onDestroyView terminateRequired="+terminateRequired);
        if (getDialog() != null && getRetainInstance())
            getDialog().setDismissMessage(null);
        super.onDestroyView();
    };

    @Override
    public void onCancel(DialogInterface di) {
        if (DEBUG_ENABLE) Log.v(APPLICATION_TAG,"onCancel terminateRequired="+terminateRequired);
//	    super.onCancel(di);
        if (!terminateRequired) {
            Button btnOk = (Button) mDialog.findViewById(com.sentaroh.android.Utilities.R.id.common_dialog_btn_ok);
            Button btnCancel = (Button) mDialog.findViewById(com.sentaroh.android.Utilities.R.id.common_dialog_btn_cancel);
            if (mDialogTypeNegative) btnCancel.performClick();
            else btnOk.performClick();
        }
        super.onCancel(di);
    };

    @Override
    public void onDismiss(DialogInterface di) {
        if (DEBUG_ENABLE) Log.v(APPLICATION_TAG,"onDismiss terminateRequired="+terminateRequired);
        super.onDismiss(di);
    };

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (DEBUG_ENABLE) Log.v(APPLICATION_TAG,"onCreateDialog terminateRequired="+terminateRequired);
        mDialog=new Dialog(getActivity());//, MiscUtil.getAppTheme(getActivity()));
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setCanceledOnTouchOutside(false);

        if (!terminateRequired) {
            mThemeColorList=ThemeUtil.getThemeColorList(getActivity());
            initViewWidget();
        }

        return mDialog;
    };

    private void reInitViewWidget() {
        if (DEBUG_ENABLE) Log.v(APPLICATION_TAG,"reInitViewWidget");
        if (!terminateRequired) {
            Handler hndl=new Handler();
            hndl.post(new Runnable(){
                @Override
                public void run() {
                    mDialog.hide();
//		    		mDialog.getWindow().getCurrentFocus().invalidate();
                    initViewWidget();
                    CommonDialog.setDlgBoxSizeCompact(mDialog);
                    mDialog.onContentChanged();
                    mDialog.show();
                }
            });
        }
    };

    private void initViewWidget() {
        if (DEBUG_ENABLE) Log.v(APPLICATION_TAG,"initViewWidget");

        mDialog.setContentView(com.sentaroh.android.Utilities.R.layout.common_dialog);

        ImageView title_icon=(ImageView)mDialog.findViewById(com.sentaroh.android.Utilities.R.id.common_dialog_icon);
        TextView title=(TextView)mDialog.findViewById(com.sentaroh.android.Utilities.R.id.common_dialog_title);
        LinearLayout title_view=(LinearLayout)mDialog.findViewById(com.sentaroh.android.Utilities.R.id.common_dialog_title_view);
        title_view.setBackgroundColor(mThemeColorList.dialog_title_background_color);
        ScrollView msg_view=(ScrollView)mDialog.findViewById(com.sentaroh.android.Utilities.R.id.common_dialog_msg_view);
        msg_view.setBackgroundColor(mThemeColorList.dialog_msg_background_color);

        LinearLayout btn_view=(LinearLayout)mDialog.findViewById(com.sentaroh.android.Utilities.R.id.common_dialog_btn_view);
        btn_view.setBackgroundColor(mThemeColorList.dialog_msg_background_color);

        if (mDialogTitleType.equals("I")) {
            title_icon.setImageResource(com.sentaroh.android.Utilities.R.drawable.dialog_information);
            title.setTextColor(mThemeColorList.text_color_info);
        } else if (mDialogTitleType.equals("W")) {
            title_icon.setImageResource(com.sentaroh.android.Utilities.R.drawable.dialog_warning);
//			title.setTextColor(Color.YELLOW);
        } else if (mDialogTitleType.equals("E")) {
            title_icon.setImageResource(com.sentaroh.android.Utilities.R.drawable.dialog_error);
//			title.setTextColor(mThemeColorList.text_color_error);
        }
        title.setTextColor(mThemeColorList.text_color_info);
        title.setText(mDialogTitle);
        TextView msg_text=(TextView)mDialog.findViewById(com.sentaroh.android.Utilities.R.id.common_dialog_msg);
        if (mDialogMsgText.equals("")) msg_text.setVisibility(View.GONE);
        else {
            msg_text.setText(mDialogMsgText);
            msg_text.setTextColor(mThemeColorList.text_color_primary);
            msg_text.setBackgroundColor(mThemeColorList.dialog_msg_background_color);
        }

        final Button btnOk = (Button) mDialog.findViewById(com.sentaroh.android.Utilities.R.id.common_dialog_btn_ok);
        final Button btnCancel = (Button) mDialog.findViewById(com.sentaroh.android.Utilities.R.id.common_dialog_btn_cancel);
        if (mDialogTypeNegative) btnCancel.setVisibility(View.VISIBLE);
        else  btnCancel.setVisibility(View.GONE);

        if (Build.VERSION.SDK_INT<=10 && mThemeColorList.theme_is_light) {
            btnOk.setTextColor(mThemeColorList.text_color_info);
            btnCancel.setTextColor(mThemeColorList.text_color_info);
        }

//		CommonDialog.setDlgBoxSizeCompact(mDialog);

        // OKボタンの指定
        btnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//				mDialog.dismiss();
                mFragment.dismissAllowingStateLoss();//.dismiss();
                if (mNotifyEvent!=null) mNotifyEvent.notifyToListener(true,null);
            }
        });
        // CANCELボタンの指定
        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//				mDialog.dismiss();
                mFragment.dismissAllowingStateLoss();//.dismiss();
                if (mNotifyEvent!=null) mNotifyEvent.notifyToListener(false,null);
            }
        });
    }

    //    public void showDialog(FragmentManager fm, NotifyEvent ntfy) {
//    	if (DEBUG_ENABLE) Log.v(APPLICATION_TAG,"showDialog");
//    	mNotifyEvent=ntfy;
//	    FragmentTransaction ft = fm.beginTransaction();
//	    ft.add(mFragment,null);
//	    ft.commitAllowingStateLoss();
////      show(fm, "MessageDialogFragment");
//    };
    public void showDialog(FragmentManager fm, Fragment frag, NotifyEvent ntfy) {
        if (DEBUG_ENABLE) Log.v(APPLICATION_TAG,"showDialog");
        terminateRequired=false;
        mNotifyEvent=ntfy;
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(frag,null);
        ft.commitAllowingStateLoss();
//    	show(fm, APPLICATION_TAG);
    };
}