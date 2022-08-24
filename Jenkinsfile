pipeline {
  agent {
      label 'zvisitor-node'
  }
  parameters{
    choice(name: 'Branch', choices: ['develop','master','devops'], description: "")
  }
  stages {
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