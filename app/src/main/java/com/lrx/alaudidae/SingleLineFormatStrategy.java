package com.lrx.alaudidae;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.LogStrategy;
import com.orhanobut.logger.LogcatLogStrategy;
import com.orhanobut.logger.Logger;

/**
 * Draws borders around the given log message along with additional information such as :
 * <p>
 * <ul>
 * <li>Thread information</li>
 * <li>Method stack trace</li>
 * </ul>
 * <p>
 * <pre>
 * </pre>
 */
public class SingleLineFormatStrategy implements FormatStrategy {
    /**
     * The minimum stack trace index, starts at this class after two native calls.
     */
    private static final int MIN_STACK_OFFSET = 5;


    private final int methodCount;

    @NonNull
    private final LogStrategy logStrategy;
    @Nullable
    private final String      tag;

    private SingleLineFormatStrategy(@NonNull Builder builder) {
        checkNotNull(builder);

        methodCount = builder.methodCount;
        logStrategy = builder.logStrategy;
        tag = builder.tag;
    }

    @NonNull
    public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public void log(int priority, @Nullable String onceOnlyTag, @NonNull String message) {
        checkNotNull(message);

        String tag = formatTag(onceOnlyTag);

        Thread thread = Thread.currentThread();

        StringBuilder builder = new StringBuilder();

        String name = thread.getName();
        builder.append(name).append(" ");

        StackTraceElement[] traces = Thread.currentThread().getStackTrace();


        int methodCount = this.methodCount;
        int stackOffset = getStackOffset(traces);

        //corresponding method count with the current stack may exceeds the stack trace. Trims the count
        if (methodCount + stackOffset > traces.length) {
            methodCount = traces.length - stackOffset - 1;
        }

        String level = "";
        for (int i = methodCount; i > 0; i--) {
            int stackIndex = i + stackOffset;
            if (stackIndex >= traces.length) {
                continue;
            }
            StackTraceElement trace = traces[stackIndex];

            builder .append(level)
                    .append(trace.getFileName())
                    .append(":")
                    .append(trace.getLineNumber())
                    .append(" ")
                    .append(getSimpleClassName(trace.getClassName()))
                    .append(".")
                    .append(trace.getMethodName());
            level += "\n   ";
        }

        builder.append( "> " ).append( message );
        logChunk(priority, tag, builder.toString());
    }

    private void logChunk(int priority, @Nullable String tag, @NonNull String chunk) {
        checkNotNull(chunk);
        logStrategy.log(priority, tag, chunk);
    }

    private String getSimpleClassName(@NonNull String name) {
        checkNotNull(name);

        int lastIndex = name.lastIndexOf(".");
        return name.substring(lastIndex + 1);
    }

    /**
     * Determines the starting index of the stack trace, after method calls made by this class.
     *
     * @param traces the stack trace
     * @return the stack offset
     */
    private int getStackOffset(@NonNull StackTraceElement[] traces) {
        checkNotNull(traces);
        String printerClassName = "com.orhanobut.logger.LoggerPrinter";
        String loggerClassName  = Logger.class.getName();

        for (int i = MIN_STACK_OFFSET; i < traces.length; i++) {
            StackTraceElement trace = traces[i];
            String            name  = trace.getClassName();
            if (!name.equals(printerClassName) && !name.equals(loggerClassName)) {
                return --i;
            }
        }
        return -1;
    }

    @Nullable
    private String formatTag(@Nullable String tag) {
        if (!isEmpty(tag) && !equals(this.tag, tag)) {
            return this.tag + "-" + tag;
        }
        return this.tag;
    }

    public static class Builder {
        int methodCount = 1;
        @Nullable
        LogStrategy logStrategy;
        @Nullable
        String tag = "Logger";

        private Builder() {
        }


        @NonNull
        public SingleLineFormatStrategy build() {
            if (logStrategy == null) {
                logStrategy = new LogcatLogStrategy();
            }
            return new SingleLineFormatStrategy(this);
        }
    }

    @NonNull
    private static <T> T checkNotNull(@Nullable final T obj) throws NullPointerException {
        if (obj == null) {
            throw new NullPointerException();
        }
        return obj;
    }

    private static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    private static boolean equals(CharSequence a, CharSequence b) {
        if (a == b) {
            return true;
        }
        if (a != null && b != null) {
            int length = a.length();
            if (length == b.length()) {
                if (a instanceof String && b instanceof String) {
                    return a.equals(b);
                } else {
                    for (int i = 0; i < length; i++) {
                        if (a.charAt(i) != b.charAt(i)) {
                            return false;
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }
}

