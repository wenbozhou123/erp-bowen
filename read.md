### 数据库  
[参考链接](https://www.jianshu.com/p/68be095f983c) 

    1. 安装Postgresql (ubuntu) 
        执行如下命令安装postgresql
        sudo apt-get install postgresql
        切换到Linux的postgres用户下
        sudo su postgres
        登陆postgresql
        psql
    2. 配置Postgresql
        由于目前为止，只能本机登陆到数据库，但实际情况是会选择远程登录，所以需要做如下配置：
        打开vim /etc/postgresql/10/main/postgresql.conf
        - 监听任何地址访问，修改连接权限
        #listen_addresses = ‘localhost’ 改为启用，且改为*
        listen_addresses = ‘*’
        - 修改pg_hba.conf
        pg_hba.conf，位置与postgresql.conf相同，虽然上面配置允许任意地址连接PostgreSQL，但是这在pg中还不够，我们还需在pg_hba.conf中配置服务端允许的认证方式。任意编辑器打开该文件，编辑或添加下面一行。
        # TYPE  DATABASE  USER  CIDR-ADDRESS  METHOD
        host  all  all 0.0.0.0/0 md5
        默认pg只允许本机通过密码认证登录，修改为上面内容后即可以对任意IP访问进行密码验证。
        启用密码验证
        #password_encryption = on 改为启用
        password_encryption = on
        - 重启PostgreSQL数据库
        sudo /etc/init.d/postgresql restart
    3. 创建用户
        -  创建数据库用户corey，并指定为超级用户
        sudo -u postgres createuser --superuser corey
        - 登录数据库控制台，为刚刚创建的用户设置密码
        sudo -u postgres psql
        \password corey
        \q
        - 为刚创建的用户创建数据库
        sudo -u postgres createdb --owner=corey corey
        - 登录数据库
        psql -U corey -d corey -h 127.0.0.1 -p 5432
        密码: admin_1234

-----------------------------------





