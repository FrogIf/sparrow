package sch.frog.sparrow.doodle;

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
        char[] chars = { 'a', 'b', 'c', 'd' };
        for(int i = 0; i < 24; i++){
            int[] indices = invCantorExpansion(i, 4);
            System.out.println(Arrays.toString(new char[]{
                    chars[indices[0] - 1],
                    chars[indices[1] - 1],
                    chars[indices[2] - 1],
                    chars[indices[3] - 1]
            }) + " -- " + i);
        }
//        char[] chars = { 'a', 'b', 'c', 'd' };
//        ArrayList<char[]> permutations = new ArrayList<>(24); // A(4, 4)
//
//        int[] path = { -1, -1, -1, -1 };
//        int d = 0;
//        outer: while(true){
//            path[d] = path[d] + 1;
//            d++;
//            if(d == 4){
//                permutations.add(new char[]{chars[path[0]], chars[path[1]], chars[path[2]], chars[path[3]]});
//                d--;
//                while(path[d] == 3){
//                    path[d] = -1;
//                    d--;
//                    if(d == -1){ break outer; }
//                }
//            }
//        }
//
//        for (char[] permutation : permutations) {
//            System.out.println(permutation);
//        }
    }

}
