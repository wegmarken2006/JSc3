package weg;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.net.*;

public class Osc {
    private Osc() {
    }

    public static byte[] encode_i8(int val) {
        var bb = ByteBuffer.allocate(2);
        bb.putChar((char) val);
        var aa = bb.array();
        var b1 = new byte[1];
        b1[0] = aa[1];
        return b1;
    }

    public static int decode_i8(byte[] buf) {
        var b2 = new byte[2];
        b2[1] = buf[0];
        b2[0] = 0;
        var bb = ByteBuffer.wrap(b2);
        var cc = bb.getChar();
        return (int) cc;
    }

    public static byte[] encode_i16(int val) {
        var bb = ByteBuffer.allocate(2);
        bb.putShort((short) val);
        return bb.array();
    }

    public static int decode_i16(byte[] buf) {
        var bb = ByteBuffer.wrap(buf);
        var ii = bb.getShort();
        return (int) ii;
    }

    public static byte[] encode_i32(int val) {
        var bb = ByteBuffer.allocate(4);
        bb.putInt(val);
        return bb.array();
    }

    public static int decode_i32(byte[] buf) {
        var bb = ByteBuffer.wrap(buf);
        var ii = bb.getInt();
        return ii;
    }

    public static byte[] encode_f32(double val) {
        var bb = ByteBuffer.allocate(4);
        bb.putFloat((float) val);
        return bb.array();
    }

    public static float decode_f32(byte[] buf) {
        var bb = ByteBuffer.wrap(buf);
        var ii = bb.getFloat();
        return ii;
    }

    public static byte[] encode_f64(double val) {
        var bb = ByteBuffer.allocate(8);
        bb.putDouble(val);
        return bb.array();
    }

    public static double decode_f64(byte[] buf) {
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

    public static int align(int n) {
        return 4 - n % 4;
    }

    public static byte[] extend_(byte[] pad, byte[] bts) {
        var outb = new ByteArrayOutputStream();
        outb.write(bts, 0, bts.length);
        var n = align(bts.length);

        for (var ind = 0; ind < n; ind++) {
            outb.write(pad, bts.length + ind, pad.length);
        }
        return outb.toByteArray();
    }

    public static byte[] encode_string(String str) {
        var eb = new byte[] { 0x0 };
        // eb[0] = 0;
        return extend_(eb, encode_str(str));
    }

    public static byte[] encode_blob(byte[] bts) {

        var outb = new ByteArrayOutputStream();
        outb.write(bts.length);

        var ind = outb.toByteArray().length;
        var eb = new byte[] { 0x0 };
        var edb = extend_(eb, bts);
        outb.write(edb, ind, edb.length);
        return outb.toByteArray();
    }

    public static byte[] encode_datum(Object dt) {
        if (dt instanceof Integer)
            return encode_i32((Integer) dt);
        if (dt instanceof Double)
            return encode_f32((Double) dt);
        if (dt instanceof String)
            return encode_string((String) dt);
        if (dt instanceof byte[])
            return encode_blob((byte[]) dt);
        return new byte[] {};
    }

    public static String tag(Object dt) {
        if (dt instanceof Integer)
            return "i";
        if (dt instanceof Double)
            return "f";
        if (dt instanceof String)
            return "s";
        if (dt instanceof byte[])
            return "b";
        return "";
    }

    public static String descriptor(Object[] dt) {
        var sout = ",";
        for (var item : dt) {
            sout = sout + tag(item);
        }

        return sout;
    }

    public class Message {
        public String Name;
        public Object[] LDatum;

        Message(String name, Object[] ldatum) {
            this.Name = name;
            this.LDatum = ldatum;
        }
    }

    public static byte[] encode_message(Message message) {
        var outb = new ByteArrayOutputStream();

        var datum = encode_datum(message.Name);
        var len = datum.length;
        outb.write(datum, 0, len);
        var ind = len;
        datum = encode_datum(descriptor(message.LDatum));
        len = datum.length;
        outb.write(datum, ind, len);

        for (var item : message.LDatum) {
            ind = ind + len;
            datum = encode_datum(item);
            len = datum.length;
            outb.write(datum, ind, len);
        }
        return outb.toByteArray();
    }

    public static void send_message(Message message) {
        var bmsg = encode_message(message);
        System.out.println("\nDEBUG");
        System.out.println(bmsg);
        osc_send(bmsg);
    }

    public static class PortConfig {
        public static InetAddress UdpIP;
        public static int UdpPort;
        public static DatagramSocket UdpCl;
    }

    public static void osc_set_port()
    {
        try {
            PortConfig.UdpPort = 57110;
            PortConfig.UdpIP = InetAddress.getByName("localhost");
            PortConfig.UdpCl = new DatagramSocket();
                
        } catch (Exception e) {
            System.out.println("Connect error: ");
            System.out.println(e.getMessage());
        }      
        
    }

    public static void osc_send(byte[] message) {
        try {
            DatagramPacket sendPacket = 
            new DatagramPacket(message, message.length, PortConfig.UdpIP, PortConfig.UdpPort);
            PortConfig.UdpCl.send(sendPacket);
                
        } catch (Exception e) {
            System.out.println("Send error: ");
            System.out.println(e.getMessage());
        }        

        byte[] receiveData = new byte[1024];
        try {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            PortConfig.UdpCl.receive(receivePacket);
            System.out.println("Received: ");
            System.out.println(new String(receivePacket.getData()));
        } catch (Exception e) {
            System.out.println("Receive error: ");
            System.out.println(e.getMessage());
        }

    }
}