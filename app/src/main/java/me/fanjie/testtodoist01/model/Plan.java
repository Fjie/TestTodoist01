package me.fanjie.testtodoist01.model;

import android.text.TextUtils;

import com.avos.avoscloud.AVUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Plan implements Serializable {

    private String planName;
    private List<Task> tasks;
    private List<Task> completeTasks;
    private List<User> users;
    private String objectId;
    private String relationObjectId;
    private User author;
    private String authorName;

    public Plan() {
    }

    public Plan(String planName, List<Task> tasks) {
        this.planName = planName;
        this.tasks = tasks;
    }

    public Plan(String planName) {
        this.planName = planName;
    }

    public void removeTask(int position){
        tasks.remove(position);
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public List<Task> getTasks() {
        if(tasks == null){
            tasks = new ArrayList<>();
        }
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<Task> getCompleteTasks() {
        return completeTasks;
    }

    public void setCompleteTasks(List<Task> completeTasks) {
        this.completeTasks = completeTasks;
    }

    public List<Task> getAllTasks(){
        List<Task> tasks = new ArrayList<>();
        if(completeTasks!=null) {
            tasks.addAll(completeTasks);
        }
        if(completeTasks!=null){
            tasks.addAll(this.tasks);
        }
        return tasks;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<String> getUserIds(){
        if(getUsers()!= null){
            List<String> userIds = new ArrayList<>();
            for (User u : getUsers()) {
                userIds.add(u.getObjectId());
            }
            return userIds;
        }else {
            return null;
        }
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getRelationObjectId() {
        return relationObjectId;
    }

    public void setRelationObjectId(String relationObjectId) {
        this.relationObjectId = relationObjectId;
    }

    public int getUserCount(){
        if(getUsers() == null){
            return 0;
        }else {
            return getUsers().size();
        }
    }

    public int getTaskCount(){
        if(getTasks() == null){
            return 0;
        }else {
            return getTasks().size();
        }
    }

    public int getCompleteCount(){
        if(getCompleteTasks() == null){
            return 0;
        }else {
            return getCompleteTasks().size();
        }
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

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Plan{" +
                "planName='" + planName + '\'' +
                ", tasks=" + tasks +
                ", completeTasks=" + completeTasks +
                ", users=" + users +
                ", objectId='" + objectId + '\'' +
                ", relationObjectId='" + relationObjectId + '\'' +
                '}';
    }
}
