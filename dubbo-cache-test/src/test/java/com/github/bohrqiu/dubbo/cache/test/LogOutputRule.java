package com.github.bohrqiu.dubbo.cache.test;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.filter.LevelFilter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import ch.qos.logback.core.spi.FilterReply;
import org.assertj.core.util.Lists;
import org.junit.rules.ExternalResource;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author qiuboboy@qq.com
 * @date 2018-07-28 16:25
 */
public class LogOutputRule extends ExternalResource {

    private ListAppender<ILoggingEvent> listAppender = new ListAppender<ILoggingEvent>();
    ;
    private boolean registered = false;

    private Level level = Level.INFO;

    public LogOutputRule() {
    }

    public LogOutputRule(Level level) {
        this.level = level;
    }

    @Override
    protected void before() throws Throwable {
        if (!registered) {
            register();
        }
    }

    private void register() {
        ch.qos.logback.classic.Logger logbackLogger = ((ch.qos.logback.classic.Logger) LoggerFactory.getLogger("ROOT"));
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        LevelFilter levelFilter = new LevelFilter();
        levelFilter.setContext(loggerContext);
        levelFilter.setLevel(level);
        levelFilter.setOnMatch(FilterReply.ACCEPT);
        levelFilter.setOnMismatch(FilterReply.DENY);

        listAppender = new ListAppender<ILoggingEvent>();
        listAppender.addFilter(levelFilter);
        listAppender.setContext(loggerContext);
        listAppender.start();
        logbackLogger.addAppender(listAppender);
        registered = true;
    }

    @Override
    protected void after() {
        listAppender.list.clear();
        ch.qos.logback.classic.Logger logbackLogger = ((ch.qos.logback.classic.Logger) LoggerFactory.getLogger("ROOT"));
        logbackLogger.detachAppender(listAppender);
    }

    /**
     * 获取日志数据
     */
    public List<String> getLogs() {
        List<String> list = Lists.newArrayList();
        for (ILoggingEvent iLoggingEvent : listAppender.list) {
            list.add(iLoggingEvent.getFormattedMessage());
        }
        return list;
    }

    public void clear() {
        listAppender.list.clear();
    }

    public String getLogsContent() {
        StringBuilder stringBuilder = new StringBuilder();
        for (ILoggingEvent iLoggingEvent : listAppender.list) {
            stringBuilder.append(iLoggingEvent.getFormattedMessage());
        }
        return stringBuilder.toString();
    }
}
