package sch.frog.sparrow.doodle;

import java.util.Scanner;

/**
 * 倒水问题 <<同构-编程中的数学>> - 递归
 *  问题描述:
 *      有两个水瓶, 一个容量是9升, 一个容量是4升. 问如何才能从河中打出6升水.
 *
 * 数学基础:
 *  扩展欧几里得算法
 */
public class PourWater {

    public static void main(String[] args){
        // 扩展欧几里得方法演示
//        int a = 15;
//        int b = 6;
//        int[] res = gcmex(a, b);
//        System.out.println(res[0] + " = " + a + " * " + res[1] + " + " + b + " * " + res[2]);

        // 倒水问题演示
        Scanner sc = new Scanner(System.in);
        while(true){
            System.out.print("请输入一个水杯的容积: ");
            int capacityA = sc.nextInt();

            System.out.print("请输入另一个水杯的容积: ");
            int capacityB = sc.nextInt();

            System.out.print("请输入需要打水的体积:");
            int targetWaterLevel = sc.nextInt();

            pourWater(capacityA, capacityB, targetWaterLevel);
        }
    }

    private static void pourWater(int capacityA, int capacityB, int targetWaterLevel){
        if(capacityA < capacityB){
            int temp = capacityA;
            capacityA = capacityB;
            capacityB = temp;
        }
        Cup bigCup = new Cup(capacityA, "A");
        Cup smallCup = new Cup(capacityB, "B");
        DiophantineAnswer answer = solveDiophantine(bigCup.capacity, smallCup.capacity, targetWaterLevel);
        if(answer != null){
            int[] minimize = minimizeSolve(answer);
            int x = minimize[0];
            int y = minimize[1];
            System.out.println("最优解为: x = " + x + ", y = " + y + "; " + targetWaterLevel + " = " + x + " * " + bigCup.capacity + " + " + y + " * " + smallCup.capacity);
            System.out.println(bigCup.name + "杯容积:" + bigCup.capacity + ", " + smallCup.name + "杯容积:" + smallCup.capacity);
            step(minimize[0], minimize[1], bigCup, smallCup);
        }else{
            System.out.println("无解");
        }
    }

    private static void step(int x, int y, Cup a, Cup b){
        System.out.println("当前" + a.name + "中" + a.waterLevel + "升水, " + b.name + "中" + b.waterLevel + "升水.");
        if(x == 0 && y == 0){
            System.out.println("finish");
            return;
        }
        if(x > 0 && a.isEmpty()){
            a.full();
            x--;
            System.out.println("将" + a.name + "中装满水");
        }else if(y > 0 && b.isEmpty()){
            b.full();
            y--;
            System.out.println("将" + b.name + "中装满水");
        }else if(x < 0 && a.isFull()){
            System.out.println("将" + a.name + "中的水倒掉");
            x++;
            a.empty();
        }else if(y < 0 && b.isFull()){
            System.out.println("将" + b.name + "中的水倒掉");
            y++;
            b.empty();
        }else if(y < 0){
            a.to(b);
            System.out.println("将" + a.name + "中的水倒入" + b.name + "中");
        }else if(x < 0){
            b.to(a);
            System.out.println("将" + b.name + "中的水倒入" + a.name + "中");
        }else{
            throw new IllegalStateException("未知的状况, a : " + a + ", b : " + b);
        }

        step(x, y, a, b);
    }

    /**
     * 求solveDiophantine所得到的最小整数解
     * 即
     *      x = x1 - ku
     *      y = y1 + kv
     *
     * 此外, k为整数
     *
     * return [x, y]
     */
    private static int[] minimizeSolve(DiophantineAnswer answer){
        int m = Math.max(Math.abs(answer.x1 / answer.u), Math.abs(answer.y1 / answer.v)) + 1;
        int k = 0;
        int min = Integer.MAX_VALUE;
        for(int i = -m; i <= m; i++){
            int temp = Math.abs(answer.x1 - i * answer.u) + Math.abs(answer.y1 + i * answer.v);
            if(temp < min){
                min = temp;
                k = i;
            }
        }
        return new int[]{answer.x1 - k * answer.u, answer.y1 + k * answer.v};
    }

    /**
     * 求丢番图方程的整数通解
     * x = x1 - ku
     * y = y1 + kv
     */
    private static DiophantineAnswer solveDiophantine(int a, int b, int c){
        if(b > a){
            throw new IllegalArgumentException("b must less than a");
        }
        DiophantineAnswer answer = solveDiophantineSpecial(a, b, c);
        if(answer == null){
            return null;
        }
        // 非齐次方程的通解 = 齐次方程的通解 + 非齐次方程的特解
        int g = answer.g;
        answer.u = b / g;
        answer.v = a / g;
        return answer;
    }

    /**
     * 求解丢番图方程的整数特解: ax + by = c
     */
    private static DiophantineAnswer solveDiophantineSpecial(int a, int b, int c){
        int[] res = gcmex(a, b);
        int g = res[0];
        if(c % g != 0){ // c不能整除g, 则无解
            return null;
        }else{
            DiophantineAnswer answer = new DiophantineAnswer();
            answer.x1 = res[1] * c / g;
            answer.y1 = res[2] * c / g;
            answer.g = g;
            return answer;
        }
    }

    /**
     * 扩展欧几里得算法
     * gcm_ex(a, b) =
     *     * b = 0:
     *          (a, 1, 0)
     *     * else:
     *          (g, y', x' - y' * floor(a / b)) 其中:(g, x', y') = gcm_ex(b, a mod b)
     *
     * 利用扩展欧几里得算法, 可以求得最大公约数g, 以及另外两个数x, y, 使得:
     *      ax + by = g (该等式被称为 贝祖等式, 在a, b固定的情况下, 任意x, y属于Z, ax + by的最小值就是g)
     */
    private static int[] gcmex(int a, int b){
        if(b > a){
            throw new IllegalArgumentException("b must less than a");
        }
        if(b == 0){
            return new int[]{ a, 1, 0 };
        }else{
            int[] res = gcmex(b, a % b);
            int g = res[0];
            int xp = res[1];
            int yp = res[2];
            return new int[]{g, yp, xp - yp * (a / b)};
        }
    }

    /**
     * 通解:
     * x = x1 - ku
     * y = y1 + kv
     */
    private static class DiophantineAnswer{
        int x1;
        int y1;
        int u;
        int v;
        /**
         * 最大公约数
         */
        int g;
    }

    /**
     * 水杯
     */
    private static class Cup {
        /**
         * 容积
         */
        final int capacity;

        /**
         * 当前水位
         */
        int waterLevel;

        final String name;

        public Cup(int capacity, String name) {
            this.capacity = capacity;
            this.name = name;
        }

        // 装满
        void full(){
            this.waterLevel = this.capacity;
        }

        // 倒掉
        void empty(){
            this.waterLevel = 0;
        }

        boolean isFull(){
            return this.waterLevel == this.capacity;
        }

        boolean isEmpty(){
            return this.waterLevel == 0;
        }

        void to(Cup another){
            int ability = another.capacity - another.waterLevel;
            int holder = this.waterLevel;
            int toVal = Math.min(ability, holder);
            another.waterLevel += toVal;
            this.waterLevel -= toVal;
        }

        @Override
        public String toString() {
            return "Cup{" +
                    "capacity=" + capacity +
                    ", waterLevel=" + waterLevel +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

}
