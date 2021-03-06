package weg;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import weg.JSc3.*;

/**
 * Unit test for simple App.
 */
public class AppTest {
      


    @Test
    public void test1() {

        try {
            Constant<Integer> c1 = new Constant<Integer>(1);
            Constant<Double> c2 = new Constant<Double>(3.3);
            Control k1 = new Control("K1");
            List<Rate> tempListR = new ArrayList<Rate>();
            tempListR.addAll(List.of(Rate.RateKr, Rate.RateIr));
            
            Primitive p1 = new Primitive("P1").inputs(new UgenL(c1, c2)).rate(Rate.RateKr)
                    .outputs(tempListR);
            Primitive p2 = new Primitive("P2").rate(Rate.RateAr);

            Mce mc1 = new Mce(new UgenL(p1, p1));
            Mce mc2 = new Mce(new UgenL(p1, p2));
            Mce mc3 = new Mce(new UgenL(p1, p2, mc1));
            tempListR = new ArrayList<Rate>();
            
            tempListR.addAll(List.of(Rate.RateIr));
            Primitive p3 = new Primitive("P3").inputs(new UgenL(mc1, mc3)).rate(Rate.RateKr)
                  .outputs(tempListR);
            UgenL il1 = new UgenL(c1, p2);
            UgenL il2 = new UgenL(c1, p2, c1, p2, c1);
            Mrg mg1 = new Mrg(p1, mc1);
            Mrg mg2 = new Mrg(p2, p1);
            Mrg mg3 = new Mrg(mc1, p2);
            List<List<Object>> ill1 = List.of(List.of(1, 2, 3), List.of(4, 5, 6));
            List<List<Object>> ill2 = JSc3.transposer(ill1);
            List<IUgen> exmg1 = new ArrayList<>();
            List<IUgen> exmg2 = new ArrayList<>();
            int mcdg1;
            Mce mc10;
            UgenL mc11; 
            NodeC nc1 = new NodeC(10, 3);
            NodeK nk1 = new NodeK( 11, "nk1").deflt(5);
            FromPortC fpc1 = new FromPortC(100);
            FromPortK fpk1 = new FromPortK(101);
            FromPortU fpu1 = new FromPortU(102, 13);
            NodeC ndc1 = new NodeC(20, 320);
            NodeC ndc2 = new NodeC(21, 321);
            NodeK ndk1 = new NodeK(30, "ndk1");
            NodeK ndk2 = new NodeK(31, "ndk2");
            NodeU ndu1 = new NodeU(40,  "ndu1", new UgenL(mg1, mg2), 
            		List.of(Rate.RateAr, Rate.RateKr, Rate.RateIr), 2).rate(Rate.RateAr);
            NodeU ndu2 = new NodeU(41, "ndu2", new UgenL(), List.of(), 3).rate(Rate.RateAr);
            Graph gr1 = new Graph(11, List.of(ndc1, ndc2), List.of(ndk1, ndk2),
            		            List.of(ndu1, ndu2));
            MMap m1 = JSc3.mk_map(gr1);
            var mcs1 = m1.cs;
            var n1 = JSc3.mk_node_c(new Constant<Integer>(320), gr1);
            var nn = (NodeC)n1.one;
            var ck1 = new Control("ndk1").rate(Rate.RateKr).index(3);
            var n2 = JSc3.mk_node_k(ck1, gr1);
            var nn2 = (NodeK)n2.one;
            var n3 = JSc3.mk_node(new Constant<Integer>(320), gr1);
            var nn3 = n3.one;
            var cs1 = new Constant<Integer>(11);
            var p4 = new Primitive("p4").rate(Rate.RateAr).inputs(new UgenL(cs1, cs1))
                    .outputs(List.of(Rate.RateIr)) .special(0).index(0);
//            var n4 = JSc3.mk_node(p4, gr1);
//            var nn4 = n4.one;

            assertTrue("T1", c1.value == 1);
            assertTrue("T2", c1 instanceof Constant<?>);
            assertTrue("T3", p1.name == "P1");
            assertTrue("T4", p1 instanceof Primitive);
            assertTrue("T6", JSc3.extend(il1.l, 7).size() == 7);

            exmg1 = JSc3.mce_extend(3, mg2);
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
            assertTrue(mc11.l.get(0) instanceof Mrg);
            assertTrue(mc11.l.get(1) instanceof Primitive);
            assertTrue(JSc3.node_c_value(nc1) == 3);
            assertTrue(JSc3.node_k_default(nk1) == 5);
            assertTrue(m1.cs.containsAll(List.of(20, 21)));
            assertTrue(JSc3.find_c_p(3, nc1) == true);
            assertTrue(JSc3.find_k_p("ndk1", ndk1) == true);
            assertTrue(nn.nid == 20);
            assertTrue(nn2.nid == 30);
            assertTrue(((NodeC)nn3).nid == 20);
            assertTrue(JSc3.fetch(31, JSc3.mk_map(gr1).ks) == 1);
//            assertTrue(nn4.name == "p4");
            



        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    
}
