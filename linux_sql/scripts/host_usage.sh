psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

# Check # of args
if [ "$#" -ne 5 ]; then
    echo "Illegal number of parameters"
    exit 1
fi

vmstat_mb=$(vmstat --unit M)
hostname=$(hostname -f)

timestamp=$(vmstat -t| tail -1 | awk '{print $18, $19}')
memory_free=$(vmstat --unit M | tail -1 | awk -v col="4" '{print $col}')

cpu_idle=$(echo "$vmstat_mb"|tail -1| awk '{print $15}'| xargs)
cpu_kernel=$(echo "$vmstat_mb"|tail -1| awk '{print $14}'| xargs)
disk_io=$(echo "$vmstat_mb"| tail -1 | awk '{print $9+$10}')
disk_available=$(df -BM /|tail -1| awk '{print $4}'| sed 's/M//')

host_id="(SELECT id FROM host_info WHERE hostname='$hostname')";

insert_stmt="INSERT INTO host_usage(timestamp, memory_free, cpu_idle, cpu_kernel, disk_io, disk_available) VALUES('$timestamp', '$memory_free', '$cpu_idle', '$cpu_kernel', '$disk_io', '$disk_available');"

export PGPASSWORD=$psql_password
psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_stmt"
exit $?

