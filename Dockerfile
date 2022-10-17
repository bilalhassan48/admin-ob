
FROM 524540894843.dkr.ecr.eu-west-1.amazonaws.com/aws-runatlantis:latest AS AWSCLI
RUN apk --no-cache add aws-cli
RUN aws --version
ARG ACCESS_KEY
ARG SECRET_KEY
RUN echo $ACCESS_KEY && echo $SECRET_KEY
RUN echo ${ACCESS_KEY} && echo ${SECRET_KEY}
RUN export AWS_ACCESS_KEY_ID=${ACCESS_KEY} && export AWS_SECRET_ACCESS_KEY=${SECRET_KEY} && aws s3 cp s3://kyc-deployment-jar-files/admin-sdk/admin-sdk.jar /app/admin-sdk.jar

RUN apk update

RUN apk add fontconfig && apk add curl bash wget fontconfig ttf-dejavu freetype libpng libjpeg-turbo freetype-dev libpng-dev libjpeg-turbo-dev freetype-dev libjpeg-turbo-dev libpng-dev

FROM 524540894843.dkr.ecr.eu-west-1.amazonaws.com/jdk17:latest
RUN mkdir /app
COPY --from=AWSCLI /app/admin-sdk.jar /app

RUN java -version

WORKDIR /app

EXPOSE 8080
RUN java -version

ENTRYPOINT java -jar admin-sdk.jar

#adding test comment