pipeline {
    agent any

    tools {
        jdk 'JDK 17'
        maven 'Maven 3.9'
    }

    environment {
        APP_NAME = "safari"
    }

    stages {
        stage('Checkout') {
            steps {
                echo "Pulling source code from Git"
                git branch: 'main', url: 'https://github.com/composureR3j3c/sprung_again.git'
            }
        }

        stage('Build') {
            steps {
                echo "Compiling project..."
                sh 'mvn clean compile'
            }
        }

        stage('Test') {
            steps {
                echo "Running tests..."
                sh 'mvn test'
            }
        }

        stage('Package') {
            steps {
                echo "Packaging application..."
                sh 'mvn package -DskipTests'
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }

        stage('Run App (Optional)') {
            when {
                expression { fileExists('target/*.jar') }
            }
            steps {
                echo "Starting Spring Boot app for verification..."
                sh '''
                    PID=$(lsof -t -i:9090 || echo "")
                    if [ ! -z "$PID" ]; then kill -9 $PID; fi
                    nohup java -jar target/*.jar > app.log 2>&1 &
                    sleep 10
                    curl -f http://localhost:9090/api/v1/customers || true
                '''
            }
        }
    }

    post {
        success {
            echo "Build successful!"
        }
        failure {
            echo "Build failed. Check logs above."
        }
    }
}
