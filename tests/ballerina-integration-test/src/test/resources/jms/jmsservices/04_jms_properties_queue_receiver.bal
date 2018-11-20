import ballerina/jms;
import ballerina/io;
import ballerina/http;


// Initialize a JMS connection with the provider.
jms:Connection conn3 = new ({
        initialContextFactory: "bmbInitialContextFactory",
        providerUrl: "amqp://admin:admin@carbon/carbon?brokerlist='tcp://localhost:5772'"
    });

// Initialize a JMS session on top of the created connection.
jms:Session jmsSession3 = new (conn3, {
        // Optional property. Defaults to AUTO_ACKNOWLEDGE
        acknowledgementMode: "AUTO_ACKNOWLEDGE"
    });

// Initialize a Queue consumer using the created session.
endpoint jms:QueueReceiver queueConsumer3 {
    session: jmsSession3,
    queueName: "MyPropQueue"
};

// Bind the created consumer to the listener service.
service<jms:Consumer> jmsListener3 bind queueConsumer3 {

    // OnMessage resource get invoked when a message is received.
    onMessage(endpoint consumer, jms:Message message) {
        var messageText = message.getTextMessageContent();
        var booleanVal = message.getBooleanProperty("booleanProp");
        if (booleanVal is boolean) {
             io:print("booleanVal:" + booleanVal);
        } else {
             panic booleanVal;
        }
        var intVal = message.getIntProperty("intProp");
        if (intVal is int) {
             io:print("|intVal:" + intVal);
        } else {
             panic intVal;
        }
        var floatVal = message.getFloatProperty("floatProp");
        if (floatVal is float) {
             io:print("|floatVal:" + floatVal);
        } else {
             panic floatVal;
        }
        var stringProp = message.getStringProperty("stringProp");
        if (stringProp is string) {
             io:print("|stringVal:" + stringProp);
        } else if (stringProp is error) {
             panic stringProp;
        }
        if (messageText is string) {
             io:println("|message:" + messageText);
        } else {
             panic messageText;
        }
    }
}

// This is to make sure that the test case can detect the PID using port. Removing following will result in
// intergration testframe work failing to kill the ballerina service.
endpoint http:Listener helloWorldEp3 {
    port:9093
};

@http:ServiceConfig {
    basePath:"/jmsDummyService"
}
service<http:Service> helloWorld3 bind helloWorldEp3 {

    @http:ResourceConfig {
        methods:["GET"],
        path:"/"
    }
    sayHello (endpoint client, http:Request req) {
        // Do nothing
    }
}
