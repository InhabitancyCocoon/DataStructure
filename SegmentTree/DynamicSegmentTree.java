public class DynamicSegmentTree {
	/**
	 * The node in segment tree.
	 * ls: left child.
	 * rs: right child.
	 * add: lazy mark.
	 * val: interval sum.
	 */
	class Node {
		int ls, rs, add, val;
	}
  
	/**
	 * N   represents the range, M is the query count.
	 * cnt represents the node index.
	 * We use an array to implement segment tree.
	 */
	int N = (int)1e9, M = (int)1e5, cnt = 1;
	Node[] tree = new Node[M];
	
	/**
	 * Create the node, its left child and right child if they doesn't exist.
	 * 
	 * @param u the node index 
	 */
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
	
	/**
	 * Spread the lazy mark down to its child. 
	 * 
	 * @param u    the node index
	 * @param len  length of the interval which tree[u] represents
	 */
	void spread(int u, int len) {
		if (tree[u].add != 0) {
			tree[tree[u].ls].add += tree[u].add;
			tree[tree[u].rs].add += tree[u].add;
			tree[tree[u].ls].val += (len - len / 2) * tree[u].add;
			tree[tree[u].rs].val += (len / 2) * tree[u].add;
			tree[u].add = 0;
		}
	}
	
	/**
	 * After we find the target node recursively, we have to update the vals
	 * that all the nodes which on the path hold.  
	 * 
	 * @param u
	 */
	void backTrace(int u) {
		tree[u].val = tree[tree[u].ls].val + tree[tree[u].rs].val;
	}
	
	
	/**
	 * Add the {@param v} to the value of each point on the interval [l, r]
	 * 
	 * @param u    the node index
	 * @param lc   left endpoint of the interval which tree[u] represent
	 * @param rc   right endpoint of the interval which tree[u] represent
	 * @param l    left endpoint of the interval we want to update
	 * @param r    right endpoint if the interval we want to update
	 * @param v    the value to add
	 */
	void update(int u, int lc, int rc, int l, int r, int v) {
		if (l <= lc && r >= rc) {
			tree[u].val += v;
			tree[u].add += v;
			return;
		}
		lazyCreate(u);
		spread(u, rc - lc + 1);
		int mid = (lc + rc) >> 1;
		if (l <= mid) {
			update(tree[u].ls, lc, mid, l, r, v);
		}
		if (r > mid) {
			update(tree[u].rs, mid + 1, rc, l, r, v);
		}
		backTrace(u);
	}
	
	/**
	 * Get the sum of the interval [l, r]
	 * 
	 * @param u    the node index
	 * @param lc   left endpoint of the interval which tree[u] represent
	 * @param rc   right endpoint of the interval which tree[u] represent
	 * @param l    left endpoint of the interval we want to update
	 * @param r    right endpoint if the interval we want to update
	 * @return     the interval sum
	 */
	int query(int u, int lc, int rc, int l, int r) {
		if (l <= lc && r >= rc) {
			return tree[u].val;
		}
		lazyCreate(u);
		spread(u, rc - lc + 1);
		int mid = (lc + rc) >> 1, res = 0;
		if (l <= mid) {
			res += query(tree[u].ls, lc, mid, l, r);
		}
		if (r > mid) {
			res += query(tree[u].rs, mid + 1, rc, l, r);
		}
		return res;
	}
}
