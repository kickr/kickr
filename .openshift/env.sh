export JAVA_HOME=${OPENSHIFT_DATA_DIR}/jdk-8
export M2_HOME=${OPENSHIFT_DATA_DIR}/maven-3.2
export M2=${M2_HOME}/bin
export PATH=${JAVA_HOME}/bin:$M2:$PATH
