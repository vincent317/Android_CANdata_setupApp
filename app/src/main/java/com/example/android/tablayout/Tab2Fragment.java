package com.example.android.tablayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class Tab2Fragment extends Fragment {
    private String anquandizhi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_two, container, false);
        final SharedPreferences database = PreferenceManager.getDefaultSharedPreferences(getActivity());

        ArrayList<String> data_list = new ArrayList<String>();
        Spinner shuaxie = v.findViewById(R.id.shuaxieliucheng);
        data_list.add("北汽");
        data_list.add("长城");
        data_list.add("福田");
        data_list.add("BJEV");
        data_list.add("东风");
        data_list.add("吉利");
        ArrayAdapter<String> arr_adapter= new ArrayAdapter<>(getContext(), R.layout.spinner_item, data_list);
        arr_adapter.setDropDownViewResource(R.layout.spinner_item);
        shuaxie.setAdapter(arr_adapter);
        for (int i=0;i<shuaxie.getCount();i++){
            if (shuaxie.getItemAtPosition(i).equals(database.getString("shuaxieliucheng",""))){
                shuaxie.setSelection(i);
                break;
            }
        }

        anquandizhi = database.getString("anquan", "");
        HorizontalNumberPicker p2server = v.findViewById(R.id.p2server);
        p2server.setMax(1000);
        p2server.setMin(5);
        p2server.setValue(database.getInt("p2server", 5));
        HorizontalNumberPicker p2server_2 = v.findViewById(R.id.p2server_2);
        p2server_2.setMax(1000);
        p2server_2.setMin(5);
        p2server_2.setValue(database.getInt("p2server_2", 5));
        HorizontalNumberPicker fuweishijian = v.findViewById(R.id.fuweishijian);
        fuweishijian.setMax(1000);
        fuweishijian.setMin(5);
        fuweishijian.setValue(database.getInt("fuweishijian", 5));
        final CheckBox zidongme = v.findViewById(R.id.zidongme);
        zidongme.setChecked(database.getBoolean("zidongme", true));
        final HorizontalNumberPicker zidongkaishi = v.findViewById(R.id.zidongkaishi);
        if(zidongme.isChecked()) {
            zidongkaishi.setVisibility(View.VISIBLE);
        }else{
            zidongkaishi.setVisibility(View.INVISIBLE);
        }
        zidongkaishi.setMin(5);
        zidongkaishi.setMax(1000);
        zidongkaishi.setValue(database.getInt("zidongkaishi", 5));
        zidongme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(zidongme.isChecked()) {
                    zidongkaishi.setVisibility(View.VISIBLE);
                }else{
                    zidongkaishi.setVisibility(View.INVISIBLE);
                }
            }
        });
        CheckBox baocunme = v.findViewById(R.id.baocunme);
        baocunme.setChecked(database.getBoolean("baocunme", true));
        CheckBox fugaime = v.findViewById(R.id.fugaime);
        fugaime.setChecked(database.getBoolean("fugaime", true));

        TextView anquan = v.findViewById(R.id.anquan);
        anquan.setText(anquandizhi.substring(anquandizhi.lastIndexOf('/') + 1));
        return v;
    }

    public void onPause() {
        super.onPause();

        SharedPreferences database = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = database.edit();
        HorizontalNumberPicker p2server = getView().findViewById(R.id.p2server);
        HorizontalNumberPicker p2server_2 = getView().findViewById(R.id.p2server_2);
        HorizontalNumberPicker fuweishijian = getView().findViewById(R.id.fuweishijian);
        CheckBox zidongme = getView().findViewById(R.id.zidongme);
        HorizontalNumberPicker zidongkaishi = getView().findViewById(R.id.zidongkaishi);
        CheckBox baocunme = getView().findViewById(R.id.baocunme);
        CheckBox fugaime = getView().findViewById(R.id.fugaime);
        TextView anquan = getView().findViewById(R.id.anquan);
        Spinner shuaxie = getView().findViewById(R.id.shuaxieliucheng);

        editor.putString("shuaxieliucheng",(String)shuaxie.getSelectedItem());
        editor.putInt("p2server", p2server.getValue());
        editor.putInt("p2server_2", p2server_2.getValue());
        editor.putInt("fuweishijian", fuweishijian.getValue());
        editor.putBoolean("zidongme", zidongme.isChecked());
        editor.putInt("zidongkaishi", zidongkaishi.getValue());
        editor.putBoolean("baocunme", baocunme.isChecked());
        editor.putBoolean("fugaime", fugaime.isChecked());
        editor.putString("anquan", anquandizhi);
        editor.apply();
    }

    public void anquan_choose(View v) {
        Intent choosefile = new Intent(Intent.ACTION_GET_CONTENT);
        choosefile.setType("*/*");

        startActivityForResult(choosefile, 0);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        TextView temp = (TextView) getView().findViewById(R.id.anquan);
        Uri uri = data.getData();

        anquandizhi = uri.getPath().toString();
        temp.setText(anquandizhi.substring(anquandizhi.lastIndexOf('/') + 1));
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void exportData(List<BeanExportData> datas){
        //需要导出的excel文件的文件名
        String fileName ="考情统计.xls";
        //操作excel的对象
        WritableWorkbook wwb = null;
        try {
            //根据当前的文件路径创建统计的文件并且实例化出一个操作excel的对象
            wwb = Workbook.createWorkbook(new File(Environment.getExternalStorageDirectory()+"/"+fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (wwb != null ){
            //创建底部的选项卡  传参是选项卡的名称  和  选型卡的索引
            WritableSheet writableSheet = wwb.createSheet("2017年3月7日考勤",0);
            //创建excel的表头的信息
            String [] topic ={"序号","姓名","年龄","日期"};
            for (int i = 0 ; i<topic.length  ; i++ ){
                //横向的在单元格中填写数据
                Label labelC = new Label(i,0,topic[i]);
                try {
                    writableSheet.addCell(labelC);
                } catch (WriteException e) {
                    e.printStackTrace();
                }
            }
            //从实体中遍历数据并将数据写入excel文件中
            BeanExportData account;
            ArrayList<String> li;
            for ( int j = 0 ; j < datas.size() ; j++ ){
                //将数据源列表中的数据整合成 一个个的字符串列表
                account = datas.get(j);
                li = new ArrayList<>();
                li.add(account.getNumber());
                li.add(account.getName());
                li.add(account.getAge());
                li.add(account.getData());
                int k = 0;
                for (String l:li){
                    //将单个的字符串列表横向的填入到excel表中
                    Label labelC = new Label(k,j+1,l);
                    k++;
                    try {
                        writableSheet.addCell(labelC);
                    } catch (WriteException e) {
                        e.printStackTrace();
                    }
                }
                li = null;
            }
        }
        //将文件从内存写入到文件当中
        try {
            wwb.write();
            wwb.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }
    }

}