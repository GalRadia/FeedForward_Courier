package com.example.feedforward_courier.models.server.command;

import com.example.feedforward_courier.models.server.user.UserSession;

import java.util.Date;
import java.util.Map;


public class CommandBoundary {
    private CommandId commandId;
    private String command;
    private TargetObject targetObject; //objectId
    private Date invocationTimestamp;
    private InvokedBy invokedBy; //userId
    private Map<String, Object> commandAttributes;




    public CommandBoundary() {

    }
    public CommandBoundary(String command){
        this.setCommandId(new CommandId(UserSession.getInstance().getSUPERAPP(),UserSession.getInstance().getBoundaryId(),"123"));
        this.setInvokedBy(new InvokedBy(UserSession.getInstance().getSUPERAPP(),UserSession.getInstance().getUserEmail()));
        this.setCommandAttributes(null);
        this.setTargetObject(new TargetObject(UserSession.getInstance().getSUPERAPP(),UserSession.getInstance().getBoundaryId()));
        this.setCommand(command);
    }
    public CommandId getCommandId() {
        return commandId;
    }
    public void setCommandId(CommandId commandId) {
        this.commandId = commandId;

    }
    public String getCommand() {
        return command;
    }
    public void setCommand(String doSomething) {
        this.command = doSomething;
    }
    public TargetObject getTargetObject() {
        return targetObject;
    }
    public void setTargetObject(TargetObject targetObject) {
        this.targetObject = targetObject;
    }
    public Date getInvocationTimestamp() {
        return invocationTimestamp;
    }
    public void setInvocationTimestamp(Date invocationTimestamp) {
        this.invocationTimestamp = invocationTimestamp;
    }
    public InvokedBy getInvokedBy() {
        return invokedBy;
    }
    public void setInvokedBy(InvokedBy invokedBy) {
        this.invokedBy = invokedBy;
    }
    public Map<String, Object> getCommandAttributes() {
        return commandAttributes;
    }
    public void setCommandAttributes(Map<String, Object> commandAttributes) {
        this.commandAttributes = commandAttributes;
    }
    @Override
    public String toString() {
        return "CommandBoundary [commandId=" + commandId + ", command=" + command + ", targetObject=" + targetObject
                + ", invocationTimestamp=" + invocationTimestamp + ", invokedBy=" + invokedBy + ", commandAttributes="
                + commandAttributes + "]";
    }











}
