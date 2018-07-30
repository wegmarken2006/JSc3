package weg;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
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
    
    public static class NodeC
    {
        public int nid, value;
        public NodeC(int nid, int value)
        {
            this.nid = nid;
            this.value = value;
        }
    }


    public static class NodeK
    {
        public int nid;
        public int deflt = 0;
        public String name;
        public Rate rate = Rate.RateKr;
        
        public NodeK(int nid, String name)
        {
        	this.name = name;
            this.nid = nid;
        }
        
        public NodeK deflt(int deflt)
        {
            this.deflt = deflt;
            return this;
        }

        public NodeK rate(Rate rate)
        {
            this.rate = rate;
            return this;
        }

    }
    
    public static class NodeU
    {
        public int nid;
        public String name;        
        public List<Object> inputs;
        public List<Rate> outputs;
        public int special;
        public int ugenId = 0;
        public Rate rate = Rate.RateKr;
        public NodeU(int nid, String name, List<Object> inputs,
        		List<Rate> outputs, int ugenId) {
        	this.nid = nid;
        	this.name = name;
        	this.inputs = inputs;
        	this.outputs = outputs;
        }
        public NodeU ugenId(int ugenId) {
        	this.ugenId = ugenId;
        	return this;
        }
        public NodeU rate(Rate rate) {
        	this.rate = rate;
        	return this;
        }

    }

    public static class FromPortC {
    	public int port_nid;
        public FromPortC(int port_nid) {
            this.port_nid = port_nid;
        }
    }
        
    public static class  FromPortK {
    	public int port_nid;
        public FromPortK(int port_nid) {
            this.port_nid = port_nid;
        }
    }

    public static class  FromPortU {
    	public int port_nid;
    	public int port_idx;
        public FromPortU(int port_nid, int port_idx) {
            this.port_nid = port_nid;
            this.port_idx = port_idx;
        }
    }

    public static class Graph {
    	public int nextId;
    	public List<NodeC> constants;
    	public List<NodeK> controls;
    	public List<NodeU> ugens;
    	public Graph(int nextId, List<NodeC> constants, 
    			List<NodeK> controls, List<NodeU> ugens) {
    		this.nextId = nextId;
    		this.constants = constants;
    		this.controls = controls;
    		this.ugens = ugens;
    	}
    }
    
    public static class MMap {
    	public List<Integer> cs;
    	public List<Integer> ks;
    	public List<Integer> us;
    	public MMap(List<Integer> cs, List<Integer> ks, List<Integer> us) {
            this.cs = cs;
            this.ks = ks;
            this.us = us;		
    	}
    }

    public static class Input {    	
    	public int u;
    	public int p;
        public Input (int u, int p) {
            this.u = u;
            this.p = p;
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

    public static List<Integer> iota (int n, int init, int step)
    {
        if (n == 0) {
            return new ArrayList<Integer>();
        }
        else {
            var outInit = List.of(init); //immutable!!
            var outv = new ArrayList<Integer>();
            outv.addAll(outInit);
            var retList = iota(n-1, init+step, step);
            outv.addAll(retList);   
            return outv;
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
                var outInit = List.of(ugen); //immutable!!
                var outv = new ArrayList<Object>();
                outv.addAll(outInit);
                outv.addAll(ex.subList(1, n));
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
    public static Object mce_channel(int n, Object ugen) throws Exception
    {
        if (ugen instanceof Mce) {
            List<Object> ugens = ((Mce)ugen).ugens.l;
            return ugens.get(n);
        }
        else throw new Exception("Error: mce_channel");
    }

    public static UgenL mce_channels(Object ugen) throws Exception
    {
        if (ugen instanceof Mce)
        {
            UgenL ugens = ((Mce)ugen).ugens;
            return ugens;
        }
        else if (ugen instanceof Mrg) {
            Object left =  ((Mrg)ugen).left;
            Object right =  ((Mrg)ugen).right;
            UgenL lst = mce_channels(left);
            int len = lst.l.size();
            if (len > 1) {
                Mrg mrg1 = new Mrg(lst.l.get(0), right);
                var outInit = List.of(mrg1);
                var outv = new ArrayList<Object>();
                outv.addAll(outInit);
                outv.addAll(lst.l.subList(1, len));
                UgenL newOut = new UgenL();
                newOut.l = outv;
                return newOut;

            }
            else throw new Exception("Error: mce_channels");
        }
        else {
            List<Object> outv = new ArrayList<Object>();
            outv.add(ugen);
            UgenL newOut = new UgenL();
            newOut.l = outv;
            return newOut;
        }            
    }

    public static Object proxify (Object ugen) throws Exception
    {
        if (ugen instanceof Mce) {
            var lst = new UgenL();
            for (Object elem : ((Mce)ugen).ugens.l) {
                lst.l.add(proxify(elem));
            }
            return new Mce(lst);
        }
        else if (ugen instanceof Mrg) {
            var prx = proxify(((Mrg)ugen).left);
            return new Mrg(prx, ((Mrg)ugen).right);
        }
        else if (ugen instanceof Primitive) {
            var ln = ((Primitive)ugen).outputs.size();
            if (ln < 2) {
                return ugen;
            }
            else {
                var lst1 = iota(ln, 0, 1);
                var lst2 = new UgenL();
                for (var ind : lst1) {
                    lst2.l.add(proxify(new Proxy((Primitive)ugen).index(ind)));
                }
                return new Mce(lst2);
            }

        }
        else {
            throw new Exception("proxify");
        }
    }

	public static Object mk_ugen(String name, UgenL inputs, 
			List<Rate> outputs) throws Exception {
		var ind = 0;
		var sp = 0;
		var rate = Rate.RateKr;
		return mk_ugen(name, inputs, outputs, ind, sp, rate);
	}

	public static Object mk_ugen(String name, UgenL inputs, 
			List<Rate> outputs, int ind) throws Exception {
		var sp = 0;
		var rate = Rate.RateKr;
		return mk_ugen(name, inputs, outputs, ind, sp, rate);
	}

	public static Object mk_ugen(String name, UgenL inputs, 
			List<Rate> outputs, int ind, int sp) throws Exception {
		var rate = Rate.RateKr;
		return mk_ugen(name, inputs, outputs, ind, sp, rate);
	}

	public static Object mk_ugen(String name, UgenL inputs, 
			List<Rate> outputs, int ind, int sp, Rate rate) throws Exception {		
		var pr1 = new Primitive(name).rate(rate).inputs(inputs).outputs(outputs).special(sp).index(ind);
		try {
			return proxify(mce_expand(pr1));	
		} catch (Exception e) {
			throw new Exception("mk_ugen");
		}
		
	}
    public static int node_c_value(NodeC node) {
        return node.value;
    }
        
    public static int  node_k_default(NodeK node) {
        return node.deflt;
    }
    
    public static MMap  mk_map(Graph graph) {
        var cs = new ArrayList<Integer>();
        var ks = new ArrayList<Integer>();
        var us = new ArrayList<Integer>();
        for (var el1 : graph.constants) {
            cs.add(el1.nid);
        }
        for (var el2 : graph.controls) {
        	ks.add(el2.nid);
        }            
        for (var el3 : graph.ugens) {
        	us.add(el3.nid);
        }
            
        return new MMap(cs, ks, us);    	
    }
    
    public static int fetch(int val, List<Integer> lst) {
    	var it = lst.listIterator();
    	while (it.hasNext()) {
    		if (val == it.next()) {
    			return it.nextIndex();
    		}    		
    	}
        return -1;
    }
    
    public static boolean find_c_p(int val, Object node) throws Exception {
        if (node instanceof NodeC) {
            return val == ((NodeC)node).value;
        }
        throw new Exception("find_c_p");
    }

    public static class Tuple2<T, U> {
    	public T one;
    	public U two;
    	public Tuple2 (T one, U two) {
    		this.one = one;
    		this.two = two;
    	}
    }
    
    public static Tuple2<Object, Graph> push_c(int val, Graph gr) {
        var node = new NodeC(gr.nextId+1, val);
        var consts = new ArrayList<NodeC>();
        consts.add(node);
        consts.addAll(gr.constants);
        var gr1 = new Graph(gr.nextId+1, consts, gr.controls, gr.ugens);
        return new Tuple2<Object, Graph>(node, gr1);
    }

    public static Tuple2<Object, Graph> mk_node_c(Object ugen, Graph gr) throws Exception{
		try {
			var val = ((Constant<Integer>) ugen).value;
			for (var nd : gr.constants) {
				if (find_c_p(val, (Object) nd)) {
					return new Tuple2<Object, Graph>(nd, gr);
				}
			}
			return push_c(val, gr);
			
		} catch (Exception e) {
			throw new Exception("make_node_c");
		}
    }
      


}