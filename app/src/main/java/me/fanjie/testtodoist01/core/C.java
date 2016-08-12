package me.fanjie.testtodoist01.core;

/**
 * Created by fanjie on 2016/5/28.
 */
public final class C {

    public static final String PLAN_RELATION = "PlanRelation";

    public static final int TAG_TASK = 66666;
    public static final int TAG_PLAN = 88888;

    public static abstract class Plan{
        public static final String CLASS_NAME = "Plan";
        public static final String PLAN_NAME = "planName";
        public static final String RELATION_OBJECT_ID = "relationObjectId";
        public static final String AUTHOR_NAME = "authorName";
    }

    public static abstract class Task{
        public static final String CLASS_NAME = "Task";
        public static final String TASK_NAME = "taskName";
        public static final String TASK_TIME = "taskTime";
        public static final String PLAN_OBJECT_ID = "planObjectId";
        public static final String COMPLETE = "complete";
        public static final String AUTHOR_NAME = "authorName";
    }

    public static abstract class User{
        public static final String CLASS_NAME = "_User";
        public static final String USER_NAME = "username";
        public static final String PET_NAME = "petName";
        public static final String RELATION_OBJECT_ID = Plan.RELATION_OBJECT_ID;
    }

    public static abstract class Relation{
        public static final String CLASS_NAME = "Relation";
        public static final String USER_OBJECT_ID = "userObjectId";
        public static final String PLAN_OBJECT_ID = Task.PLAN_OBJECT_ID;
    }
}
