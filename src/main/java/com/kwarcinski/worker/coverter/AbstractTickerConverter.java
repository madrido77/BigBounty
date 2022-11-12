package com.kwarcinski.worker.coverter;

import com.kwarcinski.worker.entity.GlobalTicker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public abstract class AbstractTickerConverter<E, R> {

    @Autowired
    protected E api;

    public abstract R getGlobalTicker() throws IOException;

    public abstract Map<String, R> getGlobalTickerAll() throws IOException;
}
