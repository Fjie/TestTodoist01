package me.fanjie.testtodoist01.model;

import java.io.Serializable;

/**
 * Created by fanjie on 2016/5/24.
 * TODO 用户类
 */

public class User implements Serializable {

    private String userName;
    private String objectId;
    private String petName;
    private String relationObjectId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getPetName() {
        if(petName == null){
            return getUserName();
        }else {
            return petName;
        }
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getRelationObjectId() {
        return relationObjectId;
    }

    public void setRelationObjectId(String relationObjectId) {
        this.relationObjectId = relationObjectId;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", objectId='" + objectId + '\'' +
                ", petName='" + petName + '\'' +
                ", relationObjectId='" + relationObjectId + '\'' +
                '}';
    }
}
