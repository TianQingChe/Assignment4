import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;

public class DynamoClient {
	
	private AmazonDynamoDB dynamoDB;
	private DynamoDBMapper mapper;
	private final String tableName = "Walk1024";
	
	public DynamoClient() throws Exception {
		init();
		mapper = new DynamoDBMapper(dynamoDB);
	}
	
    private void init() throws Exception {
        /*
         * The ProfileCredentialsProvider will return your [default]
         * credential profile by reading from the credentials file located at
         * (C:\\Users\\tianq\\.aws\\credentials).
         */
        ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();
        try {
            credentialsProvider.getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                    "Please make sure that your credentials file is at the correct " +
                    "location (C:\\Users\\tianq\\.aws\\credentials), and is in valid format.",
                    e);
        }
        dynamoDB = AmazonDynamoDBClientBuilder.standard()
            .withCredentials(credentialsProvider)
            .withRegion("us-west-2")
            .build();
        
        
    }
    
    public void post(int user_id, int day, int time_interval, int step_count) throws Exception{
//        Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
//        item.put("user_id", new AttributeValue().withN(Integer.toString(user_id)));
//        item.put("day", new AttributeValue().withN(Integer.toString(day)));
//        item.put("time_interval", new AttributeValue().withN(Integer.toString(time_interval)));
//        item.put("step_count", new AttributeValue().withN(Integer.toString(step_count)));
//        PutItemRequest putItemRequest = new PutItemRequest(tableName, item);
//        PutItemResult putItemResult = dynamoDB.putItem(putItemRequest);
        
        StepItem item = new StepItem();
        item.setUser_id(user_id);
        item.setDay(day);
        item.setTime_interval(time_interval);
        item.setStep_count(step_count);
        
        mapper.save(item);
    }
    
//    public static void main(String[] args) {
//    	try {
//			DynamoClient test = new DynamoClient();
//			test.post(1, 1, 1, 1);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    }
    
    public String getStepCount(int user_id, int day) throws Exception{
    	HashMap<String, AttributeValue> key = new HashMap<String, AttributeValue>();
        key.put("user_id", new AttributeValue().withN(Integer.toString(user_id)));
        key.put("day", new AttributeValue().withN(Integer.toString(day)));

        GetItemRequest request = new GetItemRequest()
            .withTableName(tableName)
            .withKey(key);

        GetItemResult result = dynamoDB.getItem(request);
        AttributeValue stepCount = result.getItem().get("step_count");
        return stepCount.getN();
    }
    
    public List<String> getStepCountRange(int user_id, int start_day, int end_day) throws Exception{


        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":val1", new AttributeValue().withN(Integer.toString(user_id)));
        eav.put(":val2", new AttributeValue().withN(Integer.toString(start_day)));
        eav.put(":val3", new AttributeValue().withN(Integer.toString(end_day)));

        DynamoDBQueryExpression<StepItem> queryExpression = new DynamoDBQueryExpression<StepItem>()
            .withKeyConditionExpression("user_id = :val1 and day between :val2 and :val3")
            .withExpressionAttributeValues(eav);

        List<StepItem> betweenReplies = mapper.query(StepItem.class, queryExpression);
        
        List<String> res = new ArrayList<>();
        for (StepItem reply : betweenReplies) {
            res.add(reply.getStep_count().toString());
        }
        
        return res;
    }

}
