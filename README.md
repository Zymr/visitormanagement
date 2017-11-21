# ZVisitor

    ZVisitor is Evident way to check in visitors.
    Having functionality to collect visitor information, snap thier photos and have their NDA signs.  
    And also notify the concerned person or group about their arrival on SLACK and EMAIL.

## Getting Started

### Prerequisites

    Install Java - 1.8.0_101 set `JAVA_HOME` environment variable points to the JDK8 latest installation.  
    Install MongoDB - 3.4.9


### Configuration

*	Configure database connection in `application.properties` file

    You need to set the following connection configurations:
    -   spring.data.mongodb.database=database_name
    -   spring.data.mongodb.host=localhost
    -   spring.data.mongodb.port=27017


*	Mail configuration in `application.properties` file

    -   spring.mail.host=smtp.gmail.com
    -   spring.mail.port=587
    -   spring.mail.username=test@gmail.com
    -   spring.mail.password=*****


*	Slack account configuration in `application.properties` file

    You need to set Authentication token from configured slack account.
    -   slack.token=xoxp-29****7900-**dfgdg***-*******-dgdhfg*******
    
*    Other Configuration :
    -   job.cronexp = 0 0 10 * * *
        - Employee sync job will be schedule based on cron expression. 
        - Job is for syncing employee details from appropriate slack channels.
    -   config.fileUploadsPath = file-upload-path
        - File system base path where application all files will be store . EX: /home/root
    -   config.fileBaseDir = file-base-dir-name
        - Application specific file system directroy name where related files will be stored. 
            EX: zvisitor. So, Final file system path will be /home/root/zvisitor.
    -   config.ndaFile = NDA.pdf
        - Recommended - Don't change anything apart from organization name.
    -   baseUrl = 'server-host:server-port'
        - Configure proper server host or server port EX : 'http://20.20.1.49:8080'. Don't remove single quotes.  
    -   valid.email.domain = valid-email-domains (EX: @zymr.com, @zymrinc.com)
        - It will used to validate employees of organization with their specific email id.
    -   zymr.department = {'slack-group1-slackid':'slack-group1-email', 'slack-group2-slackid':'slack-group2-email'}
        - Organization departments configured with slack group and thier email id.
        - List will be displayed in App so visitor can choose concerned department and thier information will be notify according to slack      group and email.
        - EX: {'G5FPBCQR5':'test@gmail.com', 'G5AFJQ8TX':'test2@gmail.com', 'GNGP7NS2B':'test3@gmail.com', 'G5FPCCQR6':'test5@gmail.com'}
    -   location = {'location-abbreviation':{'slack-channel-id':'location-name'}, 'location-abbreviation':{'slack-channel-id':'location-name'}}
        - App will have location settings to differentiate visitor location.
        - Locations will be populated on App side with their name.
        - Slack channel id used to get employees with their location.
        - Ex: {'z2':{'A2LFBPHAQ9':'Ahmedabad'},'z3':{'I2LEWP1B2':'Pune'}}
*   Note : Update configuration file as follow before running the build


### Built With

*   [Maven](https://maven.apache.org/) - Dependency Management
    Build tool is Maven, with following mostly used tasks:  
    ```mvn clean package```


### Run

    java -jar ZVisitor-0.0.1-SNAPSHOT.jar
