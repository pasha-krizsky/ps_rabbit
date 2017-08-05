package com.pasha.rabbitutils;

import com.pasha.rabbitutils.conf.AppConf;
import com.pasha.rabbitutils.reader.ConsoleCommandReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * The main class.
 *
 * Created by Pavel.Krizskiy on 8/5/2017.
 */
public class App {

    /** Entry point */
    public static void main(String[] args) throws Exception {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConf.class);
        ConsoleCommandReader reader = (ConsoleCommandReader) ctx.getBean("consoleCommandReader");
        new Thread(reader).start();
    }
}
