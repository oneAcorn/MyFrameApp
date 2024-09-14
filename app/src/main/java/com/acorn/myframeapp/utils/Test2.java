package com.acorn.myframeapp.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by acorn on 2024/3/8.
 */
public class Test2 {

    public static void main(String[] args) {
        Test2 t2 = new Test2();
        t2.GetArcCenter(0.0, 4.0, 4.0, 0.0, 4.0);
    }

    public void GetArcCenter(double x1, double y1, double x2, double y2, double radius) {

        double CenterX = 0, CenterY = 0;
        List<String> CenterList = new ArrayList<String>();
        double c1 = (x2 * x2 - x1 * x1 + y2 * y2 - y1 * y1) / (2 * (x2 - x1));
        double c2 = (y2 - y1) / (x2 - x1);  //斜率
        double A = (c2 * c2 + 1);
        double B = (2 * x1 * c2 - 2 * c1 * c2 - 2 * y1);
        double C = x1 * x1 - 2 * x1 * c1 + c1 * c1 + y1 * y1 - radius * radius;
        CenterY = (-B + Math.sqrt(B * B - 4 * A * C)) / (2 * A);
        CenterX = c1 - c2 * CenterY;

        System.out.println("圆心" + CenterX + "," + CenterY);
    }
}
