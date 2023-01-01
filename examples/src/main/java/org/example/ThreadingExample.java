package org.example;

public class ThreadingExample {

    public static class SlowWorker implements Runnable {
        @Override
        public void run() {
            int counter = 0;
            while (true) {
                try {
                    System.out.println("SlowWorker ran="+counter);
                    // causes this thread to sleep for 3 seconds
                    Thread.sleep(1000);
                    counter++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class FastWorker implements Runnable {
        @Override
        public void run() {
            int counter = 0;
            int resets = 0;
            while (true) {
                // causes this thread to print a message
                // every 10,000 iterations of this loop
                if (counter % 1_000_000_000 == 0) {
                    System.out.println("FastWorker ran=" + counter + ", resets="+resets);
                }
                counter++;
                if (counter == Integer.MAX_VALUE) {
                    System.out.println("FastWorker, resetting counter");
                    counter = 0;
                    resets ++;
                }
            }
        }
    }

    public static void main(String[] args) {
        // SlowWorker represents work we wish to do in parallel
        // slow worker implements Runnable, therefore can be run by a Thread
        SlowWorker slowWorker = new SlowWorker();
        // make an unnamed thread, which Java will assign a name
        Thread threadA = new Thread(slowWorker);
        // starts and runs threadA on another core on your machine
        threadA.start();

        // FastWorker is work which will consume a CPU,
        // also in parallel
        FastWorker fastWorker = new FastWorker();
        // make a thread named "fastWorkerA", to run "fastWorker" in the background
        Thread threadB = new Thread(null, fastWorker, "fastWorkerA");
        // starts and runs fastWorker on another core
        threadB.start();
        
        // continues looping forever in this the "main" thread
        int mainCounter = 0;
        while (true) {
            try {
                System.out.println("Main thread ran="+mainCounter);
                // causes this thread to sleep for 5 seconds
                Thread.sleep(3000);

                while ((mainCounter % 1_000_000_000) != 0)
                    mainCounter++;

                mainCounter++;
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
