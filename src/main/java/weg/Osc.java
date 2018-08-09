package weg;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class Osc {
    private Osc(){}

    public static byte[] encode_i8 (int val) {
        var bb = ByteBuffer.allocate(2); 
        bb.putChar((char)val);
        var aa = bb.array();
        var b1 = new byte[1];
        b1[0] = aa[1];
        return b1;
    }

    public static int decode_i8 (byte[] buf) {
        var b2 = new byte[2];
        b2[1] = buf[0];
        b2[0] = 0;
        var bb = ByteBuffer.wrap(b2); 
        var cc = bb.getChar();
        return (int)cc;
    }

    public static byte[] encode_i16 (int val) {
        var bb = ByteBuffer.allocate(2); 
        bb.putShort((short)val);
        return bb.array();
    }

    public static int decode_i16 (byte[] buf) {
        var bb = ByteBuffer.wrap(buf); 
        var ii = bb.getShort();
        return (int)ii;
    }

    public static byte[] encode_i32 (int val) {
        var bb = ByteBuffer.allocate(4); 
        bb.putInt(val);
        return bb.array();
    }

    public static int decode_i32 (byte[] buf) {
        var bb = ByteBuffer.wrap(buf); 
        var ii = bb.getInt();
        return ii;
    }

    public static byte[] encode_f32 (double val) {
        var bb = ByteBuffer.allocate(4); 
        bb.putFloat((float)val);
        return bb.array();
    }

    public static float decode_f32 (byte[] buf) {
        var bb = ByteBuffer.wrap(buf); 
        var ii = bb.getFloat();
        return ii;
    }

    public static byte[] encode_f64 (double val) {
        var bb = ByteBuffer.allocate(8); 
        bb.putDouble(val);
        return bb.array();
    }

    public static double decode_f64 (byte[] buf) {
        var bb = ByteBuffer.wrap(buf); 
        var ii = bb.getDouble();
        return ii;
    }
 
    public static byte[] encode_str(String str) {
        return str.getBytes();
    }

    public static byte[] str_pstr(String str) {

        var outb = new ByteArrayOutputStream();
        outb.write(str.length());
        try {
            outb.write(str.getBytes());            
        } catch (Exception e) {   
        }
        return outb.toByteArray();
    }
}