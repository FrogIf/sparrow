package sch.frog.sparrow.doodle;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 广度优先搜索
 */
public class BFS {

    public static void main(String[] args){
        int maxRange = 60;
        int[] steps = buildMinStepTable(maxRange);
        for(int num = 0; num < maxRange; num++){
            System.out.println(num + 1 + " --- " + steps[num]);
        }
    }

    private final static int[] moves = { 1, 7, 8, 9 };

    /**
     * 构建最小步表格
     * 由moves中的元素重复相加, 组成一系列数, 求出[1,maxRange]范围内, 每个数需要的最少moves元素个数
     */
    private static int[] buildMinStepTable(int maxRange){
        Queue<Integer> queue = new LinkedList<>();
        int[] steps = new int[maxRange];
        queue.offer(0);
        int depth = 0;
        while(!queue.isEmpty()){
            depth++;
            int size = queue.size();
            while(size > 0){ // 遍历当前层的所有元素, 如果等于0, 说明当前层遍历完成了, 跳出循环, 深度加1
                Integer node = queue.poll();
                size--;
                for (int move : moves) {
                    int next = node + move;
                    if(next <= maxRange){
                        if(steps[next - 1] == 0){
                            steps[next - 1] = depth;
                        }
                        queue.offer(next);  // 将下一层的元素放入队列中
                    }
                }
            }
        }
        return steps;
    }
}
