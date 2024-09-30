#!/bin/bash

# Configuration Environment Variables
AWS_REGION="us-east-1"
AWS_PROFILE="localstack-profile"
AWS_ENDPOINT_URL=http://localhost:4566
BUCKET_NAME="person-bucket"

aws s3api create-bucket --bucket $BUCKET_NAME --endpoint-url $AWS_ENDPOINT_URL --profile $AWS_PROFILE --region $AWS_REGION