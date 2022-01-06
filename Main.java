package com.company;
import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws FileNotFoundException, IOException {

        int[] A = new int[10];

        /*
        Reading input from the text file "phw_input.txt".
        Parses through the commas and converts each String symbol into and integer
        puts the integers into the array A in order.

        I used StackOverflow to help me write this as I did not know how to write it before hand.
        https://stackoverflow.com/questions/13185727/reading-a-txt-file-using-scanner-class-in-java
         */
        File file = new File("phw_input.txt");
        Scanner scnr = new Scanner(file);
        String nextLine = scnr.nextLine();

        for(int i = 0; i < A.length; i++){
            if(nextLine.contains(",")){
                int j = Integer.parseInt(nextLine.substring(0,nextLine.indexOf(",")));
                A[i] = j;
                nextLine = nextLine.substring(nextLine.indexOf(",") + 1,nextLine.length());
            } else {
                int j = Integer.parseInt(nextLine);
                A[i] = j;
            }
        }

        // running the algorithms using the data from phw_input.txt and printing the output to the console
        int ans1 = Algorithm_1(A);
        int ans2 = Algorithm_2(A);
        int ans3 = MaxSum(A,0, A.length-1);
        int ans4 = Algorithm_4(A);

        System.out.println("algorithm-1: " + ans1);
        System.out.println("algorithm-2: " + ans2);
        System.out.println("algorithm-3: " + ans3);
        System.out.println("algorithm-4: " + ans4);

        /*
        sequence is a collection of lists sized, 10 - 100 inc by 5.
        each element in these lists is a random number generated between -500 and 500.
        This is the 'fake' data that the algorithms run through.
         */
        int[][] sequence = new int[19][];
        int k = 0;
        for(int i = 10; i <= 100; i += 5){
            int[] subSeq = new int[i];
            for(int j = 0; j < subSeq.length; j++){
                subSeq[j] = (int)((Math.random() * 1000) - 500);
            }
            sequence[k] = subSeq;
            k++;
        }

        /*
        Below are the empirically collected runtime data for the four algorithms
        This data is then stored in the first four columns of avgExeTime
        aveExeTime is grid where all of the runtime and runtime complexities will be stored
         */
        long[][] avgExeTime = new long[19][8];

        // Algorithm 1 runtime
        for(int i = 0; i < 19; i++){
            long total = 0;
            long t1 = System.nanoTime();
            for(int j = 0; j < 1000; j++) {
                Algorithm_1(sequence[i]);
                long t2 = System.nanoTime();
                long t = t2 - t1;
                total += t;
            }
            long avg = total / 1000;
            avgExeTime[i][0] = avg;
        }

        // Algorithm 2 runtime
        for(int i = 0; i < 19; i++){
            long total = 0;
            long t1 = System.nanoTime();
            for(int j = 0; j < 1000; j++) {
                Algorithm_2(sequence[i]);
                long t2 = System.nanoTime();
                long t = t2 - t1;
                total += t;
            }
            long avg = total / 1000;
            avgExeTime[i][1] = avg;
        }

        // Algorithm 3 runtime
        for(int i = 0; i < 19; i++){
            long total = 0;
            long t1 = System.nanoTime();
            for(int j = 0; j < 1000; j++) {
                MaxSum(sequence[i], 0, sequence[i].length - 1);
                long t2 = System.nanoTime();
                long t = t2 - t1;
                total += t;
            }
            long avg = total / 1000;
            avgExeTime[i][2] = avg;
        }

        // Algorithm 4 runtime
        for(int i = 0; i < 19; i++){
            long total = 0;
            long t1 = System.nanoTime();
            for(int j = 0; j < 1000; j++) {
                Algorithm_4(sequence[i]);
                long t2 = System.nanoTime();
                long t = t2 - t1;
                total += t;
            }
            long avg = total / 1000;
            avgExeTime[i][3] = avg;
        }

        /*
        This is the calculation for the runtime complexities.
        These calculations come from pages 4 and 5 of the instructions.
        Fills the last four columns of avgExeTime- T1(i), T2(i), T3(i), T4(i)
         */
        int erow = 0;
        for(int i = 10; i <= 100; i += 5){
            avgExeTime[erow][4] = (long) Math.ceil(Math.pow(i, 3) * 1000);
            avgExeTime[erow][5] = (long) Math.ceil(Math.pow(i, 2) * 1000);
            avgExeTime[erow][6] = (long) Math.ceil(i * (Math.log(i) / Math.log(2)) * 1000);  // scaling by 1000 to get
            avgExeTime[erow][7] = (long) Math.ceil(i * 1000);                                // numbers into a similar range
            erow++;
        }

        /*
        Creating output ArrayList to make writing the data to a text file easier.
        the block of code iterates though the avgExeTime double array and puts each element into a single ArrayList output
         */
        ArrayList<String> output = new ArrayList<String>();
        for (int i = 0; i < 19; i++) {
            String outputLine = "";
            for (int j = 0; j <= 6; j++ ) {
                outputLine += avgExeTime[i][j] + ",";
            }
            outputLine += avgExeTime[i][7];
            output.add(outputLine);
        }


        /*
        This block of code creates a new text file "rdh_phw_output.txt".
        It writes the contents from the 'output' Arraylist which
        which contains the average run times and runtime complexities from each of the four algorithms.

        This code segment was written with the help of the website StackOverflow
        https://stackoverflow.com/questions/11496700/how-to-use-printwriter-and-file-classes-in-java
         */
        try {
            PrintWriter writer = new PrintWriter("rdh0033_phw_output.txt", "UTF-8");
            writer.println("algorithm-1,algorithm-2,algorithm-3,algorithm-4,T1(n),T2(n),T3(n),T4(n)");
            for (String s : output) {
                writer.println(s);
            }
            writer.close();
        }
        catch (IOException e) {
            System.out.println("Error writing to file.");
        }
    }
    /*
    End of main method
    Start of the implementation of the four different MSCS algorithms
     */

    // implementation of algorithm-1
    public static int Algorithm_1(int[] X){

        int P = 0;
        int Q = X.length;

        int maxSoFar = 0;

        for(int L = P; L <= Q; L++){
            for(int U = L; U <= Q; U++){
                int sum = 0;
                for(int I = L; I < U; I++){
                    sum += X[I];
                }
                maxSoFar = Math.max(maxSoFar,sum);
            }
        }
        return maxSoFar;
    }

    // implementation of algorithm-2
    public static int Algorithm_2(int[] X){

        int P = 0;
        int Q = X.length;

        int maxSoFar = 0;
        int sum;

        for(int L = P; L < Q; L++){
            sum = 0;
            for(int U = L; U < Q; U++){
                sum += X[U];
                maxSoFar = Math.max(maxSoFar,sum);
            }
        }
        return maxSoFar;
    }

    // // implementation of algorithm-3
    public static int MaxSum(int[] X, int L, int U){

        int sum;

        if(L > U)
            return 0;
        if(L == U) {
            return Math.max(0, X[L]);
        }

        int M = (L + U)/2;

        sum = 0;
        int maxToLeft = 0;
        for(int i = M; i >= L;i--){
            sum += X[i];
            maxToLeft = Math.max(maxToLeft,sum);
        }

        sum = 0;
        int maxToRight = 0;
        for(int i = M+1; i <= U; i++){
            sum += X[i];
            maxToRight = Math.max(maxToRight,sum);
        }

        int maxCrossing = maxToLeft + maxToRight;

        int maxInA = MaxSum(X, L, M);
        int maxInB = MaxSum(X, M+1, U);
        return Math.max(maxCrossing,Math.max(maxInA,maxInB));
    }

    // implementation of algorithm-4
    public static int Algorithm_4(int[] X){

        int P = 0;
        int Q = X.length;

        int maxSoFar = 0;
        int maxEndingHere = 0;

        for(int i = P; i < Q; i++){
            maxEndingHere = Math.max(0, maxEndingHere + X[i]);
            maxSoFar = Math.max(maxSoFar,maxEndingHere);
        }

        return maxSoFar;
    }
}
