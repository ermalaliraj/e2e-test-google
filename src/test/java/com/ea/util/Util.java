package com.ea.util;

import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@UtilityClass
public class Util {

    private static final Logger logger = LoggerFactory.getLogger(Util.class);

    public static void wait(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (Exception e) {
            logger.error("Exception occurred while waiting", e);
        }
    }

}