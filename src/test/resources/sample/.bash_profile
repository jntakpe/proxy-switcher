alias pgq='pg_ctl -D /usr/local/var/postgres stop -s -m fast'
alias pgs='pg_ctl -D /usr/local/var/postgres -l /usr/local/var/postgres/server.log start'
alias ll='ls -l'
export HTTP_PROXY=some.proxy.host.value:8080
export HTTPS_PROXY=$HTTP_PROXY
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0_144.jdk/Contents/Home