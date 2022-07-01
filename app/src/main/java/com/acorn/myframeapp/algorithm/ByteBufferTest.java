package com.acorn.myframeapp.algorithm;

import com.acorn.myframeapp.utils.ByteBufferUtil;

import java.nio.ByteBuffer;


/**
 * Created by acorn on 2022/7/1.
 */
class ByteBufferTest {
    public static void main(String[] args) {
        //组包
        testPacket();
    }

    /**
     * 网络上有多条数据发送给服务端，数据之间使用 \n 进行分隔
     * 但由于某种原因这些数据在接收时，被进行了重新组合，例如原始数据有3条为
     * <p>
     * Hello,world\n
     * I'm zhangsan\n
     * How are you?\n
     * <p>
     * 变成了下面的两个 byteBuffer (黏包，半包)
     * <p>
     * * Hello,world\nI'm zhangsan\nHo
     * * w are you?\n
     * <p>
     * 现在要求你编写程序，将错乱的数据恢复成原始的按 \n 分隔的数据
     */
    private static void testPacket() {
        ByteBuffer source = ByteBuffer.allocate(64);
        //                     11            24
        source.put("Hello,world\nI'm zhangsan\nHo".getBytes());
        split(source);

        source.put("ooooo,ho".getBytes());
        split(source);

        source.put("w are you?\nhaha!\n".getBytes());
        split(source);
    }

    private static void split(ByteBuffer source) {
        //进入读取模式,翻转
        /**
         * limit=position;position=0;mark=-1;
         */
        source.flip();
        for (int i = 0; i < source.limit(); i++) {
            if (source.get(i) == '\n') {
                int length = i + 1 - source.position();
                ByteBuffer target = ByteBuffer.allocate(length);
                for (int j = 0; j < length; j++) {
                    target.put(source.get());
                }
                ByteBufferUtil.debugAll(target);
            }
        }
        //进入写入模式
        /**
         * compact函数:
         * 将 position 到 limit 之间还未读取的数据拷贝到 ByteBuffer 中数组的最前面，
         * 然后再将 position 移动至这些数据之后的一位，将 limit 移动至 capacity。
         * 这样 position 和 limit 之间就是已经读取过的老的数据或初始化的数据，就可以放心大胆地继续写入覆盖了
         */
        source.compact();
        System.out.println("end");
    }
}
