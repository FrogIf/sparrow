package sch.frog.sparrow.doodle;

/**
 * 倒水问题:
 *     有两个水瓶, 一个容量是9升, 一个容量是4升. 问如何才能从河中打出6升水.
 */
public class PourWaterPuzzle {

    public static void main(String[] args){
        final int ACapacity = 35; // A的容积
        final int BCapacity = 14; // B的容积
        final int targetWater = 14; // 目标水量
        int[] result = gcdEx(ACapacity, BCapacity);
        if(targetWater % result[0] > 0){
            System.out.println("无解");
            return;
        }else{
            int f = targetWater / result[0];
            int aStepCount = result[1] * f;
            int bStepCount = result[2] * f;
            step(aStepCount, bStepCount, 0, 0, ACapacity, BCapacity);
        }

        // ------最短步数方案-------
        System.out.println("----最佳方案----");
        int[] generalSolutionCoefficient = generalSolution(ACapacity, BCapacity, targetWater, result);
        int[] solution = searchMinSolution(generalSolutionCoefficient);
        step(solution[0], solution[1], 0, 0, ACapacity, BCapacity);
    }

    /**
     * 根据丢番图方程通解系数, 获取通解的最小值
     */
    private static int[] searchMinSolution(int[] gsc){
        int m = Math.max(Math.abs(gsc[0] / gsc[2]), Math.abs(gsc[1] / gsc[3])) + 1;
        int min = Integer.MAX_VALUE;
        int k = 0;
        for(int i = -m; i <= m; i++){
            int temp = Math.abs(gsc[0] - i * gsc[2]) + Math.abs(gsc[1] + i * gsc[3]);
            if(temp < min){
                min = temp;
                k = i;
            }
        }
        return new int[]{ gsc[0] - k * gsc[2], gsc[1] + k * gsc[3] };
    }

    /**
     * 根据扩展欧几里得算法得到的解, 生成通解系数
     * 返回通解系数数组:
     *      { x1, y1, b/g, a/g }
     */
    private static int[] generalSolution(int a, int b, int c, int[] gcdExResult){
        int g = gcdExResult[0];
        int x0 = gcdExResult[1];
        int y0 = gcdExResult[2];
        int x1 = x0 * c / g;
        int x2 = y0 * c / g;
        return new int[]{ x1, x2, b/g, a/g };
    }

    private static int[] gcdEx(int a, int b){
        int t;
        if(a < b){
            t = a;
            a = b;
            b = t;
        }
        if(b > 0){
            int[] arr = gcdEx(b, a % b);
            return new int[]{ arr[0], arr[2], arr[1] - arr[2] * (a / b) };
        }else{
            return new int[]{ a, 1, 0 };
        }
    }


    private static void step(int x, int y, int a, int b, final int A, final int B){
        if(x == 0 && y == 0){
            System.out.println("finish");
            return;
        }
        if(x > 0 && a == 0){
            a = A;
            System.out.print("将A中装满水");
            x--;
        }else if(y > 0 && b == 0){
            b = B;
            System.out.print("将B中装满水");
            y--;
        }else if(x < 0 && a == A){
            a = 0;
            System.out.print("将A中的水倒掉");
            x++;
        }else if(y < 0 && b == B){
            b = 0;
            System.out.print("将B中的水倒掉");
            y++;
        }else if(y < 0){
            int moveVal = Math.min(B - b, a); // 判断B中剩余的空间和A中现有水量哪个小
            b += moveVal;
            a -= moveVal;
            System.out.print("将A中的水倒入B中");
        }else if(x < 0){
            int moveVal = Math.min(A - a, b);
            b -= moveVal;
            a += moveVal;
            System.out.print("将B中的水倒入A中");
        }else{
            throw new IllegalStateException("未知的状况, a : " + a + ", b : " + b);
        }

        System.out.println(" -- A中有" + a + "升水, B中有" + b + "升水.");

        step(x, y, a, b, A, B);
    }

}
