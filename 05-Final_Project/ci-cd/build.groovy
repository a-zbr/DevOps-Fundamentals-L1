pipeline {
    agent { label 'amazon' }

    parameters {
        string(name: 'Branch',
            description: 'Default branch to create/use',
            defaultValue: 'main', trim: true)
        string(
            name: 'Git_repository_URL',
            description: 'Public/Private full URL for repository',
            defaultValue: 'git@github.com:a-zbr/DevOps-Fundamentals-L1.git', trim: true)
    }

    options {
        buildDiscarder(logRotator(numToKeepStr: '10', artifactNumToKeepStr: '10'))
    }

    tools {
        maven '3.9.0'
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
                    docker ps -a -q | xargs --no-run-if-empty docker rm -f
                    #docker rm -f $(docker ps -a -q) &> /dev/null
                '''
            }
        }

        stage('Checkout') {
            steps{
                checkout([$class                           : 'GitSCM', branches: [[name: params.Branch]],
                    doGenerateSubmoduleConfigurations: false, extensions: [],
                    submoduleCfg                     : [],
                    userRemoteConfigs                : [[credentialsId: "github-final-project-key",
                                                        url          : params.Git_repository_URL]]])
            }
        }

        stage('Compile') {
            steps {
                dir('05-Final_Project/spring-application') {
                    sh 'mvn compile'
                }
            }
        }

        stage('Tests') {
            steps {
                dir('05-Final_Project/spring-application') {
                    sh 'mvn test'
                }
            }
        }

        stage('Build') {
            steps {
                dir('05-Final_Project/spring-application') {
                    sh 'mvn clean package -DskipTests=true'
                }
            }
        }

        stage('Build docker image') {
            steps {
                dir('05-Final_Project/spring-application') {
                    sh 'docker build -t azbr/final-project:dev -f docker/Dockerfile .'
                }
            }
        }

        stage('Push docker image') {
            steps {
                withDockerRegistry(credentialsId: 'dockerhub-azbr', url: 'https://index.docker.io/v1/') {
                    sh 'docker push azbr/final-project:dev'
                }
            }
        }

        stage('Delete docker image locally') {
            steps {
                sh 'docker rmi azbr/final-project:dev'
            }
        }

        // stage ('Starting Deploy job') {
        //     steps {
        //         echo "Triggering Deploy job for this application"
        //         build job: 'Deploy-shopping-cart-app', wait: false
        //     }
        // }
    }
}