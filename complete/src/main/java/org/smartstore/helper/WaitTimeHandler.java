package org.smartstore.helper;

import org.smartstore.IoTScheduleController;

/**
 * Created by SwathiBala on 5/4/16.
 */
public class WaitTimeHandler {

    private int defaultWaitTime=60;

    public int calculateWaitTime(int count,long startTime)
    {
        int returnWaitTime=0;
        //base case if no one stands in line
        if(count==0)
        {
           return returnWaitTime;
        }

        //someoneleft the line
        int prevCount= IoTScheduleController.prevCount;
        if(count<prevCount)
        {
            IoTScheduleController.startTime=System.currentTimeMillis();
            int pplLeft=prevCount-count;
            long endTime=System.currentTimeMillis();
            long waitTime = endTime - startTime;
            System.out.println("difference between time is::"+waitTime);
            long formattedTime=waitTime / 1000 % 60;
            System.out.println("Formatted time is::"+formattedTime);
            int time=(int)formattedTime/pplLeft;
            System.out.println("Wait time is::"+time);
            returnWaitTime=time*pplLeft;
            System.out.println("Wait time based on number of ppl left is::"+returnWaitTime);
            IoTScheduleController.prevCount=count;
            IoTScheduleController.lastWaitTime=returnWaitTime;
        }// id prevcount and current count are same. get start and end time and multiply with count of ppl
        else if(prevCount==count)
        {
            long endTime=System.currentTimeMillis();
            System.out.println("Both counts are same ! noone left line.");
            System.out.println(IoTScheduleController.startTime);
            System.out.println(endTime);
            long waitTime = endTime - IoTScheduleController.startTime;
            System.out.println("difference between time is::"+waitTime);
            long formattedTime=waitTime / 1000;
            System.out.println("Formatted time is::"+formattedTime);
            returnWaitTime=(int)formattedTime*count;
            IoTScheduleController.lastWaitTime=returnWaitTime;
        }
        else if(prevCount<count) // if additional ppl joined line 1 to 5
        {
            if(IoTScheduleController.lastWaitTime!=null) // if last wait time has no value set
            {
            returnWaitTime=IoTScheduleController.lastWaitTime*count;
            }
            else
            {
                returnWaitTime=defaultWaitTime*count;
            }
        }
        return returnWaitTime;
    }


}
