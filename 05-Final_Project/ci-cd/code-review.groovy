properties([pipelineTriggers([githubPush()])])

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

        stage('Dockerfile lint') {
            steps {
                sh 'hadolint docker/Dockerfile'
            }
        }

        stage ('Starting Build job') {
            steps {
                echo "Triggering Build job for this application"
                build job: 'Build-shopping-cart-app', parameters: [string(name: 'Branch', value: params.Branch),
                                                                   string(name: 'Git_repository_URL', value: params.Git_repository_URL)], wait: false
            }
        }
    }
}