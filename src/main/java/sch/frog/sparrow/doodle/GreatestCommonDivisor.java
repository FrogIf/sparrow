package sch.frog.sparrow.doodle;

import java.util.Random;

/**
 * 欧几里得算法计算最大公约数
 */
public class GreatestCommonDivisor {

    public static void main(String[] args){
        Random r = new Random();

        int step = 0;
        for(int i = 0; i < 1000000000; i++){
            if(i % 10000000 == 0){
                System.out.println("process -- " + step++);
            }
            gcdTest(r.nextInt(1000), r.nextInt(1000));
        }
        System.out.println("finish");
    }

    private static void gcdTest(int a, int b){
        if(a == 0 || b == 0){ return; }
        int r1 = gcd(a, b);
        int r2 = gcdOpt(a, b);
        int r3 = gcdLoop(a, b);
        int r4 = gcdStein(a, b);
        int r5 = gcdBit(a, b);
        if(r1 != r2 || r2 != r3 || r4 != r3 || r5 != r4){
            System.out.println("error a = " + a + ", b = " + b);
        }
    }

    /**
     * 最大公约数计算 -- 欧几里得算法
     * gcd(a, b):
     *      * a = b : a
     *      * b < a : gcd(a - b, b)
     *      * a < b : gcd(a, b - a)
     */
    private static int gcd(int a, int b){
        if(a == b){
            return a;
        }else if(a > b){
            return gcd(b, a - b);
        }else{
            return gcd(a, b - a);
        }
    }

    /**
     * 最大公约数求取方法优化
     *    原始欧几里得算法中的反复相减可以用除法和求余运算代替
     */
    private static int gcdOpt(int a, int b){
        if(b == 0){
            return a;
        }else if(a > b){
            return gcdOpt(b, a % b);
        }else{
            return gcdOpt(a, b % a);
        }
    }

    /**
     * 非递归形式的最大公约数求取算法
     */
    private static int gcdLoop(int a, int b){
        int t;
        if(b > a){   // 交换两个数, 使得a始终大于b
            t = a;
            a = b;
            b = t;
        }
        while(b > 0){
            t = a % b;
            a = b;
            b = t;
        }
        return a;
    }

    private static int gcdStein(int a, int b){
        if(a == 0){ return b; }
        if(b == 0){ return a; }

        if(a % 2 == 0 && b % 2 == 0){
            return 2 * gcdStein(a / 2, b / 2);
        }
        if(a % 2 == 0){
            return gcdStein(a / 2, b);
        }
        if(b % 2 == 0){
            return gcdStein(a, b / 2);
        }
        if(a < b){
            int t = a;
            a = b;
            b = t;
        }
        return gcdStein(a - b, b);
    }

    private static int gcdBit(int a, int b){
        if(a == 0){ return b; }
        if(b == 0){ return a; }

        int k = 0;
        while((a & 1) == 0 && (b & 1) == 0){
            k++;
            a = a >> 1;
            b = b >> 1;
        }

        while(a > 0){   // 每次循环开始, a, b必然不会都是偶数
            while((a & 1) == 0){
                a = a >> 1;
            }

            while((b & 1) == 0){
                b = b >> 1;
            }

            if(a < b){
                b = b - a;
            }else{
                a = a - b;
            }
        }

        return b << k;
    }

}
