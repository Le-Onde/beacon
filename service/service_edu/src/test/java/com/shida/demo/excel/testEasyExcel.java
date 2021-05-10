package com.shida.demo.excel;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

public class testEasyExcel {
    public static void main(String[] args) {
        //excel方法的写操作
        //1 设置写入文件夹地址和excel文件名称
       // String filename = "D://write.xlsx";
        //2 调用easyexcel里面的方法实现写操作
        //参数一 文件路径名称 参数二实体类名称
      //  EasyExcel.write(filename,DemoData.class).sheet("学生列表").doWrite(getData());

        //excel方法的读操作
        String filename = "D://write.xlsx";
        EasyExcel.read(filename,DemoData.class,new ExcelListener()).sheet().doRead();
    }
    //创建方法返回list
    private static List<DemoData> getData(){
        List<DemoData> list = new ArrayList<>();
        for(int i = 0; i<10;i++){
            DemoData demoData = new DemoData();
            demoData.setSno(i);
            demoData.setSname("lucy"+i);
            list.add(demoData);
        }
        return list;
    }
}
