package net.devkhan.opencrm.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
		basePackages = "net.devkhan.opencrm.repository"
)
public class RepoConfig {

	@Bean
	public PropertiesFactoryBean repoProps() {
		PropertiesFactoryBean bean = new PropertiesFactoryBean();
		bean.setIgnoreResourceNotFound(true);
		bean.setLocation(new ClassPathResource("repository.properties"));
		return bean;
	}

	@Bean
	public DataSource dataSource(
			@Value("#{repoProps['jdbc.url'] ?: 'localhost'}") String jdbcUrl,
			@Value("#{repoProps['jdbc.driverClassName'] ?: 'org.mariadb.jdbc.Driver'}") String jdbcDriverClassName,
			@Value("#{repoProps['jdbc.username'] ?: 'api'}") String jdbcUsername,
			@Value("#{repoProps['jdbc.password'] ?: '!akdlxlvmfh@14926'}") String jdbcPassword) {

		HikariConfig config = new HikariConfig();
		config.setJdbcUrl(jdbcUrl);
		config.setDriverClassName(jdbcDriverClassName);
		config.setUsername(jdbcUsername);
		config.setPassword(jdbcPassword);

		config.setMaximumPoolSize(5);
		config.setConnectionTestQuery("SELECT 1");
		config.setPoolName("springHikariCP");
		config.setIdleTimeout(30000);
		config.setMaxLifetime(30000);

		config.addDataSourceProperty("dataSource.cachePrepStmts", "true");
		config.addDataSourceProperty("dataSource.prepStmtCacheSize", "250");
		config.addDataSourceProperty("dataSource.prepStmtCacheSqlLimit", "2048");
		config.addDataSourceProperty("dataSource.cachePrepStmts", "true");

		return new HikariDataSource(config);
	}

	@Bean
	@Autowired
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(
			DataSource dataSource,
			@Value("#{repoProps['hibernate.scanPackages'] ?: 'net.devkhan.sample.domain' }") String[] packages,
			@Qualifier("hibernateProperties") Properties hibernateProperties,
			JpaVendorAdapter jpaVendorAdapter) {
		LocalContainerEntityManagerFactoryBean sessionFactory = new LocalContainerEntityManagerFactoryBean();
		sessionFactory.setDataSource(dataSource);
		sessionFactory.setPackagesToScan(packages);
		sessionFactory.setJpaVendorAdapter(jpaVendorAdapter);
		sessionFactory.setJpaProperties(hibernateProperties);
		return sessionFactory;
	}

	@Bean
	@Autowired
	public HibernateJpaVendorAdapter jpaVenderAdapter(
			@Value("#{repoProps['hibernate.dialect'] ?: 'org.hibernate.dialect.MySQL5InnoDBDialect' }") final String dialect,
			@Value("#{repoProps['hibernate.show_sql'] ?: 'false' }") final boolean showSql) {
		HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
		jpaVendorAdapter.setShowSql(showSql);
		jpaVendorAdapter.setDatabasePlatform(dialect);
		jpaVendorAdapter.setGenerateDdl(true);
		return jpaVendorAdapter;
	}

	@Bean
	@Autowired
	public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(entityManagerFactory);
		return txManager;
	}

	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}

	@Bean(name = "hibernateProperties")
	public Properties hibernateProperties(
			@Value("#{repoProps['hibernate.hbm2ddl.auto'] ?: 'validate' }") final String hbm2ddlAuto,
			@Value("#{repoProps['hibernate.dialect'] ?: 'org.hibernate.dialect.MySQL5InnoDBDialect' }") final String dialect,
			@Value("#{repoProps['hibernate.show_sql'] ?: 'false' }") final String showSql,
			@Value("#{repoProps['hibernate.format_sql'] ?: 'false' }") final String formatSql,
			@Value("#{repoProps['hibernate.hbm2ddl.import_files'] ?: '' }") final String importFiles) {
		return new Properties() {
			{
				if (!StringUtils.isEmpty(importFiles)) {
					setProperty("hibernate.hbm2ddl.import_files", importFiles);
				}
				setProperty("hibernate.hbm2ddl.auto", hbm2ddlAuto);
				setProperty("hibernate.dialect", dialect);
				setProperty("hibernate.show_sql", showSql);
				setProperty("hibernate.format_sql", formatSql);
				setProperty("hibernate.generate_statistics", Boolean.TRUE.toString());
				setProperty("hibernate.cache.use_structured_entire", Boolean.TRUE.toString());
			}
		};
	}
}
