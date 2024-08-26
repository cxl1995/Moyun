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
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration //注册到SpringBoot 容器中
@MapperScan(basePackages = "com.mywl.app.platform.mapper.meta",
    sqlSessionTemplateRef  = "test1SqlSessionTemplate")
public class DataSourceConfig1 {

  @Bean(name = "test1DataSource")
  @Primary
  @ConfigurationProperties(prefix = "spring.datasource.test1")
  public DataSource testDataSource() {
    return DataSourceBuilder.create().build();
  }

  @Primary
  @Bean(name = "test1SqlSessionFactory")
  public SqlSessionFactory testSqlSessionFactory
      (@Qualifier("test1DataSource") DataSource dataSource) throws Exception {
    MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
    bean.setDataSource(dataSource);
    return bean.getObject();
  }

  @Bean(name = "test1TransactionManager")
  @Primary
  public DataSourceTransactionManager testTransactionManager
      (@Qualifier("test1DataSource") DataSource dataSource) {
    return new DataSourceTransactionManager(dataSource);
  }

  @Bean(name = "test1SqlSessionTemplate")
  @Primary
  public SqlSessionTemplate testSqlSessionTemplate
      (@Qualifier("test1SqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
    return new SqlSessionTemplate(sqlSessionFactory);
  }

}