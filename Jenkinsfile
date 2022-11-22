pipeline {
    agent {
        label "'${AGENT}'"
    }
    stages {
        stage('GENERATING ENV FILES') {
            steps {
                
              sh '''
                rm -rf .env
                cat >>.env <<EOL
                MONGO_INITDB_ROOT_USERNAME=$MONGO_INITDB_ROOT_USERNAME
                MONGO_INITDB_ROOT_PASSWORD=$MONGO_INITDB_ROOT_PASSWORD
                ADMIN_MAIL=$ADMIN_MAIL
                ADMIN_PASSWORD=$ADMIN_PASSWORD
                SLACK_TOKEN=$SLACK_TOKEN
                SLACK_USERNAME=$SLACK_USERNAME
                MAIL_USERNAME=$MAIL_USERNAME
                MAIL_PASSWORD=$MAIL_PASSWORD
                MONGODB_USERNAME=$MONGODB_USERNAME
                MONGODB_PASSWORD=$MONGODB_PASSWORD
                LOGSTASH_HOSTS=$LOGSTASH_HOSTS
                '''              
            }
        }
        stage('Login into Nexus'){
            steps {
                withCredentials([usernamePassword(credentialsId: 'zymr-nexus-cred', passwordVariable: 'password', usernameVariable: 'username')]) {
                    sh 'docker login nexus.zymrinc.com:8083'
            }
          }
        }
        stage('Docker Compose Down') {
            steps {
                echo 'Taking down the Application .....'
                sh 'docker-compose down'
                sh 'docker-compose ps'
                echo 'Application down Successfully !!!'
            }
        }
        stage('DEPLOY') {
            steps {
                echo 'Deploying the Application .....'
                sh 'docker-compose up -d --build'
                sh 'docker-compose ps'
                echo 'Application Running Successfully !!!'
            }
        }
        stage('CLEANING NOT IN USE DOCKER IMAGE ') {
            steps {
                echo 'Cleaning the images on regular interval to avoid the low storage space left .....'
                sh 'docker images'
                sh 'docker image prune -af --filter until=168h'
                sh 'docker images'
                echo 'Unused images are removed Successfully !!!'
            }
        }
    }
}