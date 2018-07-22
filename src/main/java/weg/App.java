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
        Constant<Integer> c1 = new Constant<Integer>(1);
        Constant<Double> c2 = new Constant<Double>(3.3);
        Control k1 = new Control("K1");
    
        Primitive p1 = new Primitive("P1").inputs(new UgenL(c1, c2)).rate(Rate.RateKr)
                .outputs(List.of(Rate.RateKr, Rate.RateIr));
        Primitive p2 = new Primitive("P2").rate(Rate.RateAr);
    
        Mce mc1 = new Mce(new UgenL(p1, p1));
        Mce mc2 = new Mce(new UgenL(p1, p2));
        Mce mc3 = new Mce(new UgenL(p1, p2, mc1));
        Primitive p3 = new Primitive("P3").inputs(new UgenL(mc1, mc3)).rate(Rate.RateKr)
              .outputs(List.of(Rate.RateIr));
        UgenL il1 = new UgenL(c1, p2);
        UgenL il2 = new UgenL(c1, p2, c1, p2, c1);
        Mrg mg1 = new Mrg((Object) p1, (Object) mc1);
        Mrg mg2 = new Mrg((Object) p2, (Object) p1);
        Mrg mg3 = new Mrg((Object) mc1, (Object) p2);
        List<List<Object>> ill1 = List.of(List.of(1, 2, 3), List.of(4, 5, 6));
        List<List<Object>> ill2 = JSc3.transposer(ill1);

        try {
        	//var pp = JSc3.proxify(mg3);
        	System.out.println(JSc3.iota(5, 3, 2));
            System.out.println(JSc3.mce_degree(mc1));            
        } catch (Exception e) {
        	e.printStackTrace();
            //TODO: handle exception
        }




        
    }
}


