
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;

public class MyClient implements Callable<CustomResponse> {
    private final int iterNum;
    private final int userPopulation;
    private DynamoClient dynamoClient;
//    private CountDownLatch latch;

    private int requestCount = 0;
    private int successCount = 0;
    private List<Latency> latencyLst = new ArrayList<>();
    private int interval_start;
    private int interval_end;

    public MyClient(int iterNum, int dayNumber, int userPopulation, int interval_start, int interval_end) throws Exception{
    	dynamoClient = new DynamoClient();
        this.iterNum = iterNum;
        this.userPopulation = userPopulation;
        this.interval_start = interval_start;
        this.interval_end = interval_end;
    }

    private int randomGenerator(){
        return interval_start + ThreadLocalRandom.current().nextInt(interval_end - interval_start + 1);
    }

    public CustomResponse call() throws IOException {

        for(int i = 0; i < iterNum; i++){
            System.out.println(i);
            int userId1 = 1 + ThreadLocalRandom.current().nextInt(userPopulation);
            int userId2 = 1 + ThreadLocalRandom.current().nextInt(userPopulation);
            int userId3 = 1 + ThreadLocalRandom.current().nextInt(userPopulation);
            int timeInterval1 = randomGenerator();
            int timeInterval2 = randomGenerator();
            int timeInterval3 = randomGenerator();
            int stepCount1 = ThreadLocalRandom.current().nextInt(5001);
            int stepCount2 = ThreadLocalRandom.current().nextInt(5001);
            int stepCount3 = ThreadLocalRandom.current().nextInt(5001);

//            String post1Str = BASE_URI + "/" + userId1 + "/" + day + "/" + timeInterval1 + "/" + stepCount1;
//            String post2Str = BASE_URI + "/" + userId2 + "/" + day + "/" + timeInterval2 + "/" + stepCount2;
//            String post3Str = BASE_URI + "/" + userId3 + "/" + day + "/" + timeInterval3 + "/" + stepCount3;
//
//            String get1Str = BASE_URI + "/current/" + userId1;
//            String get2Str = BASE_URI + "/single/" + userId2 + "/" + day;
            post(userId1, 1, timeInterval1, stepCount1);
            post(userId2, 1, timeInterval2, stepCount2);
            get(userId1, 1);
            get(userId2, 1);
            post(userId3, 1, timeInterval3, stepCount3);

        }

//        try {
//            this.httpClient.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        this.connManager.shutdown();
//        latch.countDown();
        return new CustomResponse(requestCount,successCount,latencyLst);
    }

    private void post(int userId, int day, int timeInterval, int stepCount){
        try {
            requestCount++;
            successCount++;
            long start = System.currentTimeMillis();
            dynamoClient.post(userId, day, timeInterval, stepCount);
            long end = System.currentTimeMillis();
            Latency latency = new Latency(start, end - start);
            latencyLst.add(latency);
        } catch (Exception e) {
            successCount--;
            e.printStackTrace();
        }
    }
    
    private void get(int userId, int day){
        try {
            requestCount++;
            successCount++;
            long start = System.currentTimeMillis();
            dynamoClient.getStepCount(userId, day);
            long end = System.currentTimeMillis();
            Latency latency = new Latency(start, end - start);
            latencyLst.add(latency);
        } catch (Exception e) {
            successCount--;
            e.printStackTrace();
        }
    }


}
