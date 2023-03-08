pipeline {
    agent { label 'amazon' }

    options {
        buildDiscarder(logRotator(numToKeepStr: '10', artifactNumToKeepStr: '10'))
    }

    stages {
        stage('Clear docker environment') {
            steps{
                sh '''
                    echo 'Delete all containers'
                    docker ps -a -q | xargs --no-run-if-empty docker rm -f
                    #docker rm -f $(docker ps -a -q) &> /dev/null
                '''
            }
        }

        stage('Pull docker image') {
            steps {
                withDockerRegistry(credentialsId: 'dockerhub-azbr', url: 'https://index.docker.io/v1/') {
                    sh 'docker pull azbr/final-project:dev'
                }
            }
        }

        stage('Deploy') {
            steps {
                sh 'docker run --rm -i -p 80:80 -d --name shopping-cart azbr/final-project:dev'
                // sh 'docker run --rm -i -p 8070:8070 -d --name shopping-cart shopping-cart:dev'
            }
        }
    }
}