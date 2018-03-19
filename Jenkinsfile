node {
	stage 'Checkout'
	checkout scm

	stage 'Build'
	sh ${workarea}/gradlew clean
	sh ${workarea}/gradlew assembleDist
}
