pipeline {
    agent {
        label 'Zvisitor-stagging'
    }
    environment {
        SECRET_FILE_ID = credentials('zvisitor_config_file')
    }
    parameters {
        choice(name: 'BRANCH', choices: ['develop', 'master', 'devops-version-1.0', 'config_env_changes'], description: '')
    }
    stages {
        stage('Checkout') {
            steps {
                git branch: '${BRANCH}',
                credentialsId:'zvisitor_stagging_git',
                url:'https://github.com/Zymr/visitormanagement.git'
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
        stage('GENERATING ENV FILES') {
            steps {
                sh '''
               rm -rf .env
               mv $SECRET_FILE_ID /home/zvisitor-build/workspace/Zvisitor_stagging/.env
               '''
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
            }
        }
    }
}
