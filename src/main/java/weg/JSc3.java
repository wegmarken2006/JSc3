package weg;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JSc3 {
    public enum Rate {
        RateIr(0), RateKr(1), RateAr(2), RateDr(3);

        private int value;

        Rate(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    private JSc3() {
    }

    public static class Constant<T> {
        public T value;

        public Constant(T value) {
            this.value = value;
        }
    }

    public static class UgenL {
        public List<Object> l;

        // vararg constructor
        public UgenL(Object... values) {
            l = new ArrayList<Object>();
            for (Object value : values) {
                this.l.add(value);
            }
        }
    }

    public static class Mce {
        public UgenL ugens;
        public Mce(UgenL ugens){
            this.ugens = ugens;
        }
    }

    public static class Mrg {
        public Object left, right;
        public Mrg(Object left, Object right)
        {
            this.left = left;
            this.right = right;
        }
    }


    public static class Control {
        public String name;
        public Rate rate = Rate.RateKr;

        public Control(String name) {
            this.name = name;
        }

        public Control rate(Rate rate) {
            this.rate = rate;
            return this;
        }
    }

    public static class Primitive {
        public Rate rate = Rate.RateKr;
        public UgenL inputs = new UgenL();
        public List<Rate> outputs = null;
        public String name;
        public int special = 0;
        public int index = 0;

        public Primitive(String name) {
            this.name = name;
        }

        public Primitive inputs(UgenL inputs) {
            this.inputs = inputs;
            return this;
        }

        public Primitive outputs(List<Rate> outputs) {
            this.outputs = outputs;
            return this;
        }

        public Primitive rate(Rate rate) {
            this.rate = rate;
            return this;
        }

        public Primitive special(int special) {
            this.special = special;
            return this;
        }

        public Primitive index(int index) {
            this.index = index;
            return this;
        }
    }

    public static class Proxy {
        public Primitive primitive;
        public int index = 0;

        public Proxy(Primitive primitive) {
            this.primitive = primitive;
        }

        public Proxy index(int index) {
            this.index = index;
            return this;
        }
    }

    public static Rate max_rate(List<Rate> rates) {
        Rate start = Rate.RateIr;
        return max_rate(rates, start);
    }

    public static Rate max_rate(List<Rate> rates, Rate start) {
        int maxv = start.value;
        Rate maxr = start;
        for (Rate elem : rates) {
            if (elem.value > maxv) {
                maxr = elem;
                maxv = elem.value;
            }
        }
        return maxr;
    }

    public static int max_num(List<Integer> nums, int start) {
        int max1 = start;
        for (int elem : nums) {
            if (elem > max1)
                max1 = elem;
        }
        return max1;
    }

    public static <T> Rate rate_of(T ugen) {
        if (ugen instanceof Control) {
            return ((Control) ugen).rate;
        } else if (ugen instanceof Primitive) {
            return ((Primitive) ugen).rate;
        } else if (ugen instanceof Proxy) {
            return ((Proxy) ugen).primitive.rate;
        } else if (ugen instanceof Mce) {
            List<Rate> rates = (((Mce) ugen).ugens.l).stream().map(x -> rate_of(x)).collect(Collectors.toList());
            // ((Mce)ugen).ugens.l.Select(x -> rate_of(x)).ToList();
            return max_rate(rates);
        } else {
            return Rate.RateIr;
            // throw new Exception("Error: rate_of");
        }
    }

    public static List<Object> extend(List<Object> iList, int newLen)
    {
        int ln = iList.size();
        List<Object> vout = new ArrayList<Object>();
        if (ln > newLen)
        {
            return iList.subList(0, newLen);
        }
        else
        {
            vout.addAll(iList);
            vout.addAll(iList);
            return extend(vout, newLen);
        }
    }

    public static <T> int mce_degree(T ugen) throws Exception
    {
        if (ugen instanceof Mce)
        {
            return ((Mce)ugen).ugens.l.size();
        }
        else if (ugen instanceof Mrg)
        {
            return mce_degree(((Mrg)ugen).left);
        }
        else
        {
            throw new Exception("Error: mce_degree");
        }
    }

    public static <T> List<Object> mce_extend(int n, T ugen) throws Exception
    {
        if (ugen instanceof Mce)
        {
            List<Object> iList = ((Mce)ugen).ugens.l;
            return extend(iList, n);
        }
        else if (ugen instanceof Mrg)
        {
            List<Object> ex = mce_extend(n, ((Mrg)ugen).left);
            int len = ex.size();
            if (len > 0)
            {
                List<Object> outv = List.of(ugen);
                outv.addAll(ex.subList(1, n - 1));
                return outv;
            }
            else
            {
                throw new Exception("mce_extend");
            }
        }
        else
        {
            List<Object> outv = new ArrayList<Object>();
            for (int ind = 0; ind < n; ind++)
            {
                outv.add(ugen);
            }
            return outv;
        }
    }

    

}