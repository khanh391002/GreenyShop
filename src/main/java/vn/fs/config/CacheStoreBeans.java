package vn.fs.config;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import vn.fs.service.impl.CacheStore;

@Configuration
public class CacheStoreBeans {
	@Bean
    public CacheStore<Long, HttpSession> cartCache() {
        return new CacheStore<Long, HttpSession>(30, TimeUnit.DAYS);
    }
}
