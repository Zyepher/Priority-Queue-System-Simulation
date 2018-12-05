package com.nik; //your package

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Session {
    private int runTime;
    private int totalCallsProcessed;
    private int totalVIPCallsProcessed;
    private int totalRegularCallsProcessed;
    private int totalFirstAttempt;
    private int totalSecondAttempt;
    private int totalThirdAttempt;
    private long totalRunTime;
    private int totalArrival;
    private int numberOfServer;

    /* Constructor */
    public Session() {
        this.runTime = 1;
        this.totalCallsProcessed = 0;
        this.totalVIPCallsProcessed = 0;
        this.totalRegularCallsProcessed = 0;
        this.totalFirstAttempt = 0;
        this.totalSecondAttempt = 0;
        this.totalThirdAttempt = 0;
        this.totalRunTime = 0;
        this.totalArrival = 0;
        this.numberOfServer = 0;
    }
    /* End for constructor */

    /* Method for running the application */
    public void app() throws InterruptedException {
        /* This block of code is to generate servers that will be used to serve customers.
           The user can decide how many servers he/she wants to create. */
        ArrayList<Server> servers = new ArrayList<Server>();
        System.out.print("Enter the number of servers: ");
        Scanner serverInput = new Scanner(System.in);
        this.numberOfServer = serverInput.nextInt();
        System.out.print("\n");
        int i = 1;
        while (i <= this.numberOfServer) {
            Server s1 = new Server(i);
            servers.add(s1);
            i++;
        }//End while

        /* This block of code is to receive user's input for the application run time. */
        System.out.print("Enter the application's run time (min): ");
        Scanner inputRunTime = new Scanner(System.in);
        this.totalRunTime = inputRunTime.nextLong();

        /* This block of code is to generate customers with their details.
           Here, we are creating 400 objects of class Customer. */
        ArrayList<Customer> customers = new ArrayList<Customer>();
        for (i = 1; i <= 400; i++) {
            Customer c1 = new Customer(i);
            customers.add(c1);
        }//End for

        /* This block of code is to extract the system's current time and set the duration of time for how
           long we want the program to run. The priority queue algorithm will be inserted here. */
        System.out.println("\n=======================================================================================");
        System.out.println(">>> START OF APPLICATION");
        System.out.println("=======================================================================================");
        long t = System.currentTimeMillis(); //Extract system's current time.
        long end = t + ((this.totalRunTime * 1000) * 60); //To assign how long the user want the program to run (min).

        /* To implement priority queue algorithm, and to store some information total call duration as well as the type list
           which are needed to keep track which Customer object is currently being processed by a server. */
        PriorityQueue<Customer> customerPriorityQueue = new PriorityQueue<Customer>();
        ArrayList<Integer> customerTotalDurationList = new ArrayList<Integer>();
        ArrayList<String> customerTypeList = new ArrayList<String>();

        /* This is the time loop. All the process occurs per second in this huge while loop */
        while (System.currentTimeMillis() <= end) {
            System.out.println("Running time(sec): " + this.runTime);

            /* This block of code is to generate random call arrival, track total arrival, and add relevant data to the
               respective list. We remove the Customer object from customers ArrayList to prevent it from arriving again
               while it is being processed by a server. */
            int incoming_customer = -1;
            int incoming_duration = 0;
            Random callArrivalChance = new Random();
            if (callArrivalChance.nextDouble() <= 0.33) {
                int randomCustomerArriveIndex = ThreadLocalRandom.current().nextInt(0, customers.size()); //To obtain random index.
                incoming_duration = customers.get(randomCustomerArriveIndex).getCallDuration(); //Retrieve the call duration and assign it.
                System.out.println("\nAlert: Random " + customers.get(randomCustomerArriveIndex).getCustomerType() + " customer arrived with CallID "
                                    + customers.get(randomCustomerArriveIndex).getCallID() + ", duration " + incoming_duration + " seconds.\n");
                this.totalArrival += 1;
                customerTypeList.add(customers.get(randomCustomerArriveIndex).getCustomerType());
                customerPriorityQueue.offer(customers.get(randomCustomerArriveIndex));
                customers.remove(randomCustomerArriveIndex);
                incoming_customer++; //Increment once successfully created.
            }
            /* END */

            System.out.println("Queue: " + customerPriorityQueue + "\n"); //Prints out the Customer objects that are is the queue.
                int iterator = 0;
                int serverFound = -1;
                int serverProgress = 0;
                for (Server s : servers) {
                    if (servers.get(iterator).isProcessing()) {
                        serverProgress = servers.get(iterator).getDuration(); //getDuration will post decrement.
                        if(serverProgress != 0){
                            System.out.println("... Server " + (iterator + 1) + " is serving a "  + customerTypeList.get(iterator)
                                    + " customer's call (" + serverProgress + " secs)");
                        }
                        else {
                            System.out.println("*** Server " + (iterator + 1) + " has successfully served a "
                                    + customerTypeList.get(iterator) + " customer's call");
                            if (customers.get(iterator).getCustomerType().equals("VIP")) {
                                servers.get(iterator).setVIPCallsProcessed();
                            }
                            if (customers.get(iterator).getCustomerType().equals("regular")){
                                servers.get(iterator).setRegularCallsProcessed();
                            }
                            if (customerTotalDurationList.get(iterator) >= 15) {
                                this.totalThirdAttempt += 1;
                            } else if (customerTotalDurationList.get(iterator) >= 8) {
                                this.totalSecondAttempt += 1;
                            }
                            else if (customerTotalDurationList.get(iterator) >= 3){
                                this.totalFirstAttempt += 1;
                            }
                            servers.get(iterator).setCallsProcessed(); //To track the total number of calls processed.
                            servers.get(iterator).setDuration(0); //To set the call duration to zero.
                            servers.get(iterator).setIsProcessing(false); //To make the server available again.
                        }
                    }
                    else {
                        if (incoming_customer > -1) { /* This statement becomes True when a customer randomly arrive and there is
                                                         available server to be assigned. */
                            System.out.println("\n+++ Found server " + servers.get(iterator).getServerID() + ", assigning the " +
                                    "call that is at head of the queue to this server.");
                            customerPriorityQueue.poll(); //To retrieve and removes the head of the queue.
                            servers.get(iterator).setIsProcessing(true); //To make the server unavailable.
                            servers.get(iterator).setDuration(incoming_duration); //To set the call duration.
                            customerTotalDurationList.add(incoming_duration); /* To track the total call duration of that specific call
                                                                                 so that we'll know how many attempt(s) it took once
                                                                                 it is successfully processed. */
                            serverFound = iterator;
                        }
                    }
                    if (serverFound > -1) {
                        break;
                    }
                    iterator++;
                }//End for.
            System.out.println("=======================================================================================");
            if (this.runTime == (this.totalRunTime * 60)) {
                System.out.println(">>> END OF APPLICATION");
                System.out.println("=======================================================================================");
            }
            this.runTime += 1;
            Thread.sleep(1000);
        }//End huge while (time loop).
        System.out.println("\n=======================================================================================");
        System.out.println("SUMMARY OF DATA COLLECTED");
        System.out.println("=======================================================================================");
        System.out.println("Running time: " + this.totalRunTime + " min");
        System.out.println("=======================================================================================");
        System.out.println("Total number of calls processed by");
        i = 1;
        while (i <= this.numberOfServer) {
            System.out.println("Server " + i + ": " + servers.get(i - 1).getCallsProcessed());
            this.totalCallsProcessed += servers.get(i - 1).getCallsProcessed();
            i++;
        }//End while loop.
        System.out.println("\nTotal: " + this.totalCallsProcessed);
        System.out.println("=======================================================================================");
        System.out.println("Total number of calls processed for VIP customers by");
        i = 1;
        while (i <= this.numberOfServer) {
            System.out.println("Server " + i + ": " + servers.get(i - 1).getVIPCallsProcessed());
            this.totalVIPCallsProcessed += servers.get(i - 1).getVIPCallsProcessed();
            i++;
        }//End while loop.
        System.out.println("\nTotal: " + this.totalVIPCallsProcessed);
        System.out.println("=======================================================================================");
        System.out.println("Total number of calls processed for regular customers by");
        i = 1;
        while (i <= this.numberOfServer) {
            System.out.println("Server " + i + ": " + servers.get(i - 1).getRegularCallsProcessed());
            this.totalRegularCallsProcessed += servers.get(i - 1).getRegularCallsProcessed();
            i++;
        }//End while loop.
        System.out.println("\nTotal: " + this.totalRegularCallsProcessed);
        System.out.println("=======================================================================================");
        System.out.println("Total number of calls processed for");
        System.out.println("VIP customers: " + this.totalVIPCallsProcessed);
        System.out.println("Regular customer: " + this.totalRegularCallsProcessed);
        System.out.println("Average number of calls processed per min: " + (this.totalCallsProcessed / this.totalRunTime));
        System.out.println("Average arrival rate per min: " + (this.totalArrival / this.totalRunTime));
        System.out.println("=======================================================================================");
        System.out.println("Total number of calls processed by all servers in");
        System.out.println("One attempt: " + this.totalFirstAttempt);
        System.out.println("Two attempts: " + this.totalSecondAttempt);
        System.out.println("Three attempts: " + this.totalThirdAttempt);
        System.out.println("=======================================================================================");
        Scanner inputEndProgram = new Scanner(System.in);
        System.out.println("Enter anything to exit..");
        inputEndProgram.next();
        System.exit(0);
    }//End app() method.
}//End class Session.