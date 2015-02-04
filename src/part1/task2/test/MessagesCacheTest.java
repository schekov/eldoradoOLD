package part1.task2.test;


import part1.task2.Cache;

import java.util.Scanner;

public class MessagesCacheTest {

    private static final int TEST_TIME_TO_LIVE = 5000; // milliseconds

    public static void main(String[] args) {

        Cache cache = new Cache(TEST_TIME_TO_LIVE);
        Scanner scaner = new Scanner(System.in);
        System.out.println("Enter message or 'qqq' for exit:");
        String msg;
        while (true) {
            msg = scaner.nextLine();
            if(msg.equals("qqq")){
                break;
            }
            cache.put(msg);
        }
    }
}
