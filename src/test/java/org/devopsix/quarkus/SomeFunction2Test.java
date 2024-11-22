package org.devopsix.quarkus;

import io.quarkus.google.cloud.functions.test.FunctionType;
import io.quarkus.google.cloud.functions.test.WithFunction;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
@WithFunction(FunctionType.CLOUD_EVENTS)
class SomeFunction2Test extends SomeFunctionAbstractTest {

    @Test
    void test1() {
        test();
    }

    @Test
    void test2() {
        test();
    }
}
