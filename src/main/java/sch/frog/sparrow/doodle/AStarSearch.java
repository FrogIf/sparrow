package sch.frog.sparrow.doodle;

import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * A*搜索算法
 */
public class AStarSearch {

    public static void main(String[] args){
        int origin = 0;
        int target = 150;
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

    /**
     * 搜索经过moves中的动作, 从originalState状态到达targetState状态的最短路径
     * 或者说, targetState = originalState + m1 + m2 + ..., 求这个最短的序列{m1, m2, m3, ... }
     */
    private static LinkedList<Integer> minStep(int originalState, int targetState){
        PriorityQueue<Node> queue = new PriorityQueue<>((a, b) -> {
            int totalDistanceA = distanceEstimate(a.value, targetState) + a.depth;
            int totalDistanceB = distanceEstimate(b.value, targetState) + b.depth;
            return Integer.compare(totalDistanceA, totalDistanceB);
        });
        Node root = new Node(0, originalState, 0);
        queue.offer(root);
        while(!queue.isEmpty()){
            Node parent = queue.poll();
            for(int move : moves){
                int next = move + parent.value;
                Node node = new Node(move, next, parent.depth + 1);
                node.parent = parent;
                if(next == targetState){ // 找到最短路径
                    return buildPath(node);
                }else if(next < targetState){
                    queue.offer(node);
                }
            }
        }
        return null;
    }

    /**
     * 估计当前状态到目标状态的距离, 这个距离一定小于等于实际距离
     * 这个就是启发函数
     */
    private static int distanceEstimate(int originState, int targetState){
        return (targetState - originState) / MAX_MOVE;
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

    private static class Node{
        private Node parent; // 当前节点的父节点
        private final int value; // 当前节点累加值
        private final int move; // 当前节点代表的移动步数
        private final int depth;  // 当前节点到起始节点的距离
        public Node(int move, int value, int currentToRootDistance) {
            this.move = move;
            this.value = value;
            this.depth = currentToRootDistance;
        }
    }

}
