package sch.frog.sparrow.doodle;

import java.util.LinkedList;

/**
 * 迭代加深搜索算法demo
 */
public class IDAlgorithm {

    public static void main(String[] args){
        int origin = 23;
        int target = 160;
        LinkedList<Integer> path = IDSearch(origin, target);
        if(path != null){
            System.out.print(origin);
            for (Integer m : path) {
                System.out.print(" + " + m);
            }
            System.out.println(" = " + target);
        }
    }

    private final static int[] moves = { 1, 7, 8, 9 };

    /**
     * 搜索经过moves中的动作, 总originalState状态到达targetState状态的最短路径
     * 或者说, targetState = originalState + m1 + m2 + ..., 求这个最短的序列{m1, m2, m3, ... }
     */
    public static LinkedList<Integer> IDSearch(int originalState, int targetState){
        int maxDepth = (int) Math.ceil((targetState - originalState) / 1.0); // 1.0是moves的最小值
        int depth = 1;
        while(depth < maxDepth){
            LinkedList<Integer> path = doSearch(originalState, targetState, 0, depth);
            if(path != null){
                return path;
            }
            depth++;
        }
        return null;
    }

    public static LinkedList<Integer> doSearch(int origin, int target, int depth, int limit){
        if (depth <= limit) {
            for (int move : moves) {
                int result = origin + move;
                if (result == target) { // 如果找到
                    LinkedList<Integer> path = new LinkedList<>();
                    path.add(move);
                    return path;
                }else{
                    LinkedList<Integer> path = doSearch(result, target, depth + 1, limit);
                    if (path != null) { // 如果找到
                        path.addFirst(move);
                        return path;
                    }
                }
            }
        }
        return null;
    }

}
