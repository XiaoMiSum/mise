package xyz.migoo.mise.report;

import xyz.migoo.mise.framework.TestResult;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaomi
 * @date 2019-08-17 17:32
 */
public class Report {

    private List<TestResult> results;

    private String fName;

    public Report(){
        results = new ArrayList<>();
    }

    private void index(String fName){
        this.fName = fName;
    }

    public void addResult(TestResult result){
        results.add(result);
    }
}
