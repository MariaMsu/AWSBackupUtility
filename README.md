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

### How to build
```shell
./gradlew build 
```

### How to test
```shell
./gradlew test --info
```
Occasionally the [ActionsTest.kt](src%2Ftest%2Fkotlin%2Factions%2FActionsTest.kt) may fail 
because of the network issues. 

### How to use, quick example
(optional) Build and run a docker container to use it as a sandbox.
You can test the AWS Backup Utility inside this docker container.
```shell
docker build --build-arg key_id=${AWS_ACCESS_KEY_ID} --build-arg key=${AWS_SECRET_ACCESS_KEY} -t aws-util:02 .
docker run -it aws-util:02
```

In this example we create the following folders structure and store 'dir'
```
testUtility321
└── dir
    ├── dir1
    │   └── spell.md
    └── text.txt
```
```shell
mkdir -p ./testUtility321/dir/dir1
echo "text - text - text" > testUtility321/dir/text.txt
echo "Kreks Peks Shmeks" > testUtility321/dir/dir1/spell.md

# make the backup
java -jar ./build/libs/AWSBackupUtility-1.0.0.jar store \
 -i ./testUtility321/dir -k backupTest

# restore the whole directory to ./testUtility321/out-dir
java -jar ./build/libs/AWSBackupUtility-1.0.0.jar restore \
 -o ./testUtility321/out-dir -k backupTest

# restore a single file to ./testUtility321/out-file
java -jar ./build/libs/AWSBackupUtility-1.0.0.jar restore-file \
 -o ./testUtility321/out-file -f dir1/spell.md -k backupTest
```

Delete test folder:
```shell
rm -rf ./testUtility321/
```


### How to use, the whole guide
The AWS Backup Utility can be run as a gradle project `./gradlew run --args="<command> <command_arguments>"` 
or as a fat jar `java -jar ./build/libs/AWSBackupUtility-1.0.0.jar <command> <command_arguments>`.
The following examples are provided for a fat jar.

Backup a directory:
```shell
java -jar ./build/libs/AWSBackupUtility-1.0.0.jar store \
  -i <path to the storing directory> \
  -k <the backup key (i.e. file name) in the S3 bucket> \
  -b <S3 bucket name>
```

List a bucket:
```shell
java -jar ./build/libs/AWSBackupUtility-1.0.0.jar list-bucket \
  -b <S3 bucket name>
```

Restore the whole directory:
```shell
java -jar ./build/libs/AWSBackupUtility-1.0.0.jar restore \
  -o <path to the restoring directory> \
  -k <the backup key (i.e. file name) in the S3 bucket> \
  -b <S3 bucket name>
```

Restore a single file:
```shell
java -jar ./build/libs/AWSBackupUtility-1.0.0.jar restore-file \
  -o <path to the restoring directory> \
  -f <the relative file path from the stored directory root> \
  -k <the backup key (i.e. file name) in the S3 bucket> \
  -b <S3 bucket name>
```

Delete a backup from the bucket:
```shell
java -jar ./build/libs/AWSBackupUtility-1.0.0.jar delete-backup \
  -k <the backup key (i.e. file name) in the S3 bucket> \
  -b <S3 bucket name>
```

Delete the whole bucket:
```shell
java -jar ./build/libs/AWSBackupUtility-1.0.0.jar delete-bucket \
  -b <S3 bucket name>
```
