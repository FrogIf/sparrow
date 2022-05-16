package sch.frog.sparrow.doodle;

import java.util.Arrays;

/**
 * 扩展欧几里得算法
 */
public class ExtendedEuclideanAlgorithm {

    public static void main(String[] args){
        int a = 35;
        int b = 14;
        int[] result = gcdExNoRe(a, b);
        System.out.println(Arrays.toString(result));
        System.out.println(result[1] + " * " + a + " + " + result[2] + " * " + b + " = " + result[0]);
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

    private static int[] gcdExNoRe(int a, int b){
        if(a < b){
            a = a ^ b;
            b = a ^ b;
            a = a ^ b;
        }

        int[] u = new int[]{ a, 1, 0 };
        int[] v = new int[]{ b, 0, 1 };
        int q;
        int[] t;
        while(v[0] > 0){
            q = u[0] / v[0];
            t = new int[]{ u[0] - v[0] * q, u[1] - v[1] * q, u[2] - v[2] * q };
            u = v;
            v = t;
        }
        return u;
    }

}
