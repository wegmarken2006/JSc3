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

    public class Mce {
        public UgenL ugens;
        public Mce(UgenL ugens){
            this.ugens = ugens;
        }
    }

    public class Mrg {
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
        public List<Rate> output = null;
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

        public Primitive output(List<Rate> output) {
            this.output = output;
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

    

}