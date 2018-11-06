package com.example

import org.junit.ClassRule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.kafka.test.rule.KafkaEmbedded
import org.springframework.messaging.support.MessageBuilder
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import spock.lang.Shared
import spock.util.concurrent.BlockingVariable

@DirtiesContext
@ActiveProfiles('test')
@SpringBootTest
// Using @SpringBootTest wires up the whole context and @TestConfiguration just sits on top and doesn't replace the
// actual configuration so the MockConfig class has to be imported like a real config class.
@Import(MockConfig.class)
class CustomListenerSpec extends TestSpecBase {

    @ClassRule
    @Shared
    private KafkaEmbedded embeddedKafka = new KafkaEmbedded(1, false, TOPIC_NAME)

    @Autowired
    private KafkaTemplate<String, String> template

    @Autowired
    private SimpleService service

    final def TOPIC_NAME = 'my-topic'

    def setupSpec() {
        System.setProperty('spring.kafka.bootstrap-servers', embeddedKafka.getBrokersAsString());
    }

    def 'Sample test'() {
        def testMessagePayload = "Test message"
        def message = MessageBuilder.withPayload(testMessagePayload).setHeader(KafkaHeaders.TOPIC, TOPIC_NAME).build()
        def result = new BlockingVariable<Boolean>(5)
        // Use this as a mock response and make it's response be to set the value in the BlockingVariable to true
        service.handleMessage(_ as String) >> {
            result.set(true)
        }

        when: 'We put a message on the topic'
        template.send(message)

        then: 'the service should be called'
        result.get()
    }
}