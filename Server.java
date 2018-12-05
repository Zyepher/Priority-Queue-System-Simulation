package com.nik; //your package

public class Server {
    private int serverID;
    private int callsProcessed;
    private int VIPCallsProcessed;
    private int regularCallsProcessed;
    private boolean isProcessing;
    private int duration;

    /* Constructor */
    public Server(int serverID) {
        /* These properties are used to store the data in order to generate
           the servers' information which are server ID, total number of calls
           processed, total number of VIP calls processed, total number of
           regular calls processed, and the status of the server */
        this.serverID = serverID;
        this.callsProcessed = 0;
        this.VIPCallsProcessed = 0;
        this.regularCallsProcessed = 0;
        this.isProcessing = false;
    }
    /* End constructor */

    /* Is used when we need a string representation of Server object. */
    public String toString(){
        return "Server ID: " + this.serverID + " has been created!\n";
    }

    /* Setters and getters */
    public int getServerID() {
        return serverID;
    }

    public int getCallsProcessed() {

        return callsProcessed;
    }

    public void setCallsProcessed() {
        this.callsProcessed += 1;
    }

    public int getVIPCallsProcessed() {
        return VIPCallsProcessed;
    }

    public void setVIPCallsProcessed() {
        this.VIPCallsProcessed += 1;
    }

    public int getRegularCallsProcessed() {
        return regularCallsProcessed;
    }

    public void setRegularCallsProcessed() {
        this.regularCallsProcessed += 1;
    }

    public boolean isProcessing() {
        return isProcessing;
    }

    public void setIsProcessing(boolean isProcessing) {
        this.isProcessing = isProcessing;
    }

    public void setDuration(int set_duration) {
        this.duration = set_duration;
    }
    public int getDuration() {
        return duration--;
    }
    /* End setters and getters */
}


