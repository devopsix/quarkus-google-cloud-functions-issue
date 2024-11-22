# Quarkus Google Cloud Functions Test @WithFunction Issue Demonstration

```shell script
./mvnw test
```

`SomeFunction1Test` and `SomeFunction2Test` and both annotated with `@WithFunction`.
`SomeFunction1Test` contains 1 test method, `SomeFunction2Test` contains 2 test methods.
All test methods are identical.

Expected: All tests in `SomeFunction1Test` and `SomeFunction2Test` pass.

Actual: `SomeFunction1Test` passes, but the second test in `SomeFunction2Test` fails.

```
org.junit.jupiter.api.extension.TestInstantiationException: Failed to create test instance
        at io.quarkus.test.junit.QuarkusTestExtension.initTestState(QuarkusTestExtension.java:773)
        at io.quarkus.test.junit.QuarkusTestExtension.interceptTestClassConstructor(QuarkusTestExtension.java:739)
        at java.base/java.util.Optional.orElseGet(Optional.java:364)
        at java.base/java.util.ArrayList.forEach(ArrayList.java:1596)
        at java.base/java.util.ArrayList.forEach(ArrayList.java:1596)
Caused by: java.lang.RuntimeException: java.io.IOException: Failed to bind to 0.0.0.0/0.0.0.0:8081
        at io.quarkus.google.cloud.functions.test.CloudFunctionTestResource.inject(CloudFunctionTestResource.java:40)
        at io.quarkus.test.common.TestResourceManager.inject(TestResourceManager.java:201)
        at java.base/java.lang.reflect.Method.invoke(Method.java:580)
        at io.quarkus.test.junit.QuarkusTestExtension.createActualTestInstance(QuarkusTestExtension.java:784)
        at io.quarkus.test.junit.QuarkusTestExtension.initTestState(QuarkusTestExtension.java:767)
        ... 4 more
Caused by: java.io.IOException: Failed to bind to 0.0.0.0/0.0.0.0:8081
        at org.eclipse.jetty.server.ServerConnector.openAcceptChannel(ServerConnector.java:349)
        at org.eclipse.jetty.server.ServerConnector.open(ServerConnector.java:310)
        at org.eclipse.jetty.server.AbstractNetworkConnector.doStart(AbstractNetworkConnector.java:80)
        at org.eclipse.jetty.server.ServerConnector.doStart(ServerConnector.java:234)
        at org.eclipse.jetty.util.component.AbstractLifeCycle.start(AbstractLifeCycle.java:73)
        at org.eclipse.jetty.server.Server.doStart(Server.java:401)
        at org.eclipse.jetty.util.component.AbstractLifeCycle.start(AbstractLifeCycle.java:73)
        at com.google.cloud.functions.invoker.runner.Invoker.startServer(Invoker.java:324)
        at com.google.cloud.functions.invoker.runner.Invoker.startTestServer(Invoker.java:276)
        at io.quarkus.google.cloud.functions.test.CloudFunctionsInvoker.start(CloudFunctionsInvoker.java:26)
        at io.quarkus.google.cloud.functions.test.CloudFunctionTestResource.inject(CloudFunctionTestResource.java:38)
        ... 8 more
Caused by: java.net.BindException: Address already in use: bind
        at java.base/sun.nio.ch.Net.bind0(Native Method)
        at java.base/sun.nio.ch.Net.bind(Net.java:565)
        at java.base/sun.nio.ch.ServerSocketChannelImpl.netBind(ServerSocketChannelImpl.java:344)
        at java.base/sun.nio.ch.ServerSocketChannelImpl.bind(ServerSocketChannelImpl.java:301)
        at java.base/sun.nio.ch.ServerSocketAdaptor.bind(ServerSocketAdaptor.java:89)
        at org.eclipse.jetty.server.ServerConnector.openAcceptChannel(ServerConnector.java:344)
        ... 18 more
```

Suspected cause: In [`CloudFunctionTestResource.inject(TestInjector)`](https://github.com/quarkusio/quarkus/blob/8df922d7f8a07ef97a73ba066f2496486140eb67/test-framework/google-cloud-functions/src/main/java/io/quarkus/google/cloud/functions/test/CloudFunctionTestResource.java#L32)
a new invoker is started for each test without stopping an invoker created previously.

Potential remedy: Invokers should either be reused between tests or stopped after each test.
