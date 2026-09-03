package contest.algo;

/**
 * Система непересекающихся множеств (DSU / Union-Find).
 *
 * Отвечает на вопрос «лежат ли два элемента в одной компоненте» почти за O(1).
 * Нужна там, где объекты постепенно склеиваются в группы: связность графа при
 * добавлении рёбер, задачи «когда всё станет одним куском», Краскал.
 *
 * Две оптимизации, без которых это работает за O(n) на запрос:
 *  - сжатие путей: после поиска все узлы пути подвешиваются прямо к корню;
 *  - объединение по рангу: меньшее дерево цепляется к большему, чтобы не росла высота.
 * Вместе дают почти константу (обратная функция Аккермана).
 *
 * ВАЖНО: DSU умеет только объединять. Разъединять она не умеет — если в задаче
 * рёбра УДАЛЯЮТСЯ, разворачивайте процесс во времени и добавляйте их с конца.
 */
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

    /**
     * Корень компоненты. Реализация итеративная намеренно: рекурсия на цепочке
     * в 10^5 элементов даёт StackOverflowError, а на контесте стек не увеличить.
     */
    public int find(int x) {
        if (x < 0 || x >= parent.length) {
            throw new IndexOutOfBoundsException(
                    "элемент " + x + " вне диапазона 0.." + (parent.length - 1));
        }
        int root = x;
        while (parent[root] != root) {
            root = parent[root];
        }
        // Второй проход — сжатие путей.
        while (parent[x] != root) {
            int next = parent[x];
            parent[x] = root;
            x = next;
        }
        return root;
    }

    /** Возвращает true, если элементы были в разных компонентах и мы их объединили. */
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

    /** Сколько сейчас компонент связности. Часто это и есть ответ задачи. */
    public int componentCount() {
        return componentCount;
    }
}
