package sch.frog.sparrow.doodle;

/**
 * 欧几里得算法计算最大公约数
 */
public class GreatestCommonDivisor {

    public static void main(String[] args){
        System.out.println(gcd(10, 6));
        System.out.println(gcdOpt(10, 6));
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

}
