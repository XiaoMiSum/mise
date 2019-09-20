package xyz.migoo.mise.test.example;

import org.junit.jupiter.api.Test;
import xyz.migoo.mise.runner.TestRunner;

/**
 * @author xiaomi
 * @date 2019/9/18 22:41
 */
public class MiseTest {

    @Test
    public void test(){
        new TestRunner("MiSe").run("./case/case.yml", "./case/env.yml", "htmlunit");
    }
}
