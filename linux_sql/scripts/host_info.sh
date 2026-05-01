psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

if [ "$#" -ne 5 ]; then
	echo "Illegal number of parameters"
	exit 1
fi

# save hostname as a variable
vmstat_mb=$(vmstat --unit M)
hostname=$(hostname -f)
# save the number of CPUs to a variable
lscpu_out=$(lscpu)
cpu_number=$(echo "$lscpu_out"  | egrep "^CPU\(s\):" | awk '{print $2}' | xargs)
# tip: `xargs` is a trick to remove leading and trailing white spaces
# tip: the $2 is instructing awk to find the second field

# hardware info
cpu_architecture=$(echo "$lscpu_out" | egrep "^Architecture" | awk '{print $2}' | xargs)
cpu_model=$(echo "$lscpu_out" | egrep "^Model name" | awk -F ':''{print $2}'| xargs)
cpu_mhz=$(cat /proc/cpuinfo | grep "cpu MHz" | head -1 | awk -F ':' '{print $2}' | xargs)
l2_cache=$(echo "$lscpu_out"  | egrep "L2" | awk -F ' ' '{print $3}' | xargs)
total_mem=$(cat /proc/meminfo | egrep "^MemTotal" | awk '{print $2}' | xargs)
timestamp=$(vmstat -t | tail -1 | awk '{print $18, $19}')


# insert statement
insert_stmt="INSERT INTO host_info(hostname, cpu_number, cpu_architecture, cpu_model, cpu_mhz, l2_cache, timestamp, total_mem) VALUES('$hostname', '$cpu_number', '$cpu_architecture', '$cpu_model','$cpu_mhz', '$l2_cache', '$timestamp', '$total_mem');"

export PGPASSWORD=$psql_password
psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_stmt"
exit $?
