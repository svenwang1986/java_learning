package com.sven.netty.service;

import java.util.Date;

public interface DateTimeService {


    String hello(String name);

    String getNow();

    String format(String format, Date date);

    String format(Date date);
}
