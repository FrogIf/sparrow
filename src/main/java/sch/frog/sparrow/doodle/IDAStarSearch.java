package sch.frog.sparrow.doodle;

import java.util.LinkedList;
import java.util.Stack;

public class IDAStarSearch {

    public static void main(String[] args){
        int origin = 0;
        int target = 100000000;
        LinkedList<Integer> path = minStep(origin, target);
        if(path != null){
            System.out.print(0);
            for (Integer m : path) {
                System.out.print(" + " + m);
            }
            System.out.println(" = " + target);
        }
    }

    private static final int[] moves = { 1, 7, 8, 9 };
    private static final int MAX_MOVE = 9; // moves的最大值
    private static final int MIN_MOVE = 1; // moves的最小值

    /**
     * 搜索经过moves中的动作, 从originalState状态到达targetState状态的最短路径
     * 或者说, targetState = originalState + m1 + m2 + ..., 求这个最短的序列{m1, m2, m3, ... }
     */
    private static LinkedList<Integer> minStep(int originState, int targetState){
        double maxDepth = Math.ceil((targetState - originState) * 1.0 / MIN_MOVE);
        int depth = distanceEstimate(originState, targetState) - 1; // 用启发函数直接算出深度下界
        while(depth <= maxDepth){
            LinkedList<Integer> path = dfsWithHFunction(originState, targetState, depth);
            if(path != null){
                return path;
            }
            depth++;
        }

        return null;
    }

    // 带有启发函数的深度优先
    private static LinkedList<Integer> dfsWithHFunction(int originState, int targetState, int depth){
        Stack<Node> stack = new Stack<>();
        Node start = new Node(originState, originState, 0);
        stack.push(start);
        while(!stack.isEmpty()){
            Node parent = stack.pop();
            for(int i = moves.length - 1; i >= 0; i--){
                int m = moves[i];
                int sum = parent.sum + m;
                Node node = new Node(sum, m, parent.depth + 1);
                node.parent = parent;
                if(sum == targetState){
                    return buildPath(node);
                }else if(sum < targetState && distanceEstimate(sum, targetState) + node.depth <= depth){    // 这里会进行判断, 不符合深度要求的, 直接会进行剪枝
                    stack.push(node);
                }
            }
        }
        return null;
    }

    private static LinkedList<Integer> buildPath(Node tail){
        LinkedList<Integer> path = new LinkedList<>();
        Node cursor = tail;
        while(cursor.parent != null){
            path.addFirst(cursor.move);
            cursor = cursor.parent;
        }
        return path;
    }

    /**
     * 估计当前状态到目标状态的距离, 这个距离一定小于等于实际距离
     * 这个就是启发函数
     */
    private static int distanceEstimate(int originState, int targetState){
        return (targetState - originState) / MAX_MOVE;
    }

    private static class Node{
        private final int sum;
        private final int move;
        private final int depth;
        private Node parent;
        public Node(int sum, int move, int depth) {
            this.sum = sum;
            this.move = move;
            this.depth = depth;
        }
    }
}
