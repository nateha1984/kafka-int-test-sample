package com.example

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification
import spock.mock.DetachedMockFactory

@ActiveProfiles('test')
abstract class TestSpecBase extends Specification {
    @TestConfiguration
    static class MockConfig {
        def detachedMockFactory = new DetachedMockFactory()

        @Bean
        SimpleService mockService() {
            detachedMockFactory.Mock(SimpleService)
        }
    }
}