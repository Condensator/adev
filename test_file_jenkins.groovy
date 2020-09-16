pipeline {
    agent any
    stages{
        stage('Example') {
            steps{
                script{
                    sh "find . -type f -printf \"%T@ %p\\n\" | sort -nr | cut -d\\  -f2-"
                }
            }
        }
    }
}
