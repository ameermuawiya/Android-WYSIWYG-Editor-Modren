package com.github.irshulx.wysiwyg;

import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;

public class MyApplication extends Application {

    private String someVariable;

    public String getSomeVariable() {
        return someVariable;
    }

    public void setSomeVariable(String someVariable) {
        this.someVariable = someVariable;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {

            try {
                // Convert stacktrace to string
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                throwable.printStackTrace(pw);
                String crashLog = sw.toString();

                // Save to internal file
                File file = new File(getFilesDir(), "crash_log.txt");
                FileWriter writer = new FileWriter(file, false);
                writer.write(crashLog);
                writer.flush();
                writer.close();

                // Copy to clipboard
                ClipboardManager clipboard =
                        (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

                ClipData clip = ClipData.newPlainText("CrashLog", crashLog);
                clipboard.setPrimaryClip(clip);

            } catch (Exception ignored) {
            }

            // Kill process after logging
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        });
    }
}