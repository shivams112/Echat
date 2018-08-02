package io.github.shivams112.echat;

public class InstantMessage {
    private String inputMsg;
    private String user;

    public InstantMessage(String inputMsg, String user) {
        this.inputMsg = inputMsg;
        this.user = user;
    }
    public InstantMessage(){

    }

    public String getInputMsg() {
        return inputMsg;
    }

    public String getUser() {
        return user;
    }
}
