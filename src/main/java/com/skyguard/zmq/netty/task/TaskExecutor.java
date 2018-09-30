package com.skyguard.zmq.netty.task;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TaskExecutor {

    private ListeningExecutorService service;

    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(PROCESSORS,PROCESSORS*2,20, TimeUnit.SECONDS,new LinkedBlockingDeque<>(PROCESSORS*2));

    private static final int PROCESSORS = 5;

    public TaskExecutor(){
        service = MoreExecutors
                .listeningDecorator(threadPoolExecutor);
    }

    public ListeningExecutorService getService(){
        return service;    }

}
