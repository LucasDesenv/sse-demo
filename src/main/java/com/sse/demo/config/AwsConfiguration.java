package com.sse.demo.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.sse.demo.domain.repositories.OrderRepository;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.config.annotation.EnableSqs;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * To deploy a DynamoDB in a private access:
 * 1 - Create a VPC
 * 2 - Create a subnet if not already created.
 * 3 - Create a Security Group with inbound and outbound rules for your IP only.
 * 4 - Create an Endpoint in your VPC.
 * 4.1 - Assign the endpoint to the Dynamodb service.
 * 5 - Set Env variables AWS_ACCESS_KEY and AWS_SECRET_KEY
 */
@Profile("!test")
@Configuration
@EnableDynamoDBRepositories(basePackageClasses = {OrderRepository.class})
@EnableSqs
public class AwsConfiguration {

  @Value("${amazon.dynamodb.endpoint}")
  private String amazonDynamoDBEndpoint;

  @Value("${amazon.aws.accesskey}")
  private String amazonAWSAccessKey;

  @Value("${amazon.aws.secretkey}")
  private String amazonAWSSecretKey;
  private final Region region = Region.getRegion(Regions.EU_CENTRAL_1);


  @Bean
  public AmazonDynamoDB amazonDynamoDB() {
    AmazonDynamoDB amazonDynamoDB = new AmazonDynamoDBClient(amazonAWSCredentials());
    amazonDynamoDB.setRegion(region);
    return amazonDynamoDB;
  }

  @Bean
  public AWSCredentials amazonAWSCredentials() {
    return new BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey);
  }

  /**
   * Change Access Policy in the queue to enable our security-group to access the queue.
   * Eg.:
   {
   "Sid": "StmtXXXXXXXX",
   "Effect": "Allow",
   "Principal": {
   "AWS": "arn:aws:iam::XXXXXXXX:user/my-localhost-app"
   },
   "Action": "sqs:*",
   "Resource": "arn:aws:sqs:eu-central-1:XXXXXXXX:orders-queue"
   }
   * @param amazonAWSCredentials
   * @return
   */
  @Bean
  public AmazonSQS amazonSQSClient(AWSCredentials amazonAWSCredentials) {
    return AmazonSQSClientBuilder.standard()
      .withCredentials(new AWSStaticCredentialsProvider(amazonAWSCredentials))
      .withRegion(region.getName())
      .build();
  }

}