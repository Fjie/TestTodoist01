package me.fanjie.testtodoist01.core;

import android.support.annotation.NonNull;

import com.avos.avoscloud.AVACL;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVRelation;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import me.fanjie.testtodoist01.model.Plan;
import me.fanjie.testtodoist01.model.Task;
import me.fanjie.testtodoist01.model.User;
import me.fanjie.testtodoist01.utils.L;
import me.fanjie.testtodoist01.utils.UiUtil;

/**
 * Created by fanjie on 2016/5/28.
 */
public class DataCenter {

    public static DataCenter dataCenter;
    public static List<Plan> plens;

    public static void init() {
        if (dataCenter == null) {
            dataCenter = new DataCenter();
        }
    }

    private DataCenter() {
        getPlanList(new FindTaskCallback() {
            @Override
            public void lastTaskDone(boolean havaData) {
                if (!havaData) {
                    plens = new ArrayList<Plan>();
                }
            }
        });
    }

    public static synchronized DataCenter getInstance() {
        if (dataCenter == null) {
            throw new IllegalAccessError("先要初始化数据中心");
        }
        return dataCenter;
    }

    public List<Plan> getPlanList() {
        return getPlanList(null, null);
    }

    public List<Plan> getPlanList(FindPlanCallback callback) {
        return getPlanList(callback, null);
    }

    public List<Plan> getPlanList(FindTaskCallback callback) {
        return getPlanList(null, callback);
    }

    public List<Plan> getPlanList(FindPlanCallback callback, FindTaskCallback taskCallbask) {
        if (plens == null) {
            findPlans(callback, taskCallbask);
        } else {
            if (callback != null) {
                callback.done(plens);
            }
            if (taskCallbask != null) {
                taskCallbask.lastTaskDone(true);
            }
        }
        return plens;
    }

    public void refreshPlanList(FindTaskCallback taskCallback) {
        findPlans(null, taskCallback);
    }

    public void refreshPlanList(FindPlanCallback callback, FindTaskCallback taskCallback) {
        findPlans(callback, taskCallback);
    }

    public void addPlan(final Plan plan, final PutDoneCallback callback) {

        final AVObject object = new AVObject(C.Plan.CLASS_NAME);
        object.put(C.Plan.PLAN_NAME, plan.getPlanName());
        object.put(C.Plan.AUTHOR_NAME,AVUser.getCurrentUser().getUsername());
        addPermissing(object, AVUser.getCurrentUser());
        AVRelation relation = object.getRelation(C.PLAN_RELATION);
        relation.add(AVUser.getCurrentUser());
        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    plan.setObjectId(object.getObjectId());
                    if (plens == null) {
                        plens = new ArrayList<Plan>();
                    }
                    plens.add(plan);
                    callback.done(object.getObjectId());
                } else {

                }
            }
        });
    }

    public void addTask(Task task, PutDoneCallback callback) {
        addTask(task, null, callback);
    }

    public void addTask(final Task task, final List<String> userIds, final PutDoneCallback callback) {
        L.e(task.toString());
        final AVObject object = new AVObject(C.Task.CLASS_NAME);
        object.put(C.Task.TASK_NAME, task.getTaskName());
        object.put(C.Task.TASK_TIME, task.getTimeMillis());
        object.put(C.Task.AUTHOR_NAME,AVUser.getCurrentUser().getUsername());
        object.put(C.Task.PLAN_OBJECT_ID, task.getPlanObjectId());
        object.put(C.Task.COMPLETE, task.isComplete());
        if (task.getObjectId() != null) {
            object.setObjectId(task.getObjectId());
        }
        if (userIds != null) {
            addPermissing(object, userIds);
        }
        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    callback.done(object.getObjectId());
                } else {
                    L.e("done,e == " + e);
                }
            }
        });
    }

    public void addFriend(final Plan plan, final String userName) {

        findUser(userName, new FindOneUserCallback() {
            @Override
            public void done(final User user) {
                final AVObject planObject = new AVObject(C.Plan.CLASS_NAME);
                planObject.setObjectId(plan.getObjectId());

                plan.getUsers().add(user);

                addPermissing(planObject, plan.getUserIds());

                L.e("addFirend-----------------" + plan.toString());

                AVObject userObject = new AVObject(C.User.CLASS_NAME);
                userObject.setObjectId(user.getObjectId());

                AVRelation<AVObject> relation = planObject.getRelation(C.PLAN_RELATION);
                relation.add(userObject);
                planObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {

                            UiUtil.toast("添加成功");

                        } else {
                            L.e("addFriend ---------------- " + e.getMessage());
                        }
                    }
                });
            }
        });


    }

    private void findPlans(final FindPlanCallback callback, final FindTaskCallback taskCallbask) {

        AVQuery<AVObject> avQuery = new AVQuery<AVObject>(C.Plan.CLASS_NAME);
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null && list.size() > 0) {
                    List<Plan> plans = new ArrayList<>();
                    for (int i = 0; i < list.size(); i++) {
                        AVObject o = list.get(i);
                        Plan plan = new Plan();
                        plan.setPlanName(o.getString(C.Plan.PLAN_NAME));
                        plan.setAuthorName(o.getString(C.Plan.AUTHOR_NAME));
                        plan.setObjectId(o.getObjectId());
                        plans.add(plan);
                        if (taskCallbask != null) {
                            findTasks(plan, taskCallbask, i == list.size() - 1);
                        }
                        findUsers(plan, null);
                        L.e(plan.toString());
                    }
                    if (DataCenter.plens == null) {
                        DataCenter.plens = plans;
                    } else {
                        DataCenter.plens.clear();
                        DataCenter.plens.addAll(plans);
                    }

                    if (callback != null) {
                        callback.done(plans);
                    }

                } else {
                    if (taskCallbask != null) {
                        taskCallbask.lastTaskDone(false);
                    }
                }

            }
        });

    }

    private void findPlans(final FindPlanCallback callback) {
        findPlans(callback, null);
    }

    private void findTasks(final Plan plan, final FindTaskCallback callbask, final boolean isLastPlan) {

        AVQuery<AVObject> avQuery = new AVQuery<AVObject>(C.Task.CLASS_NAME);
        avQuery.whereEqualTo(C.Task.PLAN_OBJECT_ID, plan.getObjectId());
        avQuery.orderByAscending(C.Task.TASK_TIME);
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {

                if (list != null && list.size() > 0) {
                    List<Task> tasks = new ArrayList<>();
                    List<Task> completeTasks = new ArrayList<>();
                    Task task;
                    for (AVObject o : list) {
                        task = new Task();
                        task.setTaskName(o.getString(C.Task.TASK_NAME));
                        task.setTime(o.getLong(C.Task.TASK_TIME));
                        task.setPlanObjectId(o.getString(C.Task.PLAN_OBJECT_ID));
                        task.setAuthorName(o.getString(C.Task.AUTHOR_NAME));
                        task.setComplete(o.getBoolean(C.Task.COMPLETE));
                        task.setObjectId(o.getObjectId());
                        if (task.isComplete()) {
                            completeTasks.add(task);
                        } else {
                            tasks.add(task);
                        }

                    }
                    plan.setTasks(tasks);
                    plan.setCompleteTasks(completeTasks);
                }
                if (isLastPlan) {
                    callbask.lastTaskDone(true);
                }

            }
        });
    }

    public void findUsers(final Plan plan, final FindUserCallback callback) {
        AVObject planObject = AVObject.createWithoutData(C.Plan.CLASS_NAME, plan.getObjectId());
        planObject.getRelation(C.PLAN_RELATION).getQuery().findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    List<User> users = new ArrayList<User>();
                    for (int i = 0; i < list.size(); i++) {
                        AVObject o = list.get(i);
                        User user = new User();
                        user.setObjectId(o.getObjectId());
                        user.setUserName(o.getString(C.User.USER_NAME));
                        user.setPetName(o.getString(C.User.PET_NAME));
                        if(i == 0){
                            plan.setAuthor(user);
                        }
                        users.add(user);
                        L.e("findUsers -------------" + user.toString());
                    }
                    L.e("findUsers -------------" + list.toString());
                    plan.setUsers(users);
                    plan.toString();
                    if (callback != null) {
                        callback.done(users);
                    }
                } else {
                    L.e("findUsers ------------- " + e.getMessage());
                }
            }
        });

    }

    public void findUser(final String userName, final FindOneUserCallback callback) {
        AVQuery<AVObject> query = new AVQuery<>(C.User.CLASS_NAME);
        query.whereEqualTo(C.User.USER_NAME, userName);
        query.getFirstInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject o, AVException e) {
                if (e == null && o!=null) {
                    User user = new User();
                    user.setObjectId(o.getObjectId());
                    user.setUserName(o.getString(C.User.USER_NAME));
                    user.setPetName(o.getString(C.User.PET_NAME));
                    L.e("findUser----------" + user.toString());
                    callback.done(user);
                } else {
                    UiUtil.toast("用户不存在");
                    L.e(e.getMessage());
                }
            }
        });
    }

    public void addPermissing(AVObject object, AVUser user) {
        List<String> userIds = new ArrayList<>();
        userIds.add(user.getObjectId());
        addPermissing(object, userIds);
    }

    public void addPermissing(AVObject object, String userId) {
        List<String> userIds = new ArrayList<>();
        userIds.add(userId);
        addPermissing(object, userIds);
    }

    public void addPermissing(AVObject object, @NonNull List<String> userIds) {
        L.e("addPermissing-------------------" + userIds);
        AVACL avacl = new AVACL();
        for (String id : userIds) {
            avacl.setReadAccess(id, true);
            avacl.setWriteAccess(id, true);
        }
        object.setACL(avacl);
    }

    public interface FindPlanCallback {
        void done(List<Plan> plans);
    }

    public interface FindTaskCallback {
        void lastTaskDone(boolean havaData);
    }

    public interface FindUserCallback {
        void done(List<User> users);
    }

    public interface FindOneUserCallback {
        void done(User user);
    }

    public interface PutDoneCallback {
        void done(String id);
    }

}
