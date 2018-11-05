package com.example

import org.junit.ClassRule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.kafka.test.rule.KafkaEmbedded
import org.springframework.messaging.support.MessageBuilder
import org.springframework.test.annotation.DirtiesContext
import spock.lang.Shared

@DirtiesContext
@SpringBootTest(classes = [KafkaIntTestApplication.class])
class CustomListenerSpec extends TestSpecBase {
    @ClassRule
    @Shared
    private KafkaEmbedded embeddedKafka = new KafkaEmbedded(1, true, 'my-topic')

    @Autowired
    private KafkaTemplate<String, String> template

    @Autowired
    private SimpleService service

    final def TOPIC_NAME = 'my-topic'

    def setupSpec() {
        System.setProperty('spring.kafka.bootstrap-servers', embeddedKafka.getBrokersAsString());
        System.setProperty('spring.cloud.stream.kafka.binder.zkNodes', embeddedKafka.getZookeeperConnectionString());
    }

    def 'Sample test'() {
        def testMessagePayload = "Test message"
        def message = MessageBuilder.withPayload(testMessagePayload).setHeader(KafkaHeaders.TOPIC, TOPIC_NAME).build()

        when: 'We put a message on the topic'
        template.send(message)

        then: 'the service should be called'
        1 * service.handleMessage(_)
    }
}