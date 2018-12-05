package com.nik; //your package

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Customer implements Comparable<Customer> {
    /* These properties are used to store the data in order to generate the
       customers' information which are call ID, whether or not they are VIP,
       and the type of the customer (VIP/Regular). VIP customers has higher
       priority over regular customers. */
    private int callID;
    private boolean isVIP;
    private int priorityValue;
    private String customerType;
    private int callDuration = ThreadLocalRandom.current().nextInt(3, 16 + 1); //working

    /* Constructor */
    public Customer(int callID) {
        this.callID = callID;
        determineCustomerType();
        this.customerType = (this.isVIP) ? "VIP" : "regular"; //If true, assign "VIP". If false, assign "Regular".
        this.priorityValue = (this.isVIP) ? 1 : 2;
        determineCallDuration();
    }
    /* End constructor */

    /* Is used when we need a string representation of Server object. */
    public String toString() {
        return "\nID: " + this.callID + "; Type: " + this.customerType + "; Duration: " + this.callDuration + " secs";
    }

    /* Setters and getters */
    public int getCallID() {
        return callID;
    }

    public String getCustomerType() {
        return customerType;
    }

    public int getCallDuration() {
        return callDuration;
    }

    /* End setters and getters */

    /* Method to determine the type of the customer */
    private void determineCustomerType() {
        /* To generate a value from 0.1 to 1.0 with 35% chance for the customer to be VIP,
           65% chance for the customer to be regular. If the value is <= 0.30, then it
           is VIP customer. Otherwise, it is regular customer. */
        Random determineCustomerType = new Random();
        this.isVIP = determineCustomerType.nextDouble() <= 0.35;
    }

    /* Method to determine the duration of the call */
    private void determineCallDuration() {
        this.callDuration = ThreadLocalRandom.current().nextInt(3, 16 + 1);
    }

    @Override
    public int compareTo(Customer other) {
        if(this.priorityValue > other.priorityValue){
            return 1;
        }
        else if(this.priorityValue < other.priorityValue){
            return -1;
        }
        else{
            return 0;
        }
    }
}


