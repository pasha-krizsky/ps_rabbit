package com.pasha.rabbitutils.processor;

/**
 * An interface for all processors.
 *
 * Created by Pavel.Krizskiy on 8/5/2017.
 */
public interface IProcessor<T> extends Runnable {
    void sendMessage(T message);
}
