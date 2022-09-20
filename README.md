# ZVisitor

    ZVisitor is Evident way to check in visitors.
    Having functionality to collect visitor information, snap thier photos and have their NDA signs.  
    And also notify the concerned person or group about their arrival on SLACK and EMAIL.

## Getting Started

### Prerequisites

    Install Java - 1.8.0_101 set `JAVA_HOME` environment variable points to the JDK8 latest installation.  
    Install MongoDB - 3.4.9


### Configuration:


#### Database configuration
Configure database connection in `application.yml` file.You need to set the following database connection configurations:
 ```
 spring:
  data:
    mongodb:
      database: <database-name>
      host: <database-server-host>
      port: 27017
      username: <database-user-username> 
      password: <database-user-password>
      authentication-database: <authentication-database>
```

#### Mounted Volume Path
Path are as follows: 
  - data: /var/lib/docker/volumes/zymrvisitor_data
  - logs: /var/lib/docker/volumes/zymrvisitor_logs
  - mongologs: /var/lib/docker/volumes/zymrvisitor_mongologs
  - files: /var/lib/docker/volumes/zymrvisitor_files

#### Mail configuration



Mail configuration in `application.yml` file
```
    mail:
    host: smtp.gmail.com
    port: 587
    username: <email-id>
    password: <email-password>
    properties:
      mail.smtp.auth: true
      mail.smtp.starttls.enable: true
      
app:
    mail-personal: <mail-from-personal>
```

#### Slack configuration

Slack account configuration in `application.properties` file.
This slack token and username will be used to send slack notification to other slack users to notify about visitor information.

```
slack:
      token: <slack-user-account-token>
      user-name: <slack-user-account-username>
```
    
#### App configuration

```
    emp-sync-job: 0 0 10 * * *
```
* Employee sync job will be schedule based on cron expression. 
* Job is for syncing employee details from appropriate slack channels.
 

```
    admin-email: admin
    admin-password: admin
```
* Admin username password need to configure here.It will be used for login in admin panel.


```
    secretkey: <Secret Key>
```
* SecretKey will be used for encrypt/decrypt password.

```
    config:
      file-uploads-path: <file-upload-path>
```
* File system base path where application all files will be store . EX: /home/root/zvisitor/

```
    org:
        valid-email-domains:
          - <valid-email-domains>
```
* It will used to validate employees of organization with their specific email id. EX: `- zymr.com -zymrinc.om`
* This email domain will be used to filter out only employess while syncing from different slack channels (configured slack channels in below locations section). This employee list will be used to display on app side where visitor has to select whom to meet.

```
 department:
        -
          slackid: <slack-channel-id1>
          email: <slack-group1-email1>
        -
          slackid: <slack-channel-id2>
          email: <slack-group1-email2>
        -
          slackid: <slack-channel-id3>
          email: <slack-group1-email3>
```
 - Organization departments need to configured with slack group and thier email id.
    - List will be displayed in App so visitor can choose concerned department and thier information will be notify according to configured slackid and  email.
    - EX: `slackid: G5FPBCQR5
          email: test@gmail.com`

```
 locations:
        -
          abbr: <location-abbreviation1>
          slackid: <slack-channel-id1>
          name: <location-name1>
        -
          abbr: <location-abbreviation2>
          slackid: <slack-channel-id2>
          name: <location-name2>
```
 - App will have location settings to differentiate visitor location.
    - Locations will be populated on App side with their name.
    - Slack channel id used to get employees with their location.
    - Ex: `abbr: z2
          slackid: A2LFBPHAQ9
          name: Ahmedabad`
```
server:
  address: <server-host>
  port: <server-port>
```
* Need to configure machine ip where server is running. Ex : 20.10.65.87.address default value will be 127.0.0.1 and port default will be 8080.

*   Note : Update configuration file as follow before running the build


### Built With

*   [Maven](https://maven.apache.org/) - Dependency Management
    Build tool is Maven, with following mostly used tasks:  
    ```mvn clean package```


### Run

    java -jar <jar-file-name-with-extension>
