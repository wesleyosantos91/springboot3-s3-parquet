package io.github.wesleyosantos91.config;

import com.globalmentor.apache.hadoop.fs.BareLocalFileSystem;
import org.apache.hadoop.fs.FileSystem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class HadoopConfig {

    @Bean
    public org.apache.hadoop.conf.Configuration hadoopConfiguration() {
        org.apache.hadoop.conf.Configuration configuration = new org.apache.hadoop.conf.Configuration();
        configuration.setClass("fs.file.impl", BareLocalFileSystem.class, FileSystem.class);
        return configuration;
    }
}