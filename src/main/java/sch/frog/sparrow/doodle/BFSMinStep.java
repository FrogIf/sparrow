package sch.frog.sparrow.doodle;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 采用广度优先搜索的最小步查找
 */
public class BFSMinStep {

    public static void main(String[] args){
        int target = 150;
        LinkedList<Integer> path = minStepSearch(target);
        if(path != null){
            System.out.print(0);
            for (Integer m : path) {
                System.out.print(" + " + m);
            }
            System.out.println(" = " + target);
        }
    }

    private final static int[] moves = { 1, 7, 8, 9 };

    private static LinkedList<Integer> minStepSearch(int targetSum){
        Queue<Node> queue = new LinkedList<>();
        queue.offer(new Node(0, 0, 0));
        while(!queue.isEmpty()){
            Node parent = queue.poll();
            for (int move : moves) {
                int next = move + parent.value;
                Node node = new Node(move, next, parent.depth + 1);
                node.parent = parent;
                if(next == targetSum){
                    return buildPath(node);
                }else if(next < targetSum){
                    queue.offer(node);
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

    private static class Node{
        private Node parent; // 当前节点的父节点
        private final int value; // 当前节点累加值
        private final int move; // 当前节点代表的移动步数
        private final int depth;  // 当前节点到起始节点的距离

        public Node(int move, int value, int depth) {
            this.value = value;
            this.move = move;
            this.depth = depth;
        }
    }

}
