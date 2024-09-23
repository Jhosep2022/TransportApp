package com.example.apptransport.Entity;

import java.util.List;

public class ChatOperatorReceive {
    private List<ChatOperator> operators;

    public ChatOperatorReceive(List<ChatOperator> operators) {
        this.operators = operators;
    }

    public List<ChatOperator> getOperators() {
        return operators;
    }

    public void setOperators(List<ChatOperator> operators) {
        this.operators = operators;
    }
}
