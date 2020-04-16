package ru.work.webcalendar.Service.DynamoDB;


import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class AWSDynamoDB {
    @Value("${amazon.dynamodb.accessKey}")
    private  String ACCESS_KEY;
    @Value("${amazon.dynomodb.secretKey}")
    private  String SECRET_KEY ;
    private AmazonDynamoDB amazonDynamoDB;
    private DynamoDB dynamoDB;

    /*@Bean
    public DynamoDbClient dynamoDbClient(){

        return DynamoDbClient.builder().credentialsProvider(awsCredentialsProvider()).region(Region.US_EAST_2).build();
    }*/

    @Bean
    public AmazonDynamoDB getDynamoDBClientV2(){
        amazonDynamoDB= AmazonDynamoDBClient.builder().withCredentials(awsCredentialsProviderv2()).withRegion(Regions.US_EAST_2).build();
        return amazonDynamoDB;
    }
    @Bean
    public DynamoDB getDynomoDb(){
        dynamoDB=new DynamoDB(getDynamoDBClientV2());
        return dynamoDB;
    }
   /* @Bean
    public AwsCredentialsProvider awsCredentialsProvider(){
        return new AwsCredentialsProvider() {
            @Override
            public AwsCredentials resolveCredentials() {
                return new AwsCredentials() {
                    @Override
                    public String accessKeyId() {
                        return ACCESS_KEY;
                    }

                    @Override
                    public String secretAccessKey() {
                        return SECRET_KEY;
                    }
                };
            }
        };
    }*/

    @Bean
    public AWSCredentialsProvider awsCredentialsProviderv2(){
       return new AWSCredentialsProvider() {
           @Override
           public AWSCredentials getCredentials() {
               return new AWSCredentials() {
                   @Override
                   public String getAWSAccessKeyId() {
                       return ACCESS_KEY;
                   }

                   @Override
                   public String getAWSSecretKey() {
                       return SECRET_KEY;
                   }
               };
           }

           @Override
           public void refresh() {

           }
       };
    }


}
