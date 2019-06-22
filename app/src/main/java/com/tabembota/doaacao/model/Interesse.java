package com.tabembota.doaacao.model;

public class Interesse {
    private String user_id;
    private String op_id;
    private long time_stamp;
    private long stopped_at;

    public Interesse(){

    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getOp_id() {
        return op_id;
    }

    public void setOp_id(String op_id) {
        this.op_id = op_id;
    }

    public long getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(long time_stamp) {
        this.time_stamp = time_stamp;
    }

    public long getStopped_at() {
        return stopped_at;
    }

    public void setStopped_at(long stopped_at) {
        this.stopped_at = stopped_at;
    }
}
