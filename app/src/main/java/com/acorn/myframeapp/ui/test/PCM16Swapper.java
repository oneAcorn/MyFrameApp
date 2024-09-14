package com.acorn.myframeapp.ui.test;

/**
 * Created by acorn on 2024/9/5.
 */
class PCM16Swapper {
    public static byte[] get16BitPcm(short[] data) {
        byte[] resultData = new byte[2 * data.length];
        int iter = 0;

        String TAG = "get16BitPcm";
        for (short sample : data) {
            resultData[iter++] = (byte)( sample & 0xff);     //低位存储，0xff是掩码操作
            resultData[iter++] = (byte)((sample >>8) & 0xff); //高位存储
            /*测试生成的字节是否正确
            //ByteBuffer bb = ByteBuffer.allocate(2);
            //bb.order(ByteOrder.LITTLE_ENDIAN);
            //bb.put((byte)(sample & 0xff));
            //bb.put((byte)((sample >>8) & 0xff));
            //short shortVal = bb.getShort(0);

            //Log.d(TAG,"iter is:"+iter+"\tshortVal is: "+ shortVal);
            //Log.d(TAG,"==========================");
            */
        }
        return resultData;
    }

    public static short swap(short ori) {
        byte[] bus = getBytes(ori);
        return (short) ((bus[1]) << 8 | bus[0]);
    }

    public static byte[] getBytes(short s) {
        byte[] buf = new byte[2];
        for (int i = 0; i < buf.length; i++) {
            buf[i] = (byte) (s & 0x00ff);
            s >>= 8;
        }
        return buf;
    }
}
