package weg;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
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

    public static <T> void printUgen(T ugen)
    {
        if (ugen instanceof Control)
        {
            System.out.println("K: " + ((Control)ugen).name);
        }
        else if (ugen instanceof Primitive)
        {
            System.out.println("P: " + ((Primitive)ugen).name);
        }
        else if (ugen instanceof Constant<?>)
        {
            if (((Constant)ugen).value instanceof Integer) {
                System.out.println("C: " + Integer.toString((int)((Constant)ugen).value));    
            }
            else {
                System.out.println("C: " + Double.toString((double)((Constant)ugen).value));    
            }
        }
        else if (ugen instanceof Mce)
        {
            System.out.println("Mce: " + Integer.toString(((Mce)ugen).ugens.l.size()));
            printUgens(((Mce)ugen).ugens);
        }
        else if (ugen instanceof Mrg)
        {
            System.out.println("Mrg: ");
            System.out.print(" * left: ");
            printUgen(((Mrg)ugen).left);
            System.out.print(" * right: ");
            printUgen(((Mrg)ugen).right);
        }
        else if (ugen instanceof Proxy)
        {
            System.out.println("Proxy: ");
        }
        else
        {
            System.out.print(ugen);
        }


    }
    public static void printUgens(UgenL ugenl)
    {
        List<Object> ugens = ugenl.l;
        for (Object ugen : ugens)
        {
            System.out.println(" - ");
            printUgen(ugen);
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

    public static <T>boolean is_mce(T ugen)
    {
        if (ugen instanceof Mce)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public static <T> void printLList(List<List<T>> iList)
    {
        int len1 = iList.size();
        int len2 = iList.get(0).size();

        for (int ind1 = 0; ind1 < len1; ind1++)
        {
            for (int ind2 = 0; ind2 < len2; ind2++)
            {
                printUgen(iList.get(ind1).get(ind2));
                System.out.print(" ");
            }
            System.out.println("");
        }
    }

    public static List<List<Object>> transposer(List<List<Object>> iList)
    {
        int len1 = iList.size();
        int len2 = iList.get(0).size();
        List<List<Object>> outv = new ArrayList<List<Object>>();
        for (int ind = 0; ind < len2; ind++)
        {
            outv.add(new ArrayList<Object>());
        }
        for (int ind2 = 0; ind2 < len2; ind2++)
        {
            for (int ind1 = 0; ind1 < len1; ind1++)
            {
                outv.get(ind2).add(iList.get(ind1).get(ind2));
            }
        }
        return outv;
    }

    public static <T> Mce mce_transform(T ugen) throws Exception
    {
        if (ugen instanceof Primitive)
        {
            Primitive prim = ((Primitive)ugen);
            UgenL inputs = prim.inputs;
            List<Object> ins = inputs.l.stream().filter(x -> is_mce(x)).collect(Collectors.toList());
            List<Integer> degs = new ArrayList<Integer>();
            for (Object elem : ins)
            {
                degs.add(mce_degree(elem));
            }
            int upr = max_num(degs, 0);
            List<List<Object>> ext = new ArrayList<List<Object>>();
            for (Object elem : inputs.l)
            {
                ext.add(mce_extend(upr, elem));
            }
            List<List<Object>> iet = transposer(ext);
            List<Object> outv = new ArrayList<Object>();
            //var outv = new UgenL();
            for (List<Object> elem2 : iet)
            {
                UgenL newInps = new UgenL();
                newInps.l = elem2;
                Primitive p = new Primitive(prim.name)
                 .inputs(newInps) .outputs(prim.outputs)
                .rate(prim.rate) .special(prim.special) .index(prim.index);
                //outv.l.Add(p);
                outv.add(p);

            }
            UgenL newOut = new UgenL();
            newOut.l = outv;
            //return new Mce(ugens: outv);
            return new Mce(newOut);
        }
        else
        {
            throw new Exception("Error: mce_transform");
        }
    }

    public static <T> Object mce_expand(T ugen) throws Exception
    {
        if (ugen instanceof Mce)
        {
            List<Object> lst = new ArrayList<Object>();
            List<Object> ugens = ((Mce)ugen).ugens.l;
            for (Object elem : ugens) {
                lst.add(mce_expand(elem));
            }
            UgenL outv = new UgenL();
            outv.l = lst;
            return new Mce(outv);
        }
        else if (ugen instanceof Mrg)
        {
            Object left =  ((Mrg)ugen).left;
            Object right =  ((Mrg)ugen).right;
            Object ug1 = mce_expand(left);
            return new Mrg(ug1, right);
        }
        else
        {
            Function<T, Boolean> rec = (T ug) ->
            {
                if (ugen instanceof Primitive) {
                    UgenL inputs = ((Primitive)ug).inputs;
                    List<Object> ins = inputs.l.stream().filter(x -> is_mce(x)).collect(Collectors.toList());
                    return (ins.size() > 0);
                }
                else return false;
            };
            if (rec.apply(ugen)) {
                try {
                    return mce_expand(mce_transform(ugen));        
                } catch (Exception e) {
                    throw new Exception("mce_expand");
                }
            }
            else return ugen;
        }
    }



}