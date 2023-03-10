pipeline {
    agent { label 'ubuntu' }

    options {
        buildDiscarder(logRotator(numToKeepStr: '10', artifactNumToKeepStr: '10'))
    }

    stages {
        stage('Clear docker environment') {
            steps{
                sh '''
                    echo 'Delete all containers'
                    docker rm -f $(docker ps -a -q) &> /dev/null
                '''
            }
        }

        stage('Pull docker image') {
            steps {
                withDockerRegistry(credentialsId: 'dockerhub-cred-kzbar', url: 'https://index.docker.io/v1/') {
                    sh 'docker pull kzbar/shopping-cart:dev'
                }
            }
        }

        stage('Deploy') {
            steps {
                sh 'docker run --rm -i -p 80:80 -d --name shopping-cart kzbar/shopping-cart:dev'
                // sh 'docker run --rm -i -p 8070:8070 -d --name shopping-cart shopping-cart:dev'
            }
        }
    }
}