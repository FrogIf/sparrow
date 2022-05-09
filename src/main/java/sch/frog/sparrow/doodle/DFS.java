package sch.frog.sparrow.doodle;

import java.util.Arrays;

/**
 * 深度优先搜索算法演示
 */
public class DFS {

    public static void main(String[] args){
        int len = 3;
        dfsSearch(6, 0, 1, new int[len], len);
    }

    /**
     * 把正整数num分解为3个不同的正整数, 如: 6 = 1 + 2 + 3, 排在后面的数必须大于等于前面的数, 输出所有方案.
     * @param num 需要分解的正整数
     * @param min 最小值
     * @param depth 搜索深度
     * @param arr 结果数组
     * @param limit 最大深度
     */
    private static void dfsSearch(int num, int min, int depth, int[] arr, int limit){
        if(num == 0){
            System.out.println(Arrays.toString(arr));
        }else if(depth <= limit){
            for(int j = min; j <= num; j++){
                arr[depth - 1] = j;
                dfsSearch(num - j, j, depth + 1, arr, limit);
            }
        }
    }

}
