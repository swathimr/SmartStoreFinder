package org.smartstore.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by SwathiBala on 5/7/16.
 */
@RestController
public class WaitTimeController {


    public static int waitTime;

    @RequestMapping("/hello")
    public String home() {
        return "Hi I am from the nimbleshop";
    }

    @RequestMapping("/getWaitingTime")
    public String getWaittime()
    {
        if(waitTime>=60)
        {
            return String.valueOf(waitTime/60)+" min";
        }
        return String.valueOf(waitTime)+" Sec";
    }
}
