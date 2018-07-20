package weg;

import java.util.ArrayList;
import java.util.List;

import weg.JSc3.Constant;
import weg.JSc3.Control;
import weg.JSc3.Primitive;
import weg.JSc3.Rate;
import weg.JSc3.*;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        Constant<Integer> c1 = new Constant<Integer>(100);
        Constant<Double> c2 = new Constant<Double>(100.1);
        Control k1 = new Control("K1").rate(Rate.RateAr);
        System.out.println(c1.value);
        List<Object> ul = new ArrayList<>();
        ul.add(0, c1);
        ul.add(1, k1);
        System.out.println(ul.get(0));
        System.out.println(ul.get(1));
        Primitive p1 = new Primitive("P1");
        JSc3.printUgen(c1);
        JSc3.printUgen(c2);
        List<List<Object>> ill1 = List.of(List.of(1, 2, 3), List.of(4, 5, 6));
        List<List<Object>> ill2 = JSc3.transposer(ill1);
        JSc3.printLList(ill1);
        JSc3.printLList(ill2);



        
    }
}


