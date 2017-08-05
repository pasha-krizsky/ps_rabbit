package com.pasha.rabbitutils.reader;

import com.pasha.rabbitutils.processor.IProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Scanner;

/**
 * A class that reads the commands from console.
 *
 * Created by Pavel.Krizskiy on 8/5/2017.
 */
public class ConsoleCommandReader implements ICommandReader {

    @Autowired
    @Qualifier("commandProcessor")
    private IProcessor processor;

    @Override
    public void run() {
        System.out.println("I read commands from the console :)");
        System.out.println();

        Scanner in = new Scanner(System.in);
        String command = in.nextLine();

        processor.sendMessage(command);
        new Thread(processor).start();
    }
}
