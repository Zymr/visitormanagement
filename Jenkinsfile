pipeline {
  agent {
      label 'Zvisitor-stagging'
  }
  parameters{
    choice(name: 'BRANCH', choices: ['develop','master','devops-version-1.0', 'config_env_changes'], description: "")
  }
  stages {
        stage('Checkout'){
            steps {
                git branch: '${BRANCH}',
                credentialsId:'zvisitor-github',
                url:'https://github.com/Zymr/visitormanagement.git'
                }
            }
          stage('GENERATING ENV FILES') {
            steps {
                
              sh '''
                'ls'
                rm -rf .env 
                ls -la
                cat >> .env <<EOL
                MAIL_PERSONAL:'$MAIL_PERSONAL'
                ADMIN_MAIL:'$ADMIN_MAIL'
                ADMIN_PASSWORD:'$ADMIN_PASSWORD'
                MONGODB_USERNAME:'$MONGODB_USERNAME'
                SLACK_TOKEN:'$SLACK_TOKEN'
                SLACK_USERNAME:'$SLACK_USERNAME'
                MAIL_USERNAME:'$MAIL_USERNAME'
                MAIL_PASSWORD:'$MAIL_PASSWORD'
                MONGODB_USERNAME: '$MONGODB_USERNAME'
                MONGODB_PASSWORD: '$MONGODB_PASSWORD'
                VALID_MAIL: '$VALID_MAIL'
                ORG_DEPARTMENT_EMAIL: '$ORG_DEPARTMENT_EMAIL'
                '''              
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
                sh 'sudo docker image prune -af --filter until=168h'
                sh 'docker images'
                echo 'Unused images are removed Successfully !!!'
                sh 'docker-compose ps'
            }
        }
  }
}