## Creating an event-driven architecture with CDC.

1. POST /orders
2. Stores in DynamoDB
3. DynamoDB Stream notifies upon a new entry to the Lambda
4. Lambda is triggered and publish a notification to SNS with the new Object (best practice would be sending only the id).
5. SNS receives the notification
6. SNS publishes into a SQS
7. Microservice is listening to the SQS event and receives it.

#### AWS setup

#### User:
1. First be sure to create a new User (IAM) to not use the root.
2. Enable the user if it is disabled (IAM -> Users)
3. Grant access to DynamoDB and SQS

#### Components:

1. Create a VPC
2. Create a subnet if not already created.
3. Create a Security Group with inbound and outbound rules for your IP only.
4. Create an Endpoint in your VPC.
5. Assign the endpoint to the Dynamodb service.
6. Create a SQS
   1. Add a policy in the statement array. The security group created (#3) should be allowed to access our queue.
   2.    `{
         "Sid": "StmtXXXXXXXX",
         "Effect": "Allow",
         "Principal": {
         "AWS": "arn:aws:iam::XXXXXXXX:user/my-localhost-app"
         },
         "Action": "sqs:*",
         "Resource": "arn:aws:sqs:eu-central-1:XXXXXXXX:orders-queue"
         }`
   
7. Create an SNS and subscribe to the SQS created.
8. Create a [Lambda](lambda/lambda-example.py) Function in Python that will be responsible to listen to the DynamoDB events.
   1. In the Configuration tab, click on the Role.
   2. Create 2x new permissions policies: one for [accessing the EC2](lambda/lambda-access-to-ec2-policy.json) and other to [access DynamoDB](lambda/lambda-access-to-dynamodb-stream-policy.json).
9. Grant access to the Lambda to publish to our SNS topic
   1. Go to the SNS created and click on 'Edit'.
   2. Go to Access Policy
   3. Add the Lambda role to publish to our SNS topic.
   4. `{
      "Sid": "StmtXXXXXXXX",
      "Effect": "Allow",
      "Principal": {
      "AWS": "LAMBDA_ROLE_ARN"
      },
      "Action": "sns:Publish",
      "Resource": "OUR_SNS_ARN"
      }`
10. Create a DynamoDB table.
    1. Enable the stream for new images.
    2. In the trigger panel, link it to our Lambda created.
11. Result: DynamoDB -> (CDC) Stream enabled new images event -> Lambda -> SNS -> SQS -> Listener

