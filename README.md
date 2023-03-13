# AWS Backup Utility

### How to build und run
Set the default credentials 
([the instruction for all the OS](https://docs.aws.amazon.com/sdk-for-kotlin/latest/developer-guide/setup.html
))  
An example for linux:
```shell
export AWS_ACCESS_KEY_ID=your_access_key_id 
export AWS_SECRET_ACCESS_KEY=your_secret_access_key
```

```shell
gradle build 
gradle run

java -jar ./build/libs/AWSBackupUtility-1.0-SNAPSHOT.jar
```
