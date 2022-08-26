pipeline {
  agent {
      label 'zvisitor-node'
  }
  parameters{
    choice(name: 'BRANCH', choices: ['develop','master','devops'], description: "")
  }
  stages {
        stage('Checkout'){
            steps {
                git branch: '${BRANCH}',
                credentialsId:'zvisitor-github',
                url:'https://github.com/Zymr/visitormanagement.git'
                }
            }
        stage("stop container") {
           steps {
                sh 'docker-compose down'
                sh 'docker-compose ps'
             }
           }     
        stage('Start container') {
          steps {
            sh 'docker-compose up --build -d'
            sh 'docker-compose ps'
          }
        }
  }
}