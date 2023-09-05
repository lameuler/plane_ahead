package dev.monospace.plane_ahead;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class PriorityFix {
    public static int[] videoSequence(String filename) {
        PriorityQueue<Integer, Date> queue = new PriorityQueue<>();
        try {
            Scanner scanner = new Scanner(new File(filename));
            int n = scanner.nextInt();
            int count = 0;
            int[] array = new int[n];
            for (int i = 0; i < n; i++) {
                String cmd = scanner.next();
                if (Objects.equals(cmd, "add")) {
                    int id = scanner.nextInt();
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(scanner.nextInt(), scanner.nextInt()-1, scanner.nextInt());
                    queue.enqueue(id, calendar.getTime());
                } else if (Objects.equals(cmd, "get")) {
                    array[count++] = queue.dequeue();
                }
            }
            int[] result = new int[count];
            System.arraycopy(array, 0, result, 0, count);
            return result;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        // Write any testing code here, if required.
        System.out.println(Arrays.toString(videoSequence("sample.in")));
        /*PriorityQueue<Integer, Integer> queue = new PriorityQueue<>(2);
        queue.enqueue(5,5);
        queue.enqueue(6,6);
        queue.enqueue(3,3);
        queue.enqueue(5,5);
        queue.enqueue(1,1);
        queue.enqueue(10,10);
        queue.enqueue(7,7);
        queue.enqueue(8,8);
        queue.enqueue(11,11);
        queue.enqueue(-2,-2);
        queue.enqueue(-1,-1);
        while (queue.size() > 0) {
            System.out.println(queue.dequeue());
        }*/
    }
}