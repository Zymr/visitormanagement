pipeline {
    parameters {
        choice(name: 'BRANCH', choices: ['develop', 'master', 'config_env_changes', 'devops'], description: 'Select the branch')
    }
    agent {
        label "'${AGENT}'"
    }
    // environment {
    //     SECRET_FILE_ID = credentials('zvisitor_config_file')
    // }
    stages {
        stage('GENERATING ENV FILES') {
            steps {
                
              sh '''
                rm -rf .env
                cat >>.env <<EOL
                ADMIN_MAIL:'$ADMIN_MAIL'
                ADMIN_PASSWORD:'$ADMIN_PASSWORD'
                SLACK_TOKEN:'$SLACK_TOKEN'
                SLACK_USERNAME:'$SLACK_USERNAME'
                MAIL_USERNAME:'$MAIL_USERNAME'
                MAIL_PASSWORD:'$MAIL_PASSWORD'
                MONGODB_USERNAME:'$MONGODB_USERNAME'
                MONGODB_PASSWORD:'$MONGODB_PASSWORD'
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
                sh 'docker image prune -af --filter until=168h'
                sh 'docker images'
                echo 'Unused images are removed Successfully !!!'
            }
        }
    }
}
