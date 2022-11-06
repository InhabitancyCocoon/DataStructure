package sgt;

import java.util.*;
import java.io.*;

public class ScanLineProblem {
	
	static class Node {
		int l, r, val;
		double len;
		public Node(int l, int r, int val, double len) {
			this.l = l;
			this.r = r;
			this.val = val;
			this.len = len;
		}
	}
	
	static class Edge implements Comparable<Edge>{
		double x, y1, y2;
		int flag;
		public Edge(double x, double y1, double y2, int flag) {
			this.x = x;
			this.y1 = y1;
			this.y2 = y2;
			this.flag = flag;
		}
		public int compareTo(Edge o) {
			return Double.compare(x, o.x);
		}
	}
	
	static int N = 207, k = 1;
	static Node[] tree;
	static List<Edge> edges;
	static Double[] uniqueY;
	
	static void build(int u, int l, int r) {
		//System.out.println(u + " " + l + " " + r);
		tree[u] = new Node(l, r, 0, 0D);
		if (l == r) {
			return;
		}
		int mid = (l + r) >> 1;
		build(u << 1, l, mid);
		build(u << 1 | 1, mid + 1, r);
	}
	
	static void pushUp(int u) {
		if (tree[u].val > 0) {
			tree[u].len = uniqueY[tree[u].r + 1] - uniqueY[tree[u].l];
		} else if (tree[u].l == tree[u].r) {
			tree[u].len = 0;
		} else {
			tree[u].len = tree[u << 1].len + tree[u << 1 | 1].len;
		}
	}
	
	static void update(int u, int st, int ed, int val) {
		if (st <= tree[u].l && ed >= tree[u].r) {
			tree[u].val += val;
		} else {
			int mid = (tree[u].l + tree[u].r) >> 1;
			if (st <= mid) {
				update(u << 1, st, ed, val);
			}
			if (ed > mid) {
				update(u << 1 | 1, st, ed, val);
			}
		}
		pushUp(u);
	}
	
	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String[] s = reader.readLine().split(" ");
		int n = Integer.parseInt(s[0]);
		while (n != 0) {
			edges = new LinkedList<Edge>();
			tree = new Node[N << 2];
			HashSet<Double> uniqueVals = new HashSet<Double>();
			for (int i = 0; i < n; i++) {
				s = reader.readLine().split(" ");
				double x1 = Double.parseDouble(s[0]), x2 = Double.parseDouble(s[2]);
				double y1 = Double.parseDouble(s[1]), y2 = Double.parseDouble(s[3]);
				edges.add(new Edge(x1, y1, y2, 1));
				edges.add(new Edge(x2, y1, y2, -1));
				uniqueVals.add(y1);
				uniqueVals.add(y2);
			}
			uniqueY = uniqueVals.toArray(new Double[0]);
			Arrays.sort(uniqueY);
			Collections.sort(edges);
//			for (var e : edges) {
//				System.out.println(e.x);
//			}
			build(1, 0, uniqueVals.size());
//			int idx = 0;
//			while (tree[idx] != null) {
//				idx++;
//			}
//			System.out.print(idx);
			double ans = 0;
			for (int i = 0; i < edges.size(); i++) {
				Edge e = edges.get(i);
				if (i > 0) {
					ans += tree[1].len * (e.x - edges.get(i - 1).x);
				}
				int l = Arrays.binarySearch(uniqueY, e.y1);
				int r = Arrays.binarySearch(uniqueY, e.y2);
				//System.out.print(l + " " + r + " " + e.flag + " ");
				update(1, l, r - 1, e.flag);
				//System.out.println(tree[1].len + " " + ans);
			}
			System.out.println("Test case #" + k);
			k++;
			System.out.println(String.format("Total explored area: %.2f", ans));
			System.out.println();
			s = reader.readLine().split(" ");
			n = Integer.parseInt(s[0]);
		}
	}
	
}  
