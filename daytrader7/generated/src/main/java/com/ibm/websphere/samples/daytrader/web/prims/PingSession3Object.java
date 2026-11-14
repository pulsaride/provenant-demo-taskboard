package com.ibm.websphere.samples.daytrader.web.prims;

import java.io.Serializable;

public class PingSession3Object implements Serializable {

    PingSession3Object() {
        int index;
        byte b = 0x8;
        for (index = 0; index < 16; index++) {
            byteVal[index] = (byte) (b + 2);
        }

        char c = 'a';
        for (index = 0; index < 8; index++) {
            charVal[index] = (char) (c + 2);
        }

        a = 1;
        b = 2;
        c = 3;
        d = 5;
        e = (float) 7.0;
        f = (float) 11.0;
        g = (float) 13.0;
        h = (float) 17.0;
        i = 19.0;
        j = 23.0;
    }

    private static final long serialVersionUID = 1452347702903504717L;

    byte[] byteVal = new byte[16];

    char[] charVal = new char[8];

    int a;

    int b;

    int c;

    int d;

    float e;

    float f;

    float g;

    float h;

    double i;

    double j;

    String s1 = new String("123456789012");

    String s2 = new String("abcdefghijkl");
}
