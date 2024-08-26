package com.mywl.app.platform.config;

import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration //注册到springboot 容器中
@MapperScan(basePackages = "com.mywl.app.platform.mapper.user",
    sqlSessionTemplateRef  = "test2SqlSessionTemplate")
public class DataSourceConfig2 {

  @Bean(name = "test2DataSource")
//  @Primary
  @ConfigurationProperties(prefix = "spring.datasource.test2")
  public DataSource testDataSource() {
    return DataSourceBuilder.create().build();
  }

//  @Primary
  @Bean(name = "test2SqlSessionFactory")
  public SqlSessionFactory testSqlSessionFactory
      (@Qualifier("test2DataSource") DataSource dataSource) throws Exception {
    MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
    bean.setDataSource(dataSource);
    return bean.getObject();
  }

  @Bean(name = "test2TransactionManager")
//  @Primary
  public DataSourceTransactionManager testTransactionManager
      (@Qualifier("test2DataSource") DataSource dataSource) {
    return new DataSourceTransactionManager(dataSource);
  }

  @Bean(name = "test2SqlSessionTemplate")
//  @Primary
  public SqlSessionTemplate testSqlSessionTemplate
      (@Qualifier("test2SqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
    return new SqlSessionTemplate(sqlSessionFactory);
  }

}