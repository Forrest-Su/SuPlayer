package com.example.forrestsu.suplayer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.forrestsu.suplayer.R;
import com.example.forrestsu.suplayer.activity.PlayVideoActivity;

public class InternetVideoFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "InternetVideoFragment";

    private EditText addressET;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_internet_video, container, false);
        init(view);
        return view;
    }

    public void init (View view) {
        addressET = (EditText) view.findViewById(R.id.et_address);
        Button confirmBT = (Button) view.findViewById(R.id.bt_confirm);
        confirmBT.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.bt_confirm:
                if (!TextUtils.isEmpty(addressET.getText().toString())) {
                    Intent intent = new Intent(getContext(), PlayVideoActivity.class);
                    intent.putExtra("source", addressET.getText().toString());
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "请输入正确的地址", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

}
