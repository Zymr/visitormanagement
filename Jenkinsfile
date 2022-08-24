pipeline {
  agent {
      label 'zvisitor-node'
  }
  parameters {
    choice choices: ['develop', 'master', 'devops'], name: 'Branch'
}
  stages {
        stage('Checkout'){
            steps {
                git branch:'$BRANCH',
                credentialsId:'zvisitor-github',
                url:'https://github.com/Zymr/visitormanagement.git'
                }
            }    
        
    stage("verify tooling") {
      steps {
        sh '''
          docker version
          docker info
          docker-compose version 
        '''
      }
    }
    // stage('Start container') {
    //   steps {
    //     sh 'docker-compose up'
    //     sh 'docker-compose ps'
    //   }
    // }
  }
//   post {
//     always {
//       sh 'docker-compose down --remove-orphans -v'
//       sh 'docker-compose ps'
//     }
//   }
}