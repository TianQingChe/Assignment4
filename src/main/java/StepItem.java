import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName="Walk1024")
public class StepItem {
	
	private Integer user_id;
	private Integer day;
	private Integer time_interval;
	private Integer step_count;
	
	@DynamoDBHashKey(attributeName="user_id")
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	
	@DynamoDBHashKey(attributeName="day")
	public Integer getDay() {
		return day;
	}
	public void setDay(Integer day) {
		this.day = day;
	}
	
	@DynamoDBAttribute (attributeName="time_interval")
	public Integer getTime_interval() {
		return time_interval;
	}
	public void setTime_interval(Integer time_interval) {
		this.time_interval = time_interval;
	}
	
	@DynamoDBAttribute (attributeName="step_count")
	public Integer getStep_count() {
		return step_count;
	}
	public void setStep_count(Integer step_count) {
		this.step_count = step_count;
	}
	
	

}
