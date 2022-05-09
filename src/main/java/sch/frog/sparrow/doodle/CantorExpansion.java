package sch.frog.sparrow.doodle;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 康托展开
 */
public class CantorExpansion {

    public static void main(String[] args){
        demoFullPermutation();
//        System.out.println(cantorExpansion(new int[]{4, 2, 1, 3}));
//        System.out.println(Arrays.toString(invCantorExpansion(20, 4)));
    }

    /*
     * 求一个自然数序列在康托展开下的结果
     * 注: 没有做数据溢出相关处理
     */
    public static int cantorExpansion(int[] nums){
        int result = 0;
        for(int i = 0; i < nums.length - 1; i++){
            int a = nums[i];
            int m = 0;
            for(int j = i + 1; j < nums.length; j++){
                if(a > nums[j]){
                    m++;
                }
            }
            result = (result + m) * (nums.length - i - 1);
        }
        return result;
    }

    public static int[] invCantorExpansion(int num, int n){
        int[] a = new int[n];
        for(int i = 0; i < n; i++){
            a[n - i - 1] = num % (i + 1);
            num /= (i + 1);
        }
        int[] used = new int[n];
        int[] result = new int[n];
        for(int i = 0; i < n; i++){
            int j = 0;
            int p = 0;
            int l = a[i] + 1;
            while(true){
                if(used[j] == 0){
                    p++;
                }
                if(p == l){
                    break;
                }
                j++;
            }
            used[j] = 1;
            result[i] = j + 1;
        }
        return result;
    }


    // 生成一个序列的全排列
    public static void demoFullPermutation(){
        int[] elements = new int[]{ 1, 2, 3, 4 };
        boolean[] used = new boolean[elements.length];
        ArrayList<int[]> resultContainer = new ArrayList<>();
        dfsSearch(1, used, elements, new int[elements.length], resultContainer);
        for (int[] arr : resultContainer) {
            System.out.println(Arrays.toString(arr));
        }
    }

    private static void dfsSearch(int depth, boolean[] used, int[] optionalValue, int[] record, ArrayList<int[]> resultContainer){
        if(depth > optionalValue.length){
            resultContainer.add(Arrays.copyOf(record, record.length));
        }else{
            for(int i = 0; i < optionalValue.length; i++){
                if(!used[i]){
                    used[i] = true;
                    record[depth - 1] = optionalValue[i];
                    dfsSearch(depth + 1, used, optionalValue, record, resultContainer);
                    used[i] = false;
                }
            }
        }

    }

}
