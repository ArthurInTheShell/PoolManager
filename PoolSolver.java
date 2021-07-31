import com.sun.source.tree.Tree;

import java.io.*;
import java.util.*;

public class PoolSolver {
    public void analyse(int order) throws IOException {
        //Read File into Tree Map
        int numLines = 0;
        int minimumUncoveredTime = Integer.MAX_VALUE;
        int totalCoveredTime = 0;
        int head,tail,existedTail;
        TreeMap<Integer, Integer> intervals = new TreeMap<>();
        File inputFile = new File(order + ".in");
        Scanner sc = new Scanner(inputFile);
        if(sc.hasNextLine()){
            numLines = sc.nextInt();
            //System.out.println(numLines);
        }
        if(numLines == 1) {
            writeAnswer(0, order);
            return;
        }

        for (int i = 0; i < numLines; i++) {
            head = sc.nextInt();
            tail = sc.nextInt();
            if(intervals.containsKey(head)){
                existedTail = intervals.get(head);
                intervals.replace(head, Integer.max(tail,existedTail));
                minimumUncoveredTime = 0;
            }else{
                intervals.put(head,tail);
            }
            //System.out.println(head + " "+ tail);
        }
        sc.close();

        //Analysis
        Integer[] headArr = intervals.keySet().toArray(new Integer[0]);
        Integer[] tailArr = intervals.values().toArray(new Integer[0]);
        //Test if the order is right
//        for(int i = 0; i < headArr.length; i++){
//            System.out.println(headArr[i]);
//            System.out.println(tailArr[i]);
//        }
        int index = 0;
        int nextHead,nextTail;
        while(index < headArr.length-1){
            head = headArr[index];
            tail = tailArr[index];
            nextHead = headArr[index+1];
            nextTail = tailArr[index+1];
            if(tail>=nextHead && tail>=nextTail && head<nextHead){
                //1,9 - 3,8
                // the next interval is all covered
                minimumUncoveredTime = 0;
                // set the next part solo covered time to zero
                headArr[index+1] = tail;
                tailArr[index+1] = tail;
                // truncated the first part time into two part
                tailArr[index] = nextHead;
                totalCoveredTime += tail - nextHead;
            }else if(tail>=nextHead && tail>=nextTail && head>=nextHead){
                // 8,9 - 5,7
                // the next interval is all covered
                minimumUncoveredTime = 0;
                // set the next part solo covered time to zero
                headArr[index+1] = tail;
                tailArr[index+1] = tail;
            }else if(tail>nextHead && tail<nextTail && head < nextHead){
                // 1,8 - 3,9
                // set min
                minimumUncoveredTime = Integer.min(minimumUncoveredTime, Integer.min(nextHead-head, nextTail-tail));
                // set the next part's solo covered time
                headArr[index+1] = tail;
                // truncated the first part time into two part
                tailArr[index] = nextHead;
                totalCoveredTime += tail - nextHead;

            }else if(tail>=nextHead && tail<nextTail && head >= nextHead){
                // 8,9 - 5,11
                // this case happened when the previous iteration modified the current head and tail
                // with the current head behind the next head
                // set min
                minimumUncoveredTime = 0;
                // set the next part's solo covered time to zero
                headArr[index+1] = tail;
                // truncate the current part
                headArr[index] = tail;
                totalCoveredTime += tail-head;
            }else if(tail< nextHead){
                // 8,9 - 10,13
                minimumUncoveredTime = Integer.min(minimumUncoveredTime, Integer.min(tail-head, nextTail-nextHead));
            }
            index++;
        }
        for(int i = 0; i < headArr.length; i++){
            totalCoveredTime += tailArr[i] - headArr[i] ;
        }
        writeAnswer(totalCoveredTime-minimumUncoveredTime, order);

    }

    private void writeAnswer(int time, int order) throws IOException {
        //Write that time to file
        FileWriter fileWriter = new FileWriter(order + ".out");
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.println(time);
        printWriter.close();
    }
}
