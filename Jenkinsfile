pipeline {
	agent any
	stages {
		stage ('Checkout' ) {
			steps {
				checkout scm
			}
		}
		stage ( 'Build' ) {
			steps {
				sh 'gradlew clean'
				sh 'gradlew assembleDist'
			}
		}
	}
}
