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
        Control k1 = new Control("K1").rate(Rate.RateAr);
        System.out.println(c1.value);
        List<Object> ul = new ArrayList<>();
        ul.add(0, c1);
        ul.add(1, k1);
        System.out.println(ul.get(0));
        System.out.println(ul.get(1));
        Primitive p1 = new Primitive("P1");
        List<Object> l1 = List.of(1,2,3,4,5,6);
        List<Object> l2 = JSc3.extend(l1, 3);
        for (Object elem : l2) System.out.println(elem);


        
    }
}


