package com.example.pzhxbz.note;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by pzhxbz on 2016/5/16.
 */
public class Edit extends Fragment
{
    private Button save;
    String strTitle;
    String strContent;
    private EditText title;
    private EditText content;
    public interface FOneBtnClickListener
    {
        void onFOneBtnClick(String strTitle,String strContent);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.edit, container, false);
        save = (Button)view.findViewById(R.id.save);
        title=(EditText)view.findViewById(R.id.edtitle);
        content=(EditText)view.findViewById(R.id.note);
        save.setOnClickListener(saveClick);
        return view;
    }

    private Button.OnClickListener saveClick=new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                strTitle = title.getText().toString();
                strContent = content.getText().toString();
                if (!strContent.isEmpty() && !strContent.isEmpty()) {
                    if (getActivity() instanceof FOneBtnClickListener)
                    {
                        ((FOneBtnClickListener) getActivity()).onFOneBtnClick(strTitle,strContent);
                    }
                } else {
                    AlertDialog.Builder info = new AlertDialog.Builder(getContext());
                    info.setTitle("warning");
                    info.setMessage("你好像少填了一点东西吧,我是不会保存的");
                    info.setPositiveButton("确定", null);
                    info.show();
                    return;
                }
        }
    };
}

