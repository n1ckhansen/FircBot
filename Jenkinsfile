pipeline {
  agent any
  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }
    stage('Build') {
      steps {
        sh './gradlew clean'
        sh './gradlew check'
        sh './gradlew buildEnvironment'
        sh './gradlew properties'
        sh './gradlew assembleDist'
      }
    }
    stage( 'Stage' ) {
        when {
            branch 'master'
        }
        steps {
            sh './gradlew bintrayUpload'
        }
    }
  }
}
