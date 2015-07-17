dataSource {
    pooled = true
//    jmxExport = true
    driverClassName = "com.mysql.jdbc.Driver"
    username = "root"
    password = "Pmlpml702"
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = false
//    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory' // Hibernate 3
    cache.region.factory_class = 'org.hibernate.cache.ehcache.EhCacheRegionFactory' // Hibernate 4
    singleSession = true // configure OSIV singleSession mode
    flush.mode = 'manual' // OSIV session flush mode outside of transactional context
}

// environment specific settings
environments {
    development {
        dataSource {
//			username = "dev"
//			password = "123"
//			dbCreate = "update" // one of 'create', 'create-drop', 'update', 'validate', ''
//			url = "jdbc:mysql://192.168.1.134:3306/pml_app?useUnicode=true&characterEncoding=utf-8"
			
			/** 腾讯云数据库外网链接 ***/
			username = "cdb_outerroot"
			password = "paimeilv!@2014"
			dbCreate = "update" // one of 'create', 'create-drop', 'update', 'validate', ''
			url = "jdbc:mysql://55640a98df481.gz.cdb.myqcloud.com:16858/pml_app?useUnicode=true&characterEncoding=utf-8"
        }
    }
    test {
        dataSource {
            username = "pml"
			password = "Pmlpml702"
            dbCreate = "update"
			url = "jdbc:mysql://rdsrjbjmmrjbjmm.mysql.rds.aliyuncs.com:3306/pml_app?useUnicode=true&characterEncoding=utf-8&autoReconnect=true"
        }
    }
    production {
        dataSource {
            dbCreate = "update"
//			url = "jdbc:mysql://10.66.106.228:3306/pml_app?useUnicode=true&characterEncoding=utf-8"
			
			username = "dev"
			password = "123"
			dbCreate = "update" // one of 'create', 'create-drop', 'update', 'validate', ''
			url = "jdbc:mysql://192.168.1.134:3306/pml_app?useUnicode=true&characterEncoding=utf-8"
            properties {
               // See http://grails.org/doc/latest/guide/conf.html#dataSource for documentation
               jmxEnabled = true
               initialSize = 5
               maxActive = 50
               minIdle = 5
               maxIdle = 25
               maxWait = 10000
               maxAge = 10 * 60000
               timeBetweenEvictionRunsMillis = 5000
               minEvictableIdleTimeMillis = 60000
               validationQuery = "SELECT 1"
               validationQueryTimeout = 3
               validationInterval = 15000
               testOnBorrow = true
               testWhileIdle = true
               testOnReturn = false
               jdbcInterceptors = "ConnectionState"
               defaultTransactionIsolation = java.sql.Connection.TRANSACTION_READ_COMMITTED
            }
        }
    }
}
