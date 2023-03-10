pipeline {
    agent { label 'ubuntu' }

    parameters {
        string(name: 'Branch',
            description: 'Default branch to create/use',
            defaultValue: 'main', trim: true)
        string(
            name: 'Git_repository_URL',
            description: 'Public/Private full URL for repository',
            defaultValue: 'git@github.com:k-zbar/shopping-cart.git', trim: true)
    }

    options {
        buildDiscarder(logRotator(numToKeepStr: '10', artifactNumToKeepStr: '10'))
    }

    tools {
        maven '3.8.6'
    }

    stages {
        stage('Clear workspace') {
            steps{
                echo 'Clearing workspace'
                deleteDir()
            }
        }

        stage('Clear docker environment') {
            steps{
                sh '''
                    echo 'Delete all containers'
                    docker rm -f $(docker ps -a -q) &> /dev/null
                '''
            }
        }

        stage('Checkout') {
            steps{
                checkout([$class                           : 'GitSCM', branches: [[name: params.Branch]],
                    doGenerateSubmoduleConfigurations: false, extensions: [],
                    submoduleCfg                     : [],
                    userRemoteConfigs                : [[credentialsId: "github-kzbar-shopping-cart-cred",
                                                        url          : params.Git_repository_URL]]])
            }
        }

        stage('Compile') { 
            steps {
                sh 'mvn compile'
            }
        }

        stage('Tests') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests=true'
            }
        }

        stage('Build docker image') {
            steps {
                sh 'docker build -t kzbar/shopping-cart:dev -f docker/Dockerfile .'
            }
        }

        stage('Push docker image') {
            steps {
                withDockerRegistry(credentialsId: 'dockerhub-cred-kzbar', url: 'https://index.docker.io/v1/') {
                    sh 'docker push kzbar/shopping-cart:dev'
                }
            }
        }

        stage('Delete docker image locally') {
            steps {
                sh 'docker rmi kzbar/shopping-cart:dev'
            }
        }

        stage ('Starting Deploy job') {
            steps {
                echo "Triggering Deploy job for this application"
                build job: 'Deploy-shopping-cart-app', wait: false
            }
        }
    }
}