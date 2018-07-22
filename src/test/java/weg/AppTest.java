package weg;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import weg.JSc3.Constant;
import weg.JSc3.Control;
import weg.JSc3.Mce;
import weg.JSc3.Mrg;
import weg.JSc3.Primitive;
import weg.JSc3.Rate;
import weg.JSc3.UgenL;

/**
 * Unit test for simple App.
 */
public class AppTest {
      
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
    List<Object> exmg1 = new ArrayList<>();
    List<Object> exmg2 = new ArrayList<>();
    int mcdg1;
    Mce mc10;
    UgenL mc11; 

    @Test
    public void test1() {
     
        assertTrue("T1", c1.value == 1);
        assertTrue("T2", c1 instanceof Constant<?>);
        assertTrue("T3", p1.name == "P1");
        assertTrue("T4", p1 instanceof Primitive);
        assertTrue("T6", JSc3.extend(il1.l, 7).size() == 7);
    }

    @Test
    public void test2() {

        try {
            exmg1 = JSc3.mce_extend(3, mg1);
            exmg2 = JSc3.mce_extend(3, mc1);
            mcdg1 = JSc3.mce_degree(mc1);
            mc10 = JSc3.mce_transform(p3);
            mc11 = JSc3.mce_channels(mg3);
            assertTrue("T5", mcdg1 == 2);
            assertTrue("T7", exmg2.size() == 3);
            assertTrue((((Primitive)exmg2.get(2))).name == "P1");
            assertTrue((((Primitive)exmg1.get(2))).name == "P2");
            assertTrue(JSc3.is_mce(mc1));
            assertFalse(JSc3.is_mce(mg1));
            assertTrue(ill2.size() == 3);
            assertTrue(exmg1.size() == 3);
            assertTrue(mc10 instanceof Mce);
            assertTrue(((Primitive)mc10.ugens.l.get(2)).name == "P3");   
            assertTrue(mc11.l.size() == 2);
            assertTrue(mc11.l.get(1) instanceof Mrg);
            assertTrue(mc11.l.get(2) instanceof Primitive);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    
}
