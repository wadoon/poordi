package weigl.poordi;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

interface PrintService {
    void print(String text);
}

/**
 * @author Alexander Weigl
 * @version 1 (13.01.19)
 */
public class TestSimple {
    private PrintService printer;

    @Inject
    public void setPrinter(PrintService a) {
        printer = a;
    }

    @Test
    public void test() throws InvocationTargetException, IllegalAccessException {
        Registry r = new Registry();
        r.register(PrintService.class, new ConsolePrintService());
        r.inject(this);
        Assert.assertNotNull(printer);
        printer.print("It works!");
    }
}


class ConsolePrintService implements PrintService {
    @Override
    public void print(String text) {
        System.out.println(text);
    }
}
