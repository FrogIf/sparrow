package sch.frog.sparrow.doodle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

/**
 * <<同构-编程中的数学>> 前言-井字棋
 *
 * 一个永远不会输的井字棋机器人
 *
 * 三阶幻方:
 * 4 9 2
 * 3 5 7
 * 8 1 6
 *
 *
 * 程序原理:
 * 1. 机器人落子决策算法 -- Minimax算法:
 *      人类每次落子之后, 程序都会根据当前棋面, 穷举出接下来所有的落子方法, 并计算出每一条计算路径的得分
 *      如果机器人是正方, 则会选择分数最大的路径进行落子
 *      如果机器人是反方, 则会选择分数最小的路径进行落子
 * 2. 胜负判定 -- 三阶幻方:
 *      三阶幻方的每条连线三个数之和均为15, 任意非连线上的三个数字相加之和一定不会15
 *      因此胜利的以防落子的所有位置中, 一定有三个数字相加之和为15
 */
public class Tictactoe {

    // 三阶幻方
    private static final int[] MAGIC_SQUARE = new int[]{ 4, 9, 2, 3, 5, 7, 8, 1, 6 };

    public static void main(String[] args){
        play();
    }

    private static void play(){
        Scanner sc = new Scanner(System.in);
        ArrayList<Integer> x = new ArrayList<>();
        ArrayList<Integer> o = new ArrayList<>();
        while(!win(x) && !win(o) && !finish(x, o)){
            board(x, o);
            while(true){
                System.out.print("请输入落子位置(1--9):");
                int i = sc.nextInt();
                if(i < 1 || i > 9){
                    System.out.println("无效的位置, 请重新输入");
                }else{
                    int v = MAGIC_SQUARE[i - 1];
                    if(x.contains(v) || o.contains(v)){
                        System.out.println("位置已占用, 请重新输入");
                    }else{
                        x.add(v);
                        break;
                    }
                }
            }
            int findbest = findbest(x, o, false);
            if(findbest != 0){
                o.add(findbest);
            }else{
                break;
            }
        }
        board(x, o);
        if(win(x)){
            System.out.println("x win!");
        }else if(win(o)){
            System.out.println("o win!");
        }else{
            System.out.println("a dead heat");
        }
    }

    /**
     * 绘制当前棋面
     */
    private static void board(ArrayList<Integer> x, ArrayList<Integer> o){
        System.out.println("----------");
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                int p = MAGIC_SQUARE[i * 3 + j];
                if(x.contains(p)){
                    System.out.print("|X");
                }else if(o.contains(p)){
                    System.out.print("|O");
                }else{
                    System.out.print("| ");
                }
            }
            System.out.println("|");
        }
        System.out.println("----------");
    }

    /**
     * 寻找下一步的最好落子位置
     * 返回下一个落子位置
     */
    private static int findbest(ArrayList<Integer> x, ArrayList<Integer> o, boolean xRound){
        int best = xRound ? -INF : INF;
        int move = 0;

        for (int i = 0; i < MAGIC_SQUARE.length; i++) {
            int v = MAGIC_SQUARE[i];
            if(!x.contains(v) && !o.contains(v)){
                if(xRound){
                    ArrayList<Integer> nx = new ArrayList<>(x);
                    nx.add(v);
                    int val = minmax(nx, o, 0, false);
                    if(val > best){
                        best = val;
                        move = v;
                    }
                }else{
                    ArrayList<Integer> no = new ArrayList<>(o);
                    no.add(v);
                    int val = minmax(x, no, 0, true);
                    if(val < best){
                        best = val;
                        move = v;
                    }
                }
            }
        }
        return move;
    }

    /**
     * Minimax算法 - 极小化极大算法
     * 多用于棋局博弈
     * 整体思路是通过针对当前棋面, 穷举所有接下来轮流落子的棋面可能情况, 并对每一种情况进行评分, 然后选择穷举的所有情况中的最高分
     * 穷举之后, 最终得到一个得分, 这个得分是基于当前棋面, 推演接下来所有情况中的最佳得分
     * 如果当前是x回合, 就找到x如何落子后, 分数最大
     * 如果当前是o回合, 就找到o基于当前棋面所能到达的最小分数
     */
    private static int minmax(ArrayList<Integer> x, ArrayList<Integer> o, int depth, boolean xRound){
        int score = eval(x, o);
        if(score == WIN){
            // 如果能赢, 按说就是分数就是WIN, 但是这里减去深度(也就是执行的步数); 也就是说, 当需要走的步数过多时, 分数也会降低, 这样保证在相同分数下, 步数越少, 分数越高
            return score - depth;
        }else if(score == -WIN){
            // 如果输了, 也是用分数加上深度, 而不是直接返回-WIN
            return score + depth;
        }else if(finish(x, o)){
            // 如果已经结束了, 平局, 返回分数0
            return 0;
        }else{
            int best = xRound ? -INF : INF;   // 如果是x的回合, 则期望分数最大化 就将当前分数置为最小, 反之
            for (int i : MAGIC_SQUARE) {
                if(!x.contains(i) && !o.contains(i)){ // 当前位置还没有落子
                    ArrayList<Integer> nx;
                    ArrayList<Integer> no;
                    if(xRound){
                        nx = new ArrayList<>(x);
                        nx.add(i);
                        no = o;
                        // 推演x落子在这个位置后, 接下来的步骤中, 最大得分, 则是最优解
                        best = Math.max(best, minmax(nx, no, depth + 1, false));
                    }else{
                        no = new ArrayList<>(o);
                        no.add(i);
                        nx = x;
                        // 推演o落子在这个位置后, 接下来的步骤中, 最小得分, 则是对x最有利的得分
                        best = Math.min(best, minmax(nx, no, depth + 1, true));
                    }
                }
            }
            return best;
        }
    }

    private static final int WIN = 10;

    private static final int INF = 1000;

    /**
     * 为一个棋面进行打分
     * 如果x赢, 则加10分
     * 如果o赢, 则减10分
     * 平局则为0分
     */
    private static int eval(ArrayList<Integer> x, ArrayList<Integer> o){
        if(win(x)){
            return WIN;
        }else if(win(o)){
            return -WIN;
        }else{
            return 0;
        }
    }

    /**
     * 判断游戏是否结束
     * 当格子占满时, 则游戏结束
     */
    private static boolean finish(ArrayList<Integer> x, ArrayList<Integer> o){
        return x.size() + o.size() == 9;
    }

    /**
     * 判断是否获胜
     *
     * 判定原理:
     * 棋盘上的位置与数组{ 4, 9, 2, 3, 5, 7, 8, 1, 6 }之间形成映射,
     * 只要被占用的数字之和为15, 则一定在棋盘上连成了一条直线
     * 因此判定是否连成直线就变成了判定是否存在三个数字之和为15
     * @param occupy 落子位置所对应的数字
     */
    private static boolean win(ArrayList<Integer> occupy){
        int n = occupy.size();
        if(n < 3){
            return false;
        }
        // 从小到大排序
        occupy.sort(Comparator.naturalOrder());
        for(int i = 0; i < n - 2; i++){
            int l = i + 1;
            int r = n - 1;
            while(l < r){
                int total = occupy.get(i) + occupy.get(l) + occupy.get(r);
                if(total == 15) {
                    return true;
                }else if(total < 15){
                    l++;
                }else{
                    r--;
                }
            }
        }
        return false;
    }
}
