package weg;

import java.util.ArrayList;
import java.util.Collections;
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

	public interface IUgen
	{
		boolean isUgen();
	}

	public interface INode
	{
		boolean isNode();
	}

	public static class Constant<T> implements IUgen{
		public T value;
		public boolean isUgen() { return true; }

		public Constant(T value) {
			this.value = value;
		}
	}

	public static class UgenL {
		public List<IUgen> l;

		// vararg constructor
		public UgenL(IUgen... values) {
			l = new ArrayList<IUgen>();
			for (var value : values) {
				this.l.add(value);
			}
		}
	}

	public static class Mce implements IUgen
	{
		public UgenL ugens;

		public boolean isUgen() { return true; }
		
		public Mce(UgenL ugens) {
			this.ugens = ugens;
		}
	}

	public static class Mrg implements IUgen
	{
		public IUgen left, right;

		public boolean isUgen() { return true; }
		
		public Mrg(IUgen left, IUgen right) {
			this.left = left;
			this.right = right;
		}
	}

	public static class Control implements IUgen {
		public String name;
		public int index = 0;
		public Rate rate = Rate.RateKr;

		public boolean isUgen() { return true; }

		public Control(String name) {
			this.name = name;
		}

		public Control rate(Rate rate) {
			this.rate = rate;
			return this;
		}

		public Control index(int index) {
			this.index = index;
			return this;
		}

	}

	public static class Primitive implements IUgen 
	{
		public Rate rate = Rate.RateKr;
		public UgenL inputs = new UgenL();
		public List<Rate> outputs = null;
		public String name;
		public int special = 0;
		public int index = 0;

		public boolean isUgen() { return true; }
		
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

	public static class Proxy implements IUgen {
		public Primitive primitive;
		public int index = 0;

		public boolean isUgen() { return true; }

		public Proxy(Primitive primitive) {
			this.primitive = primitive;
		}

		public Proxy index(int index) {
			this.index = index;
			return this;
		}
	}

	public static class NodeC implements INode{
		public int nid, value;
		public boolean isNode() { return true; }
		public NodeC(int nid, int value) {
			this.nid = nid;
			this.value = value;
		}
	}

	public static class NodeK implements INode{
		public int nid;
		public int deflt = 0;
		public String name;
		public Rate rate = Rate.RateKr;
		public boolean isNode() { return true; }

		public NodeK(int nid, String name) {
			this.name = name;
			this.nid = nid;
		}

		public NodeK deflt(int deflt) {
			this.deflt = deflt;
			return this;
		}

		public NodeK rate(Rate rate) {
			this.rate = rate;
			return this;
		}

	}

	public static class NodeU implements INode{
		public int nid;
		public String name;
		public UgenL inputs;
		public List<Rate> outputs;
		public int special = 0;
		public int ugenId;
		public Rate rate = Rate.RateKr;
		public boolean isNode() { return true; }

		public NodeU(int nid, String name, UgenL inputs, List<Rate> outputs, int ugenId) {
			this.nid = nid;
			this.name = name;
			this.inputs = inputs;
			this.outputs = outputs;
		}

		public NodeU special(int special) {
			this.special = special;
			return this;
		}

		public NodeU rate(Rate rate) {
			this.rate = rate;
			return this;
		}

	}

	public static class FromPortC implements IUgen{
		public int port_nid;
		public boolean isUgen() { return true; }

		public FromPortC(int port_nid) {
			this.port_nid = port_nid;
		}
	}

	public static class FromPortK implements IUgen{
		public int port_nid;
		
		public boolean isUgen() { return true; }

		public FromPortK(int port_nid) {
			this.port_nid = port_nid;
		}
	}

	public static class FromPortU implements IUgen {
		public int port_nid;
		public int port_idx;
		public boolean isUgen() { return true; }

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

		public Graph(int nextId, List<NodeC> constants, List<NodeK> controls, List<NodeU> ugens) {
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

		public Input(int u, int p) {
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

	public static Rate rate_of(IUgen ugen) {
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

	public static void printUgen(IUgen ugen) {
		if (ugen instanceof Control) {
			System.out.println("K: " + ((Control) ugen).name);
		} else if (ugen instanceof Primitive) {
			System.out.println("P: " + ((Primitive) ugen).name);
		} else if (ugen instanceof Constant<?>) {
			if (((Constant) ugen).value instanceof Integer) {
				System.out.println("C: " + Integer.toString((int) ((Constant) ugen).value));
			} else {
				System.out.println("C: " + Double.toString((double) ((Constant) ugen).value));
			}
		} else if (ugen instanceof Mce) {
			System.out.println("Mce: " + Integer.toString(((Mce) ugen).ugens.l.size()));
			printUgens(((Mce) ugen).ugens);
		} else if (ugen instanceof Mrg) {
			System.out.println("Mrg: ");
			System.out.print(" * left: ");
			printUgen(((Mrg) ugen).left);
			System.out.print(" * right: ");
			printUgen(((Mrg) ugen).right);
		} else if (ugen instanceof Proxy) {
			System.out.println("Proxy: ");
		} else {
			System.out.print(ugen);
		}

	}

	public static void printUgens(UgenL ugenl) {
		List<IUgen> ugens = ugenl.l;
		for (var ugen : ugens) {
			System.out.println(" - ");
			printUgen(ugen);
		}

	}

	public static List<Integer> iota(int n, int init, int step) {
		if (n == 0) {
			return new ArrayList<Integer>();
		} else {
			var outInit = List.of(init); // immutable!!
			var outv = new ArrayList<Integer>();
			outv.addAll(outInit);
			var retList = iota(n - 1, init + step, step);
			outv.addAll(retList);
			return outv;
		}
	}

	public static List<IUgen> extend(List<IUgen> iList, int newLen) {
		int ln = iList.size();
		var vout = new ArrayList<IUgen>();
		if (ln > newLen) {
			return iList.subList(0, newLen);
		} else {
			vout.addAll(iList);
			vout.addAll(iList);
			return extend(vout, newLen);
		}
	}

	public static int mce_degree(IUgen ugen) throws Exception {
		if (ugen instanceof Mce) {
			return ((Mce) ugen).ugens.l.size();
		} else if (ugen instanceof Mrg) {
			return mce_degree(((Mrg) ugen).left);
		} else {
			throw new Exception("Error: mce_degree");
		}
	}

	public static List<IUgen> mce_extend(int n, IUgen ugen) throws Exception {
		if (ugen instanceof Mce) {
			var iList = ((Mce) ugen).ugens.l;
			return extend(iList, n);
		} else if (ugen instanceof Mrg) {
			var ex = mce_extend(n, ((Mrg) ugen).left);
			int len = ex.size();
			if (len > 0) {
				var outInit = List.of(ugen); // immutable!!
				var outv = new ArrayList<IUgen>();
				outv.addAll(outInit);
				outv.addAll(ex.subList(1, n));
				return outv;
			} else {
				throw new Exception("mce_extend");
			}
		} else {
			var outv = new ArrayList<IUgen>();
			for (int ind = 0; ind < n; ind++) {
				outv.add(ugen);
			}
			return outv;
		}
	}

	public static <T> boolean is_mce(T ugen) {
		if (ugen instanceof Mce) {
			return true;
		} else {
			return false;
		}
	}

	public static void printLList(List<List<IUgen>> iList) {
		int len1 = iList.size();
		int len2 = iList.get(0).size();

		for (int ind1 = 0; ind1 < len1; ind1++) {
			for (int ind2 = 0; ind2 < len2; ind2++) {
				printUgen(iList.get(ind1).get(ind2));
				System.out.print(" ");
			}
			System.out.println("");
		}
	}

	public static <T> List<List<T>> transposer(List<List<T>> iList) {
		int len1 = iList.size();
		int len2 = iList.get(0).size();
		var outv = new ArrayList<List<T>>();
		for (int ind = 0; ind < len2; ind++) {
			outv.add(new ArrayList<T>());
		}
		for (int ind2 = 0; ind2 < len2; ind2++) {
			for (int ind1 = 0; ind1 < len1; ind1++) {
				outv.get(ind2).add(iList.get(ind1).get(ind2));
			}
		}
		return outv;
	}

	public static Mce mce_transform(IUgen ugen) throws Exception {
		if (ugen instanceof Primitive) {
			Primitive prim = ((Primitive) ugen);
			UgenL inputs = prim.inputs;
			var ins = inputs.l.stream().filter(x -> is_mce(x)).collect(Collectors.toList());
			List<Integer> degs = new ArrayList<Integer>();
			for (var elem : ins) {
				degs.add(mce_degree(elem));
			}
			int upr = max_num(degs, 0);
			var ext = new ArrayList<List<IUgen>>();
			for (var elem : inputs.l) {
				ext.add(mce_extend(upr, elem));
			}
			List<List<IUgen>> iet = transposer(ext);
			var outv = new ArrayList<IUgen>();
			// var outv = new UgenL();
			for (var elem2 : iet) {
				UgenL newInps = new UgenL();
				newInps.l = elem2;
				Primitive p = new Primitive(prim.name).inputs(newInps).outputs(prim.outputs).rate(prim.rate)
						.special(prim.special).index(prim.index);
				// outv.l.Add(p);
				outv.add(p);

			}
			UgenL newOut = new UgenL();
			newOut.l = outv;
			// return new Mce(ugens: outv);
			return new Mce(newOut);
		} else {
			throw new Exception("Error: mce_transform");
		}
	}

	public static IUgen mce_expand(IUgen ugen) throws Exception {
		if (ugen instanceof Mce) {
			var lst = new ArrayList<IUgen>();
			var ugens = ((Mce) ugen).ugens.l;
			for (var elem : ugens) {
				lst.add(mce_expand(elem));
			}
			UgenL outv = new UgenL();
			outv.l = lst;
			return new Mce(outv);
		} else if (ugen instanceof Mrg) {
			var left = ((Mrg) ugen).left;
			var right = ((Mrg) ugen).right;
			var ug1 = mce_expand(left);
			return new Mrg(ug1, right);
		} else {
			Function<IUgen, Boolean> rec = (IUgen ug) -> {
				if (ugen instanceof Primitive) {
					UgenL inputs = ((Primitive) ug).inputs;
					List<IUgen> ins = inputs.l.stream().filter(x -> is_mce(x)).collect(Collectors.toList());
					return (ins.size() > 0);
				} else
					return false;
			};
			if (rec.apply(ugen)) {
				try {
					return mce_expand(mce_transform(ugen));
				} catch (Exception e) {
					throw new Exception("mce_expand");
				}
			} else
				return ugen;
		}
	}

	public static IUgen mce_channel(int n, IUgen ugen) throws Exception {
		if (ugen instanceof Mce) {
			var ugens = ((Mce) ugen).ugens.l;
			return ugens.get(n);
		} else
			throw new Exception("Error: mce_channel");
	}

	public static UgenL mce_channels(IUgen ugen) throws Exception {
		if (ugen instanceof Mce) {
			UgenL ugens = ((Mce) ugen).ugens;
			return ugens;
		} else if (ugen instanceof Mrg) {
			var left = ((Mrg) ugen).left;
			var right = ((Mrg) ugen).right;
			UgenL lst = mce_channels(left);
			int len = lst.l.size();
			if (len > 1) {
				Mrg mrg1 = new Mrg(lst.l.get(0), right);
				var outInit = List.of(mrg1);
				var outv = new ArrayList<IUgen>();
				outv.addAll(outInit);
				outv.addAll(lst.l.subList(1, len));
				UgenL newOut = new UgenL();
				newOut.l = outv;
				return newOut;

			} else
				throw new Exception("Error: mce_channels");
		} else {
			var outv = new ArrayList<IUgen>();
			outv.add(ugen);
			UgenL newOut = new UgenL();
			newOut.l = outv;
			return newOut;
		}
	}

	public static IUgen proxify(IUgen ugen) throws Exception {
		if (ugen instanceof Mce) {
			var lst = new UgenL();
			for (var elem : ((Mce) ugen).ugens.l) {
				lst.l.add(proxify(elem));
			}
			return new Mce(lst);
		} else if (ugen instanceof Mrg) {
			var prx = proxify(((Mrg) ugen).left);
			return new Mrg(prx, ((Mrg) ugen).right);
		} else if (ugen instanceof Primitive) {
			var ln = ((Primitive) ugen).outputs.size();
			if (ln < 2) {
				return ugen;
			} else {
				var lst1 = iota(ln, 0, 1);
				var lst2 = new UgenL();
				for (var ind : lst1) {
					lst2.l.add(proxify(new Proxy((Primitive) ugen).index(ind)));
				}
				return new Mce(lst2);
			}

		} else {
			throw new Exception("proxify");
		}
	}

	public static IUgen mk_ugen(String name, UgenL inputs, List<Rate> outputs) throws Exception {
		var ind = 0;
		var sp = 0;
		var rate = Rate.RateKr;
		return mk_ugen(name, inputs, outputs, ind, sp, rate);
	}

	public static IUgen mk_ugen(String name, UgenL inputs, List<Rate> outputs, int ind) throws Exception {
		var sp = 0;
		var rate = Rate.RateKr;
		return mk_ugen(name, inputs, outputs, ind, sp, rate);
	}

	public static IUgen mk_ugen(String name, UgenL inputs, List<Rate> outputs, int ind, int sp) throws Exception {
		var rate = Rate.RateKr;
		return mk_ugen(name, inputs, outputs, ind, sp, rate);
	}

	public static IUgen mk_ugen(String name, UgenL inputs, List<Rate> outputs, int ind, int sp, Rate rate)
			throws Exception {
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

	public static int node_k_default(NodeK node) {
		return node.deflt;
	}

	public static MMap mk_map(Graph graph) {
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
			var index = it.nextIndex();
			if (val == it.next()) {
				return index;
			}
		}
		return -1;
	}

	public static boolean find_c_p(int val, INode node) throws Exception {
		if (node instanceof NodeC) {
			return val == ((NodeC) node).value;
		}
		throw new Exception("find_c_p");
	}

	public static class Tuple2<T, U> {
		public T one;
		public U two;

		public Tuple2(T one, U two) {
			this.one = one;
			this.two = two;
		}
	}

	public static Tuple2<INode, Graph> push_c(int val, Graph gr) {
		var node = new NodeC(gr.nextId + 1, val);
		var consts = new ArrayList<NodeC>();
		consts.add(node);
		consts.addAll(gr.constants);
		var gr1 = new Graph(gr.nextId + 1, consts, gr.controls, gr.ugens);
		return new Tuple2<INode, Graph>(node, gr1);
	}

	public static Tuple2<INode, Graph> mk_node_c(IUgen ugen, Graph gr) throws Exception {
		try {
			var val = ((Constant<Integer>) ugen).value;
			for (var nd : gr.constants) {
				if (find_c_p(val, nd)) {
					return new Tuple2<INode, Graph>(nd, gr);
				}
			}
			return push_c(val, gr);

		} catch (Exception e) {
			throw new Exception("mk_node_c");
		}
	}

	public static boolean find_k_p(String str, INode node) throws Exception {
		if (node instanceof NodeK) {
			return str == ((NodeK) node).name;
		}
		throw new Exception("find_k_p");
	}

	public static Tuple2<INode, Graph> push_k_p(IUgen ugen, Graph gr) throws Exception {
		if (ugen instanceof Control) {
			var ctrl1 = (Control) ugen;
			var node = new NodeK(gr.nextId + 1, ctrl1.name).deflt(ctrl1.index).rate(ctrl1.rate);
			var contrs = new ArrayList<NodeK>();
			contrs.add(node);
			contrs.addAll(gr.controls);
			var gr1 = new Graph(gr.nextId + 1, gr.constants, contrs, gr.ugens);
			return new Tuple2<INode, Graph>(node, gr1);
		}
		throw new Exception("push_k_p");
	}

	public static Tuple2<INode, Graph> mk_node_k(IUgen ugen, Graph gr) throws Exception {
		try {
			var name = ((Control) ugen).name;
			for (var nd : gr.controls) {
				if (find_k_p(name, nd)) {
					return new Tuple2<INode, Graph>(nd, gr);
				}
			}
			return push_k_p(ugen, gr);

		} catch (Exception e) {
			throw new Exception("mk_node_k");
		}
	}

	public static boolean find_u_p(Rate rate, String name, int id1, INode node) throws Exception {
		if (node instanceof NodeU) {
			var nu = (NodeU) node;
			return (rate == nu.rate) && (name == nu.name) && (id1 == nu.ugenId);
		}
		throw new Exception("find_u_p");
	}

	public static Tuple2<INode, Graph> push_u(IUgen ugen, Graph gr) throws Exception {
		if (ugen instanceof Primitive) {
			var pr1 = (Primitive) ugen;
			var node = new NodeU(gr.nextId + 1, pr1.name, pr1.inputs, pr1.outputs, pr1.index).special(pr1.special)
					.rate(pr1.rate);

			var ugens = new ArrayList<NodeU>();
			ugens.add(node);
			ugens.addAll(gr.ugens);
			var gr1 = new Graph(gr.nextId + 1, gr.constants, gr.controls, ugens);
			return new Tuple2<INode, Graph>(node, gr1);
		}
		throw new Exception("push_u_p");
	}

	public static IUgen as_from_port(INode node) throws Exception {
		if (node instanceof NodeC) {
			return new FromPortC(((NodeC) node).nid);
		} else if (node instanceof NodeK) {
			return new FromPortK(((NodeK) node).nid);
		} else if (node instanceof NodeU) {
			return new FromPortU(((NodeU) node).nid, 0);
		}
		throw new Exception("as_from_port");
	}
	
	public static Tuple2<List<INode>, Graph> acc(List<IUgen> ll, List<INode>nn, Graph gr) throws Exception  
	{
		if (ll.size() == 0) {
			Collections.reverse(nn);
			return new Tuple2<List<INode>, Graph>(nn, gr);
		}
		else {
			try {
				var ng = mk_node(ll.get(0), gr);
				var ng1 = ng.one;
				var ng2 = ng.two;
				nn.add(0, ng1);
				return acc(ll.subList(1, ll.size()), nn, ng2);				
			} catch (Exception e) {
				throw new Exception("mk_node_u acc");
			}
		}	
	}
	public static Tuple2<INode, Graph> mk_node_u(IUgen ugen, Graph gr) throws Exception {
		if (ugen instanceof Primitive) {
			var pr1 = (Primitive)ugen;
			var ng = acc(pr1.inputs.l, new ArrayList<INode>(), gr);
			var gnew = ng.two;
			var ng1 = ng.one;
			var inputs2 = new UgenL();
			for (var nd : ng1) {
				inputs2.l.add(as_from_port(nd));
			}
			var rate = pr1.rate;
			var name = pr1.name;
			var index = pr1.index;
			for (var nd2 : gnew.ugens) {
				if (find_u_p(rate, name, index, nd2)) {
					return new Tuple2<INode, Graph>(nd2, gnew);
				}
			}
			var pr = new Primitive(name).rate(rate).inputs(inputs2)
					.outputs(pr1.outputs).special(pr1.special);
			return push_u(pr, gnew);
		}
		throw new Exception("mk_node_u");
	}
	
	public static <T> Tuple2<INode, Graph> mk_node(Constant<T> ugen, Graph gr) throws Exception {
		return mk_node_c(ugen, gr);
	}

	public static Tuple2<INode, Graph> mk_node(Primitive ugen, Graph gr) throws Exception {
		return mk_node_u(ugen, gr);
	}

	public static Tuple2<INode, Graph> mk_node(IUgen ugen, Graph gr) throws Exception {
		try {
			var gn = mk_node(((Mrg)ugen).right, gr);
			var g1 = gn.two;
			return mk_node(((Mrg)ugen).left, g1);			
		} catch (Exception e) {
			throw new Exception("mk_node");
		}
	}
	
	public static NodeU sc3_implicit (int num) {
	    var rates = new ArrayList<Rate>();
	    for (var ind = 1; ind < num+1; ind++) {
	        rates.add(Rate.RateKr);
	    }
	    var node = new NodeU(-1, "Control", new UgenL(), rates,0)
	    		.special(0) .rate(Rate.RateKr);
	    return node;
	}
	
	public static IUgen mrg_n(UgenL lst) {
	    if (lst.l.size() == 1) {
	        return lst.l.get(0);
	    }
	    else if (lst.l.size() == 2) {
	        return new Mrg(lst.l.get(0), lst.l.get(1));
	    }
	    else {
			var newUL = new UgenL();
			var newLst = lst.l.subList(1, lst.l.size());
			newUL.l.addAll(newLst);			
	        return new Mrg(lst.l.get(0), mrg_n(newUL));
	    }
	}
	
	public static IUgen prepare_root(IUgen ugen) {
	    if (ugen instanceof Mce) {
	        return mrg_n(((Mce)ugen).ugens);
	    }
	    else if (ugen instanceof Mrg) {
	        return new Mrg(prepare_root(((Mrg)ugen).left), prepare_root(((Mrg)ugen).right));
	    }
	    else {
	        return ugen;
	    }
	}

	public static Graph empty_graph() {
	    return new Graph(0, new ArrayList<NodeC>(), new ArrayList<NodeK>(), new ArrayList<NodeU>());
	}

	public static Graph synth(IUgen ugen) throws Exception {
		try {
		    var root = prepare_root(ugen);
		    var gn = mk_node(root, empty_graph());
		    var gr = gn.two;
		    var cs = gr.constants;
		    var ks = gr.controls;
		    var us = gr.ugens;
		    var us1 = us;
		    Collections.reverse(us1);
		    if (ks.size() != 0) {
		    	var node = sc3_implicit(ks.size());
		    	us1.add(0, node);
		    }
		    var grout = new Graph(-1, cs, ks, us1);
		    return grout;
			
		} catch (Exception e) {
			throw new Exception("synth");
		}
	}


}