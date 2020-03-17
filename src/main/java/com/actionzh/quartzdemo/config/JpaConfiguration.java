package com.actionzh.quartzdemo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by fdlessard on 17-03-02.
 */
@Configuration
@EnableJpaRepositories(value = "com.actionzh.quartzdemo.repository", transactionManagerRef = "simpleJpaTxManager")
@EnableTransactionManagement(proxyTargetClass = true)
@EnableJpaAuditing
public class JpaConfiguration {

}
