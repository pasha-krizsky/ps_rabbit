package com.pasha.rabbitutils.conf;

import com.pasha.rabbitutils.processor.CommonCommandProcessor;
import com.pasha.rabbitutils.processor.IProcessor;
import com.pasha.rabbitutils.reader.ConsoleCommandReader;
import com.pasha.rabbitutils.reader.ICommandReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class, creating beans.
 *
 * Created by Pavel.Krizskiy on 8/5/2017.
 */
@Configuration
@ComponentScan("com.pasha.rabbitutils")
public class AppConf {

    @Bean(name = "consoleCommandReader")
    public ICommandReader consoleCommandReader(){
        return new ConsoleCommandReader();
    }

    @Bean(name = "commandProcessor")
    public IProcessor commandProcessor(){
        return new CommonCommandProcessor();
    }
}
