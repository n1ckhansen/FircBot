pipeline {
	agent any
	stages {
		stage 'Checkout'
		steps {
			checkout scm
		}
		stage 'Build'
		steps {
			sh ${workarea}/gradlew clean
			sh ${workarea}/gradlew assembleDist
		}
	}
}
