package com.aimyskin.miscmodule.log;

import android.os.Environment;

import com.aimyskin.miscmodule.BuildConfig;
import com.aimyskin.miscmodule.utils.UsbStorageManager;
import com.elvishew.xlog.LogConfiguration;
import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.XLog;
import com.elvishew.xlog.printer.AndroidPrinter;
import com.elvishew.xlog.printer.Printer;
import com.elvishew.xlog.printer.file.FilePrinter;
import com.elvishew.xlog.printer.file.clean.FileLastModifiedCleanStrategy;
import com.elvishew.xlog.printer.file.naming.DateFileNameGenerator;
import com.elvishew.xlog.printer.file.writer.SimpleWriter;

import java.io.File;

import bolts.Continuation;
import bolts.Task;

public class LogConfig {

    public LogConfig(int versionCode, String versionName) {
        String SDCARD = Environment.getExternalStorageDirectory().getPath();
        String logPath = SDCARD + "/" + LogFilePath.LOG_PATH;

        LogConfiguration config = new LogConfiguration.Builder()
                .logLevel(BuildConfig.DEBUG ? LogLevel.ALL : LogLevel.ERROR)
                .tag("AIMYSKIN_TAG")
                .enableBorder()
                .borderFormatter(new LogBorderFormatter())
                .enableThreadInfo()
                .enableStackTrace(1)
                .build();

        Printer androidPrinter = new AndroidPrinter(true);
        Printer filePrinter = new FilePrinter
                .Builder(logPath)
                .fileNameGenerator(new DateFileNameGenerator())
                .cleanStrategy(new FileLastModifiedCleanStrategy(7 * 24 * 60 * 60 * 1000 /* 7 days */))
                .writer(new SimpleWriter() {                           // Default: SimpleWriter
                    @Override
                    public void onNewFileCreated(File file) {
                        super.onNewFileCreated(file);
                        final String header = "";
                        appendLog(header);
                    }
                })
                .build();

        XLog.init(config, androidPrinter, filePrinter);
    }

    public Task<Void> copyFilesToUsb() {
        UsbStorageManager usbStorageManager = new UsbStorageManager();
        Task task = Task.forResult(null);
        task = task.continueWithTask(new Continuation() {
            @Override
            public Object then(Task task) throws Exception {
                return usbStorageManager.saveSdFileToUsb(LogFilePath.LOG_PATH, LogFilePath.LOG_PATH, null);
            }
        });
        return task;
    }
}
