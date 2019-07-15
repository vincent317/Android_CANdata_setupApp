package com.example.android.tablayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class Tab1Fragment extends Fragment {
    private String qudongdizhi;
    private String yingyongdizhi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container != null) {
            View v = inflater.inflate(R.layout.fragment_one, container, false);
            SharedPreferences database = PreferenceManager.getDefaultSharedPreferences(getActivity());
            EditText t = v.findViewById(R.id.wulidizhi);
            EditText t2 = v.findViewById(R.id.gongnengdizhi);
            EditText t3 = v.findViewById(R.id.xiangyingdizhi);
            TextView qudong = v.findViewById(R.id.qudong);
            TextView yingyong = v.findViewById(R.id.yingyong);
            Spinner s = v.findViewById(R.id.botelv);
            Spinner s2 = v.findViewById(R.id.shebei);

            ArrayList<Integer> data_list = new ArrayList<>();
            data_list.add(125);
            data_list.add(500);
            ArrayList<String> shebei_list = new ArrayList<String>();
            shebei_list.add("USB-CAN-2C");
            shebei_list.add("Vector Can");
            shebei_list.add("GV8507");
            shebei_list.add("ZLG-2E-U");
            shebei_list.add("PCAN");
            ArrayAdapter<Integer> arr_adapter= new ArrayAdapter<Integer>(getContext(), R.layout.spinner_item, data_list);
            ArrayAdapter<String> arr_adapter2= new ArrayAdapter<String>(getContext(), R.layout.spinner_item, shebei_list);
            arr_adapter.setDropDownViewResource(R.layout.spinner_item);
            arr_adapter2.setDropDownViewResource(R.layout.spinner_item);
            s.setAdapter(arr_adapter);
            s2.setAdapter(arr_adapter2);

            qudongdizhi = database.getString("qudongdizhi","");
            yingyongdizhi = database.getString("yingyongdizhi","");
            t.setText(database.getString("wuli", ""));
            t2.setText(database.getString("gongneng", ""));
            t3.setText(database.getString("xiangying", ""));
            qudong.setText(qudongdizhi.substring(qudongdizhi.lastIndexOf('/') + 1));
            yingyong.setText(yingyongdizhi.substring(yingyongdizhi.lastIndexOf('/') + 1));
            for (int i=0;i<s.getCount();i++){
                if ((int)s.getItemAtPosition(i)==database.getInt("botelv",0)){
                    s.setSelection(i);
                    break;
                }
            }
            for (int i=0;i<s2.getCount();i++){
                if (s2.getItemAtPosition(i).equals(database.getString("shebei",""))){
                    s2.setSelection(i);
                    break;
                }
            }
            return v;
        }

        return null;
    }

    public void onPause() {
        SharedPreferences database = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = database.edit();
        EditText t = getView().findViewById(R.id.wulidizhi);
        EditText t2 = getView().findViewById(R.id.xiangyingdizhi);
        EditText t3 = getView().findViewById(R.id.gongnengdizhi);
        Spinner s = getView().findViewById(R.id.botelv);
        Spinner s2 = getView().findViewById(R.id.shebei);

        super.onPause();
        editor.putString("wuli", t.getText().toString());
        editor.putString("xiangying", t2.getText().toString());
        editor.putString("gongneng", t3.getText().toString());
        editor.putString("qudongdizhi",qudongdizhi);
        editor.putString("yingyongdizhi",yingyongdizhi);
        editor.putInt("botelv",(int)s.getSelectedItem());
        editor.putString("shebei",(String)s2.getSelectedItem());
        editor.apply();
    }

    public void qudong_choose(View view) {
        Intent choosefile = new Intent(Intent.ACTION_GET_CONTENT);
        choosefile.setType("*/*");

        if (view.getId() == R.id.qudongchoose) {
            startActivityForResult(choosefile, 0);
        } else {
            startActivityForResult(choosefile, 1);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        TextView temp = (TextView) getView().findViewById(R.id.qudong);
        Uri uri = data.getData();

        if (requestCode == 1) {
            temp = (TextView) getView().findViewById(R.id.yingyong);
            yingyongdizhi = uri.getPath().toString();
            temp.setText(yingyongdizhi.substring(yingyongdizhi.lastIndexOf('/') + 1));
        }else{
            qudongdizhi = uri.getPath().toString();
            temp.setText(qudongdizhi.substring(qudongdizhi.lastIndexOf('/') + 1));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}