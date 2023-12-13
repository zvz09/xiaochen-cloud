# copy jar
# Define a function for copying JAR files
copy_module_jar() {
    module_name="$1"
    source_directory="$2"
    destination_directory="$3"

    echo "Begin copy $module_name"
    cp -f "$source_directory/build/libs/$module_name-0.0.1-SNAPSHOT.jar" "$destination_directory/"
}

# Usage example
copy_module_jar "xiaochen-gateway" "../xiaochen-gateway" "./xiaochen/gateway"
copy_module_jar "xiaochen-auth" "../xiaochen-auth" "./xiaochen/auth"
copy_module_jar "xiaochen-monitor" "../xiaochen-monitor" "./xiaochen/monitor"

copy_module_jar "xiaochen-autocode" "../xiaochen-modules/xiaochen-autocode" "./xiaochen/modules/autocode"
copy_module_jar "xiaochen-blog" "../xiaochen-modules/xiaochen-blog" "./xiaochen/modules/blog"
copy_module_jar "xiaochen-file" "../xiaochen-modules/xiaochen-file" "./xiaochen/modules/file"
copy_module_jar "xiaochen-flowable" "../xiaochen-modules/xiaochen-flowable" "./xiaochen/modules/flowable"
copy_module_jar "xiaochen-job-admin" "../xiaochen-modules/xiaochen-job-admin" "./xiaochen/modules/job"
copy_module_jar "xiaochen-log-server" "../xiaochen-modules/xiaochen-log-server" "./xiaochen/modules/log"
copy_module_jar "xiaochen-message" "../xiaochen-modules/xiaochen-message" "./xiaochen/modules/message"
copy_module_jar "xiaochen-system" "../xiaochen-modules/xiaochen-system" "./xiaochen/modules/system"



