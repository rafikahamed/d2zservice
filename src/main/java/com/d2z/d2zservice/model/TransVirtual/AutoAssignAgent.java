package com.d2z.d2zservice.model.TransVirtual;

public enum AutoAssignAgent {

    BEN("Leighclare"),
    MAC("Mactrans");

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    private String agent;
    AutoAssignAgent(String agent){
      this.agent = agent ;
    }
}
