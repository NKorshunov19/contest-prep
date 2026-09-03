package contest.algo;

public class DisjointSetUnion {

    private final int[] parent;
    private final int[] rank;
    private int componentCount;

    public DisjointSetUnion(int size) {
        if (size < 0) {
            throw new IllegalArgumentException("размер не может быть отрицательным: " + size);
        }
        parent = new int[size];
        rank = new int[size];
        componentCount = size;
        for (int i = 0; i < size; i++) {
            parent[i] = i;
        }
    }

    public int find(int x) {
        if (x < 0 || x >= parent.length) {
            throw new IndexOutOfBoundsException(
                    "элемент " + x + " вне диапазона 0.." + (parent.length - 1));
        }
        int root = x;
        while (parent[root] != root) {
            root = parent[root];
        }
        // второй проход — сжатие путей
        while (parent[x] != root) {
            int next = parent[x];
            parent[x] = root;
            x = next;
        }
        return root;
    }

    public boolean union(int a, int b) {
        int rootA = find(a);
        int rootB = find(b);
        if (rootA == rootB) {
            return false;
        }
        if (rank[rootA] < rank[rootB]) {
            int tmp = rootA;
            rootA = rootB;
            rootB = tmp;
        }
        parent[rootB] = rootA;
        if (rank[rootA] == rank[rootB]) {
            rank[rootA]++;
        }
        componentCount--;
        return true;
    }

    public boolean connected(int a, int b) {
        return find(a) == find(b);
    }

    public int componentCount() {
        return componentCount;
    }
}
