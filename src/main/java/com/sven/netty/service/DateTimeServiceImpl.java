package com.sven.netty.service;

import io.netty.util.internal.StringUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeServiceImpl implements DateTimeService {

    private String DATA_FORMAT = "yyyy-mm-dd hh:MM:ss";

    private SimpleDateFormat sdf = new SimpleDateFormat(DATA_FORMAT);

    @Override
    public String hello(String name) {
        return "[Netty Server Said] hello, " + name;
    }

    @Override
    public String getNow() {
        return sdf.format(new Date());
    }

    @Override
    public String format(String format, Date date) {

        if (StringUtil.isNullOrEmpty(format)) {
            return sdf.format(date);
        } else {
            SimpleDateFormat sdf1 = new SimpleDateFormat(format);
            return sdf1.format(date);
        }

    }

    @Override
    public String format(Date date) {
        return sdf.format(date);
    }
}
