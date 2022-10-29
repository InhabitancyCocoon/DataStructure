package sgt;

import java.util.*;
import java.io.*;

class Node {
	int ls, rs;
	int val = 1;
	boolean add = false;
}

public class ColorProblem {
	
	int N, cnt = 1, M = (int)1e6;
	
	Node[] tree;
	
	void lazyCreate(int u) {
		if (tree[u] == null) {
			tree[u] = new Node();
		}
		if (tree[u].ls == 0) {
			tree[u].ls = ++cnt;
			tree[tree[u].ls] = new Node();
		}
		if (tree[u].rs == 0) {
			tree[u].rs = ++cnt;
			tree[tree[u].rs] = new Node();
		}
	}
	
	void spread(int u) {
		if (tree[u].add) {
			tree[tree[u].ls].add = tree[u].add;
			tree[tree[u].rs].add = tree[u].add;
			tree[tree[u].ls].val = tree[u].val;
			tree[tree[u].rs].val = tree[u].val;
			tree[u].add = false;
		}
	}
	
	void backTrace(int u) {
		//System.out.println(Integer.toBinaryString(tree[tree[u].ls].val));
		//System.out.println(Integer.toBinaryString(tree[tree[u].rs].val));
		tree[u].val = tree[tree[u].ls].val | tree[tree[u].rs].val;
	}
	
	void update(int u, int lc, int rc, int l, int r, int v) {
		if (lc >= l && rc <= r) {
			tree[u].add = true;
			tree[u].val = 1 << v - 1;
			return;
		}
		lazyCreate(u);
		spread(u);
		int mid = (lc + rc) >> 1;
		if (l <= mid) {
			update(tree[u].ls, lc, mid, l, r, v);
		}
		if (r > mid) {
			update(tree[u].rs, mid + 1, rc, l, r, v);
		}
		backTrace(u);
	}
	
	int query(int u, int lc, int rc, int l, int r) {
		if (lc >= l && rc <= r) {
			return tree[u].val;
		}
		lazyCreate(u);
		spread(u);
		if (tree[u].add) {
			return tree[u].val;
		}
		int mid = (lc + rc) >> 1, res = 0;
		if (l <= mid) {
			res |= query(tree[u].ls, lc, mid, l, r);
		}
		if (r > mid) {
			res |= query(tree[u].rs, mid + 1, rc, l, r);
		}
		return res;
	}
	
	static int getColorCount(int x) {
		//System.out.println(Integer.toBinaryString(x));
		int cnt = 0;
		while (x > 0) {
			cnt += x & 1;
			x = x >> 1;
		}
		return cnt;
	}
	
	void solve () throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		int L, T, O;
		String[] s = in.readLine().split(" ");
		L = Integer.parseInt(s[0]);
		T = Integer.parseInt(s[1]);
		O = Integer.parseInt(s[2]);
		N =  L + 7;
		tree = new Node[M];
		int l, r, c;
		while (O-- > 0) {
			s = in.readLine().split(" ");
			l = Integer.parseInt(s[1]);
			r = Integer.parseInt(s[2]);
			if (l > r) {
				int tmp = l;
				l = r;
				r = tmp;
			}
			if (s[0].equals("C")) {
				c = Integer.parseInt(s[3]);
				update(1, 1, N, l, r, c);
			} else {
				int res = query(1, 1, N, l, r);
				System.out.println(getColorCount(res));
			}
		}
	}
	
	public static void main(String[] agrs) throws IOException {
		new ColorProblem().solve();
	}
}

