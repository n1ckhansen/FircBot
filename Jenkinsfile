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
        sh './gradlew test'
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
  post {
    always {
      junit 'build/test-results/**/*.xml'
    }
  }
  publishHTML ([
    allowMissing: true, 
    alwaysLinkToLastBuild: true, 
    keepAll: false, 
    reportDir: 'build/reports/testst/test', 
    reportFiles: 'index.html', 
    reportName: 'Tests Report', 
    reportTitles: ''
  ])
}
