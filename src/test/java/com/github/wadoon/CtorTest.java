package com.github.wadoon;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Alexander Weigl
 * @version 1 (13.01.19)
 */
public class CtorTest {
    @Test
    public void test() throws InvocationTargetException, IllegalAccessException, InstantiationException {
        DefaultRegistry r = new DefaultRegistry();
        r.register(PrintService.class, new ConsolePrintService());
        var test = r.getInstance(TestObject.class);
        Assert.assertNotNull(test);
        test.print();
    }
}

class TestObject {
    private final PrintService printer;

    @Inject
    public TestObject(PrintService printer) {
        this.printer = printer;
    }

    public void print() {
        printer.print("It works!");
    }
}