package com.zvz09.xiaochen.mc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class QueryRecursiveTask<R,V> extends RecursiveTask<List<R>> {
    private static final int THRESHOLD = 2; // 阈值，用于确定是否拆分任务
    private final List<V> array;
    private final int start;
    private final int end;
    private final ForkJoinOperator<R,V> combiner; // 函数接口，用于合并结果

    public QueryRecursiveTask(List<V> array, int start, int end, ForkJoinOperator<R,V> combiner) {
        this.array = array;
        this.start = start;
        this.end = end;
        this.combiner = combiner;
    }

    @Override
    protected List<R> compute() {
        int length = end - start;
        if (length <= THRESHOLD) {
            return combiner.apply(array.subList(start,end));
        } else {
            // 否则拆分任务为子任务
            int middle = start + length / 2;
            QueryRecursiveTask<R,V> leftTask = new QueryRecursiveTask(array, start, middle, combiner);
            QueryRecursiveTask<R,V> rightTask = new QueryRecursiveTask(array, middle, end, combiner);

            // 并行执行子任务
            leftTask.fork();
            List<R> rightResult = rightTask.compute();
            List<R> leftResult = leftTask.join();

            List<R> result = new ArrayList<>(rightResult);
            result.addAll(leftResult);
            // 合并子任务的结果
            return result;
        }
    }

    public static void main(String[] args) throws Exception {
        // 创建随机数组成的数组:
        List<Integer> array = new ArrayList<>();
        fillRandom(array);
        // fork/join task:
        ForkJoinPool fjp = new ForkJoinPool(4); // 最大并发数4
        ForkJoinTask<List<Integer>> task = new QueryRecursiveTask<Integer,Integer>(array, 0, array.size(), (List<Integer> v) -> {
            return v;
        });
        long startTime = System.currentTimeMillis();
        List<Integer> result = fjp.invoke(task);
        long endTime = System.currentTimeMillis();
        System.out.println("Fork/join sum: " + result + " in " + (endTime - startTime) + " ms.");
    }

    private static void fillRandom(List<Integer> array) {
        for (int i =0;i<1000;i++){
            array.add(i);
        }
    }

}

@FunctionalInterface
interface ForkJoinOperator<R,V> {
    List<R> apply(List<V> v);
}

