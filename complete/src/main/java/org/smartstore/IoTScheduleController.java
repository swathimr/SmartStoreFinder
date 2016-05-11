package org.smartstore;


import org.json.JSONObject;
import org.smartstore.controller.WaitTimeController;
import org.smartstore.helper.WaitTimeHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by SwathiBala on 5/7/16.
 */
@Component
public class IoTScheduleController {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    public static long startTime;
    public static int prevCount;
    public static Integer lastWaitTime;
    int i=1;
    int customCount =3;

    //@Scheduled(fixedRate =30000)
    @Scheduled(fixedRate=30000)
    public void reportCurrentTime() {

        System.out.println("The time is now " + dateFormat.format(new Date()));
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.getForEntity("http://192.168.1.13:5000/getPeople/api/v1.0/count", String.class);

            System.out.println(response.getStatusCode());
            JSONObject json = new JSONObject(response.getBody());
            int count = Integer.valueOf(json.get("count").toString());
            //for testing
            //int count=1;

            //starting time for first time
            if (count > 0 && i == 1) {
                System.out.println("started time");
                startTime = System.currentTimeMillis();
                prevCount = count;
                i = 0;
                //for testing
                // prevCount=2;
            } else {
                System.out.println("Resetted time dont worry");
                WaitTimeHandler waitHndlr = new WaitTimeHandler();
                WaitTimeController.waitTime = waitHndlr.calculateWaitTime(count, startTime);
                System.out.println(WaitTimeController.waitTime);
            }
        }
        catch(Exception excep)
        {
            customHandler();
        }
        finally {
            System.out.println(WaitTimeController.waitTime);
        }
    }

    // if pi is not runing
    private void customHandler() {

        int customDefaultTime=40;
        if(customCount==3)
        {
           WaitTimeController.waitTime=3*40;

            customCount=customCount-1;
        }
        else if(customCount==2)
        {
            WaitTimeController.waitTime=2*40;
            customCount=customCount-1;
        }
        else if(customCount==1){
            WaitTimeController.waitTime=1*40;
            customCount=customCount-1;
        }
        else{
            WaitTimeController.waitTime=0;
            customCount=3;
        }
    }
}
