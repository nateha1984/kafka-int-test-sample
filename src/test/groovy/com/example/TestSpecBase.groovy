package com.example

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import spock.lang.Specification
import spock.mock.DetachedMockFactory

abstract class TestSpecBase extends Specification {

    // This has to be marked with a "real" @Configuration annotation in order for it to replace the standard config
    // class and respect the @Primary bean.
    @Configuration
    static class MockConfig {
        def detachedMockFactory = new DetachedMockFactory()

        @Bean
        @Primary
        SimpleService mockService() {
            detachedMockFactory.Mock(SimpleService)
        }
    }
}