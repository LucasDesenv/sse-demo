import json

def lambda_handler(event, context):
    try:
        sns = boto3.client("sns")
        topic_arn = "arn:aws:sns:eu-central-1:ID_HERE:TOPIC_NAME"

        sns.publish(
            TopicArn=topic_arn,
            Message=json.dumps(event["Records"][0]["dynamodb"]["NewImage"])
        )

        return "Message published successfully!"
    except Exception as e:
        print(f"Error publishing message: {e}")
        raise