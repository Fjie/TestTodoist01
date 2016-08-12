package me.fanjie.testtodoist01.model;

import android.text.TextUtils;

import com.avos.avoscloud.AVUser;

import java.io.Serializable;
import java.util.Calendar;

import me.fanjie.testtodoist01.utils.TimeUtil;

/**
 * Created by fanjie on 2016/5/18.
 */
public class Task implements Serializable{

    private String taskName;
    private Calendar time;
    private String planObjectId;
    private String objectId;
    private boolean complete;
    private String authorName;


    public void setTime(long timeMillis){
        if(this.time == null) {
         this.time = Calendar.getInstance();
        }
        time.setTimeInMillis(timeMillis);
    }

    public String getTaskTimeString(){
        if(getTime()!=null) {
            return TimeUtil.getSimpleDateToShow(getTime());
        }else {
            return "";
        }
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getPlanObjectId() {
        return planObjectId;
    }

    public void setPlanObjectId(String planObjectId) {
        this.planObjectId = planObjectId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Calendar getTime() {
        return time;
    }

    public Long getTimeMillis(){
        return getTime().getTimeInMillis();
    }

    public void setTime(Calendar time) {
        this.time = time;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public String getAuthorName() {
        if(TextUtils.equals(authorName, AVUser.getCurrentUser().getUsername())){
            return "我";
        }
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskName='" + taskName + '\'' +
                ", time=" + time +
                ", planObjectId='" + planObjectId + '\'' +
                ", objectId='" + objectId + '\'' +
                ", complete=" + complete +
                '}';
    }
}
