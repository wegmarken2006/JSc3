package weg;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import org.junit.Test;

import weg.JSc3.Constant;
import weg.JSc3.Control;
import weg.JSc3.Primitive;
import weg.JSc3.Rate;
import weg.JSc3.UgenL;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        Constant<Integer> c1 = new Constant<Integer>(1);
        Constant<Double> c2 = new Constant<Double>(3.3);
        Control k1 = new Control("K1");

        Primitive p1 = new Primitive("P1")
          .inputs(new UgenL(c1, c2)).rate(Rate.RateKr)
          .output(List.of(Rate.RateKr, Rate.RateIr));

        assertTrue( c1.value == 1 );
        assertTrue( c1 instanceof Constant<?> );
        assertTrue( p1.name == "P1" );
        
    }
}
