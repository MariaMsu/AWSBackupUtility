# AWS Backup Utility

### How to set credentials
Set the default credentials before running the utility
([the instruction for all the OS](https://docs.aws.amazon.com/sdk-for-kotlin/latest/developer-guide/setup.html
)).  
An example for linux:
```shell
export AWS_ACCESS_KEY_ID=your_access_key_id 
export AWS_SECRET_ACCESS_KEY=your_secret_access_key
```

### How to build:
```shell
gradle build 
```

### How to test:
```shell
gradle test -i
```
Occasionally the [ActionsTest.kt](src%2Ftest%2Fkotlin%2Factions%2FActionsTest.kt) may fail 
because of the network issues. 

### How to use, quick example
```shell
# make the backup
java -jar ./build/libs/AWSBackupUtility-1.0-SNAPSHOT.jar store \
 -i /home/omar/Desktop/testDir -k backupTest

# restore the whole directory
java -jar ./build/libs/AWSBackupUtility-1.0-SNAPSHOT.jar restore \
 -o /home/omar/Desktop/testDirOut -k backupTest

# restore a single file
java -jar ./build/libs/AWSBackupUtility-1.0-SNAPSHOT.jar restore-file \
 -o /home/omar/Desktop/testDirSingleFile -f gradle/wrapper/gradle-wrapper.properties -k backupTest
```

### How to use, the whole guide
The AWS Backup Utility can be run as a gradle project `gradle run --args="<command> <command_arguments>"` 
or as a fat jar `java -jar ./build/libs/AWSBackupUtility-1.0-SNAPSHOT.jar <command> <command_arguments>`.
The following examples are provided for a fat jar.

Backup a directory:
```shell
java -jar ./build/libs/AWSBackupUtility-1.0-SNAPSHOT.jar store \
  -i <path to the storing directory> \
  -k <the backup key (i.e. file name) in the S3 bucket> \
  -b <S3 bucket name>
```

List a bucket:
```shell
java -jar ./build/libs/AWSBackupUtility-1.0-SNAPSHOT.jar list-bucket \
  -b <S3 bucket name>
```

Restore the whole directory:
```shell
java -jar ./build/libs/AWSBackupUtility-1.0-SNAPSHOT.jar restore \
  -o <path to the restoring directory> \
  -k <the backup key (i.e. file name) in the S3 bucket> \
  -b <S3 bucket name>
```

Restore a single file:
```shell
java -jar ./build/libs/AWSBackupUtility-1.0-SNAPSHOT.jar restore-file \
  -o <path to the restoring directory> \
  -f <the relative file path from the stored directory root> \
  -k <the backup key (i.e. file name) in the S3 bucket> \
  -b <S3 bucket name>
```

Delete a backup from the bucket:
```shell
java -jar ./build/libs/AWSBackupUtility-1.0-SNAPSHOT.jar delete-backup \
  -k <the backup key (i.e. file name) in the S3 bucket> \
  -b <S3 bucket name>
```

Delete the whole bucket:
```shell
java -jar ./build/libs/AWSBackupUtility-1.0-SNAPSHOT.jar delete-bucket \
  -b <S3 bucket name>
```
