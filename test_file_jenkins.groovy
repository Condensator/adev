pipeline {
    agent any

    stages {
        stage('Build') {
            script{
                sh "find . -type f -printf \"%T@ %p\\n\" | sort -nr | cut -d\\  -f2-"
            }
        }
        stage('Test') {
            steps {
                echo 'Testing..'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying....'
            }
        }
    }
}
