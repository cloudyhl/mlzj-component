
package com.mlzj.component.generator.config;


import com.mlzj.component.generator.dao.GeneratorDao;
import com.mlzj.component.generator.dao.OracleGeneratorDao;
import com.mlzj.component.generator.dao.PostgreSQLGeneratorDao;
import com.mlzj.component.generator.utils.RRException;
import com.mlzj.component.generator.dao.MySQLGeneratorDao;
import com.mlzj.component.generator.dao.SQLServerGeneratorDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 数据库配置
 *
 * @author yhl
 * @since 2022/8/4
 */
@Configuration
public class DbConfig {
    @Value("${shuhai.database: mysql}")
    private String database;
    @Autowired
    private MySQLGeneratorDao mySQLGeneratorDao;
    @Autowired
    private OracleGeneratorDao oracleGeneratorDao;
    @Autowired
    private SQLServerGeneratorDao sqlServerGeneratorDao;
    @Autowired
    private PostgreSQLGeneratorDao postgreSQLGeneratorDao;

    @Bean
    @Primary
    public GeneratorDao getGeneratorDao() {
        if ("mysql".equalsIgnoreCase(database)) {
            return mySQLGeneratorDao;
        } else if ("oracle".equalsIgnoreCase(database)) {
            return oracleGeneratorDao;
        } else if ("sqlserver".equalsIgnoreCase(database)) {
            return sqlServerGeneratorDao;
        } else if ("postgresql".equalsIgnoreCase(database)) {
            return postgreSQLGeneratorDao;
        } else {
            throw new RRException("不支持当前数据库：" + database);
        }
    }
}
